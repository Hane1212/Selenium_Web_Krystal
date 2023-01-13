package pages;

import API.common.Environment;
import API.common.TokenAllowance;
import API.accounts.balancesAPI;
import API.token.tokenListAPI;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.testng.Assert;
import utils.ActionUtil;
import utils.Util;
import utils.logs.Log;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class ComPage extends BasePage{
    static ActionUtil objAction;
    //  Input
    static String RATE_USD = Util.getPropertyValue("RATE");
    By max_btn = By.xpath("//*[contains(@class, \"max-button\")]//button");
    static By max_val = By.xpath("//*[@class='chakra-text css-ez8qn2']");
    static By toolTipsMsg = By.xpath("//*[@class='chakra-text css-116hesr']");
    public static By src_input = By.xpath("//*[contains(@class, \"input-amount\")]");
    public static By current_src_token = By.xpath("//*[contains(@class, \"current-src-token\")]");
    public static By src_token_opt = By.xpath("//*[contains(@class, \"open-select-token-modal\")]");
    public static String token_str = "//*[contains(@class, \"token-";
    public static String list_tokens_str = "("+token_str+"\")])";
    public static By list_tokens = By.xpath(list_tokens_str);
//    static By search_token = By.xpath("//*[contains(@class, \"search-token-on-select-modal\")]");
    static By search_token = By.xpath("//*[contains(@class, \"select-token_input\")]");
    static By search_token_path = By.xpath("//*[contains(@class, \"search_token_item\")]");
    //  Confirm board
    static By confirm_board = By.xpath("//*[contains(@class, \"processing_form\")]");
    public static By confirm_msg = By.xpath("//*[contains(@class, \"processing_status\")]");
    static By close_board_btn = By.xpath("//*[@class='chakra-button css-9okfey']");
    By switchChain_btn = By.xpath("//*[contains(@class, \"menu-button_switch-chain\")]");
    static By current_Chain = By.xpath("//*[contains(@class, \"current-chain\")]");
    //Sidebar Menu buttons:
    public static By wallet_btn = By.xpath("//*[@class='css-1nsyz4x']");
    static By myWallet_btn = By.id("menu-button-16");
    static By copy_wallet_btn = By.xpath("//*[@class='css-esni57']");
    static String sidebar_menu = "//*[@class='chakra-text css-1d5jcfv'] ";
    static By Swap_btn = By.xpath(sidebar_menu+"[text()='Swap']");
    static By Explore_btn = By.xpath(sidebar_menu+"[text()='Explore']");
    static By Market_btn = By.xpath(sidebar_menu+"[text()='Market']");
    static By Earn_btn = By.xpath(sidebar_menu+"[text()='Earn']");

    static int waitP = Integer.parseInt(Util.getPropertyValue("WAIT_PROCESS"));
    public ComPage(WebDriver driver, Environment.Chain test_env){
        super(driver, test_env);
    }
    /**
     * Author: HuongTT
     * Updated: 2022-03-09
     * @apiNote : Switch to chain which get from config file
     */
    public void switchChain() {
        Log.info("Current Url:" + driver.getCurrentUrl());
        switchChain(Environment.test_chain);
        Log.info("Execute test on chain: "+ Environment.test_chain);
        ComPage.captureImage("AfterSwichTo"+Environment.test_chain);
    }

    public void switchChain(String targetChain){
        Log.info("Current Url:" + driver.getCurrentUrl());
        String currentChain = readText(current_Chain);
        if (!currentChain.equals(targetChain)){
//		1. Click on Switch Network button
            click(switchChain_btn);
//		2. Select Network
            By elem = By.xpath("//*[contains(@class, \"chain-"+targetChain+"\")]");
//            clickByAction(elem);
            clickByJS(elem);
            wait(1);
            currentChain = readText(current_Chain);
            if (!currentChain.equals(targetChain)){
                click(switchChain_btn);
//              clickByAction(elem);
                clickByJS(elem);
            }
            wait(2);
        }
    }

    /**
     * Select token to transfer by name of token
     * @param token name
     */
    static void selectSrcTokenByName(String token){
        selectTokenByName(src_token_opt, token);
    }

    /**
     * @apiNote : Select token by name of token
     * @param selectAsset - Select from this asset - it may src or des
     * @param token - token will be selected
     */
    public static void selectTokenByName(By selectAsset, String token){
        String currentTokenAtScr = readText(selectAsset);
        if (!currentTokenAtScr.equals(token)){
            String tokenAddress = tokenListAPI.getTokenAddress(token);
//            Click asset to select token
            click(selectAsset);
//            Click on token
            wait(1);
            writeText(search_token,tokenAddress);
            click(ComPage.getTokenElem(token));
        }
        wait(1);
    }

    public static void selectNativeToken(){
        String tokenAddress = tokenListAPI.getTokenAddress(test_env.nativeToken);
        selectSrcTokenByAddress(tokenAddress);
    }

    public static void selectSrcTokenByAddress(String tokenAddress){
        selectTokenByAddress(src_token_opt, tokenAddress);
    }

    public static void selectTokenByAddress(By selectAsset, String tokenAddress){
        By elem = By.xpath("(//*[contains(@class, \"ReactVirtualized__Grid__innerScrollContainer\")])[2]");
//        By elem = By.xpath("//*[contains(@class, \"search_token_item\")]");
        click(selectAsset);
        wait(1);
        writeText(search_token,tokenAddress);
        wait(1);
        click(elem);
//        clickByJS(search_token_path);
//        click(search_token_path);
//        clickByAction(elem);
        wait(1);
    }

    /**
     * @param token
     * @return xpath of token
     */
    public static By getTokenElem(String token){
        return By.xpath(token_str + token+"\")]");
    }
    /**
     * Author: HuongTT
     * Updated: 2022-03-07
     * @apiNote : Wait to finish swap (processing to done)
     * @param isClose Want to close board after done or not? If keep to validate- input "false", if close after done - input TRUE
     */
    public static void waitProcessDone(boolean isClose){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        wait(3);
//        switchToWindowByTitle("krystal");
        wait(1);
        ComPage.captureImage("WaitForConfirmBoardDisplay");
        boolean isExist = waitElemExist(confirm_board,30);
        if(isExist){
            ComPage.captureImage("ConfirmBoardDisplay");
            // Wait confirm board display
            String currentMessage = readText(confirm_msg) ;
            long startTime = System.currentTimeMillis();
            long timeout = System.currentTimeMillis() - startTime;
            while (timeout < waitP * 1000) {
                if (currentMessage.equals("Swapped Successfully")) {
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    Log.info("Time of processing: "+duration/1000);
                    break;
                }
                currentMessage = readText(confirm_msg);
                timeout = System.currentTimeMillis() - startTime;
                try {
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(isClose){
                closeConfirmDialog();
            }
        }else {
            ComPage.captureImage("ConfirmBoardDoesNotDisplay");
            Assert.fail("Confirm board does not display!");
        }

    }
    /**
     * Author: HuongTT
     * Updated: 2022-03-08
     * @apiNote : Close confirm board after swap or transfer done
     */
    public static void closeConfirmDialog(){
        if(isElemExist(close_board_btn)){
            click(close_board_btn);
        }
    }

    public static String getCurrentAddress() {
        click(myWallet_btn);
        click(copy_wallet_btn);
        String address = null;
        try {
            address = (String) Toolkit.getDefaultToolkit()
                    .getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public static String getCurrentChain(){
        return readText(current_Chain);
    }

    public static String getSourceToken(){
        return readText(current_src_token);
    }

    public static String getToolTipsMsg(){
        return readText(toolTipsMsg);
    }

    public static String getRandomRecipientAddress(){
        return Environment.ListRecipientAddress.get(Util.getRandomNumber(Environment.ListRecipientAddress.size()));
    }

    public static String getRandomInvalidRecipientAddress(){
        return Environment.ListInvalidRecipientAddress.get(Util.getRandomNumber(Environment.ListInvalidRecipientAddress.size()));
    }

    /**
     * Author: HuongTT
     * Updated: 2022-02-25
     * @apiNote Input amount to swap based on rate of token (currently, swap 10$)
     * @throws IOException
     * @throws UnsupportedFlavorException
     */
    public static void inputAmountToSwapByRate()  {
        String currentToken = readText(src_token_opt);
        double maxVal = Double.valueOf(getMaxAmount().replaceAll(",",""));
        System.out.println("Max value: "+maxVal);
        double amount = 0;
        if (maxVal==0){
            assert false;
        }else {
//           get Rate base on USD (Rate_usd =10 from config file)
            double rate = balancesAPI.getRateBasedUSDOfUserAddress(currentToken);
            amount = Double.valueOf(RATE_USD)/rate;
            System.out.println("Amount based USD: "+ amount);
            if(amount > maxVal){
                amount = maxVal/5;
            }
        }
        System.out.println("Amount to input "+amount);
        writeText(src_input, String.valueOf(amount));
    }

    /**
     * Author: HuongTT
     * Updated: 2022-03-09
     * @apiNote Input amount to swap - based on value of Max amount
     * @throws IOException
     * @throws UnsupportedFlavorException
     */
    public static void inputAmount() {
        Random rand = new Random();
        double maxVal = Double.valueOf(getMaxAmount().replaceAll(",",""));
        String currentToken = readText(src_token_opt);
        if (currentToken.equals("ETH")){
            writeText(src_input, String.valueOf("0.1"));
        }else if (maxVal > 1000){
            inputAmountToSwapByRate();
            }else {
    //            Option 1: Input 1/100 of Max value
                String amount = String.valueOf((maxVal/100));
                System.out.println("Amount to input "+amount);
                writeText(src_input, String.valueOf(amount));
    //            Option 2: Input random from 1 to max value
    //            int int_random = rand.nextInt((int) (maxVal+1));
    //            objAction.sendKeys(src_input, String.valueOf(int_random));
            }
    }

    /**
     * Author: HuongTT
     * Updated: 2022-02-21
     * @return Max amount of token
     */
    public static String getMaxAmount(){
        String val = readText(max_val);
        val = val.split(" ")[0];
        return val;
    }

    /**
     * @apiNote Unapprove token before perform transfer
     * @param token which use in multi send or swap
     */
    public static void unApproveToken(String token){
        String tokenAddress = tokenListAPI.getTokenAddress(token);
        String owner = Util.getPropertyValue("CONFIG_SPENDER_MULTISEND");
        String spender =  Util.getPropertyValue("USER_ADDRESS");
        String key = Util.getPropertyValue("CONFIG_PRIVATE_KEY");
        try {
            TokenAllowance.unApproveToken(owner, spender, key, tokenAddress);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void approveToken(String token){
//    TODO
    }

    public static void captureImage(String fileName){
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File("./img/"+fileName+".png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static WebElement getShadowRootElement(By element) {
        WebElement elem = driver.findElement(element);
        WebElement ele = (WebElement) ((JavascriptExecutor)driver)
                .executeScript("return arguments[0].shadowRoot", elem);
        return ele;
    }

}
