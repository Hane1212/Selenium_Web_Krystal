/**
 * Author: HuongTT
 * Updated: 2022-03-24
 */
package scripts;

import API.common.Environment;
import API.swap.gasPriceAPI;
import API.token.tokenDetailsAPI;
import API.transferAPI;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.*;
import utils.Assertion;
import utils.Util;
import utils.logs.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiSend extends BaseTest {
    String suite = Util.getPropertyValue("SUITE_MS");
    static String Mfile = "MultiSend.csv";
    Object expectValue, actualValue, sum_amount_usd;
    Double gasLimit, gasPrice, maxGasFee;
    String tokenAddress;
    int recipientNum;

    Map<String, String> listTokenAmount = new HashMap<String, String>();
    String[][] tokenList;
    //  -----------------------------------------ACCESS-------------------------------------
    /**
     * Author: HuongTT
     * Updated: 2022-03-29
     * ID: MS_ACC_001
     * Description: Access to Multi Send page by get URL of Multi Send page
     */
//    @Test(groups = {"Access"})
    public void MS_ACC_001_AccessToMultiSendByURL(){
//        1. Access to page by URL
        MultiSendPage.getMultiSendPageByURL();
//        2. Assert the existing of Max button
        AssertPage.assertElementExists(MultiSendPage.transfer_btn);
    }

    //  -----------------------------------------BUSINESS-------------------------------------
    /**
     * Author:HuongTT
     * Updated: 2022-04-06
     * MS_BIZ_001: Perform multi send based on data from file
     */
//    @Test(groups = {"Business"}, description = "Perform multi send based on data from MultiSend.csv file")
    public void MS_BIZ_001_MultiSend(){
        MultiSendPage.performTransfer("MS_001");
        AssertPage.assertString(TransferScreen.confirm_msg, Util.getMessage("CONFIRM_002"));
        ComPage.closeConfirmDialog();
    }

    /**
     * Author:HuongTT
     * Updated: 2022-04-06
     * MS_BIZ_002: Perform unApprove token before perform multi send => perform multi send include approve step
     */
//    @Test(groups = {"Business"}, description = "Perform unApprove token before perform multi send => perform multi send include approve step")
    public void MS_BIZ_002_MultiSend(){
        MultiSendPage.unApproveTokens(suite);
        MultiSendPage.performTransfer(suite);
        AssertPage.assertString(TransferScreen.confirm_msg, Util.getMessage("CONFIRM_002"));
        ComPage.closeConfirmDialog();
    }
    /**
     * Author:HuongTT
     * Updated: 2022-07-13
     * MS_BIZ_003: Validate message when User click transfer button before approve
     */
    @Test
    public void MS_BIZ_003_ShowWarningMsgToApprove(){
        MultiSendPage.unApproveTokens("MS_002");
        MultiSendPage.inputInfoToPerformMultiSend("MS_002");
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("WARN_001");
        AssertPage.assertString(MultiSendScreen.sum_warn_msg, (String) expectValue);
    }
    //  -----------------------------------------Default-----------------------------------------------
    @BeforeGroups(groups = {"Default_MultiSend", "Validate_MultiSend"})
    public void loadMultisendPage(){
        MultiSendPage.getMultiSendPageByURL();
        ComPage.switchChain();
    }

    @Test(groups = "Default_MultiSend")
    public void MTS_DF_001_NumberOfLine(){
        expectValue = 1;
        actualValue = MultiSendPage.getNumOfRecipients();
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = "Default_MultiSend")
    public void MTS_DF_002_AddressPlaceHolder(){
        expectValue = "Recipient Address";
        actualValue = ComPage.getPlaceHolder(MultiSendPage.recipient_add_ip);
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = "Default_MultiSend")
    public void MTS_DF_003_InputAmountPlaceHolder(){
        expectValue = "Enter Amount";
        actualValue = BasePage.getPlaceHolder(MultiSendPage.amount_input);
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = "Default_MultiSend")
    public void MTS_DF_004_DefaultToken(){
        expectValue = "Choose";
        actualValue = BasePage.readText(MultiSendPage.selected_token);
        Assert.assertEquals(actualValue, expectValue);
    }

//    @Test(groups = "Default_MultiSend")
    public void MTS_DF_005_EmptyTable(){
        BasePage.click(By.xpath(MultiSendPage.x_btn));
        expectValue = 1;
        actualValue = MultiSendPage.getNumOfRecipients();
        Assert.assertEquals(actualValue, expectValue);
        expectValue = "Please add more recipient";
        actualValue = BasePage.isContentExist((String) expectValue);
        Assert.assertEquals(actualValue, true);
    }

    //  -----------------------------------------Input to 1 line-----------------------------------------------
    @BeforeMethod(groups = "One_Line_MultiSend")
    public void reloadMultiSendPage(){
        MultiSendPage.getMultiSendPageByURL();
        ComPage.switchChain();
    }

    @Test(groups = "One_Line_MultiSend")
    public void MTS_VAL_1LINE_001_EmptyAddress(){
        ComPage.selectNativeToken();
        BasePage.writeText(MultiSendPage.amount_input, MultiSendPage.getBalanceOfToken());
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("WARN_003");
        actualValue = BasePage.readText(MultiSendPage.err_address);
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = "One_Line_MultiSend")
    public void MTS_VAL_1LINE_002_InvalidAddress(){
        ComPage.selectNativeToken();
        BasePage.writeText(MultiSendPage.amount_input, MultiSendPage.getBalanceOfToken());
        BasePage.writeText(MultiSendPage.recipient_add_ip, ComPage.getRandomInvalidRecipientAddress());
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("WARN_003");
        actualValue = BasePage.readText(MultiSendPage.err_address);
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = "One_Line_MultiSend")
    public void MTS_VAL_1LINE_003_DefaultToken(){
        BasePage.writeText(MultiSendPage.recipient_add_ip, Environment.user_address);
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("ERR_004");
        actualValue = BasePage.readText(MultiSendPage.err_input);
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = "One_Line_MultiSend")
    public void MTS_VAL_1LINE_004_GeneralError(){
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("ERR_005");
        actualValue = BasePage.readText(MultiSendPage.err_sum);
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = "One_Line_MultiSend")
    public void MTS_VAL_1LINE_005_Input0(){
        MultiSendPage.inputAddress();
        ComPage.selectNativeToken();
        MultiSendPage.input0();
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("ERR_006");
        actualValue = BasePage.readText(MultiSendPage.err_input);
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = "One_Line_MultiSend")
    public void MTS_VAL_1LINE_006_UnapprovedToken(){
//      Input address to Recipient address by User address
        MultiSendPage.inputAddress();
//        Select Un Approve token
        String token = ComAPI.getUnApproveToken();
        ComPage.selectSrcTokenByAddress(token);
//        Input amount of selected token (by divide amount balance)
        MultiSendPage.inputAmount(ComAPI.getDivideBalanceToken(token, 2));
        MultiSendPage.clickTransferBtn();
//        Verify that [Approve] button is visible
        actualValue = BasePage.isElemExist(MultiSendPage.approve_btn);
        Assert.assertEquals(actualValue, true);
//        Verify that [Transfer] button is disable
        actualValue = BasePage.isElemEnable(MultiSendPage.transfer_btn);
        Assert.assertEquals(actualValue, false);
    }

    @Test(groups = "One_Line_MultiSend")
    public void MTS_VAL_1LINE_007_ApprovedToken(){
        MultiSendPage.inputAddress();
        String token = ComAPI.getApproveToken();
        ComPage.selectSrcTokenByAddress(token);
        MultiSendPage.inputAmount(ComAPI.getDivideBalanceToken(token, 2));
        MultiSendPage.clickTransferBtn();
        actualValue = BasePage.isElemExist(MultiSendPage.approve_btn);
        Assert.assertEquals(actualValue, false);
        actualValue = MultiSendPage.isTrasferBtnEnable();
        Assert.assertEquals(actualValue, true);
    }

    @Test(groups = "One_Line_MultiSend")
    public void MTS_VAL_1LINE_008_AmountLargerThanBalance(){
        MultiSendPage.inputAddress();
        String token = ComAPI.getApproveToken();
        ComPage.selectSrcTokenByAddress(token);
        double amount = ComAPI.getBalanceBaseTokenUnit(token);
        MultiSendPage.inputAmount(String.valueOf(amount+1));
        MultiSendPage.clickTransferBtn();
        actualValue = MultiSendPage.isTrasferBtnEnable();
        Assert.assertEquals(actualValue, false);
        expectValue = Util.getMessage("WARN_004");
        actualValue = BasePage.readText(MultiSendPage.transfer_btn);
        Assert.assertEquals(actualValue, expectValue);
    }
    //  -----------------------------------------Input to multi lines-----------------------------------------------
    @BeforeMethod(groups = "Multi_Line_MultiSend")
    public void setupMultiSend(){
        MultiSendPage.getMultiSendPageByURL();
        ComPage.switchChain();
//        Add random number of line to perform multiSend (In range of 2->5, may change to 10)
        MultiSendPage.setNumOfLineToTransfer(Util.getRandomNumber(2, 5));
    }

    /**
     * 1. Input all valid address, approved token, amount of token
     * 2. Get random a number in range of Recipients
     * 3. Clear address at the number from step 2
     * 4. Click on [Transfer] button
     * 5. Validate message which get at the address of number from step 2
     */
    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_001_OneLineEmptyAddress(){
        MultiSendPage.setApproveList();
        int i = Util.getRandomNumber(MultiSendPage.getNumOfRecipients());
        MultiSendPage.clearAddress(i);
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("WARN_003");
        actualValue = MultiSendPage.readAddressErr(i);
        Assert.assertEquals(actualValue, expectValue);
    }

    /**
     * 1. Input all valid address, approved token, amount of token
     * 2. Get random a list number in range of Recipients (from 2 lines)
     * 3. Clear address at the number from step 2
     * 4. Click on [Transfer] button
     * 5. Validate message which get at the address of number from step 2
     */
    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_002_MultiLineEmptyAddress(){
        MultiSendPage.setApproveList();
        int max = MultiSendPage.getNumOfRecipients();
        List<Integer> list = Util.pickRandom(max, Util.getRandomNumber(2,max));
        for (int i=0; i<list.size(); i++){
            MultiSendPage.clearAddress(list.get(i));
        }
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("WARN_003");
        for (int i=0; i<list.size(); i++){
            actualValue = MultiSendPage.readAddressErr(list.get(i));
            Assert.assertEquals(actualValue, expectValue);
        }
    }

    /**
     * 1. Input all valid address, approved token, amount of token
     * 2. Get random a number in range of Recipients
     * 3. Input invalid address at the number from step 2
     * 4. Click on [Transfer] button
     * 5. Validate message which get at the address of number from step 2
     */
    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_003_OneLineInvalidAddress(){
        MultiSendPage.setApproveList();
        int i = Util.getRandomNumber(MultiSendPage.getNumOfRecipients());
        MultiSendPage.inputInvalidAddress(i);
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("WARN_003");
        actualValue = MultiSendPage.readAddressErr(i);
        Assert.assertEquals(actualValue, expectValue);
    }
    /**
     * 1. Input all valid address, approved token, amount of token
     * 2. Get random list of number in range of Recipients (from 2 to max)
     * 3. Input invalid address at the number from step 2
     * 4. Click on [Transfer] button
     * 5. Validate message which get at the address of number from step 2
     */
    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_004_MultiLineInvalidAddress(){
        MultiSendPage.setApproveList();
        int max = MultiSendPage.getNumOfRecipients();
        List<Integer> list = Util.pickRandom(max, Util.getRandomNumber(2,max));
        for (int i=0; i<list.size(); i++){
            MultiSendPage.inputInvalidAddress(list.get(i));
        }
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("WARN_003");
        for (int i=0; i< list.size(); i++){
            actualValue = MultiSendPage.readAddressErr(list.get(i));
            Assert.assertEquals(actualValue, expectValue);
        }
    }

    /**
     * 1. Input all valid address, approved token, amount of token
     * 2. Add a new line by [Add more] button
     * 3. Input valid address at the line from step 2
     * 4. Click on [Transfer] button
     * 5. Validate message which get at the token area from step 2
     */
    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_005_OneLineNotSelectedToken(){
        MultiSendPage.setApproveList();
        MultiSendPage.clickAddMoreBtn();
        int i = MultiSendPage.getNumOfRecipients();
        MultiSendPage.inputAddress(i);
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("ERR_004");
        actualValue = MultiSendPage.readTokenErr(i);
        actualValue = BasePage.readText(MultiSendPage.err_input);
        Assert.assertEquals(actualValue, expectValue);
    }

    /**
     * 1. Get number of line
     * 2. Input valid address at the all lines
     * 3. Click on [Transfer] button
     * 4. Validate message which get at the tokens area
     */
    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_006_MultiLineNotSelectedToken(){
        int max = MultiSendPage.getNumOfRecipients();
        for (int i=1; i<max+1; i++){
            MultiSendPage.inputAddress(i);
        }
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("ERR_004");
        for (int i=1; i<max+1; i++){
            actualValue = MultiSendPage.readTokenErr(i);
            Assert.assertEquals(actualValue, expectValue);
        }
    }
    /**
     * 1. Click on [Transfer] button when keep address and token empty
     * 2. Validate general message below [Transfer] button
     */
    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_007_GeneralError(){
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("ERR_005");
        actualValue = BasePage.readText(MultiSendPage.err_sum);
        Assert.assertEquals(actualValue, expectValue);
    }

    /**
     * 1. Input all valid address, approved token, amount of token
     * 2. Get a random number in range 0 -> max recipients
     * 3. Input value =0 at the amount of line from step 2
     * 4. Click on [Transfer] button
     * 5. Validate message which get at the token area from step 2
     */
    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_008_OneLineInput0(){
        MultiSendPage.setApproveList();
        int i = Util.getRandomNumber(MultiSendPage.getNumOfRecipients());
        MultiSendPage.input0(i);
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("ERR_006");
        actualValue = MultiSendPage.readTokenErr(i);
        Assert.assertEquals(actualValue, expectValue);
    }

    /**
     * 1. Input all valid address, approved token, amount of token
     * 2. Get a list random number in range 0 -> max recipients
     * 3. Input value = 0 at the amount of line from step 2
     * 4. Click on [Transfer] button
     * 5. Validate message which get at the token area from step 2
     */
    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_009_MultiLineInput0(){
        By elem;
        MultiSendPage.setApproveList();
        int max = MultiSendPage.getNumOfRecipients();
        List<Integer> list = Util.pickRandom(max, Util.getRandomNumber(2,max));
        for (int i=0; i<list.size(); i++){
            MultiSendPage.input0(list.get(i));
        }
        MultiSendPage.clickTransferBtn();
        expectValue = Util.getMessage("ERR_006");
        for (int i=0; i<list.size(); i++){
            actualValue = MultiSendPage.readTokenErr(list.get(i));
            Assert.assertEquals(actualValue, expectValue);
        }
    }

    /**
     * 1. Input all valid address, approved token, amount of token
     * 2. Get a random number in range 0 -> max recipients
     * 3. Select an unApprove token
     * 4. Validate [Approve] button is visible & enable
     * 5. [Transfer] button is disable
     */
    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_010_OneLineUnapprovedToken(){
        MultiSendPage.setApproveList();
        int i = Util.getRandomNumber(1, MultiSendPage.getNumOfRecipients());
        tokenAddress = MultiSendPage.selectUnApproveToken(i);
        MultiSendPage.inputAmount(i, ComAPI.getDivideBalanceToken(tokenAddress, 2));
        MultiSendPage.clickTransferBtn();
//        Verify that [Approve] button is visible
        actualValue = MultiSendPage.isApproveBtnExist(tokenAddress);
        Assert.assertEquals(actualValue, true);
//        Verify that [Transfer] button is disable
        actualValue = BasePage.isElemEnable(MultiSendPage.transfer_btn);
        Assert.assertEquals(actualValue, false);
    }
    /**
     * 1. Input all valid address, approved token, amount of token
     * 2. Get a random number in range 0 -> max recipients
     * 3. Select an unApprove token
     * 4. Validate [Approve] button is visible & enable
     * 5. [Transfer] button is disable
     */
    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_011_MultiLineUnapprovedToken(){
        MultiSendPage.setApproveList();
        int max = MultiSendPage.getNumOfRecipients();
        List<String> tokens = new ArrayList<>();
        List<Integer> list = Util.pickRandom(max, Util.getRandomNumber(2,max));
        for (int i=0; i<list.size(); i++){
            String tokenAddress = MultiSendPage.selectUnApproveToken(list.get(i));
            Log.info("Un Approve token: "+ tokenAddress);
            MultiSendPage.inputAmount(list.get(i),ComAPI.getDivideBalanceToken(tokenAddress, max));
            tokens.add(tokenAddress);
        }
        BasePage.click(MultiSendPage.total_rep_label);
//        Verify that [Approve] button is visible
        for (int i = 0; i<tokens.size(); i++){
            actualValue = MultiSendPage.isApproveBtnExist(tokens.get(i));
            Assert.assertEquals(actualValue, true);
        }
//        Verify that [Transfer] button is disable
        actualValue = MultiSendPage.isTrasferBtnEnable();
        Assert.assertEquals(actualValue, false);
    }
    /**
     * 1. Input all valid value to all field
     * 2. Validate [Transfer] button is enable
     */
    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_012_MultiLineApprovedToken(){
        MultiSendPage.setApproveList();
        actualValue = MultiSendPage.isTrasferBtnEnable();
        Assert.assertEquals(actualValue, true);
    }

    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_013_OneLineAmountLargerThanBalance(){
        MultiSendPage.setApproveList();
        int i = Util.getRandomNumber(MultiSendPage.getNumOfRecipients());
        tokenAddress = MultiSendPage.selectApproveToken(i);
        double amount = ComAPI.getBalanceBaseTokenUnit(tokenAddress);
        MultiSendPage.inputAmount(i, String.valueOf(amount+1));
        MultiSendPage.clickTransferBtn();
        actualValue = MultiSendPage.isTrasferBtnEnable();
        Assert.assertEquals(actualValue, false);
        expectValue = Util.getMessage("WARN_004");
        actualValue = BasePage.readText(MultiSendPage.transfer_btn);
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_014_MultiLineAmountLargerThanBalance(){
        String tokenAddress = ComAPI.getApproveToken();
        double amount = ComAPI.getBalanceBaseTokenUnit(tokenAddress);
        recipientNum = MultiSendPage.getNumOfRecipients();
        for (int i =1; i<  recipientNum+1; i++){
            MultiSendPage.inputAddress(i);
            By elem = MultiSendPage.setMultiSendElem(MultiSendPage.select_token_st, i);
            ComPage.selectTokenByAddress(elem, tokenAddress);
            MultiSendPage.inputAmount(i, String.valueOf(((amount+1)/recipientNum)));
        }
        MultiSendPage.clickTransferBtn();
        actualValue = MultiSendPage.isTrasferBtnEnable();
        Assert.assertEquals(actualValue, false);
        expectValue = Util.getMessage("WARN_004");
        actualValue = BasePage.readText(MultiSendPage.transfer_btn);
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = "Multi_Line_MultiSend")
    public void MTS_VAL_MLINE_015_AmountToken(){
        MultiSendPage.setApproveList();
        listTokenAmount = MultiSendPage.getListTokenAmount();
        MultiSendPage.AssertAmount_SUM(listTokenAmount);
    }

    //  -----------------------------------------Approve token-----------------------------------------------
    @BeforeGroups(groups = "Approve_MultiSend")
    public void setupApproveMultiSend(){
        MultiSendPage.getMultiSendPageByURL();
        ComPage.switchChain();
//        Add random number of line to perform multiSend (In range of 2->5, may change to 10)
        MultiSendPage.setNumOfLineToTransfer(Util.getRandomNumber(2, 5));
        MultiSendPage.setApproveList();
        int i = Util.getRandomNumber(MultiSendPage.getNumOfRecipients());
        tokenAddress = MultiSendPage.selectUnApproveToken(i);
        MultiSendPage.inputAmount(i,ComAPI.getDivideBalanceToken(tokenAddress, 2) );
        MultiSendPage.clickApproveBtn(tokenAddress);
    }

    @Test(groups = "Approve_MultiSend")
    public void MTS_VAL_APR_001_AddressOfToken(){
        expectValue = tokenAddress;
        actualValue = BasePage.readText(MultiSendPage.token_address);
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = "Approve_MultiSend")
    public void MTS_VAL_APR_002_GasFee(){
        expectValue = ComAPI.calculateMaxGasFee(tokenAddress, ComAPI.getDivideBalanceToken(tokenAddress, 2));
        actualValue = BasePage.readText(MultiSendPage.token_address);
        Assertion.assertAlmostEqual((String) actualValue, (String) expectValue);
    }

    @Test(groups = "Approve_MultiSend")
    public void MTS_VAL_APR_004_CancelApprove(){
        BasePage.click(MultiSendPage.canncel_approve_btn);
        //        Verify that [Approve] button is visible
        actualValue = MultiSendPage.isApproveBtnExist(tokenAddress);
        Assert.assertEquals(actualValue, true);
//        Verify that [Transfer] button is disable
        actualValue = MultiSendPage.isTrasferBtnEnable();
        Assert.assertEquals(actualValue, false);
    }
//  -----------------------------------------Transfer Confirmation-----------------------------------------------
    @BeforeGroups(groups = "Transfer_Confirmation")
    public void setupTransferMultiSend(){
        MultiSendPage.getMultiSendPageByURL();
        ComPage.switchChain();
    //        Add random number of line to perform multiSend (In range of 2->5, may change to 10)
        MultiSendPage.setNumOfLineToTransfer(Util.getRandomNumber(2, 10));
        MultiSendPage.setApproveList();
        BasePage.click(MultiSendPage.recipient_add_ip);
        recipientNum = MultiSendPage.getNumOfRecipients();
        tokenList = MultiSendPage.getListTokens();//Include toAddress
        listTokenAmount = MultiSendPage.getListTokenAmount();//token and total amount
        sum_amount_usd = String.valueOf(MultiSendPage.getTotalUSDvalue());
        gasLimit = Double.valueOf(API.multisend.buildTxAPI.getGasLimitByAPI(tokenList));
        gasPrice = gasPriceAPI.getGasPriceStandard();
        Log.info("gasLimit: "+gasLimit);
        Log.info("gasPrice: "+gasPrice);
        maxGasFee = Util.roundAvoidD(gasPrice * Double.parseDouble(String.valueOf(gasLimit)),9,5);
        MultiSendPage.clickTransferBtn();
    }

    @Test(groups = "Transfer_Confirmation")
    public void MTS_VAL_TC_001_WarningMsg(){
        expectValue = "Please sure all recipient address supports "+Environment.test_chain
                +" network. You will lose your assets if any recipient address doesn't support "
                +Environment.test_chain+" compatible retrieval.";
        actualValue = MultiSendPage.getWarnMsgAtTransferConfirm();
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = "Transfer_Confirmation")
    public void MTS_VAL_TC_002_NumberOfTransfer(){
        expectValue = recipientNum;
        actualValue = MultiSendPage.getRecipNumAtTransferConfirm();
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = "Transfer_Confirmation")
    public void MTS_VAL_TC_003_AmountOfToken(){
        MultiSendPage.AssertAmount_TC(listTokenAmount);
    }

    @Test(groups = "Transfer_Confirmation")
    public void MTS_VAL_TC_004_TotalAmountUSDValue(){
        expectValue = sum_amount_usd;
        actualValue = MultiSendPage.getTotalAmountUSD();
        Log.info("Actual Max Gas Fee: "+actualValue);
        Log.info("Expect Max Gas Fee: "+expectValue);
        Assertion.assertAlmostEqual( MultiSendPage.getTotalAmountUSD(), (String) expectValue);
    }

    @Test(groups = {"Transfer_Confirmation"})
    public void MTS_VAL_TC_005_GasFee(){
        expectValue = maxGasFee;
        actualValue = MultiSendPage.readGasFee();
        Log.info("Actual Max Gas Fee: "+actualValue);
        Log.info("Expect Max Gas Fee: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Transfer_Confirmation"})
    public void MTS_VAL_TC_006_GasFeeUSDValue(){
        expectValue = maxGasFee * tokenDetailsAPI.get_usd_value_of_native_token();
        actualValue = MultiSendPage.readGasFeeUSD();
        Log.info("Actual Max Gas Fee USD: "+actualValue);
        Log.info("Expect Max Gas Fee USD: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    //  -----------------------------------------Execute test Business-----------------------------------------------
    @Test(groups = {"MultiSend_execute"})
    public void MTS_BiZ_001_NativeToken(){
        MultiSendPage.getMultiSendPageByURL();
        ComPage.switchChain();
        MultiSendPage.setNumOfLineToTransfer(2);
        MultiSendPage.executeTransfer();
//        TODO
    }

    @Test(groups = {"MultiSend_execute"})
    public void MTS_BiZ_002_SupportToken(){
        MultiSendPage.getMultiSendPageByURL();
        ComPage.switchChain();
        MultiSendPage.setNumOfLineToTransfer(2);
        MultiSendPage.setApproveList();
        MultiSendPage.executeTransfer();
    }
    //  -----------------------------------------Execute test based on data driven-------------------------------------
    /**
     * Author: HuongTT
     * Updated: 2022-04-14
     * @implSpec get data for swap
     * @return
     */
    @DataProvider(name = "multiSendBaseDataFile")
    public Object[][] getData() {
        //feel free to replace this with the logic that reads up a csv file (using CSVReader)
        // and then translates it to a 2D array.
        String key = Util.getPropertyValue("SUITE_MS");
//      Test_ID,Token,Amount,Receiver,IsApproval,Remark
        List<String> listOfMaps = Util.getColumnOfKey(Mfile, "Test_ID");
        Object [][]data = new Object [listOfMaps.size()-1][];
        for(int i = 0; i < listOfMaps.size()-1; i++) {
            data[i]= new Object[1];
            data[i][0] = listOfMaps.get(i + 1);
        }
        System.out.println("NumberOfData: "+ data.length);
        return data;
    }
    /**
     * Author: HuongTT
     * Updated: 2022-04-14
     * @implSpec execute swap based on data from MultiSend.csv
     */
    @Test(dataProvider = "multiSendBaseDataFile", groups = {"Business"}, description = "Execute swap based on data from MultiSend.csv")
    public void MS_BIZ_005_multiSendBaseDataFile(String tcID){
        System.out.println(tcID);
        MultiSendPage.performTransfer(tcID);
    }


}

