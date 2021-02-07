package ru.appline.sberbank_test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import base.BaseTest;

@RunWith(Parameterized.class)
public class ParameterizedSberbankTest extends BaseTest {

	@Parameterized.Parameters
	public static Collection data() {
		return Arrays.asList(new String[][]{
			{"Молотов", "Евгений", "Петрович"},
			{"Новиков", "Станислав", "Иванович"},
			{"Евменов", "Дмитрий", "Васильевич"}
		});
	}

	@Parameterized.Parameter(0)
	public String lastName;
	@Parameterized.Parameter(1)
	public String firstName;
	@Parameterized.Parameter(2)
	public String middleName;


	@Test
	public void test() {

		String coockeClose = "//*[@class='kitt-cookie-warning__close']";
		WebElement coockeButton = driver.findElement(By.xpath(coockeClose));
		waitUtilElementToBeVisible(coockeButton);
		waitUtilElementToBeClickable(coockeButton);
		coockeButton.click();

		//выбрать на меню пункт - карты
		String mapButtonXPath = "//*[@aria-label='Карты']";
		List<WebElement> mapButtonList = driver.findElements(By.xpath(mapButtonXPath));
		if(!mapButtonList.isEmpty()) {
			waitUtilElementToBeClickable(mapButtonList.get(0));
			mapButtonList.get(0).click();
		}

		//выбрать подменю - дебетовые карты 
		String debitCardsButtonXPath = "//*[@aria-label='Карты']/../child::div//a";
		WebElement debitCardsButton = driver.findElement(By.xpath(debitCardsButtonXPath));
		waitUtilElementToBeClickable(debitCardsButton);
		debitCardsButton.click();

		String debitCardsXPath = "//h1[contains(text(), 'Дебетовые карты')]";
		WebElement debitElement = driver.findElement(By.xpath(debitCardsXPath));
		Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому", "Дебетовые карты",
				debitElement.getText());

		//выбрать кнопку - заказать онлайн
		String selectButtonXPath = "//*[@data-product='Молодёжная карта']//span";
		WebElement selectButton = driver.findElement(By.xpath(selectButtonXPath));
		scrollToElementJs(selectButton);
		waitUtilElementToBeClickable(selectButton);
		selectButton.click();

		scrollApp();

		String youngCardXPath = "//a[contains(@data-test-id, 'Page')]/../..//child::h1";
		WebElement youngCard = driver.findElement(By.xpath(youngCardXPath));
		Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому", "Молодёжная карта",
				youngCard.getText());

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//выбрать кнопку - оформить онлайн
		String getButtonCardsOnlineXPath = "//a[contains(@data-test-id, 'Page')]//child::span";
		WebElement getButtonCardsOnline = driver.findElement(By.xpath(getButtonCardsOnlineXPath));
		scrollToElementJs(driver.findElement(By.xpath("//*[contains(@data-name,'series')]")));
		waitUtilElementToBeClickable(getButtonCardsOnline);
		getButtonCardsOnline.click();

		scrollToElementJs(driver.findElement(By.xpath("//h2[text()='Личные данные' and  contains(@class,'odc')]")));

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String fieldXPath = "//input[@data-name='%s']";
		fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "lastName"))), lastName);
		fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "firstName"))), firstName);
		fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "middleName"))), middleName);
		fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "birthDate"))), "21.01.1999");
		fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "email"))), "avtovaz@mail.ru");
		fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "phone"))), "9993432312");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		scrollToElementJs(driver.findElement(By.xpath("//*[contains(@aria-label,'Паспорт')]")));

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String furtherButtonXPath = "//span[contains(text(), 'Далее')]";
		WebElement furtherButton = driver.findElement(By.xpath(furtherButtonXPath));
		waitUtilElementToBeClickable(furtherButton);
		furtherButton.click();

		checkErrorMessageAtField(driver.findElement(By.xpath(String.format(fieldXPath, "series"))), "Обязательное поле");
		checkErrorMessageAtField(driver.findElement(By.xpath(String.format(fieldXPath, "number"))), "Обязательное поле");
		String buttonXPath = "//*[@class='odcui-error__text' and contains(text(),'Обязательное поле')]";
		WebElement button = driver.findElement(By.xpath(buttonXPath));
		Assert.assertEquals("Проверка ошибки у поля не была пройдена",
				"Обязательное поле", button.getText());

	}
	public void scrollApp() {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,-250)");
	}
	public void scrollToElementJs(WebElement element) {
		JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
		javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void waitUtilElementToBeClickable(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public void waitUtilElementToBeVisible(By locator) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void waitUtilElementToBeVisible(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public void fillInputField(WebElement element, String value) {
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
	public void checkErrorMessageAtField(WebElement element, String errorMessage) {
		element = element.findElement(By.xpath("../child::div"));
		Assert.assertEquals("Проверка ошибки у поля не была пройдена",
				errorMessage, element.getText());
	}

}
