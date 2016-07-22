package automation.utility;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import automation.page.FirstPage;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;

public class StepDef {
	
	private static Scenario scenario;
	public static WebDriver driver;
	ConfigPropertyReader properties = new ConfigPropertyReader();
	static String campName;

	@Before
	public void setUp(Scenario scenario) {
		setUpBrowser();
		StepDef.scenario = scenario;
		DriverUtility.setDriver(driver);
	}

	@After
	public void embedScreenshot(Scenario scenario) {
		if (scenario.isFailed()) {
			try {

				scenario.embed(getScreenShot(), "image/png");
			} catch (WebDriverException wde) {
				System.err.println(wde.getMessage());
			} catch (ClassCastException cce) {
				cce.printStackTrace();
			}
		}
	}

	// Driver manager method

	public byte[] getScreenShot() {
		byte[] scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
		return scrFile;
	}

	public static void writeLog(String log) {
		log = String.format("<b><p style =\"color:#228B22;\">%s</p></b>", log);
		scenario.write(log);
	}

	private void setUpBrowser() {
		chooseBrowser(properties.getDefaultBrowser());
		driver.manage().window().maximize();
	}

	/**
	 * This method used to create driver for specified browser
	 * 
	 * @param browser
	 */
	private void chooseBrowser(String browser) {
		System.out.println(browser);
		switch (browser) {
		case "firefoxDriver":

			driver = new FirefoxDriver();
			break;

		case "chromeDriver":

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+properties.getDriverFile());
			driver = new ChromeDriver();
			break;
		}
	}

	/**
	 * This method used to wait till particular element present
	 * 
	 * @param locStrategy
	 * @param locator
	 */

	public static WebDriver getDriver() {
		return driver;
	}

	public void attachedScreenshot() {

	}
	
	FirstPage firstPage= new FirstPage();
	@Given("^Test method$")
	public void testMethod() {
		firstPage.testPageMethod();
	}
}
