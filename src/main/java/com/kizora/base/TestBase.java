package com.kizora.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.Optional;

//import com.relevantcodes.extentreports.ExtentReports;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.relevantcodes.extentreports.LogStatus;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.kizora.util.Constant;
import com.kizora.util.TakeScreenshot;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {
	// global variables
	public static WebDriver driver;
	public static Properties prop;
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest logger;

	public static String TestCaseName;
	public static String childTestCaseName;

	public TestBase() {

		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(
					System.getProperty("user.dir") + "/src/main/resources/config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void initialization(@Optional("") String browser) {

		String browserName = null;
		if (browser.contains("")) {
			browser = prop.getProperty("browser");
			browserName = browser;
		} else
			browserName = browser;

		System.out.println(browserName);

		if (browserName.equals("chrome")) {

			ChromeOptions options = new ChromeOptions();
			options.addArguments("--remote-allow-origins=*");

			WebDriverManager.chromedriver().setup();

			// System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")
			// + "/drivers/chromedriver.exe");
			driver = new ChromeDriver(options);

		} else if (browserName.equals("FF")) {

			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--remote-allow-origins=*");

			WebDriverManager.firefoxdriver().setup();
			// System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") +
			// "/drivers/geckodriver.exe");
			driver = new FirefoxDriver();

		} else if (browserName.equals("edge")) {

			EdgeOptions options = new EdgeOptions();
			options.addArguments("--remote-allow-origins=*");

			WebDriverManager.edgedriver().setup();
			// System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") +
			// "/drivers/msedgedriver.exe");
			driver = new EdgeDriver();

		} else if (browserName.equals("headless")) {

			WebDriverManager.chromedriver().setup();
			// System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")
			// + "/drivers/chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("headless");
			options.addArguments("window-size=1200x600");
			driver = new ChromeDriver(options);
		}

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		TestBase.driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Constant.PAGE_LOAD_TIMEOUT));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constant.IMPLICIT_WAIT));

		driver.get(prop.getProperty("url"));

	}

	public static void setExtent(String Title, String ReportName, String Environment) {

		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/ExtentReports/TestReport.html");
		// Create an object of Extent Reports
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host Name", System.getProperty("os.name"));
		extent.setSystemInfo("Environment", Environment);
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		htmlReporter.config().setDocumentTitle(Title);
		// Name of the report
		htmlReporter.config().setReportName(ReportName);
		// Dark Theme
		htmlReporter.config().setTheme(Theme.DARK);

	}

	public static String getScreenShot(WebDriver driver, String screenshotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		// after execution, you could see a folder "FailedTestsScreenshots" under src
		// folder
		String destination = System.getProperty("user.dir") + "/Screenshots/" + screenshotName + dateName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	public static void endReport() {
		extent.flush();

	}

	public static void setExtentLogAndAddScreenshot(ITestResult result) throws IOException {

		if (result.getStatus() == ITestResult.FAILURE) {
			// MarkupHelper is used to display the output in different colors
			logger.log(Status.FAIL,
					MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
			logger.log(Status.FAIL,
					MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
			// To capture screenshot path and store the path of the screenshot in the string
			// "screenshotPath"
			// We do pass the path captured by this method in to the extent reports using
			// "logger.addScreenCapture" method.
			// String Scrnshot=TakeScreenshot.captuerScreenshot(driver,"TestCaseFailed");
			String screenshotPath = getScreenShot(driver, result.getName());
			// To add it in the extent report
			logger.fail("Test Case Failed Snapshot is below " + logger.addScreenCaptureFromPath(screenshotPath));
		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.log(Status.SKIP,
					MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.log(Status.PASS,
					MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
		}
		// extent.endTest(extentTest);
	}

}
