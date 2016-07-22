package automation.utility;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DriverUtility {
	DataPropertyReader dataProperties = new DataPropertyReader();
	public static WebDriver driver;
	ConfigPropertyReader properties = new ConfigPropertyReader();

	public boolean waitForAjaxToComplete() {
		System.out.println("waiting for Ajax to complete");
		return (boolean) (new WebDriverWait(driver, 60)).until(new ExpectedCondition<Object>() {
			public Boolean apply(WebDriver d) {
				JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
				return (Boolean) javascriptExecutor.executeScript("return jQuery.active == 0");
			}
		});
	}

	public void sendKeysByJs(WebElement element, String newValue) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].setAttribute('value', arguments[1])", element, newValue);

	}

	public void verifyTrue(boolean condition, String passMessage, String failedMessage) {
		if (condition) {
			StepDef.writeLog(String.format(dataProperties.getReporterAssertPassMsg(), passMessage));
		} else {
			DataPropertyReader.setIsTestFailed(true);
			StepDef.writeLog(String.format(dataProperties.getReporterAssertFailMsg(), failedMessage, getScreenShot()));
		}
	}

	public void verifyText(WebElement element, String text, String label) {
		System.out.println(element.getText());
		System.out.println(text);
		if (element.getText().equalsIgnoreCase(text)) {
			StepDef.writeLog(String.format(dataProperties.getAssertTextPass(), label, text, label, element.getText()));
		} else {
			DataPropertyReader.setIsTestFailed(true);
			StepDef.writeLog(String.format(dataProperties.getAssertTextFail(), label, text, label, element.getText(),
					getScreenShot()));
		}
	}

	public void verifyNotPresent(String xpath) {
		boolean flag;
		try {
			driver.findElement(By.xpath(xpath));
			flag = true;
			DataPropertyReader.setIsTestFailed(true);
		} catch (Exception e) {
			flag = false;
		}
	}

	public boolean isDisplayed(String locStrategy, String loc) {
		if (isPresent(locStrategy, loc)) {
			if (getElement(locStrategy, loc).isDisplayed()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public WebElement getElement(String locStrategy, String loc) {
		if (locStrategy.equalsIgnoreCase("xpath")) {
			return driver.findElement(By.xpath(loc));
		} else if (locStrategy.equalsIgnoreCase("CSS")) {
			return driver.findElement(By.cssSelector(loc));
		} else {
			return null;
		}
	}

	public boolean isPresent(String locStrategy, String loc) {
		boolean flag = false;
		try {

			if (locStrategy.equalsIgnoreCase("xpath")) {
				driver.findElement(By.xpath(loc));
				flag = true;
				DataPropertyReader.setIsTestFailed(true);
			} else if (locStrategy.equalsIgnoreCase("CSS")) {
				driver.findElement(By.cssSelector(loc));
				flag = true;
				DataPropertyReader.setIsTestFailed(true);
			} else {
				System.out.println("Invalid locator strategy");
			}

		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * Verify specified element contains specified text or not and print result
	 * into the report
	 * 
	 * @param element
	 * @param text
	 * @param label
	 */
	public void verifyTextContains(WebElement element, String text, String label) {
		if (element.getText().contains(text)) {
			StepDef.writeLog(String.format(dataProperties.getAssertTextPass(), label, text, label, element.getText()));
		} else {
			DataPropertyReader.setIsTestFailed(true);
			StepDef.writeLog(String.format(dataProperties.getAssertTextFail(), label, text, label, element.getText(),
					getScreenShot()));
		}
	}

	/**
	 * Verify element is visible or not and print result into the report
	 * 
	 * @param element
	 * @param label
	 */

	public void verifyVisible(WebElement element, String label) {
		CommonUtility.wait(3000);
		if (element.isDisplayed()) {
			StepDef.writeLog(String.format(dataProperties.getAssertElementVisiblePassMsg(), label, label));
		} else {
			DataPropertyReader.setIsTestFailed(true);
			StepDef.writeLog(
					String.format(dataProperties.getAssertElementVisibleFailMsg(), label, label, getScreenShot()));
		}
	}

	public String getScreenShot() {
		DateFormat df = new SimpleDateFormat("DD_MMM_yyyy_hh-mm-ss");
		String screenshotName = df.format(new Date());
		String screenshotPath = null;
		File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		try {
			screenshotPath = System.getProperty("user.dir") + "\\img\\" + screenshotName + ".png";
			FileUtils.copyFile(scrFile, new File(screenshotPath));

		} catch (IOException e) {
			e.printStackTrace();
		}

		return screenshotPath;
	}

	public void waitForElementClickable(WebElement ele) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Long.parseLong(properties.getDefaultTimeOut()));
		try {

			wait.until(ExpectedConditions.elementToBeClickable(ele));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void waitForElementNotPresent(String locStrategy, String locator) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Long.parseLong(properties.getDefaultTimeOut()));
		try {
			if (locStrategy.equalsIgnoreCase("xpath")) {
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
			} else if (locStrategy.equalsIgnoreCase("CSS")) {
				wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(locator)));
			} else {
				System.out.println("Invalid locator strategy");
			}
		} catch (Exception e) {

		}

	}

	public List<WebElement> getElementListByXpath(String xpath) {
		waitForElementPresent("xpath", xpath);
		return driver.findElements(By.xpath(xpath));
	}

	public static void setDriver(WebDriver driver) {
		DriverUtility.driver = driver;
	}

	public static WebDriver getDriver() {
		return driver;
	}

	public void clickElementUsingJs(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	public void navigateToUrl(String url) {
		if (System.getProperty("env.baseurl") != null) {
			driver.get(System.getProperty("env.baseurl") + url);
		} else {
			driver.get(properties.getBaseUrl() + url);
		}
	}

	/**
	 * This method used to create driver for specified browser
	 * 
	 * @param browser
	 */

	/**
	 * This method used to wait till particular element present
	 * 
	 * @param locStrategy
	 * @param locator
	 */
	public void waitForElementPresent(String locStrategy, String locator) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Long.parseLong(properties.getDefaultTimeOut()));
		try {
			if (locStrategy.equalsIgnoreCase("xpath")) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
			} else if (locStrategy.equalsIgnoreCase("CSS")) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locator)));
			} else {
				System.out.println("Invalid locator strategy");
			}
		} catch (Exception e) {

		}

	}

	public void waitForElementVisible(WebElement ele) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Long.parseLong(properties.getDefaultTimeOut()));
		wait.until(ExpectedConditions.visibilityOf(ele));
	}

	public void waitForElementNotVisible(String loc) throws InterruptedException {
		Thread.sleep(2000);
		WebDriverWait wait = new WebDriverWait(getDriver(), Long.parseLong(properties.getDefaultTimeOut()));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(loc)));
	}

	/**
	 * Selenium method to get element by Text
	 * 
	 * @param text
	 * @return
	 */
	public WebElement getElementByText(String text) {
		String xpath = CommonUtility.convertXpathFromText(text);
		waitForElementPresent("xpath", xpath);
		return driver.findElement(By.xpath(xpath));
	}

	/**
	 * Selenium method to get element by Xpath
	 * 
	 * @param text
	 * @return
	 */

	public WebElement getElementByXpath(String xpath) {
		waitForElementPresent("xpath", xpath);
		return driver.findElement(By.xpath(xpath));
	}

	/**
	 * Selenium method to get element by CSS
	 * 
	 * @param text
	 * @return
	 */

	public WebElement getElementByCSS(String CSS) {
		waitForElementPresent("CSS", CSS);
		return driver.findElement(By.cssSelector(CSS));
	}

	/**
	 * Selenium method to get element list by CSS
	 * 
	 * @param text
	 * @return
	 */

	public List<WebElement> getElementListByCSS(String CSS) {
		waitForElementPresent("CSS", CSS);
		return driver.findElements(By.cssSelector(CSS));
	}

	/**
	 * Open Link in new window
	 * 
	 * @param element
	 * @return
	 */

	public String openLinkInNewWindow(WebElement element) {
		Actions actionOpenLinkInNewTab = new Actions(driver);
		actionOpenLinkInNewTab.moveToElement(element).keyDown(Keys.COMMAND).keyDown(Keys.SHIFT).click(element)
				.keyUp(Keys.COMMAND).keyUp(Keys.SHIFT).perform();
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		return tabs.get(0);
	}

	/**
	 * This is used to check element is present or not
	 * 
	 * @param locator
	 * @return
	 */

	/**
	 * This is used to switch to new window
	 * 
	 * @return
	 * @throws Exception
	 */

	public String switchToNewWindow() throws Exception {
		String winHandleBefore = getDriver().getWindowHandle();
		Thread.sleep(5000);
		for (String winHandle : getDriver().getWindowHandles()) {
			getDriver().switchTo().window(winHandle);
		}
		return winHandleBefore;
	}

}
