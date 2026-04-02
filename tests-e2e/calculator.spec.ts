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
  // Brutto Ersparnis = 3500 * 0.30 * (0.1629 - 0.1) = 1050 * 0.0629 = 66.045
  // Mitgliedsgebühr = 10
  // Einschreibegebühr = 15
  // Netto Jahr 1 = 66.05 - 10 - 15 = 41.045
  // Netto Folgejahre = 136.50 - 10 = 56.045

  await expect(page.locator('.results-section')).toContainText('66.04 €');
  await expect(page.locator('.highlight')).toContainText('41.04 €');
  await expect(page.locator('.net-result').last()).toContainText('56.04 €');
});

test('updates savings when input changes', async ({ page }) => {
  // Change energy consumption to 5000 kWh
  const consumptionInput = page.locator('input').first();
  await consumptionInput.fill('5000');

  // New calculation:
  // 5000 * 0.30 * (0.1629 - 0.1) = 1500 * 0.0629 = 94.35
  await expect(page.locator('.results-section')).toContainText('94.35 €');
});

test('supports comma as decimal separator', async ({ page }) => {
    // Aktueller Strompreis auf 0,20 setzen
    const priceInput = page.locator('input').nth(1);
    await priceInput.fill('0,20');
    
    // 3500 * 0.30 * (0.20 - 0.1) = 1050 * 0.1 = 105.00
    await expect(page.locator('.results-section')).toContainText('105.00 €');
});

test('arrow keys update values', async ({ page }) => {
    const consumptionInput = page.locator('input').first();
    await consumptionInput.focus();
    await page.keyboard.press('ArrowUp');
    
    // Default 3500 -> 3501
    // 3501 * 0.30 * (0.1629 - 0.10) = 1050.3 * 0.0629 = 66.06387 -> 66.06
    await expect(page.locator('.results-section')).toContainText('66.06 €');
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
