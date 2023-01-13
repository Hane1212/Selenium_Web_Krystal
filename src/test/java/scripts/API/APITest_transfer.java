package scripts.API;

import API.common.Environment;
import API.swap.gasPriceAPI;
import API.transferAPI;
import com.aventstack.extentreports.Status;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.BasePage;
import pages.ComAPI;
import scripts.BaseTest;
import utils.Util;
import utils.extentreports.ExtentTestManager;
import utils.logs.Log;

public class APITest_transfer extends BaseTest {
    String amount;
    double maxGasFee;

    //    Swap screen
    Object expectValue, actualValue;
    //  -----------------------------------------CheckGasLimit-----------------------------------------------
    @DataProvider(name = "List token for Ethereum")
    public Object[][] getData_001() {
        return  ComAPI.getDataForTransfer("ethereum");
    }

    @Test(dataProvider = "List token for Ethereum" , groups = {"API_TRANS_Test"}, description = "Get Gas Limit During Perform Transfer Ethereum")
    public void API_TRF_GL_001(String token){
        executeTestAPIForTransfer("Ethereum", token);
    }

    @DataProvider(name = "List token for Cronos")
    public Object[][] getData_002() {
        return  ComAPI.getDataForTransfer("cronos");
    }

    @Test(dataProvider = "List token for Cronos" , groups = {"API_TRANS_Test"}, description = "Get Gas Limit During Perform Transfer Cronos")
    public void API_TRF_GL_002(String token){
        executeTestAPIForTransfer("Cronos", token);
    }

    @DataProvider(name = "List token for BSC")
    public Object[][] getData_003() {
        return  ComAPI.getDataForTransfer("bsc");
    }

    @Test(dataProvider = "List token for BSC" , groups = {"API_TRANS_Test"}, description = "Get Gas Limit During Perform Transfer BSC")
    public void API_TRF_GL_003(String token){
        executeTestAPIForTransfer("BSC", token);
    }

    @DataProvider(name = "List token for Polygon")
    public Object[][] getData_004() {
        return  ComAPI.getDataForTransfer("polygon");
    }

    @Test(dataProvider = "List token for Polygon" , groups = {"API_TRANS_Test"}, description = "Get Gas Limit During Perform Transfer Polygon")
    public void API_TRF_GL_004(String token){
        executeTestAPIForTransfer("Polygon", token);
    }

    @DataProvider(name = "List token for Fantom")
    public Object[][] getData_005() {
        return  ComAPI.getDataForTransfer("fantom");
    }

    @Test(dataProvider = "List token for Fantom" , groups = {"API_TRANS_Test"}, description = "Get Gas Limit During Perform Transfer Fantom")
    public void API_TRF_GL_005(String token){
        executeTestAPIForTransfer("Fantom", token);
    }

    @DataProvider(name = "List token for Arbitrum")
    public Object[][] getData_006() {
        return  ComAPI.getDataForTransfer("arbitrum");
    }

    @Test(dataProvider = "List token for Arbitrum" , groups = {"API_TRANS_Test"}, description = "Get Gas Limit During Perform Transfer Arbitrum")
    public void API_TRF_GL_006(String token){
        executeTestAPIForTransfer("Arbitrum", token);
    }

    @DataProvider(name = "List token for Avalanche")
    public Object[][] getData_007() {
        return  ComAPI.getDataForTransfer("avalanche");
    }

    @Test(dataProvider = "List token for Avalanche" , groups = {"API_TRANS_Test"}, description = "Get Gas Limit During Perform Transfer Avalanche")
    public void API_TRF_GL_007(String token){
        executeTestAPIForTransfer("Avalanche", token);
    }

    @DataProvider(name = "List token for Aurora")
    public Object[][] getData_008() {
        return  ComAPI.getDataForTransfer("aurora");
    }

    @Test(dataProvider = "List token for Aurora" , groups = {"API_TRANS_Test"}, description = "Get Gas Limit During Perform Transfer Aurora")
    public void API_TRF_GL_008(String token){
        executeTestAPIForTransfer("Aurora", token);
    }

    @DataProvider(name = "List token for Klaytn")
    public Object[][] getData_009() {
        return  ComAPI.getDataForTransfer("klaytn");
    }

    @Test(dataProvider = "List token for Klaytn" , groups = {"API_TRANS_Test"}, description = "Get Gas Limit During Perform Transfer Klaytn")
    public void API_TRF_GL_009(String token){
        executeTestAPIForTransfer("Klaytn", token);
    }

    @DataProvider(name = "List token for Optimism")
    public Object[][] getData_010() {
        return  ComAPI.getDataForTransfer("optimism");
    }

    @Test(dataProvider = "List token for Optimism" , groups = {"API_TRANS_Test"}, description = "Get Gas Limit During Perform Transfer Optimism")
    public void API_TRF_GL_010(String token){
        executeTestAPIForTransfer("Optimism", token);
    }


    private void executeTestAPIForTransfer(String chain, String srcToken){
        double amount_ = ComAPI.getBalanceBaseTokenUnit(chain, srcToken);
        amount = String.format("%.9f",amount_/2);
        prepareUIToTransfer(chain, srcToken, amount);
        maxGasFee = ComAPI.calculateMaxGasFee(srcToken, amount);
        expectValue = maxGasFee;
        actualValue = TransPage.getMaxGasFee();
        Log.info("Actual Max Gas Fee: "+actualValue);
        Log.info("Expect Max Gas Fee: "+expectValue);
        AssertPage.assertAlmostEqual(String.valueOf(expectValue), String.valueOf(actualValue));
    }

    private void prepareUIToTransfer(String chain, String srcAddress, String amount){
        TransPage.getTransferPageByURL();
        ComPage.switchChain(chain);
        TransPage.setupTransferByAddress(chain, srcAddress, amount, Environment.recipientAddress);
        ExtentTestManager.logMessage(Status.INFO, srcAddress);
        ExtentTestManager.logMessage(Status.INFO, amount);
    }

}
