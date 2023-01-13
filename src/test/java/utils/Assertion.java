package utils;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

//import org.apache.commons.io.FileUtils;
//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.imgproc.Imgproc;
import API.common.Environment;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import org.testng.asserts.SoftAssert;
import pages.BasePage;
import utils.extentreports.ExtentTestManager;
import utils.logs.Log;

public class Assertion extends BasePage {
    String screenshotFolder = "./screenshots/";
    static double imageCompareRate = 0.97;
    static int waitE = Integer.parseInt(Util.getPropertyValue("WAIT"));
    static WebDriver driver;

    String testID = new String();
    public Assertion(WebDriver driver, Environment.Chain test_env) {
        super(driver, test_env);
    }

    public static void assertAlmostEqual(String actual, String expect){
        double exp_;
        double act_;
        if (expect.contains("-")){
            if (actual.contains("-")){
                exp_ = Double.parseDouble(expect.replace("-",""));
                act_ = Double.parseDouble(actual.replace("-",""));
                Assert.assertTrue(almostEqual(exp_, act_));
            }else assertNG("Actual and Expect are different!");
        }else if (!actual.contains("-")){
            exp_ = Double.parseDouble(expect.replace(",",""));
            act_ = Double.parseDouble(actual.replace(",",""));
            Assert.assertTrue(almostEqual(exp_, act_));
        }else assertNG("Actual and Expect are different!");
    }

    public static boolean almostEqual(double a, double b){
        double eps = Double.parseDouble(Util.getPropertyValue("EPS"));
        boolean result = false;
        if (a == 0 && b == 0){
            result = true;
        }else if (!(a==0)){
            result = (Math.abs(a-b)/a)<eps;
        }
        return result;
    }

    public static void assertElementExists(By elem) {
        String msg = "";
        //captureScreen(driver, testcase);
        try{
            if(driver instanceof WebDriver) {
                (new WebDriverWait(((WebDriver)driver), waitE)).until(ExpectedConditions.presenceOfElementLocated(elem));
            }else if (driver instanceof RemoteWebDriver) {
                (new WebDriverWait(((RemoteWebDriver)driver), waitE)).until(ExpectedConditions.presenceOfElementLocated(elem));
            }
            msg = "Element with path " + elem + " exists";
            Assert.assertTrue(true, msg);
        }catch(Exception e) {
            msg = "Element with path " + elem + " doesn't exist";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
            LocalDateTime now = LocalDateTime.now();
            Assert.fail(msg);
        }

    }

    public static void assertElementNotExists(By elem) {
        String msg = "";
        //captureScreen(driver, testcase);
        try{
            if(driver instanceof WebDriver) {
                (new WebDriverWait(((WebDriver)driver), waitE)).until(ExpectedConditions.invisibilityOfElementLocated(elem));
            }else if (driver instanceof RemoteWebDriver) {
                (new WebDriverWait(((RemoteWebDriver)driver), waitE)).until(ExpectedConditions.invisibilityOfElementLocated(elem));
            }
            msg = "Element with path " + elem + " doesn't exists";
            Assert.assertTrue(true, msg);
        }catch(Exception e) {
            msg = "Element with path " + elem + " exist";
            Assert.fail(msg);
        }
    }

    public static void assertElementVisible(By elem) {
        String msg = "";
        //captureScreen(driver, testcase);
        try{
            if(driver instanceof WebDriver) {
                (new WebDriverWait(((WebDriver)driver), waitE)).until(ExpectedConditions.visibilityOfElementLocated(elem));
            }else if (driver instanceof RemoteWebDriver) {
                (new WebDriverWait(((RemoteWebDriver)driver), waitE)).until(ExpectedConditions.visibilityOfElementLocated(elem));
            }
            msg = "Element with path " + elem + " exists";
            Assert.assertTrue(true, msg);
        }catch(Exception e) {
            msg = "Element with path " + elem + " doesn't exist";
            Assert.fail(msg);
        }
    }

    public void assertAttributeValue(By elem, String attribute, By expectedValue, Method method) {
        String attributeValue = null;
        if(driver instanceof WebDriver) {
            attributeValue = ((WebDriver)driver).findElement(elem).getAttribute(attribute);
        }else if (driver instanceof RemoteWebDriver) {
            attributeValue = ((RemoteWebDriver)driver).findElement(elem).getAttribute(attribute);
        }
        assertString(expectedValue, attributeValue);
    }

    public static void assertString(By elem, String expectValue) {
        try {
            String actualValue = readText(elem);
            String msg = "";
            if(actualValue == expectValue && expectValue == null) {
                msg = "Value is correct: " + actualValue;
                Assert.assertTrue(true, msg);
            }
            else if(actualValue.equals(expectValue)) {
                msg = "Value is correct: " + actualValue;
                Assert.assertTrue(true, msg);
            }else {
//                Util.captureScreen(method.getName());
                msg = "Value is not correct: Expected: " + expectValue + "; Actual: " + actualValue;
                Assert.fail(msg);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void assertValueExistInStringArray(String[] strArray, String expectedValue, String testcase) {
        String msg = "";
        for(int i = 0; i < strArray.length; i++) {
            if(strArray[i].equals(expectedValue)) {
                msg = "String "+ expectedValue +" exists in the expected array";
                Assert.assertTrue(true, msg);
                break;
            }else if(i == strArray.length - 1) {
                msg = "String "+ expectedValue +" doesn't exist in the expected array";
                Assert.fail(msg);
            }
        }
    }

    public void assertDate(Date sendTime, String testcase) {
        testID = testcase;
        String messageTimePath = ".//span[@class = 'flxp-chat-message-item flxp-chat-message-time']";
        List<WebElement> allSendTime = driver.findElements(By.xpath(messageTimePath));
        String actualSendTime = allSendTime.get(allSendTime.size()-1).getText();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(actualSendTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int t1 = (int) (date.getTime() % (24*60*60*1000L));
        int t2 = (int) (sendTime.getTime() % (24*60*60*1000L));
        System.out.println(t1);
        System.out.println(t2);
        int diffInMillies = Math.abs(t1 - t2);
        System.out.println(diffInMillies);
        String msg = "";
        if(diffInMillies < 5000) {
            msg = "Time is correct: " + actualSendTime;
            Assert.assertTrue(true, msg);
        }else {
            msg = "Time is not correct: Expected: " + sendTime.toString() + "; Actual: " + actualSendTime ;
            Assert.fail(msg);
        }
    }

    public static void assertBGColorByClassName(String className, String color) {
        Boolean flag = false;
        String script = "return getComputedStyle(document.getElementsByClassName('"+className+"')[0], null).getPropertyValue('background-color');";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String colorJS = js.executeScript(script).toString();
        String hex = Color.fromString(colorJS).asHex();
        System.out.println(hex);
        if (hex.equals(color)) {
            flag = true;
        }
        Assert.assertEquals(flag.booleanValue(), true);
    }

    public static void assertBGColorByClassName(String className, String color, int num) {
        Boolean flag = false;
        String colorJS = "";
        String script = "return getComputedStyle(document.getElementsByClassName('"+className+"')["+num+"], null).getPropertyValue('background-color');";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        colorJS = js.executeScript(script).toString();
        System.out.println(Integer.toString(num) + " " +colorJS);
        String hex = Color.fromString(colorJS).asHex();
        System.out.println(hex);
        if (hex.equals(color)) {
            flag = true;
        }
        Assert.assertEquals(flag.booleanValue(), true);
    }


    public static void assertNG(String tcID){
        SoftAssert softassert = new SoftAssert();
        try {
            softassert.fail("NG: "+tcID);
        }catch (Exception e){
            System.out.println(e);
        }
    }


    public void assertOK(String tcID){
        try {
            Assert.assertTrue(true, tcID);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static void assertFail(String msg){
        Log.error(msg);
        ExtentTestManager.logMessage(Status.FAIL, msg);
        Assert.assertEquals(true, false);
    }

    public static void assertWarn(String msg){
        Log.error(msg);
        ExtentTestManager.logMessage(Status.WARNING, msg);
    }

}
