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
}
