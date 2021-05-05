package com.MailerApplication;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.naming.ldap.ExtendedRequest;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.lib.util.ApplicationUtility;
import com.lib.util.Credentials;
import com.lib.util.ObjectDefinitionLib;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MailerApplication {
	WebDriver driver;
	Credentials cred;
	ApplicationUtility apputil = new ApplicationUtility();
	ObjectDefinitionLib odl = new ObjectDefinitionLib();
	ArrayList<Credentials> user_details = new ArrayList<Credentials>();
	ArrayList<Credentials> receiver_details = new ArrayList<Credentials>();
	String mail_body = "";

	ExtentReports extent;
	ExtentTest test;
	ExtentHtmlReporter htmlReporter;
	int mailcounter;

	@BeforeTest
	public void start_test() throws IOException {
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "//Reports//MailingReport.html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		Reporter.log("---Starting Test Execution---");
		// Browser Launch
		WebDriverManager.chromedriver().setup();
		// System.setProperty("webdriver.chrome.driver", apputil.getValue("chromepath")
		// );
		driver = new ChromeDriver();
		Reporter.log("ChromeDriver Launched", true);
		extent.createTest("Browser Launch", "Chrome Browser lanunched").createNode("test");
	}

	@Test
	public void send_mail() throws AWTException, IOException {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		mail_body = apputil.readFile(apputil.getValue("emailbody"));
		user_details = apputil.readCredentials(apputil.getValue("sendertxt"));
		for (int i = 1; i < user_details.size(); i++) {
			
			driver.get(apputil.getValue("url"));
			
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(odl.signin)));
				if (driver.findElement(By.xpath(odl.signin)).isDisplayed()) {
					driver.findElement(By.xpath(odl.signin)).click();
				}
			} catch (Exception e) {
				System.out.println("exception:" + e.getMessage());
			}
			try {
				WebElement username = driver.findElement(By.xpath(odl.user_details));
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(odl.user_details)));
				if (username.isDisplayed()) {
					driver.findElement(By.partialLinkText(odl.another_account)).click(); //Use another account
				}
			} catch (Exception e) {
				System.out.println("exception:" + e.getMessage());
			}

			// Login into Yahoo Mail
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(odl.username_txtbox)));
			driver.findElement(By.id(odl.username_txtbox)).sendKeys(user_details.get(i).getEmailId().trim());
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(odl.signin_btn)));
			driver.findElement(By.id(odl.signin_btn)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(odl.password_txtbox)));
			driver.findElement(By.id(odl.password_txtbox)).sendKeys(user_details.get(i).getPassword().trim());
			driver.findElement(By.id(odl.next_btn)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(odl.account)));
			if ((driver.findElement(By.id(odl.account))).isDisplayed()) {

				String nodename = "Login Succesfull: " + user_details.get(i).getEmailId();
				extent.createTest("Yahoo Login").createNode(nodename);

				Reporter.log(user_details.get(i).getEmailId() + " have logged in succesfully", true);
				receiver_details = apputil.readCredentials(apputil.getValue("receivertxt"));
				for (int j = 0; j < receiver_details.size(); j++) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(odl.compose_mail)));
					driver.findElement(By.xpath(odl.compose_mail)).click();

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(odl.to)));
					driver.findElement(By.xpath(odl.to)).sendKeys(receiver_details.get(j).getEmailId());

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(odl.subject)));
					String subject_line = (i + 1) + " " + apputil.getValue("name") + " " + (j + 1);
					driver.findElement(By.xpath(odl.subject)).sendKeys(subject_line);

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(odl.mail_body)));
					driver.findElement(By.xpath(odl.mail_body)).sendKeys(mail_body);

					apputil.replaceText(apputil.getValue("attachment"), subject_line);
					System.out.println("value: " + apputil.readFile(apputil.getValue("attachment")));
					driver.findElement(By.xpath(odl.attachment)).click();
					wait.until(ExpectedConditions.elementToBeClickable(By.xpath(odl.attach_file)));
					WebElement attach_file = driver.findElement(By.xpath(odl.attach_file));
					JavascriptExecutor js = (JavascriptExecutor) driver;
					// attaching file
					js.executeScript("arguments[0].click()", attach_file);
					apputil.uploadFile(System.getProperty("user.dir") + apputil.getValue("attachmentPath"));
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(odl.attached_file)));

					if ((driver.findElement(By.xpath(odl.attached_file))).isDisplayed()) {
						Reporter.log("File Attached Succesfully..Sending Mail " + (j + 1) + " with Attachment", true);
					} else {
						Reporter.log("File Attchement UnSuccesfull..Sending Mail " + (j + 1) + " without Attachment",
								true);
					}

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(odl.send_btn)));
					driver.findElement(By.xpath(odl.send_btn)).click();
					Reporter.log((j + 1) + " mail sent!");
					String sent_node = "Mail Sent to " + receiver_details.get(j).getEmailId();

					extent.createTest("Send Mail").createNode(sent_node);

					mailcounter++;
					
				}
			} else {
				Reporter.log(user_details.get(i).getEmailId() + " login unsuccesfull");
				extent.createTest("Send Mail").createNode("Login Unsuccesfull").fail("TestCase Failed");

			}
			// logout
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(odl.account)));
			Actions a = new Actions(driver);
			a.moveToElement(driver.findElement(By.id(odl.account)))
					.moveToElement(driver.findElement(By.xpath(odl.sign_out))).click().perform();
			System.out.println("logged out");

		}
		extent.createTest(mailcounter+" Mails Sent").createNode("Count of Mails:" + mailcounter);
		Reporter.log((mailcounter) + " mails sent");
	}

	@AfterTest
	public void end_test() {
		driver.close();
		extent.flush();

	}

}
