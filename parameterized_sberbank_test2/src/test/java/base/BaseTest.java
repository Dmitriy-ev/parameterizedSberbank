package base;


import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;



public class BaseTest {

	protected WebDriver driver;
	protected WebDriverWait wait;


	@Before
	public void before() {

		System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");

		ChromeOptions options = new ChromeOptions();
		options = new ChromeOptions();
		options.addArguments("--disable-notifications");
		options.addArguments("disable-infobars");
		options.addArguments("enable-popup-blocking");
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		wait = new WebDriverWait(driver, 20, 20000);

		String baseUrl = "http://www.sberbank.ru/ru/person";
		driver.get(baseUrl);

		new WebDriverWait(driver, 1).until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return document.readyState").equals("complete"));
	}

	@After
	public void after(){
		driver.quit();
	}
	protected void scrollApp() {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,-250)");
	}
	protected void scrollToElementJs(WebElement element) {
		JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
		javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	protected void waitUtilElementToBeClickable(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	protected void waitUtilElementToBeVisible(By locator) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	protected void waitUtilElementToBeVisible(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	protected void fillInputField(WebElement element, String value) {
		waitUtilElementToBeClickable(element);
		element.click();
		element.sendKeys(value);
		if(value.equals("9993432312")) {
			Assert.assertEquals("Поле было заполнено некорректно",
					"+7 (999) 343-23-12", element.getAttribute("value"));
		}else {
			Assert.assertEquals("Поле было заполнено некорректно",
					value, element.getAttribute("value"));
		}
	}
	protected void checkErrorMessageAtField(WebElement element, String errorMessage) {
		element = element.findElement(By.xpath("../child::div"));
		Assert.assertEquals("Проверка ошибки у поля не была пройдена",
				errorMessage, element.getText());
	}
}
