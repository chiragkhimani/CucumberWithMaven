package automation.page;

import automation.locators.FirstPageLocators;
import automation.utility.DriverUtility;
import automation.utility.StepDef;

public class FirstPage extends DriverUtility implements FirstPageLocators {

	public void testPageMethod() {
		System.out.println("First Method");
		StepDef.writeLog("First Test log");
	}

}
