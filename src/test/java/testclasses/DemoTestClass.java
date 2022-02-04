package testclasses;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.utils.FileUtil;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;

public class DemoTestClass {

	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest extentTest;

	WebDriver driver;

	@BeforeClass
	public void beforeClass() {

		htmlReporter = new ExtentHtmlReporter("./reports/extent.html");
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setDocumentTitle("Automation Reports");
		htmlReporter.config().setReportName("Automation Test Results");
		htmlReporter.config().setTheme(Theme.STANDARD);

		extent = new ExtentReports();
		extent.setSystemInfo("Oraganization", "Misra Solutions");
		extent.setSystemInfo("Browser", "Chrome");
		extent.attachReporter(htmlReporter);

		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\nammisra\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get("https:www.google.com");
	}

	@Test
	public void TestOne() {

		extentTest = extent.createTest("TestOne");
	}

	@Test
	public void TestTwo() {

		extentTest = extent.createTest("TestTwo");
		Assert.fail();
	}

	@Test
	public void TestThree() {

		extentTest = extent.createTest("TestThree");
		throw new SkipException("Skip Test Executed");
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {

		String methodName = result.getMethod().getMethodName();
		if (result.getStatus() == ITestResult.FAILURE) {

			String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
			extentTest.fail("<details><summary><b><font color=red> Exception Occured, click to see details:"
					+ "</font></b></summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details> \n");

			String path = takeScreenshot(result.getMethod().getMethodName());
			try {
				extentTest.fail("<b><font color=red>" + "Screenshot of failure" + "</font></b>",
						MediaEntityBuilder.createScreenCaptureFromPath(path).build());

			} catch (IOException e) {
				extentTest.fail("Test Failed, cannot attach the screenshot");
			}

			String logText = "<b> Test Method" + methodName + "Failed</b>";
			Markup m = MarkupHelper.createLabel(logText, ExtentColor.RED);
			extentTest.log(Status.FAIL, m);

		} else if (result.getStatus() == ITestResult.SUCCESS) {
			String logText = "<b> Test Method" + methodName + "Successful</b>";
			Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
			extentTest.log(Status.PASS, m);

		}

		else if (result.getStatus() == ITestResult.SKIP) {
			String logText = "<b> Test Method" + methodName + "Skipped</b>";
			Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
			extentTest.log(Status.SKIP, m);

		}

	}

	public String takeScreenshot(String methodName) {

		String fileName = getScreenshotName(methodName);
		String directory = System.getProperty("user.dir") + "/screenshots/";
		new File(directory).mkdirs();
		String path = directory + fileName;

		try {
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File(path));
			System.out.println("************************************************");
			System.out.println("Screenshot stored at:" + path);
			System.out.println("************************************************");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return path;
	}

	public static String getScreenshotName(String methodName) {
		Date d = new Date();
		String fileName = methodName + "_" + d.toString().replace(":", "_").replace(" ", "_") + ".png";

		return fileName;

	}

	@AfterClass
	public void afterClass() {
		driver.quit();
		extent.flush();
	}

}
