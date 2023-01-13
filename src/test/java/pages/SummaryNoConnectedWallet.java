
package pages;

import API.common.Environment;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import scripts.BaseTest;
import utils.Util;

public class SummaryNoConnectedWallet extends BasePage {
    WebDriverWait wait;
    JavascriptExecutor executor;
    static String Krystal_URL = Environment.base_url;
    By btnConnectWallet = By.xpath("//div[@class='btn btn--gradient mr-5']");
    By summary_btn = By.xpath("//p[text()='Portfolio']");
    By connectWallet_btn = By.xpath("//button[text()='Connect wallet']");
    By supply_tab = By.id("tabs-78--tab-0");

    //Contructor
    public SummaryNoConnectedWallet(WebDriver driver, Environment.Chain test_env){
        super(driver, test_env);
    }

    public void clickBtnConnectWallet(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnConnectWallet));
        WebElement eBtnConnectWallet = driver.findElement(btnConnectWallet);
        executor.executeScript("arguments[0].click();", eBtnConnectWallet);
    }

    public void getMainPage(){
        driver.get(Krystal_URL);
        waitVisibility(summary_btn);
        wait(3);
//        waitVisibility(supply_tab);
    }
}
