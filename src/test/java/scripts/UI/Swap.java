package scripts.UI;

import API.accounts.balancesAPI;
import API.common.Environment;
import API.swap.allRatesAPI;
import API.swap.gasPriceAPI;
import API.token.tokenDetailsAPI;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.*;
import scripts.BaseTest;
import utils.*;
import utils.logs.Log;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class Swap extends BaseTest {
    static String Sfile = "Swap.csv";
    //Common
    String srcToken, destToken, amount;
//    Swap screen
    Object expectValue, actualValue;
    Double destIncome, slipPage, minReceive, network_fee, max_network_fee, gasPrice;
    Double usd_rate, priceImpact, estGasConsumed, gasLimit, rate;
    Double gasFee, usd;
// Swap confirmation

    @BeforeClass
    public void log(){
        Log.info("Start test for Swap function.");
        SwapPage.getSwapPageByURL();
    }

//  -----------------------------------------ACCESS-------------------------------------
    /**
     * Author: HuongTT
     * Updated: 2022-03-07
     * ID: SWP_001
     * Description: Access to Swap page by get URL of swap page
     */
    @Test(groups = {"Access"})
    public void SWP_ACC_001_AccessToSwapByURL(){
//        1. Access to page by URL
        SwapPage.getSwapPageByURL();
//        2. Assert the existing of Max button
        AssertPage.assertElementExists(SwapScreen.src_token_opt);
    }

    /**
     * Author: HuongTT
     * Updated: 2022-03-07
     * ID: SWP_002
     * Description: Access to Swap page by click on swap button from main page
     */
    @Test(groups = {"Access"})
    public void SWP_ACC_002_AccessToSwapBySwapBtn(){
//         1. Access to page by Swap button
        SwapPage.getSwapPageBySwapBtn();
//        2. Assert the existing of Max button
        AssertPage.assertElementExists(SwapScreen.max_btn);
    }

    /**
     * Author: HuongTT
     * Updated: 2022-03-07
     * ID: SWP_003
     * @implSpec : Access to Swap page by click on swap button from explore page
     */
    @Test(groups = {"Access"})
    public void SWP_ACC_003_AccessToSwapByExplore(){
//        1. Access to page by Swap from Explore
        SwapPage.getSwapPageByExplore();
//        2. Assertion the exist of Max button
        Assertion.assertElementExists(SwapScreen.max_btn);
    }
//  -----------------------------------------VALIDATION-------------------------------------
    //  --------------------------------------SWAP-------------------------------------
    @BeforeGroups(groups = "Validate_Default_Swap", alwaysRun = true)
    public void switchToTestChain(){
        //      Reload Swap page
        SwapPage.getSwapPageByURL();
        //      Switch chain
        ComPage.switchChain();
    }
    /**
     * Author: HuongTT
     * Updated: 2022-11-07
     * ID : SWP_005
     * @implSpec : Default token is native token
     */
    @Test(groups = {"Validate_Default_Swap"})
    public void SWP_VAL_DF_001_ValidateDefaultSrcToken(){
//        1. Get Native token
        expectValue = test_env.nativeToken;
//        2. Access to Swap page
        SwapPage.getSwapPageByURL();
//        3. Verify that, current token at source is native token
        actualValue = pages.ComPage.getSourceToken();
        Log.info("Actual Default Src token: "+actualValue);
        Log.info("Expect Default Src token: "+expectValue);
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = {"Validate_Default_Swap"})
    public void SWP_VAL_DF_002_DefaultValue(){
        expectValue = "0.00";
        actualValue = SwapPage.getPlaceHolder();
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = {"Validate_Default_Swap"})
    public void SWP_VAL_DF_003_EmptyAmount(){
        actualValue = BasePage.readText(SwapScreen.default_btn);
        expectValue = Util.getMessage("WARN_002");
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = {"Validate_Default_Swap"})
    public void SWP_VAL_DF_004_InputAmountLargerThanBalance(){
        SwapPage.inputAmountLargerThanBalance();
        actualValue = ComPage.getSourceToken();
        expectValue = "Insufficient "+actualValue.toString().toUpperCase()+ " Balance";
        actualValue = BasePage.readText(SwapScreen.default_btn);
        Assert.assertEquals(actualValue, expectValue);
    }

//    @Test(groups = {"Validate_Default_Swap"})
    public void SWP_VAL_DF_005_MaxAmountOfNativeToken(){
        SwapPage.selectNativeTokenAtSrc();
        actualValue = ComPage.getSourceToken();
        expectValue = "A small amount of "+actualValue.toString().toUpperCase()+" will be used for transaction fee";
        BasePage.click(SwapScreen.src_balance);
        actualValue = ComPage.getToolTipsMsg();
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = {"Validate_Default_Swap"})
    public void SWP_VAL_DF_006_ValidAmount(){
        SwapPage.inputValidAmount();
        Assert.assertEquals(true, BasePage.isElemExist(SwapScreen.review_swap_btn));
    }


    /**
     * Author: HuongTT
     * Updated: 2022-03-07
     * ID : SWP_006
     * @implSpec : Default destination token is "Choose"
     */
//    @Test(groups = {"Validation"})
    public void SWP_007_DefaultDesToken(){
        Assertion.assertString(SwapScreen.des_token_opt, "Choose");
    }


    /**
     * Author: HuongTT
     * Updated: 2022-02-21
     * @implSpec Verify that, it allows select Support token at source
     */
//    @Test(groups = {"Validation"})
    public void SelectSupTokenAtSrc(){
        String selectToken = SwapPage.selectSrcToken();
        AssertPage.assertString(SwapScreen.src_token_opt, selectToken);
    }
    /**
     * Author: HuongTT
     * Updated: 2022-02-21
     * @implSpec Verify that, it allows select Native token at source
     */
//    @Test(groups = {"Validation"})
    public void SelectNaTokenAtSrc(Method method){
        String nativeToken = test_env.nativeToken;
        SwapPage.selectNativeTokenAtSrc();
        AssertPage.assertString(SwapScreen.src_token_opt, nativeToken);
    }
    /**
     * Author: HuongTT
     * Updated: 2022-02-21
     * @implSpec Verify that, it allows select Support token at destination
     */
//    @Test(groups = {"Validation"})
    public void SelectSupTokenAtDes(Method method){
        String selectToken = SwapPage.selectDesToken();
        AssertPage.assertString(SwapScreen.des_token_opt, selectToken);
    }
    /**
     * Author: HuongTT
     * Updated: 2022-02-21
     * @implSpec Verify that, it allows select Native token at destination
     */
//    @Test(groups = {"Validation"})
    public void SelectNaTokenAtDes(Method method){
        String nativeToken = test_env.nativeToken;
        SwapPage.selectNativeTokenAtDes();
        AssertPage.assertString(SwapScreen.des_token_opt, nativeToken);
    }
    /**
     * Author: HuongTT
     * Updated: 2022-02-21
     * @implSpec Verify that, it allows select Native token at destination
     */
    public void SelectMaxAmount(){
        String expMsg = "";
        String msg = SwapPage.getMessageAtMaxAmount();
        Assert.assertEquals(msg, expMsg);
    }

    @BeforeGroups(groups = "Validate_Review_Swap", alwaysRun = true)
    public void preDataForReviewSwap(){
        srcToken = SwapPage.getSourceTokenForAPITest();
        destToken = SwapPage.getDestTokenForAPITest();
        amount = SwapPage.getAmountForAPITest();
        slipPage = Double.valueOf("0.5");
        //      Reload Swap page
        SwapPage.getSwapPageByURL();
        //      Switch chain
        ComPage.switchChain();
        Log.info("[SWAP] srcToken: "+ srcToken);
        Log.info("[SWAP] destToken: "+ destToken);
        SwapPage.setupSwap(srcToken, destToken, amount, false);
        //  -----------------------------------------PREPARE DATA TO VALIDATE SWAP-------------------------------------
        //  -----------------------------------------COMMON DATA FROM ALLRATES-----------------------------------------
        //      Get Information from all Rates API
        //        (amount * rate) * rate_usd - net fee est

        Map<String, String> dict = allRatesAPI.getAllRatesInfo(srcToken, destToken, amount);

        priceImpact = Double.valueOf(dict.get("priceImpact"))/100;
        Double rate_ = Double.valueOf(dict.get("rate"));
        //  -----------------------------------------SPECIFIC DATA FROM API--------------------------------------------
        estGasConsumed = Double.valueOf(dict.get("estGasConsumed"));
        gasLimit = Double.valueOf(dict.get("estimatedGas"));
        usd_rate = tokenDetailsAPI.get_usd_value_of_native_token();
        gasPrice = gasPriceAPI.getGasPriceStandard();
        //  -----------------------------------------COMMON VALUE FOR ALL SCREENS---------------------------------------
        //      Get Rate
        rate = Util.roundAvoidD(rate_, 18, 6);
        //      Dest Income = rate * amount
        destIncome = rate *Double.valueOf(amount);
        Log.info("destIncome: "+ destIncome);
        //      Minimum Receive = DesAmount - DesAmount * Slippage
        minReceive = Util.roundPlace(destIncome - (destIncome * slipPage/100),6);
        //      Common calculate
        common_calculate();
    }
    //  ---------------------------------------------REVIEW SWAP----------------------------------------
    @Test(groups = {"Validate_Review_Swap"})
    public void SWP_VAL_RS_001_inputAmountUSDValue(){
        expectValue = Double.parseDouble(amount) * tokenDetailsAPI.get_usd_rate(srcToken);
        actualValue = SwapPage.getInputAmountUSDValue();
        Log.info("Actual Input Amount in USD: "+actualValue);
        Log.info("Expect Input Amount in USD: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Review_Swap"})
    public void SWP_VAL_RS_002_srcMaxBalance(){
        expectValue = balancesAPI.getMaxBalanceOfUserAddress(srcToken);
        actualValue = SwapPage.getBalanceOfSourceInRS();
        Log.info("Actual Max amount of source token: "+actualValue);
        Log.info("Expect Max amount of source token: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
}

    @Test(groups = {"Validate_Review_Swap"})
    public void SWP_VAL_RS_003_destMaxBalance(){
        expectValue = balancesAPI.getMaxBalanceOfUserAddress(destToken);
        actualValue = SwapPage.getBalanceOfDestInRS();
        Log.info("Actual Max amount of dest token: "+actualValue);
        Log.info("Expect Max amount of dest token: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Review_Swap"})
    public void SWP_VAL_RS_004_destIncomeInPlatform(){
        expectValue = destIncome;
        actualValue = SwapPage.getDestIncome();
        Log.info("Actual dest income: "+actualValue);
        Log.info("Expect dest income: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Review_Swap"})
    public void SWP_VAL_RS_006_destIncomeUSDValInPlatform(){
        expectValue = destIncome * tokenDetailsAPI.get_usd_rate(destToken);
        actualValue = SwapPage.getDestIncomeUSD();
        Log.info("Actual dest income in USD: "+actualValue);
        Log.info("Expect dest income in USD: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Review_Swap"})
    public void SWP_VAL_RS_007_rate(){
        expectValue = rate;
        actualValue = SwapPage.getActualRate();
        Log.info("Actual Rate: "+actualValue);
        Log.info("Expect Rate: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Review_Swap"})
    public void SWP_VAL_RS_008_minReceived(){
        expectValue = minReceive;
        actualValue = SwapPage.getMinReceive();
        Log.info("Actual Min Receive: "+actualValue);
        Log.info("Expect Min Receive: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Review_Swap"})
    public void SWP_VAL_RS_009_networkFeeEst(){
        expectValue = network_fee;
        actualValue = SwapPage.getNetworFeeEst();
        Log.info("Actual Network Fee Est In Review Swap: "+actualValue);
        Log.info("Expect Network Fee Est In Review Swap: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Review_Swap"})
    public void SWP_VAL_RS_010_maxNetworkFee(){
        expectValue = max_network_fee;
        actualValue = SwapPage.getMaxNetworkFee();
        Log.info("Actual Max Network Fee In Review Swap: "+actualValue);
        Log.info("Expect Max Network Fee In Review Swap: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Review_Swap"})
    public void SWP_VAL_RS_011_priceImpact(){
        expectValue = String.valueOf(priceImpact);
        actualValue = SwapPage.getPriceImpact();
        Log.info("Actual Price impact: "+actualValue);
        Log.info("Expect Price impact: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    //  -----------------------------------------SLIPPAGE TOLERANCE-------------------------------------


    //  -----------------------------------------PLATFORM-----------------------------------------------

    //  -----------------------------------------SWAP CONFIRMATION--------------------------------------



    @BeforeGroups(groups = "Validate_Summary_Swap", alwaysRun = true)
    public void preDataForSummarySwap(){
        srcToken = SwapPage.getSourceTokenForAPITest();
        destToken = SwapPage.getDestTokenForAPITest();
        amount = SwapPage.getAmountForAPITest();

        slipPage = Double.valueOf("0.5");
        //      Reload Swap page
        SwapPage.getSwapPageByURL();
        //      Switch chain
        ComPage.switchChain();
        Log.info("[SWAP] srcToken: "+ srcToken);
        Log.info("[SWAP] destToken: "+ destToken);
        SwapPage.setupSwap(srcToken, destToken, amount, true);
        //  -----------------------------------------PREPARE DATA TO VALIDATE SWAP-------------------------------------
        //  -----------------------------------------COMMON DATA FROM ALLRATES-----------------------------------------
        //      Get Information from all Rates API
        Map<String, String> dict = allRatesAPI.getAllRatesInfo(srcToken, destToken, amount);
        priceImpact = Double.valueOf(dict.get("priceImpact"))/100;
        Log.info("Price Impact From API: "+ priceImpact);
        Double rate_ = Double.valueOf(dict.get("rate"));
        gasPrice = gasPriceAPI.getGasPriceStandard();
        //  -----------------------------------------SPECIFIC DATA FROM API--------------------------------------------
        usd_rate = tokenDetailsAPI.get_usd_value_of_native_token();
        //  -----------------------------------------SPECIFIC DATA FROM BUILDTX----------------------------------------
//        estGasConsumed = ;
//        gasPrice = ;
//        gasLimit = ;
        //  -----------------------------------------COMMON VALUE FOR ALL SCREENS--------------------------------------
        //      Get Rate
        rate = Util.roundAvoidD(rate_, 18, 6);
        //      Dest Income = rate * amount
        destIncome = rate *Double.valueOf(amount);

        //      Minimum Receive = DesAmount - DesAmount * Slippage
        minReceive = Util.roundPlace(destIncome - (destIncome * slipPage/100),4);
        //      Common calculate
        common_calculate();
    }
    
    @Test(groups = {"Validate_Summary_Swap"})
    public void SWP_VAL_CS_001_inputAmount(){
        BasePage.click(SwapScreen.review_swap_btn);
        expectValue = amount;
        actualValue = SwapPage.getSrcValueAtConfirm();
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = {"Validate_Summary_Swap"})
    public void SWP_VAL_CS_002_inputAmountUSDValue(){
        expectValue = Double.parseDouble(amount) * tokenDetailsAPI.get_usd_rate(srcToken);
        actualValue = SwapPage.getSrcValueUSD();
        Log.info("Actual Source Value USD In Summary Swap: "+actualValue);
        Log.info("Expect Source Value USD In Summary Swap: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Summary_Swap"})
    public void SWP_VAL_CS_003_srcToken(){
        expectValue = srcToken;
        actualValue = SwapPage.getSrcTokenAtConfirm();
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = {"Validate_Summary_Swap"})
    public void SWP_VAL_CS_004_destIncome(){
        expectValue = Util.roundPlace(destIncome,4);
        actualValue = SwapPage.getDestValueAtConfirm();
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Summary_Swap"})
    public void SWP_VAL_CS_005_destIncomeUSDValue(){
        expectValue = destIncome * tokenDetailsAPI.get_usd_rate(destToken);
        actualValue = SwapPage.getDestUSDValueAtConfirm();
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Summary_Swap"})
    public void SWP_VAL_CS_006_destToken(){
        expectValue = destToken;
        actualValue = SwapPage.getDestTokenAtConfirm();
        Assert.assertEquals(actualValue, expectValue);
    }

    @Test(groups = {"Validate_Summary_Swap"})
    public void SWP_VAL_CS_007_rate(){
        expectValue = rate;
        actualValue = SwapPage.getActualRate();
        Log.info("Actual Rate in Summary Swap: "+actualValue);
        Log.info("Expect Rate in Summary Swap: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Summary_Swap"})
    public void SWP_VAL_CS_008_minReceived(){
        expectValue = minReceive;
        actualValue = SwapPage.getMinReceive();
        Log.info("Actual Min Receive in Summary Swap: "+actualValue);
        Log.info("Expect Min Receive in Summary Swap: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Summary_Swap"})
    public void SWP_VAL_CS_009_networkFeeEst(){
        expectValue = network_fee;
        actualValue = SwapPage.getNetworFeeEst();
        Log.info("Actual Network Fee Est in Summary Swap: "+actualValue);
        Log.info("Expect Network Fee Est in Summary Swap: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Summary_Swap"})
    public void SWP_VAL_CS_010_maxNetworkFee(){
        expectValue = max_network_fee;
        actualValue = SwapPage.getMaxNetworkFee();
        Log.info("Actual Max Network Fee in Summary Swap: "+actualValue);
        Log.info("Expect Max Network Fee in Summary Swap: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    @Test(groups = {"Validate_Summary_Swap"})
    public void SWP_VAL_CS_011_priceImpact(){
        expectValue = String.valueOf(priceImpact);
        actualValue = SwapPage.getPriceImpact();
        Log.info("Actual Price Impact in Summary Swap: "+actualValue);
        Log.info("Expect Price Impact in Summary Swap: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(actualValue), String.valueOf(expectValue));
    }

    //  -----------------------------------------COMMON CALCULATE-------------------------------------
    private void common_calculate(){
        //      network_fee
        if (Environment.test_chain.equals("Ethereum") || Environment.test_chain.equals("Polygon") || Environment.test_chain.equals("Avalanche")) {
            Double baseFee = gasPriceAPI.getBaseFee();
            Double priorityFee = gasPriceAPI.getPriorityFee();
            //       # Network fee est	= Est.Gas consumed * (base_fee + priority_fee)
            network_fee =  estGasConsumed * (baseFee + priorityFee);
        }else {
            //       # network_fee = Est.Gas consumed * selected gas price
            network_fee = estGasConsumed * gasPrice;
        }
        network_fee = Util.roundAvoidD(network_fee * usd_rate, 9, 4);

        //      # max_network_fee = Gas limit * selected gas price
        max_network_fee = gasLimit *  gasPrice;
        max_network_fee = Util.roundAvoidD(max_network_fee* usd_rate, 9, 4);
    }

    //  -----------------------------------------TRANSACTION SETTING-------------------------------------

    //  -----------------------------------------TRANSACTION SETTING-------------------------------------


    //  -----------------------------------------BUSINESS-----------------------------------------------


    /**
     * Author: HuongTT
     * Updated: 2022-03-08
     * @implSpec Swap from Native token to support token Fake KNC
     */
//    @Test(groups = {"Validation", "Ethereum"})
    public void swapNativeToSupOnETHER(){
        SwapPage.swapFromNativeToken("USDC");
    }

    /**
     * Author: HuongTT
     * Updated: 2022-03-09
     */
//    @Test(groups = {"Business"})
    public void SWP_BIZ_001_swapNative() {
//        1. Perform swap from native token
        SwapPage.swapFromNativeToken("FSW");
//        2. After swap done, confirm message display
        AssertPage.assertString(ComPage.confirm_msg, Util.getMessage("CONFIRM_001"));
//        3. Close board casted popup
        ComPage.closeConfirmDialog();
    }


    /**
     * Author: HuongTT
     * Updated: 2022-03-31
     */
//    @Test(groups = {"Business"})
    public void SWP_BIZ_002_swapSupToNative(){
//        1. Select support token at source
        SwapPage.swapSupToNative();
    }
    /**
     * Author: HuongTT
     * Updated: 2022-03-31
     */
//    @Test(groups = {"Business"})
    public void SWP_BIZ_003_swapSupToSup(){
//        1. Perform swap from Support token to support token
        SwapPage.swapSup2Sup();
    }



    //  -----------------------------------------BUSINESS-----------------------------------------------
    @BeforeMethod(onlyForGroups = {"Business_Swap"}, alwaysRun = true)
    void switchChain(ITestResult result){
        Log.info("Start test on "+ result.getMethod().getMethodName());
//        Reload Swap page
        SwapPage.getSwapPageByURL();
//        Switch chain
        ComPage.switchChain();
    }
    //  -----------------------------------------Execute test based on data driven-----------------------------------------------
    /**
     * Author: HuongTT
     * Updated: 2022-03-31
     * @implSpec get data for swap
     */
    @DataProvider(name = "swapBaseDataFile")
    public Object[][] getData() {
        //feel free to replace this with the logic that reads up a csv file (using CSVReader)
        // and then translates it to a 2D array.
        String key = Util.getPropertyValue("SUITE_SWAP");
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
    /**
     * Author: HuongTT
     * Updated: 2022-03-31
     * @implSpec execute swap based on data from swap.csv
     */
//    @Test(dataProvider = "swapBaseDataFile", groups = {"Business_Swap"}, description = "Execute swap based on data from Swap.csv")
    public void SWP_BIZ_005_swapBaseDataFile(String tcID, String srcToken, String desToken, String amount, String IsApproval){
//        1. Perform swap based on data from CSV file
       SwapPage.swapToken(srcToken, desToken, amount, IsApproval);
//        2. After swap done, confirm message display
        AssertPage.assertString(ComPage.confirm_msg, Util.getMessage("CONFIRM_001"));
//        3. Close board casted popup
        ComPage.closeConfirmDialog();
    }


//    @Test(groups = {"Swap_Abnormal"}, description = "Swap when input amount larger than max amount.")
    public void SWP_BIZ_006_swapOutOfBalance(){
        SwapPage.swapOutOfBalance();
        AssertPage.assertString(SwapScreen.err_msg, Util.getMessage("ERR_002"));
    }
//----------------------------------------------------------------------
    /**
     * Author: HuongTT
     * Updated: 2022-09-27
     * @implSpec Execute swap Native token to Support token
     */
    @Test(groups = {"Business_Swap"}, description = "Execute swap Native token to Support token")
    public void SWP_001_SwapNativeToSupportToken(){
        pages.ComPage.captureImage("StartTestSWP001");
        performSwapToken("SWP_001");
    }

    /**
     * Author: HuongTT
     * Updated: 2022-09-27
     * @implSpec Execute swap Support token to native token
     */
//    @Test(groups = {"Business_Swap"})
    public void SWP_002_SwapSupportToNativeToken(){
        performSwapToken("SWP_002");
    }

    /**
     * Author: HuongTT
     * Updated: 2022-09-27
     * @implSpec Execute swap Support token to Support token
     */
//    @Test(groups = {"Business_Swap"})
    public void SWP_003_SwapSupportToSupportToken(){
        performSwapToken("SWP_003");
    }

    /**
     * Author: HuongTT
     * Updated: 2022-09-27
     * @implSpec Execute Swap with Unapprove token
     */
//    @Test(groups = {"Business_Swap"})
    public void SWP_004_SwapUnapproveToken(){
        performSwapToken("SWP_004");
    }

    private void performSwapToken(String tcID){
        List<Map<String, String>> listOfMaps = Util.getListTestDataBaseOnTCID(Sfile, tcID);
        SwapPage.swapToken(listOfMaps.get(0).get("TokenSource"), listOfMaps.get(0).get("TokenDes"), listOfMaps.get(0).get("Amount"), listOfMaps.get(0).get("IsApproval"));
//        2. After swap done, confirm message display
        AssertPage.assertString(ComPage.confirm_msg, Util.getMessage("CONFIRM_001"));
//        3. Close board casted popup
        ComPage.closeConfirmDialog();
    }
}