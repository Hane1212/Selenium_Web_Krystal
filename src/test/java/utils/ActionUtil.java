package utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

public class ActionUtil {
    public static WebDriver userADriver;
    public static RemoteWebDriver userBDriver;
    public static RemoteWebDriver userBDriverFirefox;
    public static String testID="NotSet";
    public static String resultFolder = "./test-output";
    public String remoteIP = "http://10.116.42.203:5566/wd/hub";
    public static String EdgeDriverPath = "./libs/MicrosoftWebDriver.exe";
    public static String ChromeDriverPath = "./libs/chromedriver.exe";

    public static String autoIT_FAQ = "";
    public static final int baseWaitTime = 5;
    public static final double similarityThreshold = 0.99;

    public static double timeOutWaitClickEle = 3;
    public static double timeOut = 1;
    public static int toSeconds = 1;
    public static int waitEleExist = 5;
    public static int waitToAssert = 3;
    public static double imageCompareRate = 0.97;

//	public static String domain = getPropertyValue("DOMAIN");
//	public static String SIMdomain = getPropertyValue("SIM");

    public static String screenshotFolder = "./screenshots/";

    public static String IEBrowser = "IEDriver";
    public static String ChromeBrowser = "Chrome";
    public static String EdgeBrowser = "Edge";

    public static String comPwd = "UserManagement";

    static WebDriver driver;
    static Util objUtil;

    public ActionUtil(WebDriver driver) {
        this.driver = driver;
        objUtil = new Util(driver);
    }

    /**
     *
     * @param elem: Click on element - xpath
     * @param expectedElem: Expected element appear after click on element - xpath
     * @param waitSecond
     */
    public static void clickObscuredElement(By elem, By expectedElem, double waitSecond) {
        long startTime = System.currentTimeMillis();
        long timeout = System.currentTimeMillis() - startTime;
        while (timeout < waitSecond * 1000) {
            try {
                Thread.sleep(10);
                driver.findElement(elem).click();
                new WebDriverWait(((WebDriver)driver), 1).until(ExpectedConditions.presenceOfElementLocated(expectedElem));
                break;
            } catch(Exception e) {
            }
            timeout = System.currentTimeMillis() - startTime;
        }
    }





    /**
     * Find & input string to text field by xpath
     * @param elem
     * @param message
     */
    public void sendKeys(By elem, String message) {
//        try {
//        new WebDriverWait(driver,30).until(ExpectedConditions.elementToBeClickable(elem)).sendKeys(message);
            driver.findElement(elem).clear();
            driver.findElement(elem).sendKeys(message);
//        }catch(Exception e) {
//            captureScreen(driver, "sendKeys");
//            System.out.println("Can not found Element");
//            Util.wait(toSeconds);
//            driver.findElement(elem).sendKeys(message);
//        }
    }



    public static void captureScreen(Object driver, String testcase){
        File scrFile = null;
        if(driver instanceof WebDriver)
            scrFile = ((TakesScreenshot)((WebDriver)driver)).getScreenshotAs(OutputType.FILE);
        else if(driver instanceof RemoteWebDriver)
            scrFile = ((TakesScreenshot)((RemoteWebDriver)driver)).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(resultFolder + "/img/" + testcase + ".jpg"));
        } catch (IOException e) {

        }
    }

    /**
     * Random click on element in table
     * @param table
     * @return
     */
    public static WebElement selectItemOnTable(String table) {
        // If there's no item, test case is skippedc
        List<WebElement> allItems = driver.findElements(By.xpath(table));
        if(allItems.size() == 0)
            throw new SkipException("No items in list");
        Random randomItem = new Random();
        WebElement selectedItem = null;
        while(selectedItem == null) {
            selectedItem = allItems.get(randomItem.nextInt(allItems.size()));
        }
//		WebElement selectedItem = allItems.get(randomItem.nextInt(allItems.size()));
//		Util.sleep(2);
        return selectedItem;
    }

    /**
     * @implSpec: select item on table by name
     * @param name: expected value of row
     * @return
     */
    public static WebElement selectItemOnTableByName(By table, String name) {
        // If there's no item, test case is skippedc
        List<WebElement> allItems = driver.findElements(table);
        if(allItems.size() == 0)
            throw new SkipException("No items in list");
        WebElement selectedItem;
        Random randomItem;
        while(true) {
            randomItem = new Random();
            int num = randomItem.nextInt(allItems.size());
            selectedItem = allItems.get(num);
            String selectedItemText = selectedItem.getText();
            if(selectedItemText.contains(name)) {
                break;
            }
        }
        return selectedItem;
    }

    /**
     * Select value from combo box by xpath of combo box
     * @param elem
     * @param value
     */
    public static void selectComboxByValue(By elem, final String value) {
        final Select selectBox = new Select(driver.findElement(elem));
        selectBox.selectByValue(value);
    }

    /**
     * Click on radio button
     * @param elem of radio button
     * @param isSelect(True-select, FAISE-NotSelect)
     */
    public void checkRadioBtn(By elem, boolean isSelect) {
        WebElement btn = driver.findElement(elem);
        if(isSelect) {
            if(!btn.isSelected()) {
                btn.click();
            }
        }else {
            if (btn.isSelected()) {
                btn.click();
            }
        }
    }

    public static WebElement expandRootElement(By elem) {
        WebElement element = driver.findElement(elem);
        WebElement ele = (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot",element);
        return ele;
    }

}