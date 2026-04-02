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
  // 3500 kWh, 0.25 €/kWh, 0.12 €/kWh, 30% coverage
  // Brutto Ersparnis = 3500 * 0.30 * (0.25 - 0.12) = 1050 * 0.13 = 136.50
  // Mitgliedsgebühr = 10
  // Einschreibegebühr = 15
  // Netto Jahr 1 = 136.50 - 10 - 15 = 111.50
  // Netto Folgejahre = 136.50 - 10 = 126.50

  await expect(page.locator('.results-section')).toContainText('136.50 €');
  await expect(page.locator('.highlight')).toContainText('111.50 €');
  await expect(page.locator('.net-result').last()).toContainText('126.50 €');
});

test('updates savings when input changes', async ({ page }) => {
  // Change energy consumption to 5000 kWh
  const consumptionInput = page.locator('input').first();
  await consumptionInput.fill('5000');

  // New calculation:
  // 5000 * 0.30 * (0.25 - 0.12) = 1500 * 0.13 = 195.00
  await expect(page.locator('.results-section')).toContainText('195.00 €');
});

test('supports comma as decimal separator', async ({ page }) => {
    // Aktueller Strompreis auf 0,20 setzen
    const priceInput = page.locator('input').nth(1);
    await priceInput.fill('0,20');
    
    // 3500 * 0.30 * (0.20 - 0.12) = 1050 * 0.08 = 84.00
    await expect(page.locator('.results-section')).toContainText('84.00 €');
});

test('arrow keys update values', async ({ page }) => {
    const consumptionInput = page.locator('input').first();
    await consumptionInput.focus();
    await page.keyboard.press('ArrowUp');
    
    // Default 3500 -> 3501
    // 3501 * 0.30 * (0.25 - 0.12) = 1050.3 * 0.13 = 136.539 -> 136.54
    await expect(page.locator('.results-section')).toContainText('136.54 €');
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
