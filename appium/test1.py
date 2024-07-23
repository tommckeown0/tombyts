import unittest
from appium import webdriver
from appium.options.android import UiAutomator2Options
from appium.webdriver.common.appiumby import AppiumBy
import time

capabilities = dict(
    platformName='Android',
    automationName='uiautomator2',
    deviceName='Android',
    appPackage='com.example.tombyts',
    appActivity='.ui.MainActivity',
    language='en',
    locale='US'
)

appium_server_url = 'http://localhost:4723'

class TestTombyts(unittest.TestCase):
    def setUp(self) -> None:
        self.driver = webdriver.Remote(
            appium_server_url, options=UiAutomator2Options().load_capabilities(capabilities)
        )

    def tearDown(self) -> None:
        if self.driver:
            self.driver.quit()

    def test_click_button(self) -> None:
        button = self.driver.find_element(
            by=AppiumBy.ACCESSIBILITY_ID,
            value="Simple Button"
        )
        button.click()
        time.sleep(2)

    @unittest.skip("Skipping API call button test for now")
    def test_click_simple_button_2(self) -> None:
        button = self.driver.find_element(
            by=AppiumBy.XPATH,  # Adjust locator if needed
            value="//android.widget.Button[contains(@text, 'API call')]"
        )
        button.click()
        time.sleep(2)  # Allow time for API call and Snackbar

        # 1. Verify Snackbar Presence (if applicable)
        snackbar_element = self.driver.find_element(
            by=AppiumBy.XPATH,
            value="//android.widget.TextView[contains(@text, 'Hello from TypeScript and Express!')]"
        )
        self.assertTrue(snackbar_element.is_displayed())

        # 2. Option A: Verify API Response Code (if accessible)
        # ... (Add logic to retrieve and assert the response code) ...

        # 2. Option B: Verify Snackbar Message Content
        snackbar_text = snackbar_element.text
        self.assertIn("Hello from TypeScript and Express!", snackbar_text)

if __name__ == '__main__':
    unittest.main()