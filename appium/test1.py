import unittest
from appium import webdriver
from appium.options.android import UiAutomator2Options
from appium.webdriver.common.appiumby import AppiumBy

capabilities = dict(
    platformName='Android',
    automationName='uiautomator2',
    deviceName='Android',
    appPackage='com.example.tombyts',
    appActivity='.ui.theme.MainActivity.kt',
    language='en',
    locale='US'
)

appium_server_url = 'http://localhost:4723'

class TestTombyts(unittest.TestCase):
    def setUp(self):
        self.driver = webdriver.Remote(appium_server_url, capabilities)

    def tearDown(self):
        if self.driver:
            self.driver.quit()

    def test_click_button(self):
        button = self.driver.find_element(by=AppiumBy.XPATH, value="//android.widget.Button[@content-desc='simpleButton']")
        button.click()

if __name__ == '__main__':
    unittest.main()