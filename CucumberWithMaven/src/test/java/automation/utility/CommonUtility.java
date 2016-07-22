package automation.utility;

public class CommonUtility {

	DataPropertyReader dataProperties = new DataPropertyReader();

	/**
	 * Wait for specified millisecond
	 * 
	 * @param millis
	 */

	
	public static void wait(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String convertXpathFromText(String text) {
		String xpath = "//*[text()=\"%s\"]";
		xpath = String.format(xpath, text);
		return xpath;
	}

}
