package automation.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class is used to read properties from config.properties
 * 
 * @author chirag.khimani
 * 
 */
public class ConfigPropertyReader {
	private String baseUrl;
	private String defaultBrowser;
	private String driverFile;
	private String retryCount;
	private String defaultTimeOut;

	public ConfigPropertyReader() {
		loadProperties(System.getProperty("user.dir") + "\\resources\\config.properties");
	}

	public void loadProperties(String propPath) {
		System.out.println("=====>" + propPath);
		Properties properties = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(propPath);
			properties.load(input);
			baseUrl = properties.getProperty("env.baseurl");
			defaultBrowser = properties.getProperty("selenium.defaultBrowser");
			driverFile = properties.getProperty("webdriver.chrome.driver");
			retryCount = properties.getProperty("retry.count");
			defaultTimeOut = properties.getProperty("selenium.defaultTimeOut");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String getRetryCount() {
		return retryCount;
	}

	public String getDefaultTimeOut() {
		return defaultTimeOut;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getDefaultBrowser() {
		return defaultBrowser;
	}

	public String getDriverFile() {
		return driverFile;
	}

}
