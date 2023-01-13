/*
Create date:
Author:
Description:
Flow to Import wallet:
1. Click button Get Start
2. Click button Import Wallet
3. Click button Agree
4. Input Phrase
5. Input new password
6. Input Confirm password
7. Click checkbox
8. Click button Import


 */
package pages;
import API.common.Environment;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.Util;
import utils.logs.Log;

import java.util.ArrayList;
import java.util.List;

import static scripts.BaseTest.SumPage;

public class ImportMetaWallet extends BasePage {
    JavascriptExecutor executor;
    String strPhrase = "input your phrase";
    String strPassword = "23698788";
    static String metamask_title = "MetaMask";
    String exten = "chrome-extension";

    //List of elements in flow that need to use to connect wallet
    By btnGetStart = By.xpath("//button[@class='button btn--rounded btn-primary first-time-flow__button']");
    By btnImportWallet = By.xpath("//button[@class='button btn--rounded btn-primary first-time-flow__button']");
    By btnAgree = By.xpath("//button[@class='button btn--rounded btn-primary page-container__footer-button']");
    By txtPhrase = By.xpath("(//input[@class='MuiInputBase-input MuiInput-input'])[1]");
    By txtNewPassword = By.xpath("(//input[@class='MuiInputBase-input MuiInput-input'])[2]");
    By txtConfirmPassword = By.xpath("(//input[@class='MuiInputBase-input MuiInput-input'])[3]");
    By ckbTermsOfUse = By.xpath("//div[@class='first-time-flow__checkbox first-time-flow__terms']");
    By btnImport = By.xpath("//button[@class='button btn--rounded btn-primary first-time-flow__button']");
    //	List Wallet
    public static String W_META = Util.getPropertyValue("W_META");
    String W_COIN = Util.getPropertyValue("W_COIN");
    String W_CONNECT = Util.getPropertyValue("W_CONNECT");
//    By addWallet_btn = By.id("menu-button-15");
    By addWallet_btn = By.xpath("//*[contains(@class, \"open-wallets-box\")]");
    By connectWallet_btn = By.xpath("//*[contains(@class, \"open-connect-modal\")]");
    By ImportWallet_opt = By.xpath("//*[text()='Imported Wallet']");
    By WatchedWallet_opt = By.xpath("//*[text()='Watched Wallets']");
    By AcceptTerms_chbox = By.xpath("//*[@class='chakra-checkbox__control css-1d33u3e']");
    //Elements - Meta Mask page
    By exten_start_btn = By.xpath("//*[text()='Get Started']");
    By import_opt_btn = By.xpath("//*[text()='Import wallet']");
    By reject_btn = By.xpath("//*[text()='No Thanks']");
    By Secret_Recovery_Phrase = By.xpath("//*[@placeholder='Paste Secret Recovery Phrase from clipboard']");
    public static By pwd_input = By.id("password");
    By pwd_confirm_input = By.id("confirm-password");
    By check_box = By.xpath("//*[@class='first-time-flow__checkbox first-time-flow__terms']");
    By import_btn = By.xpath("//*[@class='button btn--rounded btn-primary first-time-flow__button']");
    By done_btn = By.xpath("//*[@class='button btn--rounded btn-primary first-time-flow__button']");
    //  Import page - after unlock
    By unlock_btn = By.xpath("//*[@class='button btn--rounded btn-default']");
    By select_network = By.xpath("//*[contains(@class, \"network-display--clickable\")]");
    By ethereum_mainet = By.xpath("//*[text()='Ethereum Mainnet']");
    By next_btn = By.xpath("//*[@class='button btn--rounded btn-primary']");
    By remind_later_btn = By.xpath("//*[@class='button btn--rounded btn-secondary first-time-flow__button']");
    By connect_btn = By.xpath("//*[@class='button btn--rounded btn-primary page-container__footer-button']");
    By close_btn = By.xpath("//*[@class='fas fa-times popover-header__button']");
    By list_wallets = By.xpath("//*[@class='permissions-connect-choose-account__account__label']");
    By checked_wallets = By.xpath("//*[@class='check-box permissions-connect-choose-account__list-check-box fa fa-check-square check-box__checked']");
    //    Confirmation
    static By switch_chain_btn = By.xpath("//*[@class='button btn--rounded btn-primary']");
    static By confirm_btn = By.xpath("//*[@class='button btn--rounded btn-primary page-container__footer-button']");
    //Properties
    int waitE = Integer.parseInt(Util.getPropertyValue("WAIT"));
    int waitM = Integer.parseInt(Util.getPropertyValue("WAIT_META"));

    String address = Util.getPropertyValue("ADDRESS");
    String key = Util.getPropertyValue("PHRASE");
    String pwd = Util.getPropertyValue("PWD");
    String ad_shorkey = Util.getPropertyValue("SHORT_KEY");

    //Contructor
    public ImportMetaWallet(WebDriver driver, Environment.Chain test_env){
        super(driver, test_env);
    }

    public void ImportMetaMaskWallet(){
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        int intNumOfTabs = tabs.size();
        //Check if number of tabs > 0 -> Switch to Tab1
        if(intNumOfTabs>0){
            driver.switchTo().window(tabs.get(1));
        }
        driver.get("chrome-extension://nkbihfbeogaeaoehlefnkodbefgpgknn/home.html#initialize/welcome");

        driver.manage().window().maximize();


        //1.Click btn Get Start
        clickBtnGetStart();
        //2.Click button Import Wallet
        clickBtnImportWallet();
        //3.Click button Agree
        clickBtnAgree();
        //4. Input Phrase
        inputPhrase(strPhrase);
        //5. Input Password
        inputNewPassword(strPassword);
        //6. Input Confirm password
        inputConfirmPassword(strPassword);
        //7. Click checkbox
        clickCheckboxTermofUse();
        //8. Click button Import
        clickBtnImport();

        //If number of tabs > 0 -> Close tab1 and back to Main
        if(intNumOfTabs>0){
            driver.close();
            driver.switchTo().window(tabs.get(0));
        }
}

    private void clickBtnGetStart(){
        waitVisibility(btnGetStart);
        WebElement eBtnGetStart = driver.findElement(btnGetStart);
        executor.executeScript("arguments[0].click();", eBtnGetStart);
    }

    private void clickBtnImportWallet(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnImportWallet));
        WebElement eBtnImportWallet = driver.findElement(btnImportWallet);
        executor.executeScript("arguments[0].click();", eBtnImportWallet);
    }

    private void clickBtnAgree(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(btnAgree));
        WebElement eBtnAgree = driver.findElement(btnAgree);
        executor.executeScript("arguments[0].click();", eBtnAgree);
    }

    private void inputPhrase(String strPhrase){
        wait.until(ExpectedConditions.visibilityOfElementLocated(txtPhrase));
        WebElement eTxtPhrase = driver.findElement(txtPhrase);
        eTxtPhrase.sendKeys(strPhrase);
    }

    private void inputNewPassword(String strNewPassword){
        WebElement eTxtNewPassword = driver.findElement(txtNewPassword);
        eTxtNewPassword.sendKeys(strNewPassword);
    }

    private void inputConfirmPassword(String strConfirmPassword){
        WebElement eTxtConfirmPassword = driver.findElement(txtConfirmPassword);
        eTxtConfirmPassword.sendKeys(strConfirmPassword);
    }

    private void clickCheckboxTermofUse(){
        WebElement eCkbTermofUse = driver.findElement(ckbTermsOfUse);
        eCkbTermofUse.click();
    }

    private void clickBtnImport(){
        WebElement eBtnImport = driver.findElement(btnImport);
        eBtnImport.click();
    }

    public void handleConnectWallet(){
        waitVisibility(connectWallet_btn);
        click(connectWallet_btn);
    }

    public void ConnectWallet() {
        SumPage.goToMainScreen();
        wait(3);
        ComPage.captureImage("BeforeConnectMetaMask");
        if(ComPage.isElemExist(connectWallet_btn)){
            click(connectWallet_btn);
            ComPage.captureImage("ConnectWallet");
            WebElement host = driver.findElement(By.tagName("onboard-v2"));
            JavascriptExecutor js = (JavascriptExecutor)driver;
            SearchContext shadowRoot = (SearchContext)(js.executeScript("return arguments[0].shadowRoot", host));
            WebElement shadowContent = shadowRoot.findElement(By.cssSelector("button[class*='wallet_metamask']"));
            shadowContent.click();
        }else {
            waitVisibility(addWallet_btn);
        }
        wait(3);
        ComPage.captureImage("AfterConnectMetaMask");
    }

    public void startExtension() {
        switchToWindowByURLName(exten);
//		1. Click [start] button
        clickElem(exten_start_btn, waitE);
//        2. Click [import] button
        clickElem(import_btn, waitE);
//        3. Click [No Thanks] button
        clickElem(reject_btn, waitE);
//        objAction.click(reject_btn);
    }
    public void importWallet() {
//    	1. Input Recovery Phase
        writeText(Secret_Recovery_Phrase, key);
//    	2. Input Password
        writeText(pwd_input, pwd);
//    	3. Confirm password
        writeText(pwd_confirm_input, pwd);
//    	4. Check the checkbox
        checkRadioBtn(check_box, true);
//    	5. Click on [Import] button
        clickElem(import_btn, waitE);
//    	Util.waitForElement(driver, done_btn);
//    	Util.ClickElem(driver, done_btn, waitE*5);
    }

    public void HandleExtension() {
//		1. Start Extension
        try {
            waitForWindow(exten);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startExtension();
//		2. Import wallet to extension
        importWallet();
        driver.close();
        switchToWindowByURLName(Environment.krystal_title);
    }

    public void Unlock(){
        wait(waitE);
        switchToWindowByURLName(exten);
        Log.info("Metamask title: "+ driver.getCurrentUrl());
        waitVisibility(unlock_btn);
        writeText(pwd_input, pwd);
//        Click on [Unlock] button
        click(unlock_btn);
//        Click on [Next] button
        wait(3);
        switchToWindowByURLName(Environment.krystal_title);
    }

    public void Unlock_NonExtension(){
        wait(waitE);
        switchToWindowByURLName(exten);
        waitVisibility(unlock_btn);
//        objUtil.waitForElement(unlock_btn, waitE);
        writeText(pwd_input, pwd);
//        Click on [Unlock] button
        click(unlock_btn);
//        Click on [Next] button
        wait(3);
        click(next_btn);
//        Click on [Remind me later] button
        click(remind_later_btn);
//      Select wallet by address
        selectWallet(ad_shorkey);
//        Click on [Next] button to import wallet
        click(next_btn);
//        Click on [Connect] button
        click(connect_btn);
//        Wait to finish connect then close driver
        waitForElementClickable(close_btn, waitE);
        driver.close();
        switchToWindowByURLName(Environment.krystal_title);
    }

    /**
     * Author: HuongTT
     * Updated: 2022-02-18
     * @apiNote Select specify wallet
     * @param address of selected wallet (Ex: 65DF)
     *
     */
    public void selectWallet(String address){
        List<WebElement> listWallets = driver.findElements(list_wallets);
        removeSelectAll();
        for (int i = 0; i<listWallets.size(); i++){
            String wallet = listWallets.get(i).getText();
//            sample: Account 5 (0x5b2...4fb2)
            wallet = wallet.split("\\.\\.\\.")[1].trim();
//            wallet.replace(")", "");
//            StringUtils.substring(wallet, 0, wallet.length() - 1);
            System.out.println("i: "+ i +" wallet: " + wallet);
//            TODO: How to remove ")" from wallet??? replace & substring does not work >_<
            if (wallet.equals(address.toLowerCase()+")")){
                System.out.println("The expected wallet");
                listWallets.get(i).click();
            }else {
                System.out.println("Not the expected wallet");
            }
        }
    }

    public void removeSelectAll(){
        List<WebElement> listWallets = driver.findElements(checked_wallets);
        for (int i = 0; i<listWallets.size(); i++){
            listWallets.get(i).click();
        }
//        boolean isAllExist = objUtil.isElemExist(select_all_btn, waitE);
//        boolean isOneExist = objUtil.isElemExist(select_one_btn, waitE);
//        boolean isNotSelectExist = objUtil.isElemExist(not_select_btn, waitE);
//        while (!isNotSelectExist){
//            if (isOneExist){
//                driver.findElement(select_one_btn).click();
//            }else if (isAllExist){
//                driver.findElement(select_all_btn).click();
//            }
//            isNotSelectExist = objUtil.isElemExist(not_select_btn, waitE);
//            isAllExist = objUtil.isElemExist(select_all_btn, waitE);
//            isOneExist = objUtil.isElemExist(select_one_btn, waitE);
//        }
    }

    /**
     * Author: HuongTT
     * Updated: 2022-03-07
     * Handle confirm request from metamask
     */
    public static void handleConfirm(){
        wait(5);
        if(switchToWindowByTitle(metamask_title)) {
            waitElemExist(switch_chain_btn, 5);
            boolean isExist = isElemExist(switch_chain_btn);
//            If it requests to switch chain
            if (isExist) {
                click(switch_chain_btn);
                wait(5);
                switchToWindowByTitle(metamask_title);
//                driver.manage().window().maximize();
            }
            scrollToEndPage();
            click(confirm_btn);
//        objAction.click(confirm_btn);
        }
//      Back to Krystal page after confirm from Meta
        switchToWindowByURLName(Environment.krystal_title);
    }
}
