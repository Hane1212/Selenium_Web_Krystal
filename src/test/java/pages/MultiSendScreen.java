package pages;

import API.accounts.balancesAPI;
import API.common.Environment;
import API.token.tokenDetailsAPI;
import API.token.tokenListAPI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.Assertion;
import utils.Util;
import utils.logs.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: HuongTT
 * Created: 2022-03-28
 */
public class MultiSendScreen extends BasePage{
    String main_URL = Environment.base_url;
    String multiSend_URL = main_URL+"multi-send";

    String Mfile = "MultiSend.csv";
    public By transfer_btn = By.xpath("//*[contains(@class, \"sum_transfer-btn\")]");
    //  --------------------------------- Screen ----------------------------------
    public String recip_add_st = "(//*[contains(@class, \"ms-input_recipient_address\")])";
    public By recipient_add_ip = By.xpath(recip_add_st);
    public String select_token_st = "(//*[contains(@class, \"ms-selected_token\")])";
    public By selected_token = By.xpath(select_token_st);
    String choose_btn = "(//*[@class='css-8ary9v'])";
    String token_list = "//*[@class='css-11l8nwy']";
    public By err_sum = By.xpath("//*[contains(@class, \"sum_err-msg\")]");
//    -------1 line
    public String msg_err_addr = "[contains(@class, \"ms-msg_err_addr\")]";
    public String err_address_st = "(//*[contains(@class, \"ms-msg_err_addr\")])";
    public By err_address = By.xpath(err_address_st);
    String err_input_st = "(//*[contains(@class, \"ms-ms-msg_err_token\")])";
    public By err_input = By.xpath(err_input_st);

    public String amount_st = "(//*[contains(@class, \"ms-input_amount\")])";
    public By amount_input = By.xpath(amount_st);
    public By approve_btn = By.xpath("//*[contains(@class, \"sum_approve-btn\")]");

    public String x_btn = "(//*[@class='chakra-icon css-1vwvp6d'])";
    String balance_st = "(//*[contains(@class, \"sum-token_balance\")])";
    By balance_val = By.xpath(balance_st);
    public By add_more_btn = By.xpath("//*[contains(@class, \"ms-add_more_btn\")]");
//    -------Multi line
public By total_rep_label = By.xpath("//*[contains(@class, \"ms-total_recipients\")]");
    public static By sum_warn_msg = By.xpath("//*[contains(@class, \"sum_warn-msg\")]");
    public static By err_msg = By.xpath("//*[contains(@class, \"sum_err-msg\")]");
    By clear_all_btn =  By.xpath("//*[contains(@class, \"ms-clear_all_btn\")]");
    By OK_btn = By.xpath("//*[@type='button'][text()='Ok']");
    String sum_amount = "//*[contains(@class, \"sum-input_amount\")]";
//    ------------------------------Confirm Approve Token----------------
    public By token_address = By.xpath("//*[contains(@class, \"approve_address\")]");
    By approve_gas_fee = By.xpath("//*[contains(@class, \"approve_gas-fee\")]");
    public By canncel_approve_btn = By.xpath("//*[contains(@class, \"approve_cancel\")]");
    //  --------------------------------- Transfer Confirmation ----------------------------------
    By confirm_btn = By.xpath("//*[contains(@class, \"chakra-button\")][text()='Confirm']");
    By tc_warn_msg = By.xpath("//*[@class='css-7hupfg']");
    public By tc_num_transfers = By.xpath("//*[contains(@class, \"trans-confirm_number-of-transfers\")]");
    String tc_total_amount = "(//*[contains(@class, \"trans-confirm_total-amount\")])";
    By tc_amount_usd = By.xpath("//*[contains(@class, \"trans-confirm_usd-value-total-amount\")]");
    By tc_gas_fee = By.xpath("//*[contains(@class, \"trans-confirm_gas-fee\")]");
    By tc_gas_fee_usd = By.xpath("//*[contains(@class, \"trans-confirm_usd-value-gas-fee\")]");
    //  --------------------------------- Operation ----------------------------------
    int wait = Integer.parseInt(Util.getPropertyValue("WAIT"));
    int waitP = Integer.parseInt(Util.getPropertyValue("WAIT_PROCESS"));

    public MultiSendScreen(WebDriver driver, Environment.Chain test_env){
        super(driver, test_env);
    }

    /**
     * Author : HuongTT
     * Updated: 2022-03-28
     * @apiNote Open Multi Send page by get URL of Multi Send page
     */
    public void getMultiSendPageByURL(){
//        1. Access to Multi Send page by URL
        driver.get(multiSend_URL);
        waitVisibility(transfer_btn);
    }

    public void clickTransferBtn(){
        click(transfer_btn);
    }

    public void clickAddMoreBtn(){
        click(add_more_btn);
    }

    public void clickApproveBtn(String tokenAddress){
        String token = tokenListAPI.getTokenSymbol(tokenAddress);
        BasePage.click(setApproveBtn(token));
    }

    public void inputAddress(){
        writeText(recipient_add_ip, ComPage.getRandomRecipientAddress());
    }

    public void inputAddress(int i){
        By elem = setMultiSendElem(recip_add_st, i);
        BasePage.writeText(elem, ComPage.getRandomRecipientAddress());
    }

    public void input0(){
        writeText(amount_input, "0");
    }

    public void input0(int i){
        By elem = setMultiSendElem(amount_st, i);
        BasePage.writeText(elem, "0");
    }

    public void inputAmount(String amount){
        writeText(amount_input, amount);
    }

    public void inputAmount(int i, String amount){
        By elem = setMultiSendElem(amount_st, i);
        BasePage.writeText(elem, amount);
    }

    public void clearAddress(int i){
        By elem = setMultiSendElem(recip_add_st, i);
        cleanText(elem);
    }

    public String readAddressErr(int i){
        By elem = setErrElem(recip_add_st, i);
        return readText(elem);
    }

    public String readTokenErr(int i){
        By elem = setErrElem(amount_st, i);
        return BasePage.readText(elem);
    }

    public void inputInvalidAddress (int i){
        By elem = setMultiSendElem(recip_add_st, i);
        BasePage.writeText(elem, ComPage.getRandomInvalidRecipientAddress());
    }

    public String selectUnApproveToken(int i){
        By elem = setMultiSendElem(select_token_st, i);
        String tokenAddress = ComAPI.getUnApproveToken();
        ComPage.selectTokenByAddress(elem, tokenAddress);
        return tokenAddress;
    }

    public String selectApproveToken(int i){
        By elem = setMultiSendElem(select_token_st, i);
        String tokenAddress = ComAPI.getApproveToken();
        ComPage.selectTokenByAddress(elem, tokenAddress);
        return tokenAddress;
    }

    public boolean isTrasferBtnEnable(){
        return isElemEnable(transfer_btn);
    }

    public boolean isApproveBtnExist(String tokenAddress){
        Log.info("tokenAddress: "+ tokenAddress);
        String token = balancesAPI.getTokenSymbol(tokenAddress);
        return isElemExist(setApproveBtn(token));
    }

    public String getSelectedToken(int i){
        By elem = setMultiSendElem(select_token_st, i);
        return readText(elem);
    }

    public String getInputAmount(int i){
        By elem = setMultiSendElem(amount_st, i);
        return readAttribute(elem, "datavalue");
    }

    public String getToAddress(int i){
        By elem = setMultiSendElem(recip_add_st, i);
        return readAttribute(elem, "datavalue");
    }

    /**
     * Author : HuongTT
     * Updated: 2022-03-28
     * @apiNote Open multi Send page from explore page
     */
    public void getMultiSendPageByExplore(){
        click(ComPage.Explore_btn);
        click(ExploreScreen.multiSend_href);
        waitForElementClickable(transfer_btn, wait);
    }

    /**
     * Author : HuongTT
     * Updated: 2022-03-28
     * @apiNote Open multi Send page from transfer page
     */
    public void getMultiSendPageFromTransferPage(){
        TransferScreen.getTransferPageByURL();
        click(TransferScreen.multi_send_btn);
        waitForElementClickable(transfer_btn, wait);
    }

    /**
     * Perform Approve token by [Approve] button on Multi-send screen
     */
    public void approveToTransfer(){
        boolean isExist = isElemExist(approve_btn);
        while (isExist){
            click(approve_btn);
            click(confirm_btn);
            ImportMetaWallet.handleConfirm();
            ComPage.waitProcessDone( true);
            isExist = isElemExist(approve_btn);
        }
    }

    /**
     * @param suite which contain Un-Approval token from file
     */
    public void unApproveTokens(String suite){
        List<Map<String, String>> listOfMaps = Util.getListTestDataBaseOnTCID(Mfile, suite);
        for (int i= 0; i<listOfMaps.size();i++){
            String token = listOfMaps.get(i).get("Token");
            ComPage.unApproveToken(token);
        }
        wait(2);
    }

    public void inputInfoToPerformMultiSend(String key){
//        1. Handle number of recipients
        setNumDataToTransfer(key);
//        2. Input recipient/amount/token to transfer
        setValueForMultiSend(key);
    }



    /**
     * @apiNote perform multi send while do not need to approve
     * @param key
     */
    public void performTransfer(String key){
        inputInfoToPerformMultiSend(key);
        executeTransfer();
    }
    public void executeTransfer(){
//        3. Handle Approve token
        approveToTransfer();
//        4. Click on [Transfer] button
        click(transfer_btn);
//        5. Click on [Confirm] button
        click(confirm_btn);
//        6. Handle confirm from metamask
        ImportMetaWallet.handleConfirm();
//        7. wait to finish transfer
        ComPage.waitProcessDone(false);
    }

    /**
     * @apiNote Input Recipient, token, amount to perform multi send
     * @param key
     */
    private void setValueForMultiSend(String key){
        List<Map<String, String>> listOfMaps = Util.getListTestDataBaseOnTCID(Mfile, key);
        for (int i= 0; i<listOfMaps.size();i++){
//            1. Input Recipient Address
            By elem = By.xpath(recip_add_st + "["+(i+1)+"]");
            writeText(elem, listOfMaps.get(i).get("Receiver"));
//            2. Click Choose button
            elem = By.xpath(choose_btn + "["+(i+1)+"]");
            click(elem);
//            3. Select token //*[contains(@class, "token-ETH")]
            wait(1);
            elem = By.xpath(ComPage.token_str + listOfMaps.get(i).get("Token")+"\")]");
            click(elem);
//            3. Input amount
            wait(1);
            elem = By.xpath(amount_st + "["+(i+1)+"]");
//            writeTextByJava(elem, listOfMaps.get(i).get("Amount"));
            writeText(elem, listOfMaps.get(i).get("Amount"));
        }
        wait(2);
    }

    public void setApproveList(){
        int size = getNumOfRecipients();
        for(int i=1;i<size+1;i++){
//            Get approved token
            String token = ComAPI.getApproveToken();
//            Input Recipient Address
            By elem = setMultiSendElem(recip_add_st, i);
            writeText(elem, ComPage.getRandomRecipientAddress());
//            Select token
            elem = setMultiSendElem(select_token_st, i);
            ComPage.selectTokenByAddress(elem, token);
//            Input Amount
            elem = setMultiSendElem(amount_st, i);
            writeText(elem, ComAPI.getDivideBalanceToken(token, size+1));
        }
    }

    public By setMultiSendElem(String item, int i){
        return By.xpath(item + "["+i+"]");
    }

    public By setErrElem(String item, int i){
        return By.xpath(item +"["+i+"]"+ "/.. /.. /p ");
    }

    public By setApproveBtn(String token){
        //*[contains(@class, "sum_token-aPolUSDC")] /.. /button[contains(@class, "sum_approve-btn")]
        String elem = "//*[contains(@class, \"sum_token-"+token+"\")] /.. /button[contains(@class, \"sum_approve-btn\")]";
        return By.xpath(elem);
    }

    /**
     * @apiNote set number of input address which use to perform multi send
     * @param key test ID in MultiSend.csv file (Ex: MS_001)
     */
    private void setNumDataToTransfer(String key){
        List<Map<String, String>> listOfMaps = Util.getListTestDataBaseOnTCID(Mfile, key);
        setNumOfLineToTransfer(listOfMaps.size());
    }

    public void setNumOfLineToTransfer(int size){
        int num = getNumOfRecipients();
//      If number of data > number of input item => must click [x] to remove redundant item
        if (size<num){
            for (int i=num; i>size; i--){
                By elem = By.xpath(x_btn+"["+i+"]");
                click(elem);
            }
        }else
//      If number of data < number of input item => Click Add more to add new item
            if (size>num){
                for (int i=0; i< (size - num); i++){
                    click(add_more_btn);
                }
            }
    }

    /**
     * @apiNote get number of Recipients form UI (ex: Total: 2 Recipients)
     * @return number of recipients
     */
    public int getNumOfRecipients(){
        String text = readText(total_rep_label);
        int num = 0;
        try {
            num = Integer.parseInt(text.split(" ")[1].trim());
        }catch (Exception e){
            System.out.println(text);
            System.out.println(e);
        }
        return num;
    }

    public String getWarnMsgSUM(){
        return readText(sum_warn_msg);
    }

    public void ClearAllInput(){
        click(clear_all_btn);
        click(OK_btn);
    }

//  0.002881 MATIC (0.002594 USD)
    public String getApproveGasFeeValue(){
        return readText(approve_gas_fee).split(" ")[0];
    }

    public String getApproveGasFeeUSD(){
        return readText(approve_gas_fee).split(" ")[2].replace("(","");
    }

    /**
     * Ex: 0.4773 BNB
     * @return 0.4773
     */
    public String getBalanceOfToken(){
        String balance = readText(balance_val);
        balance = balance.split(" ")[0];
        return balance;
    }

    public double getTotalUSDvalue(){
        int recipientNum = getNumOfRecipients();
        double sum = 0;
        for (int i = 1; i<recipientNum +1; i++){
            String token = getSelectedToken(i);
            String amount = getInputAmount(i);
            sum = sum + Double.parseDouble(amount) * tokenDetailsAPI.get_usd_rate(token);
        }
        return sum;
    }
//--------------------------Transfer Confirmation Screen-------------------------------

    public String getWarnMsgAtTransferConfirm(){
        return readText(tc_warn_msg);
    }

    public String getRecipNumAtTransferConfirm(){
        return readText(tc_num_transfers);
    }

    public String getTotalAmountUSD(){
        String val = readText(tc_amount_usd);
        return val.split(" ")[1].replace("$","");
    }

    public String readGasFee(){
        return readText(tc_gas_fee).split(" ")[0];
    }

    public String readGasFeeUSD(){
        return readText(tc_gas_fee_usd).split(" ")[1];
    }

    public Map<String, String> getListTokenAmount(){
        Map<String, String> listTokenAmount = new HashMap<String, String>();
        int recipientNum = getNumOfRecipients();
        for (int i = 1; i<recipientNum +1; i++){
            String token = getSelectedToken(i);
            String amount = getInputAmount(i);
            if(listTokenAmount.containsKey(token)){
                String currentAmount = listTokenAmount.get(token);
                amount = String.valueOf(Double.parseDouble(currentAmount) + Double.parseDouble(amount));
                listTokenAmount.replace(token, currentAmount, amount);
            }else {
                listTokenAmount.put(token, amount);
            }
        }
        return listTokenAmount;
    }

//  items: amount, toAddress, tokenAddress
    public String[][] getListTokens(){
        int recipientNum = getNumOfRecipients();
        String[][] listToken = new String[recipientNum][3];
        for (int i = 1; i<recipientNum+1; i++){
            String amount = getInputAmount(i);
            listToken[i-1][0] = amount;
            String toAddress = getToAddress(i);
            listToken[i-1][1] = toAddress;
            String token = getSelectedToken(i);
            listToken[i-1][2] = balancesAPI.getTokenAddress(token);
        }
        return listToken;
    }

    public void AssertAmount_SUM(Map<String, String> expectedList){
        String sum_token_path = "(//*[contains(@class, \"sum_token-\")])";
        By sum_token = By.xpath(sum_token_path);
        List<WebElement> elems = driver.findElements(sum_token);
        if (elems.size()==expectedList.size()){
            for (int i = 1; i < elems.size()+1; i++){
                String elem = sum_token_path + "["+i+"]";
                String token = readText(By.xpath(elem));
//                (//*[contains(@class, "sum_token-")])[1]/../following-sibling::div //*[contains(@class, "sum-input_amount")]
                elem = elem + "/../following-sibling::div" + sum_amount;
                String actual = readText(By.xpath(elem));
                Object expect = expectedList.get(token);
                Assert.assertEquals(actual, expect);
            }
        }else {
            Assert.assertTrue(false);
        }
    }

    public void AssertAmount_TC(Map<String, String> expectedList){
        By tc_total_amount_ = By.xpath(tc_total_amount);
        List<WebElement> elems = driver.findElements(tc_total_amount_);
        if (elems.size()==expectedList.size()){
            for (int i = 1; i < elems.size()+1; i++){
                By elem = setMultiSendElem(tc_total_amount, i);
                String token = readText(elem).split(" ")[1];
                Object expect = expectedList.get(token);
                String actual = readText(elem).split(" ")[0];
                Assertion.assertAlmostEqual(actual, (String) expect);
            }
        }else {
            Assert.assertTrue(false);
        }
    }
}
