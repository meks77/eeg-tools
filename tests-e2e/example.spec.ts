import { test, expect } from '@playwright/test';

test('has title and welcome message', async ({ page }) => {
  // Wir gehen davon aus, dass die App auf localhost:8080 läuft während der Tests oder wir testen gegen das Build-Artefakt
  // In der CI müsste man den Server starten
  await page.goto('/');

  await expect(page).toHaveTitle(/EEG Tools/);
  const welcome = page.locator('h1');
  await expect(welcome).toContainText('Willkommen bei EEG Tools');
});

test('responsive design check - mobile', async ({ page }) => {
  await page.setViewportSize({ width: 375, height: 667 });
  await page.goto('/');
  // Hier könnten spezifische Layout-Checks für Mobile folgen
});
