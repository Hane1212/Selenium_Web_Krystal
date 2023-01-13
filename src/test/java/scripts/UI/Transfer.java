package scripts.UI;

import API.accounts.balancesAPI;
import API.common.Environment;
import API.market.overviewAPI;
import API.swap.gasPriceAPI;
import API.token.tokenDetailsAPI;
import API.token.tokenListAPI;
import API.transferAPI;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.BasePage;
import pages.ComAPI;
import pages.TransferScreen;
import scripts.BaseTest;
import utils.Util;
import utils.logs.Log;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class Transfer extends BaseTest {

    static String Tfile = "Transfer.csv";
    String toAddress = Util.getPropertyValue("RECIPIENT_ADDRESS");
    double usd;
    double amountUSD, gasPrice, amount_in_BigInteger;
    String gasLimit, srcToken, amount, srcTokenAddress;
    double maxGasFee;

    //    Swap screen
    Object expectValue, actualValue;

    @BeforeClass
    public void log(){
        Log.info("Start test for Transfer function.");
    }

    //  -----------------------------------------ACCESS-------------------------------------
    /**
     * ID: SWP_001
     * Description: Access to transfer page by get URL of transfer page
     */
    @Test(groups = {"Access"})
    public void AccessTotransferByURL(){
//        1. Access to page by URL
        TransPage.getTransferPageByURL();
//        2. Check the exist of Max button
        AssertPage.assertElementExists(ComPage.src_token_opt);
    }

    /**
     * ID: SWP_002
     * Description: Access to transfer page by click on transfer button from main page
     */
//    @Test(groups = {"Access"})
    public void AccessTotransferFromMarket(){
//         1. Access to page by transfer button
        TransPage.getTransferPageFromMarket();
//        2. Check the exist of Max button
        AssertPage.assertElementExists(TransferScreen.max_btn);
    }

    /**
     * ID: SWP_003
     * @implSpec : Access to transfer page by click on transfer button from explore page
     */
//    @Test(groups = {"Access"})
    public void AccessTotransferByExplore(){
//        1. Access to page by transfer from Explore
        TransPage.getTransferPageByExplore();
//        2. Assertion the exist of Max button
        AssertPage.assertElementExists(TransferScreen.max_btn);
    }

//    @Test(groups = {"Validation"})
    public void transferNativeOnRos(Method method) {
//        2. Perform transfer native token
        TransPage.transferNativeToken("2");
        AssertPage.assertString(TransferScreen.confirm_msg, Util.getMessage("CONFIRM_002"));
        ComPage.closeConfirmDialog();
    }

//    @Test(groups = {"Validation"})
    public void transferSupTokenOnRos(Method method)  {
//      2. Perform transfer support token
        TransPage.transferToken("KNC", "2", Environment.recipientAddress);
        AssertPage.assertString(TransferScreen.confirm_msg, Util.getMessage("CONFIRM_002"));
        ComPage.closeConfirmDialog();
    }


    @BeforeGroups(groups = "Validate_Default_Transfer", alwaysRun = true)
    public void switchToTestChain(){
        //      Reload Transfer page
        TransPage.getTransferPageByURL();
        //      Switch chain
        ComPage.switchChain();
    }

    @Test(groups = {"Validate_Default_Transfer"})
    public void TRANS_VAL_DF_001_ValidateDefaultSrcToken(){
//        1. Get Native token
        expectValue = test_env.nativeToken;
        actualValue = ComPage.getSourceToken();
//        3. Verify that, current token at source is native token
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = {"Validate_Default_Transfer"})
    public void TRANS_VAL_DF_002_DefaultValue(){
        expectValue = "0.00";
        actualValue = ComPage.getPlaceHolder(ComPage.src_input);
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = {"Validate_Default_Transfer"})
    public void TRANS_VAL_DF_003_srcMaxBalance(){
        srcToken = ComPage.getSourceToken();
        expectValue = balancesAPI.getMaxBalanceOfUserAddress(srcToken);
        actualValue = TransPage.getMaxAmount();
        Log.info("Actual Max amount of source token: "+actualValue);
        Log.info("Expect Max amount of source token: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Default_Transfer"})
    public void TRANS_VAL_DF_004_EmptyAmount(){
        BasePage.waitVisibility(ComPage.src_input).clear();
        boolean btn_state = driver.findElement(TransPage.transfer_btn).isEnabled();
        Assert.assertEquals(false, btn_state);
    }

    @Test(groups = {"Validate_Default_Transfer"})
    public void TRANS_VAL_DF_005_EmptyAddress(){
        BasePage.writeText(ComPage.src_input, "0.0001");
        BasePage.waitVisibility(TransPage.address_in).clear();
        BasePage.click(TransPage.transfer_btn);
        actualValue = BasePage.readText(TransPage.err_msg);
        expectValue = Util.getMessage("WARN_003");
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = {"Validate_Default_Transfer"})
    public void TRANS_VAL_DF_006_InvalidAddress(){
        BasePage.writeText(ComPage.src_input, "0.0001");
        BasePage.writeText(TransPage.address_in, ComPage.getRandomInvalidRecipientAddress());
        BasePage.click(TransPage.transfer_btn);
        actualValue = BasePage.readText(TransPage.err_msg);
        expectValue = Util.getMessage("WARN_003");
        Assert.assertEquals(actualValue, expectValue);
    }

//    @Test(groups = {"Validate_Default_Transfer"})
    public void TRANS_VAL_DF_007_MaxAmountOfNativeToken(){
        TransPage.selectNativeToken();
        actualValue = ComPage.getSourceToken();
        expectValue = "A small amount of "+actualValue.toString().toUpperCase()+" will be used for transaction fee";
        BasePage.click(TransPage.max_btn);
        actualValue = ComPage.getToolTipsMsg();
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = {"Validate_Default_Transfer"})
    public void TRANS_VAL_DF_008_InputAmountLargerThanBalance(){
        TransPage.inputAmountLargerThanBalance();
        BasePage.writeText(TransPage.address_in, Environment.user_address);
        BasePage.click(TransPage.transfer_btn);
        actualValue = BasePage.readText(TransPage.err_msg);
        expectValue = Util.getMessage("ERR_002");
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = {"Validate_Default_Transfer"})
    public void TRANS_VAL_DF_009_InputAmountIs0(){
        BasePage.writeText(ComPage.src_input, "0.000");
        BasePage.writeText(TransPage.address_in, Environment.user_address);
        BasePage.click(TransPage.transfer_btn);
        actualValue = BasePage.readText(TransPage.err_msg);
        expectValue = Util.getMessage("ERR_003");
        Assert.assertEquals(actualValue, expectValue);
    }


    //  -----------------------------------------TRANSFER CONFIRMATION--------------------------------------
    @BeforeGroups(groups = "Validate_Transfer")
    public void preDataToTransfer(){
        srcToken = TransPage.getSourceTokenForAPITest();
        srcTokenAddress = tokenListAPI.getTokenAddress(srcToken);
        double amount_ = ComAPI.getBalanceBaseTokenUnit(Environment.test_chain, srcTokenAddress);
        amount = String.valueOf(amount_/2);
//        Reload Swap page
        TransPage.getTransferPageByURL();
        //        Switch chain
        ComPage.switchChain();
        Log.info("Source token to Transfer: "+ srcToken);
//        SwapPage.getSwapPageByURL();
        TransPage.setupTransfer(srcToken, amount, Environment.recipientAddress);
        //          Get Rate
        srcTokenAddress = tokenListAPI.getTokenAddress(srcToken);
        usd = overviewAPI.getUSD(srcTokenAddress);
//          Dest Income = rate * amount
        amountUSD = usd *Double.valueOf(amount);
//          Minimum Receive = DesAmount - DesAmount*Slipage
        int decimals = tokenListAPI.getDecimals(srcTokenAddress);
        amount_in_BigInteger = Util.roundAvoidM(Double.parseDouble(amount), decimals) ;
        gasLimit = String.valueOf(transferAPI.getGasLimit(srcTokenAddress, Environment.recipientAddress, amount_in_BigInteger));
        gasPrice = gasPriceAPI.getGasPriceStandard();
        maxGasFee = Util.roundAvoidD(gasPrice * Double.parseDouble(gasLimit),9,5);
    }

    @Test(groups = {"Validate_Transfer"})
    public void TRANS_VAL_001_validateSourceToken(){
        expectValue = srcToken;
        actualValue = TransPage.getTransferToken();
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = {"Validate_Transfer"})
    public void TRANS_VAL_002_validateTransferAmount(){
        expectValue = amount;
        actualValue = TransPage.getTransferAmount();
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue) );
    }

    @Test(groups = {"Validate_Transfer"})
    public void TRANS_VAL_003_validateTransferAmountUSD(){
        expectValue = Double.parseDouble(amount) * usd;
        actualValue = TransPage.getTransferAmountUSD();
        Log.info("Actual Transfer Amount USD: "+actualValue);
        Log.info("Expect Transfer Amount USD: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

////    @Test(groups = {"Validate_Transfer"})
//    public void TRANS_VAL_004_validateGasPrice(){
//        expectValue = gasPrice;
//        actualValue = TransPage.getGasPrice();
//        Log.info("Actual Gas Price: "+actualValue);
//        Log.info("Expect Gas Price: "+expectValue);
//        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
//    }
//
////    @Test(groups = {"Validate_Transfer"})
//    public void TRANS_VAL_005_validateGasLimit(){
//        expectValue = String.valueOf(transferAPI.getGasLimit(srcTokenAddress, recipientAddress, amount_in_BigInteger));
//        actualValue = TransPage.getGasLimit();
//        Log.info("Actual Gas Limit: "+actualValue);
//        Log.info("Expect Gas Limit: "+expectValue);
//        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
//    }

    @Test(groups = {"Validate_Transfer"})
    public void TRANS_VAL_006_validateMaxGasFee(){
        expectValue = maxGasFee;
        actualValue = TransPage.getMaxGasFee();
        Log.info("Actual Max Gas Fee: "+actualValue);
        Log.info("Expect Max Gas Fee: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

        @Test(groups = {"Validate_Transfer"})
    public void TRANS_VAL_007_validateMaxGasFeeUSD(){
        expectValue = maxGasFee * tokenDetailsAPI.get_usd_value_of_native_token();
        actualValue = TransPage.getMaxGasFeeUSD();
        Log.info("Actual Max Gas Fee USD: "+actualValue);
        Log.info("Expect Max Gas Fee USD: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

//    @Test(groups = {"Validate_Transfer"})
//    public void TRANS_VAL_008_validateMaxBalance(){
//        expectValue = maxGasFee * usd;
//        actualValue = TransPage.getMaxGasFeeUSD();
//        Log.info("Actual Max Balance: "+actualValue);
//        Log.info("Expect Max Balance: "+expectValue);
//        AssertPage.assertAlmostEqual(String.valueOf(expectValue), String.valueOf(actualValue));
//    }

    //  -----------------------------------------Execute test based on data driven-----------------------------------------------
    @BeforeMethod(onlyForGroups = {"Business_Transfer"}, alwaysRun = true)
    public void TearUp(ITestResult result){
        Log.info("Start test on "+ result.getMethod().getMethodName());
        TransPage.getTransferPageByURL();
        ComPage.switchChain();
    }
    /**
     * Author: HuongTT
     * Updated: 2022-04-05
     * @implSpec get data for Transfer
     */
    @DataProvider(name = "transferBaseDataFile")
    public Object[][] getData() {
        //feel free to replace this with the logic that reads up a csv file (using CSVReader)
        // and then translates it to a 2D array.
        String key = Util.getPropertyValue("SUITE_TRANS");
        List<Map<String, String>> listOfMaps = Util.getListTestDataBaseOnTCID(Tfile, key);

        Object [] [] data = new Object [listOfMaps.size()] [4];
        for (int i =0; i<listOfMaps.size();i++){
            data[i][0]=listOfMaps.get(i).get("TC_ID");
            data[i][1]=listOfMaps.get(i).get("TokenSource");
            data[i][2]=listOfMaps.get(i).get("AddressDes");
            data[i][3]=listOfMaps.get(i).get("Amount");
        }
        return data;
    }

    /**
     * Author: HuongTT
     * Updated: 2022-03-31
     * @implSpec execute transfer based on data from Transfer.csv
     */
    @Test(dataProvider = "transferBaseDataFile", groups = {"Business_Transfer"}, description = "Execute transfer based on data from Transfer.csv")
    public void TRANS_BIZ_005_TransferBaseDataFile(String tcID, String srcToken, String desAddress, String amount){
//        1. Perform swap based on data from CSV file
        TransPage.transferToken(srcToken, amount, desAddress);
        AssertPage.assertString(TransferScreen.confirm_msg, Util.getMessage("CONFIRM_002"));
        ComPage.closeConfirmDialog();
    }


}
