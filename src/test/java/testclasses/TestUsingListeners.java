package testclasses;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class TestUsingListeners {

	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest extentTest;

	public WebDriver driver;

	@BeforeClass
	public void beforeClass() {

		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\nammisra\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.get("https:www.google.com");
	}

	@Test
	public void TestOne() {

		System.out.println("Executing Test Case One");
	}

	@Test
	public void TestTwo() {

		System.out.println("Executing Test Case Two");
		Assert.fail();
	}

	@Test
	public void TestThree() {

		System.out.println("Executing Test Case Three");
		throw new SkipException("Skip Test Executed");
	}

	@AfterClass
	public void afterClass() {
		driver.quit();
	}

}
