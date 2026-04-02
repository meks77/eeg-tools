import { test, expect } from '@playwright/test';

test.beforeEach(async ({ page }) => {
  await page.goto('/');
});

test('has correct title', async ({ page }) => {
  await expect(page).toHaveTitle(/EEG Tools/);
  const welcome = page.locator('h1');
  await expect(welcome).toContainText('EEG Ersparnisrechner');
});

test('calculates savings correctly with default values', async ({ page }) => {
  // Default values:
  // 3500 kWh, 0.1629 €/kWh, 0.10 €/kWh, 30% coverage
  // eegKwh = 3500 * 0.30 = 1050
  // Brutto Ersparnis = 1050 * (0.1629 - 0.1) = 66.045
  // Netznutzung = 1050 * 0.02952 = 30.996
  // Elektrizität = 1050 * 0.001 = 1.05
  // Ern. Netznutzung = 1050 * 0.00583 = 6.1215
  // Ern. Netzverlust = 1050 * 0.00037 = 0.3885
  // Gesamt = 66.045 + 30.996 + 1.05 + 6.1215 + 0.3885 = 104.601
  
  // Mitgliedsgebühr = 10
  // Einschreibegebühr = 15
  // Netto Jahr 1 = 104.601 - 10 - 15 = 79.601
  // Netto Folgejahre = 104.601 - 10 = 94.601

  await expect(page.locator('.results-section')).toContainText('66.04 €');
  await expect(page.locator('.results-section')).toContainText('31.00 €'); // Netznutzung
  await expect(page.locator('.results-section')).toContainText('104.60 €'); // Gesamt
  await expect(page.locator('.highlight')).toContainText('79.60 €');
  await expect(page.locator('.net-result').last()).toContainText('94.60 €');
});

test('updates savings when input changes', async ({ page }) => {
  // Change energy consumption to 5000 kWh
  const consumptionInput = page.locator('input').first();
  await consumptionInput.fill('5000');

  // New calculation:
  // 5000 * 0.30 = 1500 kWh
  // 1500 * (0.1629 - 0.1 + 0.02952 + 0.001 + 0.00583 + 0.00037) 
  // = 1500 * (0.0629 + 0.03672) = 1500 * 0.09962 = 149.43
  
  await expect(page.locator('.results-section')).toContainText('149.43 €');
});

test('supports comma as decimal separator', async ({ page }) => {
    // Aktueller Strompreis auf 0,20 setzen
    const priceInput = page.locator('input').nth(1);
    await priceInput.fill('0,20');
    
    // 1050 * (0.20 - 0.1 + 0.03672) = 1050 * 0.13672 = 143.556
    await expect(page.locator('.results-section')).toContainText('143.56 €');
});

test('arrow keys update values', async ({ page }) => {
    const consumptionInput = page.locator('input').first();
    await consumptionInput.focus();
    await page.keyboard.press('ArrowUp');
    
    // Default 3500 -> 3501
    // 3501 * 0.30 = 1050.3
    // 1050.3 * 0.09962 = 104.630886 -> 104.63
    await expect(page.locator('.results-section')).toContainText('104.63 €');
});

test('responsive layout check', async ({ page }) => {
    const grid = page.locator('.calculator-grid');
    
    // Desktop
    await page.setViewportSize({ width: 1024, height: 768 });
    await expect(grid).toHaveCSS('display', 'grid');
    
    // Mobile
    await page.setViewportSize({ width: 375, height: 667 });
    // In CSS media query it should still be grid but with 1 column if we set it so, 
    // or just check that it's visible.
    await expect(grid).toBeVisible();
});
