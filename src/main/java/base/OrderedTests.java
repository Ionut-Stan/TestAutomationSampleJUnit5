package base;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Class will test an end-to-end scenario of placing a successful order on an online shop using ordered tests in JUnit5
 * 
 * @author stanionut
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderedTests {

	private static WebDriver driverChrome;

	private static String url = "https://www.evomag.ro";
	private static String email = "LoginClientForm_Email";
	private static String password = "LoginClientForm_Password";
	private static String telephone = "Client_Phone";
	private static String county = "//select[@id='PartnerAddress_county']/option[2]";
	private static String town = "//select[@id='PartnerAddress_CityId']/option[2]";
	private static String address = "//textarea[@id='PartnerAddress_Address']";

	@Test
	@Order(1)
	@DisplayName("Tests if login with a valid user is possible")
	public void test01() {

		initializeBrowser();
		dontAcceptNotifications();
		acceptAllCookies();
		navigateToLoginPage();

		WebElement txtEmail = driverChrome.findElement(By.id(email));
		txtEmail.clear();
		txtEmail.sendKeys("best_jonutz@yahoo.com");

		WebElement txtPassword = driverChrome.findElement(By.id(password));
		txtPassword.clear();
		txtPassword.sendKeys("JUnit5");

		WebElement btnLogin = driverChrome.findElement(By.xpath("//div[@class='container_principal_dr']//input[@class='butn_form']"));
		btnLogin.click();

		WebElement accountItem = driverChrome.findElement(By.xpath("//a[@href='/client/details']"));

		Assertions.assertTrue(accountItem.isEnabled());
	}

	@Test
	@Order(2)
	@DisplayName("Tests if an order can be placed")
	public void test02() {

		addProductToCart();
		placeOrder();

		String actualRegistrationURL = driverChrome.getCurrentUrl();

		String expectedSuccessfulRegistrationURL = url + "/success";

		Assertions.assertEquals(expectedSuccessfulRegistrationURL, actualRegistrationURL, "Your order was not placed.");
	}

	// Utility methods

	/**
	 * Method creates and opens a browser window
	 */
	public static void initializeBrowser() {

		WebDriverManager.chromedriver()
				.win()
				.setup();
		driverChrome = new ChromeDriver();

		driverChrome.manage()
				.window()
				.maximize();

		driverChrome.get(url);
	}

	/**
	 * Deny notifications from the website
	 */
	public void dontAcceptNotifications() {

		Wait<WebDriver> wait = (new FluentWait<WebDriver>(driverChrome)).withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofSeconds(2))
				.ignoring(Exception.class)
				.withMessage("...");

		WebElement btnDenyNotifications = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='pushinstruments_button_deny']")));

		btnDenyNotifications.click();
	}

	/**
	 * Accept all cookies from the website
	 */
	public void acceptAllCookies() {

		WebElement btnAcceptCookies = driverChrome.findElement(By.xpath("//button[@class='gdpr-btn btn-1']"));

		btnAcceptCookies.click();
	}

	/**
	 * Navigate to login page
	 */
	public void navigateToLoginPage() {

		WebElement btnAccount = driverChrome.findElement(By.xpath("//div[@class='account_header']"));

		btnAccount.click();

		WebElement btnLogin = driverChrome.findElement(By.xpath("//a[@class='BtnLoginHead']"));

		Actions actions = new Actions(driverChrome);
		actions.moveToElement(btnLogin)
				.doubleClick(btnLogin)
				.build()
				.perform();

	}

	/**
	 * Add one product to cart
	 */
	public void addProductToCart() {

		WebElement linkProduct2 = driverChrome.findElement(By.xpath("//div[@class='pl_items']//div[@class='pl_image']"));
		JavascriptExecutor jse2 = (JavascriptExecutor) driverChrome;
		jse2.executeScript("arguments[0].click();", linkProduct2);

		Wait<WebDriver> waitAddToCart = (new FluentWait<WebDriver>(driverChrome)).withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofSeconds(2))
				.ignoring(Exception.class)
				.withMessage("...");

		WebElement btnAddToCart = waitAddToCart.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='cart']")));

		btnAddToCart.click();

		Wait<WebDriver> waitSeeCart = (new FluentWait<WebDriver>(driverChrome)).withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofSeconds(2))
				.ignoring(Exception.class)
				.withMessage("...");

		WebElement btnViewCart = waitSeeCart
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='nice_add_to_cart view-cart']")));

		btnViewCart.click();
	}

	/**
	 * Place order
	 */
	public void placeOrder() {

		WebElement txtTelephone = driverChrome.findElement(By.id(telephone));
		txtTelephone.clear();
		txtTelephone.sendKeys("0000000000");

		WebElement slctCounty = driverChrome.findElement(By.xpath(county));
		slctCounty.click();

		Wait<WebDriver> waitTown = (new FluentWait<WebDriver>(driverChrome)).withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofSeconds(2))
				.ignoring(Exception.class)
				.withMessage("...");

		WebElement slctTown = waitTown.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(town)));

		slctTown.click();

		WebElement txtAddress = driverChrome.findElement(By.xpath(address));
		txtAddress.clear();
		txtAddress.sendKeys("Test Address");

		WebElement radioTransport = driverChrome.findElement(By.id("SalesOrder_DeliveryTypeId_0"));
		JavascriptExecutor jse1 = (JavascriptExecutor) driverChrome;
		jse1.executeScript("arguments[0].click();", radioTransport);

		WebElement radioPayment = driverChrome.findElement(By.xpath("//label[@for='SalesOrder[PaymentTypeId][0]']"));
		JavascriptExecutor jse2 = (JavascriptExecutor) driverChrome;
		jse2.executeScript("arguments[0].click();", radioPayment);

		WebElement inputSendOrder = driverChrome.findElement(By.name("sendOrder"));

		// send order button was not clicked to avoid unnecessary spending for this tester
	}
}
