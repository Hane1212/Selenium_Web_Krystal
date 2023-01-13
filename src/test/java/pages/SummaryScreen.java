package pages;


import API.accounts.totalBalancesAPI;
import API.common.Environment;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.testng.SkipException;
import utils.Util;
import utils.logs.Log;

import java.util.List;

import static scripts.BaseTest.AssertPage;
import static scripts.BaseTest.SumPage;

public class SummaryScreen extends BasePage {
    static String Krystal_URL = Environment.base_url;
    static String Summary_URL = Krystal_URL + "summary";
    public static String page_title = "Krystal Wallet | One Platform, All DeFi";
    static By subscribe_btn = By.xpath("//*[@class='align-right primary slidedown-button']");
//  Connect wallet
    public static By root = By.xpath("//*[@class='chakra-ui-dark']");

//  Sidebar Menu
    By Summary_btn = By.xpath("//*[@href='/summary']");
    By unit_opt = By.xpath("//button[@id='menu-button-26']");

//  Total Net Worth area
    By total_net_worth_val = By.xpath("//*[contains(@class, \"total-net-worth\")]");
    String balance_val = "//*[contains(@class, \"balance_val\")]";
    By chain_plus = By.xpath("//*[contains(@class, \"chain-plus\")]");

    static By transfer_href = By.xpath("//*[@href='/transfer']");
//  Assets tab
    By total_assets = By.xpath("//*[contains(@class, \"assets_total-value\")]");
    By assets_holding_value = By.xpath("//*[contains(@class, \"assets_holding-value\")]");

    By currentChain_txt = By.xpath("//*[@class='css-1v4xcoh']");
    String listSupply_  = "(//*[@class='css-10fyhi1'])";
    By listSupply = By.xpath(listSupply_);
    By supply_tab = By.xpath("//button[text()='Supply']");
    By supply_val = By.xpath("//*[contains(@class, \"supply-total-value\")]/p");
    By liquidityPools_tab = By.xpath("//*[contains(@class, \"liquidityPools_tab\")]");
    By liquidityPools_val = By.xpath("//*[contains(@class, \"liquidityPools-total-value\")]/p");
//    Withdraw dialog
    By withDraw_btn = By.xpath("//button[text()='Withdraw']");
    By close_withdraw_btn = By.xpath("//*[@class='chakra-modal__close-btn css-24mt5z']");
//    Confirm Withdraw
    By src_input = By.xpath("//*[contains(@class, \"input-amount\")]");
    By max_btn = By.xpath("//button[text()='MAX']");
    By confirm_btn = By.xpath("//button[text()='Confirm']");

    Object expectValue, actualValue;

    public SummaryScreen(WebDriver driver, Environment.Chain test_env) {
        super(driver, test_env);
    }

    public void goToMainScreen(){
        Log.info("Access to Main page");
        driver.get(Krystal_URL);
        Log.info(driver.getTitle());
        ComPage.captureImage("LoadMainPage");
        handleSubscribe();
    }

    public static void handleSubscribe(){
        if(ComPage.isElemExist(subscribe_btn)){
            click(subscribe_btn);
        }
    }

    public void goToSumScreen(){
        Log.info("Access to Main page");
        driver.get(Summary_URL);
        Log.info(driver.getTitle());
        ComPage.captureImage("LoadSummaryPage");
        handleSubscribe();
    }

    /**
     * Updated: 2022-04-12
     * @param token
     * @param amount
     */
    public void withdrawToken(String token, String amount) {
        // If there's no item, test case is skipped
        List<WebElement> allItems = driver.findElements(listSupply);
        if (allItems.size() == 0)
            throw new SkipException("No items in list");
        else {
            for (int i=1; i<allItems.size(); i++){
                By elem = By.xpath(listSupply_ + "["+i+"]//div[2]");
                String token_ = readText(elem);
                if (token_.equals(token)) {
                    withDraw(elem, amount);
                }
            }
        }
    }

    /**
     * Updated: 2022-04-12
     * @param token
     * @param amount
     */
    public void withdrawTokenByName(String token, String amount){
//        (//*[@class='css-10fyhi1'])//div[text()='USDT']
        // If there's no item, test case is skipped
        List<WebElement> allItems = driver.findElements(listSupply);
        if (allItems.size() == 0)
            throw new SkipException("No items in list");
        else {
            By elem = By.xpath(listSupply_+"//div[text()='"+token+"']");
            withDraw(elem, amount);
        }
    }
    /**
     * Updated: 2022-04-12
     * @apiNote Perform withdraw token from supply
     * @param elem: token which use to withdraw
     * @param amount: amount of withdraw
     */
    public void withDraw(By elem, String amount){
//        1. Select token
        click(elem);
//        2. Click on [Withdraw] button
        click(withDraw_btn);
//        3. Input amount to withdraw
        inputValueToWithdraw(amount);
//        4. Click on [Confirm] button
//        click(confirm_btn);
        wait(2);
        clickByAction(confirm_btn);
//        clickByJava(confirm_btn);
//        5. Handle Confirm process from MetaMask
        ImportMetaWallet.handleConfirm();
//        6. Wait to withdraw done
        ComPage.waitProcessDone(true);
        click(close_withdraw_btn);
    }

    /**
     * Updated: 2022-04-12
     * @apiNote Input value to withdraw
     * @param amount: if amount is "0", it mean input Max value, if other, input specific
     */
    public void inputValueToWithdraw(@NotNull String amount){
        if (amount.equals("0")){
            click(max_btn);
        }else {
            writeText(src_input, amount);
        }
        waitForElementClickable(confirm_btn, 5);
    }

    /**
     * Updated: 2022-04-13
     * @param token which contain supply
     * @return value of supply of token
     */
    public String getCurrentSupply(String token){
        waitVisibility(listSupply);
        String val = new String();
        // If there's no item, test case is skipped
        List<WebElement> allItems = driver.findElements(listSupply);
        if (allItems.size() == 0)
            throw new SkipException("No items in list");
        else {
            for (int i=1; i<allItems.size(); i++){
                By elem = By.xpath(listSupply_ + "["+i+"]//div[2]");
                String token_ = readText(elem);
                if (token_.equals(token)) {
                    elem = By.xpath(listSupply_ + "["+i+"]//div[1]");
                    val = readText(elem);
                }
            }
        }
        return val;
    }

    public double getTotalSupply(){
        click(supply_tab);
        return getValRemovedUnit(supply_val);
    }

    public double getTotalLiquid(){
        click(liquidityPools_tab);
        return getValRemovedUnit(liquidityPools_val);
    }

    public double getTotalAssets(String chain){
        selectChain(chain);
        waitVisibility(total_assets);
        return getValRemovedUnit(total_assets);
    }

    private void selectChain(String chain){
        By elem = By.xpath("//*[contains(@class, \"chain-"+chain.toLowerCase()+" selected\")]");
        if(!isElemExist(elem)){
            elem = By.xpath("//*[contains(@class, \"menuitem chain-"+chain.toLowerCase()+"\")]");
            if(isElemExist(elem)){
                click(chain_plus);
                click(elem);
            }else {
                elem = By.xpath("//*[contains(@class, \"chain-"+chain.toLowerCase()+"\")]");
                click(elem);
            }

        }
    }

    public double getTotalNetWorth(){
        click(Summary_btn);
        waitVisibility(total_net_worth_val);
        return getValRemovedUnit(total_net_worth_val);
    }

    public double getBalanceOfChain(String chain){
        selectChain(chain);
        By elem = By.xpath("//*[contains(@class, \"chain-"+chain.toLowerCase()+"\")]"+balance_val);
        return getValRemovedUnit(elem);
    }
    private double getValRemovedUnit(By elem){
        return Double.parseDouble(Util.removeString(readText(elem),"$"));
    }

    public double getTotalAssets(){
        List<WebElement> elementsList =  driver.findElements(assets_holding_value);
        double totalAssets = 0;
        for (int i =0; i<elementsList.size();i++){
            totalAssets = totalAssets + Double.parseDouble(Util.removeString(elementsList.get(i).getText(),"$"));
        }
        return totalAssets;
    }

    public void ValidateTotalNetWorth(String chain){
        String user_address = Environment.user_address;
        expectValue = totalBalancesAPI.getBalanceOfChainBasedUSD(chain, user_address);
        actualValue = SumPage.getBalanceOfChain(chain);
        Log.info("Expect total balance of "+chain+": "+expectValue);
        Log.info("Actual total balance of "+chain+": "+actualValue);
        AssertPage.assertAlmostEqual(String.valueOf(expectValue), String.valueOf(actualValue));
    }

    public void ValidateTotalBalancesOfChain(String chain){
//        ComPage.switchChain(chain);
        expectValue = totalBalancesAPI.getBalanceOfChainBasedUSD(chain, Environment.user_address);
        actualValue = SumPage.getTotalAssets(chain) + SumPage.getTotalSupply() + SumPage.getTotalLiquid();
        Log.info("Expect total assets of "+chain+": "+expectValue);
        Log.info("Actual total assets of "+chain+": "+actualValue);
        Log.info("Actual Assets of "+chain+": "+SumPage.getTotalAssets(chain));
        Log.info("Actual total Supply of "+chain+": "+SumPage.getTotalSupply());
        Log.info("Actual total Liquid of "+chain+": "+SumPage.getTotalLiquid());
        AssertPage.assertAlmostEqual(String.valueOf(expectValue), String.valueOf(actualValue));
    }
}