package pages;

import API.common.Environment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.Util;
import utils.logs.Log;

import java.io.IOException;
import java.util.Random;

import static pages.ComPage.src_input;

public class TransferScreen extends BasePage {
    int wait = Integer.parseInt(Util.getPropertyValue("WAIT"));
    static String transfer_URL = Environment.base_url+"transfer";
    static String Vfile = "Transfer_ValidateAPI.csv";

    public static By max_btn = By.xpath("//*[contains(@class, \"max-button\")]");
    public static By multi_send_btn = By.xpath("//*[contains(@class, \"chakra-button\")][text()='MULTI-SEND']");

    public static By address_in = By.xpath("//*[@placeholder='Recipient Address']");
    static By max_val = By.xpath("//*[contains(@class, \"max-value\")]");
    public static By transfer_btn = By.xpath("//*[contains(@class, \"chakra-button\")][text()='Transfer']");
    By confirm_btn = By.xpath("//*[contains(@class, \"chakra-button\")][text()='Confirm']");
    public static By err_msg = By.xpath("//*[contains(@class, \"error_msg\")]");
    public static By confirm_msg = By.xpath("//*[@class='css-1hgbz6s'] //following-sibling::p");
    static By close_board_btn = By.xpath("//*[@class='chakra-button css-9okfey']");
    //  --------------------------------- Transfer Confirmation ----------------------------------
    String trans_confirm = "//*[contains(@class, \"transfer-confirm\")] ";
    static By recipt_address = By.xpath("//*[contains(@class, \"recipient-address\")]");
    static By amount_to_transfer = By.xpath("//*[contains(@class, \"src-amount\")]");
    static By amount_to_transfer_usd = By.xpath("//*[contains(@class, \"usd-amount\")]");
    static By max_gas_fee = By.xpath("//*[contains(@class, \"max-gas-fee\")]");
    static By max_gas_fee_usd = By.xpath("//*[contains(@class, \"max-gas-usd\")]");
    static By gas_price_gas_limit = By.xpath("//*[contains(@class, \"gas-price-gas-limit\")]");
    //  --------------------------------- Operation ----------------------------------
    static int waitE = Integer.parseInt(Util.getPropertyValue("WAIT"));
    int waitP = Integer.parseInt(Util.getPropertyValue("WAIT_PROCESS"));
//    int

    public TransferScreen(WebDriver driver, Environment.Chain test_env){
        super(driver, test_env);
    }
    // ------------------------ACCESS TO SWAP PAGE----------------------------------
    /**
     * @apiNote Open Swap page by get URL of swap page
     */
    public static void getTransferPageByURL(){
        driver.get(transfer_URL);
        Log.info("Direct to: "+transfer_URL);
        waitForElementClickable(max_btn, waitE);
        ComPage.captureImage("DirectToTransferScreen");
    }

    /**
     * @apiNote  Open Swap page by click on [Swap] button at sidebar menu
     */
    public void getTransferPageFromMarket(){
//        1. Click on market button
        click(ComPage.Market_btn);
//        2. Click on [Transfer] button from Market screen
        waitForElementClickable(MarketScreen.transfer_href, waitE);
        click(MarketScreen.transfer_href);
    }

    /**
     * @apiNote Open swap page from explore page
     */
    public void getTransferPageByExplore(){
        click(ComPage.Explore_btn);
        click(ExploreScreen.transfer_href);
        waitForElementClickable(max_btn, waitE);
    }

    /**
     *
     * @return max amount on GUI
     */
    public static String getMaxAmount(){
        String val =  readText(max_val);
        val = val.split(" ")[0];
        return val;
    }

    public void inputAmountLargerThanBalance(){
        String maxBalance = getMaxAmount();
        Double val = Double.valueOf(maxBalance)+1;
        writeText(src_input, val.toString());
        wait(5);
    }

    /**
     * transfer native to ken
     * @param amount of token which transfer
     */
    public void transferNativeToken(String amount){
//        1. Get transfer page
        getTransferPageByURL();
//        2. Select token
        selectNativeToken();
        transferToken(Environment.recipientAddress, amount, "");
    }

    /**
     * Transfer random support token
     * @param address which receive token
     * @param amount of token which transfer
     */
    public void transferSupportToken(String address, String amount){
//        1. Get transfer page
        getTransferPageByURL();
//        2. Select token
        selectRandomSupportToken();
        transferToken(address, amount, "");
    }


    public void setupTransfer(String srcToken, String amount, String address){
//        1. Select token if any
        ComPage.selectSrcTokenByName(srcToken);
        inputAmountAndAddress(amount, address);
    }

    public void setupTransferByAddress(String chain, String srcToken, String amount, String address){
//        1. Select token if any
        Environment.Chain chain_ = Environment.getChainByName(chain);
        if (!chain_.isNative(srcToken)) {
            ComPage.selectSrcTokenByAddress(srcToken);
        }
        inputAmountAndAddress(amount, address);
    }

    private void inputAmountAndAddress(String amount, String recipientAddress){
        wait(1);
        //        3. Input amount to transfer
        writeText(src_input, amount);
//        4. Input address
        writeText(address_in, recipientAddress);
//        5. Perform transfer
        wait(1);
//        5.1. Click on [Transfer] button
        click(transfer_btn);
    }
    /**
     * transfer token based on address, amount, token
     * @param amount of token which transfer
     * @param token name
     */
    public void transferToken( String token, String amount, String receivedAddress){
        setupTransfer(token, amount, receivedAddress);
//        driver.findElement(tra`nsfer_btn).click();
//        5.2. Click on [Confirm] button
        wait(3); //Wait for API get Gas Limit
        click(confirm_btn);
//        driver.findElement(confirm_btn).click();
//        5.3. Handle confirm from metamask
        ImportMetaWallet.handleConfirm();
//        6. wait to finish transfer
        ComPage.waitProcessDone(false);
    }

    /**
     * Select native token when perform transfer
     */
    public void selectNativeToken(){
        String nativeToken = test_env.nativeToken;
        String curSrcToken = ComPage.getSourceToken();
        if (!curSrcToken.equals(nativeToken)){
            ComPage.selectSrcTokenByName(nativeToken);
        }
    }

    /**
     * Select random support token to transfer
     * @return name of random token
     */
    private String selectRandomSupportToken(){
        Random rand = new Random(); //instance of random class
        String defaultToken = test_env.nativeToken;
        String selectToken = new String();
//        Click on source or des to select token
        click(ComPage.src_token_opt);
        int numElems = driver.findElements(ComPage.list_tokens).size();
        if(numElems>0){
//          generate random values from 1-numElems
            int int_random = rand.nextInt(numElems+1);
            selectToken = ComPage.list_tokens_str+"["+int_random+"]";
            String randomElem = readText(By.xpath(selectToken));
//          Select random token which is not native token
            while (randomElem.equals(defaultToken)){
                int_random = rand.nextInt(numElems+1);
                selectToken = ComPage.list_tokens_str+"["+int_random+"]";
                randomElem = readText(By.xpath(selectToken));
            }
//          Click Random token which does not match with default token
            driver.findElement(By.xpath(selectToken)).click();
            selectToken = readText(ComPage.src_token_opt);
        }else {
            assert false;
        }
//        driver.findElement(close_select_token_btn).click();
        return selectToken;
    }
    //  ------------------------------ READ DATA FROM FILE----------------------------------
    public static String getSourceTokenForAPITest(){
        return getValueForAPITest( "Source Token");
    }

    public String getAmountForAPITest(){
        return getValueForAPITest( "Amount");
    }

    private static String getValueForAPITest(String key){
        String sourceToken = new String();
        try {
            sourceToken = Util.getValueByKey(Vfile, key, Environment.test_chain);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sourceToken;
    }
    //  ------------------------------ READ DATA FROM SCREEN----------------------------------
    /**
     * 0.0032 ETH
     * @return 0.0032
     */
    public static String getTransferAmount(){
        return readText(amount_to_transfer).split(" ")[0];
    }
    /**
     * 0.0032 ETH
     * @return ETH
     */
    public static String getTransferToken(){
        return readText(amount_to_transfer).split(" ")[1];
    }
    /**
     * ≈ 9.4022 USD
     * @return 9.4022
     */
    public static String getTransferAmountUSD(){
        return readText(amount_to_transfer_usd).split(" ")[1];
    }

    /**
     * 0.00135 ETH
     * @return 0.00135
     */
    public static String getMaxGasFee(){
        return readText(max_gas_fee).split(" ")[0];
    }

    /**
     * ≈ 3 USD
     * @return 3
     */
    public static String getMaxGasFeeUSD(){
        return readText(max_gas_fee_usd).split(" ")[1];
    }

    /**
     * 39 (Gas Price) * 25973 (Gas Limit)
     * @return 39
     */
    public static String getGasPrice(){
        return readText(gas_price_gas_limit).split(" ")[0];
    }

    /**
     * 39 (Gas Price) * 25973 (Gas Limit)
     * @return 25973
     */
    public static String getGasLimit(){
        return readText(gas_price_gas_limit).split(" ")[4];
    }

}
