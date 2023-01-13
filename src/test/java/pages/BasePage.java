package pages;

import java.time.Duration;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import API.common.Environment;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.logs.Log;

public class BasePage {
    public static WebDriver driver;
//    public static Environment.Chain test_env;
    public static WebDriverWait wait;
    public static String main_url = Environment.base_url;
    public static Environment.Chain test_env = Environment.getChainByName(Environment.test_chain);
    //Constructor
    public BasePage(WebDriver driver, Environment.Chain test_env) {
        this.test_env = test_env;
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Author: HuongTT
     * Updated: 2022-01-23
     * @apiNote : Open page in new tab
     * @param URL
     */
    public static void getPageInNewTab(String URL){
        driver.switchTo().newWindow(WindowType.WINDOW);
        driver.get(URL);
    }
    //Click Method
    public static void click(By by) {
        waitVisibility(by).click();
        Log.info("Click on elem:" +by);
    }
    /**
     * @apiNote Click item by java script
     * @param elem
     */
    public static void clickByJS(By elem){
        WebElement element = driver.findElement(elem);
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", element);
        Log.info("Click on elem by Java: "+ elem);
    }

    /**
     * @apiNote Click item by action
     * @param elem
     */
    public static void clickByAction(By elem) {
        Actions act = new Actions(driver);
        act.moveToElement(driver.findElement(elem)).click().build().perform();
    }

    public static void clickElem(By elem, int wait) {
        try {
            if(driver instanceof WebDriver) {
                (new WebDriverWait(((WebDriver)driver), wait)).until(ExpectedConditions.visibilityOfElementLocated(elem));
            }else if (driver instanceof RemoteWebDriver) {
                (new WebDriverWait(((RemoteWebDriver)driver), wait)).until(ExpectedConditions.visibilityOfElementLocated(elem));
            }
        }catch(Exception e) {

        }
        driver.findElement(elem).click();
    }

    //Write Text
    public static void writeText(By by, String text) {
//        waitVisibility(by).sendKeys(text);
        if(!readText(by).equals(text)){
            waitVisibility(by).clear();
            waitVisibility(by).sendKeys(text);
        }
    }

    public static void cleanText(By by){
        waitVisibility(by).clear();
    }

    public static void writeTextByJava(By elem, String text){
        WebElement element = driver.findElement(elem);
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].value='"+text+"';", element);
    }

    //Read Text
    public static String readText(By by) {
        return waitVisibility(by).getText();
    }

    public static String getPlaceHolder(By item){
        return waitVisibility(item).getAttribute("placeholder");
    }

    public static String readAttribute(By item, String attribute){
        return waitVisibility(item).getAttribute(attribute);
    }
//  ------------------------------------------------------WAIT------------------------------------------
    //Wait
    public static WebElement waitVisibility(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static void waitInvisibility(By elem){
        wait.until(ExpectedConditions.invisibilityOfElementLocated(elem));
    }

    public static boolean waitElemExist(By elem, long waitSecond) {
        // Wait new URL display
        boolean isExist = isElemExist(elem);
        long startTime = System.currentTimeMillis();
        long timeout = System.currentTimeMillis() - startTime;
        while (timeout < waitSecond * 1000) {
            if (isExist) {
                break;
            }
            isExist = isElemExist(elem);
            timeout = System.currentTimeMillis() - startTime;
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return isExist;
    }
    /**
     * Wait for element appear and clickable
     * @param elem
     * @param waitSecond
     */
    public static void waitForElementClickable(By elem, int waitSecond) {
        try {
            if(driver instanceof WebDriver) {
                (new WebDriverWait(((WebDriver)driver), waitSecond)).until(ExpectedConditions.elementToBeClickable(elem));
            }else if (driver instanceof RemoteWebDriver) {
                (new WebDriverWait(((RemoteWebDriver)driver), waitSecond)).until(ExpectedConditions.elementToBeClickable(elem));
            }
        }catch(Exception e) {

        }
    }
    /**
     * Author: HuongTT
     * Updated: 2022-01-24
     * @param waitSecond
     */
    public static void wait(int waitSecond) {
        try {
            Thread.sleep(waitSecond*1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * Waits for a window with a desired url to come up.<br>
     * If it does not come up in 5 seconds, it will fail.
     * This method will set the current active window to the window requested.  If you need to switch back to the previous window, use {@link #switchToWindowByURLName(String)}
     * @param regex The title of the window. Regex enabled.
     * @return
     * @throws InterruptedException
     */
    public void waitForWindow(String regex) throws InterruptedException {
        Set<String> windows = driver.getWindowHandles();
        int attempts = 0;
        for (String window : windows) {
            try {
                driver.switchTo().window(window);
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(driver.getCurrentUrl());
                if (m.find())
                {
                    break;
                }

            } catch(NoSuchWindowException e) {
                if (attempts <= 5) {
                    attempts++;
                    wait(10);
                } else {
                    System.out.println("Window with url: " + regex + " did not appear after 5 tries. Exiting.");
                    // found
                }
            }
        }
    }

    //  ------------------------------------------------------Check Exist------------------------------------------
    /**
     * @apiNote : check if element exist in a range of time
     * @param elem: xpath of expected element
     * @param waitSecond: time out
     * @return boolean
     */
    public static boolean isElemExist(By elem, int waitSecond) {
        boolean flag = false;
        try {
            if(driver instanceof WebDriver) {
                (new WebDriverWait(((WebDriver)driver), waitSecond)).until(ExpectedConditions.visibilityOfElementLocated(elem));
            }else if (driver instanceof RemoteWebDriver) {
                (new WebDriverWait(((RemoteWebDriver)driver), waitSecond)).until(ExpectedConditions.visibilityOfElementLocated(elem));
            }
            flag = true;
        }catch(Exception e) {

        }
        return flag;
    }

    /**
     * @apiNote : Check if element exist
     * @param elem
     * @return
     */
    public static boolean isElemExist(By elem){
        boolean flag = false;
        try {
            if(!driver.findElements(elem).isEmpty()){
                flag = true;
            }
        }catch (Exception e){

        }
        return flag;
    }

    public static boolean isContentExist(String text){
        By elem = By.xpath("//*[text()='"+text+"']");
        return isElemExist(elem);
    }

    public static boolean isElemEnable(By elem){
        return driver.findElement(elem).isEnabled();
    }

    /**
     * Click on radio button
     * @param elem of radio button
     * @param isSelect(True-select, FAISE-NotSelect)
     */
    public static void checkRadioBtn(By elem, boolean isSelect) {
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

    /**
     * @apiNote : Switch the current window to other based on title
     * @param title
     * @return
     */
    public static boolean switchToWindowByTitle(String title){
        boolean flag = false;
        Set<String> windows = driver.getWindowHandles();
        for (String window : windows) {
            driver.switchTo().window(window);
//            System.out.println("Current URL: "+driver.getCurrentUrl());
            if (driver.getTitle().contains(title)) {
                flag = true;
                break;
                //TODO
            }
        }
        return flag;
    }

    /**
     * Switches the current window based on URL name
     * @param regex A regular expression window title.
     * @return
     */
    public static void switchToWindowByURLName(String regex) {
        Set<String> windows = driver.getWindowHandles();
        for (String window : windows) {
            driver.switchTo().window(window);
            if (driver.getCurrentUrl().contains(regex)) {
                break;
                //TODO
            }
        }
    }

    public static void scrollToEndPage() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        //This will scroll the page till the element is found
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public static void scrollDown (){
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,250)");
    }


}