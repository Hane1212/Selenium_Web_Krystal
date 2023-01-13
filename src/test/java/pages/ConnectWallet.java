/*
Create date:
Author:
Desciption:
Flow to connect wallet:
1. Show popup Import Wallet
2. Click checkbox Terms of Use
3. Select Wallet
4. ..
 */
package pages;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

public class ConnectWallet {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor executor;
    Actions act;
    String strPassword = "23698788";

    //List of elements in flow need to use to connect wallet
    By popupImportWallet = By.xpath("//div[@class='modal modal--active']");
    By ckbTermOfUse = By.name("checkedB");
    By iconMetaMaskWallet = By.xpath("//div[@class='account__icon metamask']");

    //Elements for unlock
    By txtPassword = By.id("password");
    By btnUnlock = By.xpath("//button[@class='button btn--rounded btn-default']");
    By btnNextToUnlock = By.xpath("//button[@class='button btn--rounded btn-primary']");
    By btnRemindMeLater = By.xpath("//button[@class='button btn--rounded btn-secondary first-time-flow__button']");
    //Finish unlock

    By btnNextToConnectWallet = By.xpath("//button[@class='button btn--rounded btn-primary']");
    By btnConnectWallet = By.xpath("//button[@class='button btn--rounded btn-primary page-container__footer-button']");

    public void connectMetaMaskWallet() throws InterruptedException {
        motoPopUpImportWallet();
        clickCkbTermsOfUse();
        clickIconMetaMaskWallet();

        Thread.sleep(1000);
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        int intNumOfTabs = tabs.size();
        //Check if number of tabs > 0 -> Switch to Tab1
        if(intNumOfTabs>0){
            driver.switchTo().window(tabs.get(1));
            String currentURL = driver.getCurrentUrl();
            System.out.println(currentURL);
            unlockWallet(strPassword);
        }

        clickBtnNextToConnectWallet();
        clickBtnConnectWallet();

        if(intNumOfTabs>0){
            driver.close();
            driver.switchTo().window(tabs.get(0));
        }


    }

    private void unlockWallet(String strPassword){
        inputPassword(strPassword);
        clickBtnUnlock();
        clickBtnNextToUnlock();
        clickBtnRemindMeLater();
    }
    private void motoPopUpImportWallet(){
        WebElement ePopUpImportWallet = wait.until(ExpectedConditions.visibilityOfElementLocated(popupImportWallet));
        Actions act = new Actions(driver);
        act.moveToElement(ePopUpImportWallet).perform();
    }

    private void clickCkbTermsOfUse(){
       driver.findElement(ckbTermOfUse).click();
    }

    private void clickIconMetaMaskWallet(){
        driver.findElement(iconMetaMaskWallet).click();
    }

    private void inputPassword(String strPassword){
        wait.until(ExpectedConditions.visibilityOfElementLocated(txtPassword));
        driver.findElement(txtPassword).sendKeys(strPassword);
    }

    private void clickBtnUnlock(){
        driver.findElement(btnUnlock).click();
    }

    private void clickBtnNextToUnlock(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnNextToUnlock));
        driver.findElement(btnNextToUnlock).click();
    }

    private void clickBtnRemindMeLater(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnRemindMeLater));
        driver.findElement(btnRemindMeLater).click();
    }
    private void clickBtnNextToConnectWallet(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnNextToConnectWallet));
        driver.findElement(btnNextToConnectWallet).click();
    }

    private void clickBtnConnectWallet(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnConnectWallet));
        driver.findElement(btnConnectWallet).click();
    }

}
