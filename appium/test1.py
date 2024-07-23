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

if __name__ == '__main__':
    unittest.main()