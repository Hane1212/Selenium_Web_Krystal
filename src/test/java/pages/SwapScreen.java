package pages;

import API.common.Environment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Util;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SwapScreen extends BasePage{

    String swap_URL = Environment.base_url+"swap";
    By copy_wallet_btn = By.xpath("//*[@class='css-1lvor1v']");
    static String pathProject = System.getProperty("user.dir");
    static String pathToData = pathProject + "/data/";
    String Sfile = pathToData + "Swap.csv";
    static String Vfile = "Swap_ValidateAPI.csv";
//  ---------------------------------- Swap GUI ----------------------------------------
    public static By max_btn = By.xpath("//*[contains(@class, \"max-button\")]//button");
    static String swap_border = "//*[contains(@class, \"swap-main\")] ";
    static By max_val = By.xpath("//*[contains(@class, \"max-value\")]");
    By tooltip_msg = By.xpath("//*[@class='chakra-text css-116hesr']");

    public static By src_token_opt = By.xpath("//*[contains(@class, \"src-token\")]");
    public static By src_input = By.xpath("//*[contains(@class, \"swap_src-amount\")]");
    public static By src_input_usd = By.xpath("//*[contains(@class, \"src-rate-value\")]");
    public static By des_token_opt = By.xpath("//*[contains(@class, \"dest-token\")]");
    static By dest_balance = By.xpath("//*[contains(@class, \"swap_dest-balance\")]");
    public static By default_btn = By.xpath("//*[contains(@class, \"swap_default_btn\")]");
    static By selected_platform = By.xpath("(//*[contains(@class, \"chakra-text css-i3jkqk\")])[1]");
    //  ---------------------------------- Review Swap ----------------------------------------
    static By dest_rate_value = By.xpath("//*[contains(@class, \"dest-rate-value\")]");
    public static By src_balance = By.xpath("//*[contains(@class, \"swap_src-balance\")]");
    static By first_dest_income = By.xpath("(//*[contains(@class, \"dest-income\")])[1]");
    static By first_dest_income_usd = By.xpath("(//*[contains(@class, \"dest-rate-value\")])[1]");
    static String token_val = "(//*[@class='css-zdpt2t'])";
    static By rate = By.xpath("//*[contains(@class, \"rate\")]//div");
    static By expand_btn = By.xpath("//*[contains(@class, \"review-swap_expand\")]");
//    static By swap_now_btn = By.xpath("//*[contains(@class, \"open-swap-modal\")]");
    static By approve_btn = By.xpath("//*[contains(@class, \"approve_btn\")]");
    public static By review_swap_btn = By.xpath("//*[contains(@class, \"review-swap_btn\")]");
    public static By err_msg = By.xpath("//*[contains(@class, \"err-msg\")]");
    By splippage_val = By.xpath("//*[contains(@class, \"max-slippage\")]");
    By min_receive_val = By.xpath("//*[contains(@class, \"min-received\")]");
    By network_fee_est = By.xpath("//*[contains(@class, \"est-network-fee\")]");
    By max_network_fee = By.xpath("//*[contains(@class, \"max-network-fee\")]");
    By price_impact_val = By.xpath("//*[contains(@class, \"price-impact\")]");
    //  ---------------------------------- SWAP SUMMARY ----------------------------------------
    static String sum_src_input = "//*[contains(@class, \"swap-src-input\")]";
    By swap_sum_src_token_name = By.xpath(sum_src_input + "//*[contains(@class, \"swap-sum_token-name\")]");
    By swap_sum_src_usd_val= By.xpath(sum_src_input + "//*[contains(@class, \"swap-sum_usd-value\")]");
    By swap_sum_src_amount= By.xpath(sum_src_input + "//*[contains(@class, \"swap-sum_amount\")]");
    static String sum_dest_input = "//*[contains(@class, \"swap-dest-input\")]";
    By swap_sum_dest_token_name = By.xpath(sum_dest_input + "//*[contains(@class, \"swap-sum_token-name\")]");
    By swap_sum_dest_usd_val= By.xpath(sum_dest_input + "//*[contains(@class, \"swap-sum_usd-value\")]");
    By swap_sum_dest_amount= By.xpath(sum_dest_input + "//*[contains(@class, \"swap-sum_amount\")]");
    By confirm_swap_btn = By.xpath("//*[contains(@class, \"swap-sum_confirm\")]");
    By accept_btn = By.xpath("//*[contains(@class, \"swap-sum_accept\")]");
//  ------------------------------------ Slippage Setting -------------------------------
    By setting_btn = By.xpath("//*[@class='chakra-icon css-1o4dc00']");
    By one_per_rabtn = By.id("radio-262");
    By custom_rabtn = By.id("radio-263");
    By custom_txt = By.xpath("//*[@class='chakra-input css-18323hz']");
    By close_slip_btn = By.xpath("//*[@class='chakra-modal__close-btn css-hvm5px']");
//  ------------------------------------ Search token ----------------------------------
//    public static By search_txt = By.id("field-203");
    By search_txt = By.xpath("//*[@placeholder='Find a token by name, symbol or address']");
    By search_token = By.xpath("//*[contains(@class, \"select-token_input\")]");
    String list_tokens_str = "//*[@class='css-11l8nwy']";
    By list_tokens = By.xpath(list_tokens_str);
    By first_token = By.xpath("("+list_tokens_str+")[1]");
    By KNC_token = By.xpath("//*[@class='css-11l8nwy'][text()='KNC']/..//*[name()='svg']");
    public static By close_select_token_btn = By.xpath("//*[@class='chakra-modal__close-btn css-hvm5px']");
    // --------------------------------- Approve dialog----------------------------------
    By approve_token_txt = By.xpath("//*[@class='css-8ccl8u']");
    By gas_fee_val = By.xpath("//*[@class='css-s2uf1z']");
    By edit_btn = By.xpath("//*[@class='chakra-button css-sberfe']");
    static By confirm_approve_btn = By.xpath("//*[@class='chakra-button css-1dfq24b']");
    // ----------------------Transaction setting in Approve dialog------------
        By basic_tab = By.id("tabs-162--tab-0");
        String gas_fee_opt = "(//*[@class='chakra-radio__control css-1yejz3r'])";
        By supfast_rabtn = By.xpath(gas_fee_opt+"[1]");
        By fast_rabtn = By.xpath(gas_fee_opt+"[2]");
        By stan_rabtn = By.xpath(gas_fee_opt+"[3]");
        By low_rabtn = By.xpath(gas_fee_opt+"[4]");
        By advance_tab = By.id("tabs-162--tab-1");
        By gas_limit_txt = By.xpath("//*[@placeholder='Gas limit']");
        By gas_price_txt = By.xpath("//*[@placeholder='Gas price']");
        By gas_price_est_txt = By.xpath("//*[@class='chakra-input__right-addon css-dobfwg']");
        By reset_btn = By.xpath("//*[@class='chakra-button css-g8q740']");
        By save_btn = By.xpath("//*[@class='chakra-button css-74p7h3']");
    // --------------------------------- Swap Confirmation ----------------------------------
    static By confirm_btn = By.xpath("//*[contains(@class, \"swap-sum_confirm\")]");
    static By swap_anw_chkbox = By.xpath("//*[@class='chakra-checkbox__control css-1d33u3e']");
    By cancel_btn = By.xpath("//*[@class='chakra-button css-pxka2z']");
//  --------------------------------- Operation ----------------------------------
    static int wait = Integer.parseInt(Util.getPropertyValue("WAIT"));
    static int waitP = Integer.parseInt(Util.getPropertyValue("WAIT_PROCESS"));
//    int

    public SwapScreen(WebDriver driver, Environment.Chain test_env){
        super(driver, test_env);
    }
// ------------------------ACCESS TO SWAP PAGE----------------------------------
    /**
     * @apiNote Open Swap page by get URL of swap page
     */
    public void getSwapPageByURL(){
        driver.get(swap_URL);
        waitForElementClickable(max_btn, wait);
        SummaryScreen.handleSubscribe();
    }

    /**
     * @apiNote  Open Swap page by click on [Swap] button at sidebar menu
     */
    public void getSwapPageBySwapBtn(){
        driver.findElement(ComPage.Swap_btn).click();
        waitForElementClickable(src_token_opt, wait);
        SummaryScreen.handleSubscribe();
    }

    public void inputAmountLargerThanBalance(){
        Double maxBalance = getBalanceOfSourceInRS();
        maxBalance = maxBalance + 1;
        writeText(src_input, maxBalance.toString());
        wait(5);
    }

    public void inputValidAmount(){
        Double maxBalance = getBalanceOfSourceInRS();
        Random r = new Random();
        Double randomValue = (maxBalance) * r.nextDouble();
        writeText(src_input, randomValue.toString());
        waitElemExist(review_swap_btn, 5);
    }


    /**
     * @apiNote Open swap page from explore page
     */
    public void getSwapPageByExplore(){
        driver.findElement(ComPage.Explore_btn).click();
        driver.findElement(ExploreScreen.swap_href).click();
        waitForElementClickable(max_btn, wait);
        SummaryScreen.handleSubscribe();
    }
//  ------------------------GET VALUE ON SCREEN----------------------------------
    /**
     * @apiNote Get number of tokens which was list in selection box
     * @return number of token
     */
    public int getNumOfTokens(){
//        driver.findElement(address).click();
        int count = 0;
        count = driver.findElements(list_tokens).size();
        return count;
    }

    public String getCurrentWallet(){
        String currentWallet = new String();
        driver.findElement(ComPage.wallet_btn).click();
        driver.findElement(copy_wallet_btn).click();
        return  currentWallet;
    }

    public String getPlaceHolder(){
        WebElement elem = driver.findElement(src_input);
        return elem.getAttribute("placeholder");
    }


    // -----------------------------------SELECT TOKEN ----------------------------------
    public String selectSrcToken(String token){
        ComPage.selectTokenByName(src_token_opt, token);
        return token;
    }

    public String selectSrcToken(){
        String randomToken = selectRandomSupportToken(src_token_opt,des_token_opt);
        return randomToken;
    }

    /**
     * Author : HuongTT
     * Updated : 2022-02-25
     * @apiNote : Select token by name at destination
     * @param token selected token
     * @return
     */
    public String selectDesToken(String token){
        ComPage.selectTokenByName(des_token_opt, token);
        return token;
    }

    /**
     * Author : HuongTT
     * Updated : 2022-02-25
     * @apiNote : Select random support token at destination
     * @return random token
     */
    public String selectDesToken(){
        String randomToken = selectRandomSupportToken(des_token_opt, src_token_opt);
        return  randomToken;
    }

    /**
     * Author: HuongTT
     * Updated: 2022-02-25
     * @apiNote : Select native token at source
     */
    public void selectNativeTokenAtSrc(){
        String nativeToken = test_env.nativeToken;
        ComPage.selectTokenByName(src_token_opt, nativeToken);
    }

    /**
     * Author: HuongTT
     * Updated: 2022-02-25
     * @apiNote : Select native token at destination
     */
    public void selectNativeTokenAtDes(){
        String nativeToken = test_env.nativeToken;
        ComPage.selectTokenByName(des_token_opt, nativeToken);
    }

    /**
     * @implSpec : Select Native token at selected chain
     * @param src - Select from this asset - it may src or des
     */
    public void selectNativeTokenAtSrc(By src){
        String nativeToken = test_env.nativeToken;
        ComPage.selectTokenByName(src, nativeToken);
    }

    public void selectKNCAtDes(){
        selectKNC(des_token_opt, src_token_opt);
    }

    public void selectKNCAtSrc(){
        selectKNC(src_token_opt, des_token_opt);
    }
    /**
     * @apiNote : Select KNC which was verified (Because there are more than 1 token with name "KNC")
     * @param selectAsset : source or destination
     * @param otherAsset :reverse of selectAsset
     */
    public void selectKNC (By selectAsset, By otherAsset){
        String currentTokenAtScr = driver.findElement(selectAsset).getText();
        String currentTokenAtDes = driver.findElement(otherAsset).getText();
        if(!currentTokenAtScr.equals("KNC")){
            if (currentTokenAtDes.equals("KNC")){
                selectNativeTokenAtSrc(otherAsset);
            }
            driver.findElement(selectAsset).click();
            driver.findElement(KNC_token).click();
        }
    }

    /**
     * @apiNote - Select random support token
     * @param selectAsset - Select from this asset - it may src or des
     * @param otherAsset - The remaining asset - depend on select asset is src or des
     * @return - random token which was selected
     */
    private String selectRandomSupportToken(By selectAsset, By otherAsset){
        String currentTokenAtOther = driver.findElement(otherAsset).getText();
        Random rand = new Random(); //instance of random class
        String defaultToken = test_env.nativeToken;
        String selectToken = new String();
//        Click on source or des to select token
        driver.findElement(selectAsset).click();
        wait(2);
        int numElems = getNumOfTokens();
        if(numElems>0){
//          generate random values from 1->numElems
            int int_random = rand.nextInt(numElems-1)+1;
            selectToken = "("+list_tokens_str+")["+int_random+"]";
            String randomElem = driver.findElement(By.xpath(selectToken)).getText();
            String tokenValue = driver.findElement(By.xpath(token_val+"["+int_random+"]")).getText();
//          Select random token which is not native token and does not equal other token
            while (randomElem.equals(defaultToken) || randomElem.equals(currentTokenAtOther) ||tokenValue.equals("0")){
                int_random = rand.nextInt(numElems-1)+1;
                selectToken = "("+list_tokens_str+")["+int_random+"]";
                tokenValue = driver.findElement(By.xpath(token_val+"["+int_random+"]")).getText();
                randomElem = driver.findElement(By.xpath(selectToken)).getText();
            }
//          Click Random token which does not match with default token
            driver.findElement(By.xpath(selectToken)).click();
            selectToken = driver.findElement(selectAsset).getText();

        }else {
            assert false;
        }
//        driver.findElement(close_select_token_btn).click();
        return selectToken;
    }

    /**
     * @implSpec Get message from tooltips when click on [max] button at source asset
     * @return
     */
    public String getMessageAtMaxAmount(){
        driver.findElement(max_btn).click();
        String msg = driver.findElement(tooltip_msg).getText();
        return msg;
    }

    public static String getTheSelectedPlatform(){
        return readText(selected_platform);
    }
    // ------------------------ Approve dialog ----------------------------------
    public void setApprove(){
        click(approve_btn);
    }

    public void approveByAPI(String token, String IsApproval){
        if (IsApproval.equals("Yes")){
            ComPage.approveToken(token);
        }else {
            ComPage.unApproveToken(token);
        }
    }

    /**
     * Author: HuongTT
     * Updated: 2022-03-09
     * @apiNote Handle Approve if the token has not approved yet manually
     */
    private static void handleApprove(){
//  If the [Approve] token display
        if (isElemExist(approve_btn)){
            waitForElementClickable(approve_btn, wait);
//          1. Click on [Approve] button
            click(approve_btn);
//          2. Click on [Confirm] button
            click(confirm_approve_btn);
//          3. Handle confirm request from Metamask
            ImportMetaWallet.handleConfirm();
//          4. Wait to process done (Approve)
            ComPage.waitProcessDone(true);
        }
    }

    // ------------------------ Slippage Setting ----------------------------------
    /**
     * @apiNote : Select default option of slippage setting
     */
    public void setDefaultSlippage(){
        click(setting_btn);
        checkRadioBtn(one_per_rabtn, true);
        click(close_slip_btn);
    }

    /**
     * Author: HuongTT
     * Updated: 2022-02-14
     * @apiNote : Select custom option of slippage and input slippage value
     * @param val: Number of custom slippage
     */
    public void setCustomSlippage(int val){
        click(setting_btn);
        checkRadioBtn(custom_rabtn, true);
        writeText(custom_txt, String.valueOf(val));
        click(close_slip_btn);
    }

    public void selectTheFirstTokenSrc(){
        click(src_token_opt);
        click(first_token);
    }

    /**
     * Author: HuongTT
     * Updated: 2022-02-24
     * @apiNote : Swap from native token to support token
     * @param token support token
     * @throws IOException
     * @throws UnsupportedFlavorException
     */
    public void swapFromNativeToken(String token){
        getSwapPageByURL();
        selectNativeTokenAtSrc();
        selectDesToken(token);
        swapToken();
    }

    /**
     * Author: HuongTT
     * Updated: 2022-02-21
     * @apiNote : Swap from support to support token
     */
    public void swapSup2Sup(){
//        1. Get swap page
        getSwapPageByURL();
//        2. Select token at source
        selectSrcToken();
//        3. Select random token at des
        selectDesToken();
//        4.  Perform Swap token
        swapToken();
    }
    /**
     * Author: HuongTT
     * Updated: 2022-03-31
     * @apiNote : Swap from support to native token
     */
    public void swapSupToNative(){
//        1. Select support token at source
        selectSrcToken();
//        2. Select native token at Destination
        selectNativeTokenAtDes();
//        3. Perform swap
        swapToken();
    }


    /**
     * Author: HuongTT
     * Updated: 2022-03-07
     * @apiNote : Perform swap token
     * @throws IOException
     * @throws UnsupportedFlavorException
     */
    public static void swapToken() {
        String srcToken = driver.findElement(src_token_opt).getText();
//        1. Input amount to swap
        if (srcToken.equals("ETH")){
            writeText(src_input, "0.01");
        }else{
            ComPage.inputAmount();
        }
        swapNow();
    }

    private static void swapNow(){
//        1. Wait and click [Swap now] button
        waitForElementClickable(review_swap_btn, 20);
//         2.1. Handle Approve if token has not approved yet
        handleApprove();
//        2.2. Click on [Swap Now] button
        driver.findElement(review_swap_btn).click();
        if (isElemExist(swap_anw_chkbox, wait)){
//            2.3. Click on Swap anyway check box if any
            checkRadioBtn(swap_anw_chkbox, true);
        }
//        driver.manage().window().maximize();
//        3. Click on [Confirm] button
        driver.findElement(confirm_btn).click();
//        4. Handle Confirm from extension - metamask
        ComPage.captureImage("BeforeConfirmSwapFromMeta");
        SummaryScreen.handleSubscribe();
        ImportMetaWallet.handleConfirm();
        ComPage.captureImage("AfterConfirmSwapFromMeta");
//        5. Wait to process done
        ComPage.waitProcessDone(false);
    }



    /**
     * @apiNote Swap token based on data from Data driven
     * @param srcToken Source token
     * @param desToken destination token
     * @param amount of source token to swap
     */
    public void swapToken(String srcToken, String desToken, String amount, String isApproval){
        if(isApproval.equals("NO")){
            ComPage.unApproveToken(srcToken);
        }else ComPage.approveToken(srcToken);
        selectSrcToken(srcToken);
        selectDesToken(desToken);
        wait(1);
        writeText(src_input, amount);
        swapNow();
    }

    public void swapOutOfBalance(){
        selectSrcToken();
        selectDesToken();
        double maxVal = Double.valueOf(ComPage.getMaxAmount().replaceAll(",",""));
        writeText(src_input, String.valueOf((maxVal+1)));
        waitForElementClickable(review_swap_btn, wait);
        click(review_swap_btn);
    }
    //  ------------------------------ READ DATA FROM FILE----------------------------------
    public String getSourceTokenForAPITest(){
        return getValueForAPITest( "Source Token");
    }

    public String getDestTokenForAPITest(){
        return getValueForAPITest( "Dest Token");
    }

    public String getAmountForAPITest(){
        return getValueForAPITest( "Amount");
    }

    private String getValueForAPITest( String key){
        String sourceToken = new String();
        try {
            sourceToken = Util.getValueByKey(Vfile, key, Environment.test_chain);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sourceToken;
    }

    public Object[][] getData(String key) {
        List<Map<String, String>> listOfMaps = Util.getListTestDataBaseOnTCID(Sfile, key);
        Object [] [] data = new Object [listOfMaps.size()] [5];
        for (int i =0; i<listOfMaps.size();i++){
            data[i][0]=listOfMaps.get(i).get("TC_ID");
            data[i][1]=listOfMaps.get(i).get("TokenSource");
            data[i][2]=listOfMaps.get(i).get("TokenDes");
            data[i][3]=listOfMaps.get(i).get("Amount");
            data[i][4]=listOfMaps.get(i).get("IsApproval");
        }
        return data;
    }

    //  -----------------------------------------SWAP --------------------------------------
    public void setupSwap(String srcToken, String desToken, String amount, Boolean confirm){
        selectSrcToken(srcToken);
        selectDesToken(desToken);
        writeText(src_input, amount);
        waitForElementClickable(review_swap_btn, 10);
        if (confirm){
            click(review_swap_btn);
        }else{
            if (ComPage.isElemExist(expand_btn)) {
                scrollDown();
                click(expand_btn);
            }
        }
//        wait(5); //wait to get Rate success
    }
    //  -------------------------------------------- REVIEW SWAP----------------------------------

    public Double getInputAmountUSDValue(){
        String usd_value = readText(src_input_usd); //~$1.85
        usd_value = usd_value.replace("~$","");
        return Double.valueOf(usd_value);
    }

    public Double getBalanceOfSourceInRS(){
        return Double.parseDouble(readText(src_balance)); //0.001357
    }

    public Double getBalanceOfDestInRS(){
        return Double.parseDouble(readText(dest_balance).split(" ")[0]); //0.001357
    }

    public Double getActualRate(){
        String rate_a = readText(rate); //        1 ETH = 50.783470 KNC
        rate_a = rate_a.split(" ")[3];
        return Double.valueOf(rate_a);
    }

    public Double getNetworFeeEst(){
        String val_from_ui = readText(network_fee_est); //  $0.7671 • Standard
        val_from_ui = val_from_ui.split(" ")[0];
        return Double.valueOf(val_from_ui.replace("$",""));
    }

    public Double getMaxNetworkFee(){
        String val_from_ui = readText(max_network_fee); //  $0.6445
        return Double.valueOf(val_from_ui.replace("$",""));
    }

    public Double getDestIncome(){
        return Double.valueOf(readText(first_dest_income));//0.3005
    }

    public Double getDestIncomeUSD(){
        return Double.valueOf(readText(first_dest_income_usd).replace("~$",""));
    }

    public Double getSlipPage(){
        return Double.parseDouble(readText(splippage_val).replace("%",""));
    }

    public Double getMinReceive(){
        return Double.parseDouble(readText(min_receive_val).split(" ")[0]);
    }

    public String getPriceImpact(){
        return readText(price_impact_val).replace("%","");
    }

    //  -----------------------------------------SWAP CONFIRMATION--------------------------------------
    public String getSrcTokenAtConfirm(){
        return readText(swap_sum_src_token_name);
    }

    public String getSrcValueAtConfirm(){return readText(swap_sum_src_amount);}

    public String getSrcValueUSD(){return readText(swap_sum_src_usd_val).split(" ")[1];}

    public String getDestTokenAtConfirm(){
        return readText(swap_sum_dest_token_name);
    }

    public String getDestValueAtConfirm(){
        return readText(swap_sum_dest_amount);
    }
    public String getDestUSDValueAtConfirm(){
        return readText(swap_sum_dest_usd_val);
    }
//    public String getMinReceiveAtConfirm(){
//        return readText(minReceive_val).split(" ")[0]; //1.3363 USDT
//    }
//
//    public String getPriceImactAtConfirm(){
//        return readText(price_impace_confirm).replace("%","");
//    }

}
