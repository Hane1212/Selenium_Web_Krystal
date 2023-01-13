package scripts.API;

import API.common.Environment;
import API.earning.buildStakeTxAPI;
import API.earning.optionDetail;
import RFT.Earn;
import com.aventstack.extentreports.Status;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ComAPI;
import utils.extentreports.ExtentTestManager;
import utils.logs.Log;

public class APITest_earn {
    @BeforeClass
    public void log(){
        Log.info("API_TEST: Start test for Get GasLimit in Earn feature");
    }

    //------------------------------------EARN---------------------------------------------------
    @DataProvider(name = "List EARN token for Ethereum")
    public Object[][] getEarnData_001() {
        return  ComAPI.getListTokenToVerifyEarn("ethereum");
    }

    @Test(dataProvider = "List EARN token for Ethereum" , groups = {"API_EARN_Test"}, description = "Get Gas Limit During Perform Earn on Ethereum")
    public void API_ERN_GL_001(String chain, String platform, String type,
                               String tokenAddress, String amount){
        executeTestAPIForEarn(chain, platform, type, tokenAddress, amount);
    }

    @DataProvider(name = "List EARN token for Cronos")
    public Object[][] getEarnData_002() {
        return  ComAPI.getListTokenToVerifyEarn("cronos");
    }

    @Test(dataProvider = "List EARN token for Cronos" , groups = {"API_EARN_Test"}, description = "Get Gas Limit During Perform Earn on Cronos")
    public void API_ERN_GL_002(String chain, String platform, String type,
                               String tokenAddress, String amount){
        executeTestAPIForEarn(chain, platform, type, tokenAddress, amount);
    }

    @DataProvider(name = "List EARN token for BSC")
    public Object[][] getEarnData_003() {
        return  ComAPI.getListTokenToVerifyEarn("bsc");
    }

    @Test(dataProvider = "List EARN token for BSC" , groups = {"API_EARN_Test"}, description = "Get Gas Limit During Perform Earn on BSC")
    public void API_ERN_GL_003(String chain, String platform, String type,
                               String tokenAddress, String amount){
        executeTestAPIForEarn(chain, platform, type, tokenAddress, amount);
    }

    @DataProvider(name = "List EARN token for Polygon")
    public Object[][] getEarnData_004() {
        return  ComAPI.getListTokenToVerifyEarn("polygon");
    }

    @Test(dataProvider = "List EARN token for Polygon" , groups = {"API_EARN_Test"}, description = "Get Gas Limit During Perform Earn on Polygon")
    public void API_ERN_GL_004(String chain, String platform, String type,
                               String tokenAddress, String amount){
        executeTestAPIForEarn(chain, platform, type, tokenAddress, amount);
    }

    @DataProvider(name = "List EARN token for Fantom")
    public Object[][] getEarnData_005() {
        return  ComAPI.getListTokenToVerifyEarn("fantom");
    }

    @Test(dataProvider = "List EARN token for Fantom" , groups = {"API_EARN_Test"}, description = "Get Gas Limit During Perform Earn on Fantom")
    public void API_ERN_GL_005(String chain, String platform, String type,
                               String tokenAddress, String amount){
        executeTestAPIForEarn(chain, platform, type, tokenAddress, amount);
    }

    @DataProvider(name = "List EARN token for Arbitrum")
    public Object[][] getEarnData_006() {
        return  ComAPI.getListTokenToVerifyEarn("arbitrum");
    }

    @Test(dataProvider = "List EARN token for Arbitrum" , groups = {"API_EARN_Test"}, description = "Get Gas Limit During Perform Earn on Arbitrum")
    public void API_ERN_GL_006(String chain, String platform, String type,
                               String tokenAddress, String amount){
        executeTestAPIForEarn(chain, platform, type, tokenAddress, amount);
    }

    @DataProvider(name = "List EARN token for Avalanche")
    public Object[][] getEarnData_007() {
        return  ComAPI.getListTokenToVerifyEarn("avalanche");
    }

    @Test(dataProvider = "List EARN token for Avalanche" , groups = {"API_EARN_Test"}, description = "Get Gas Limit During Perform Earn on Avalanche")
    public void API_ERN_GL_007(String chain, String platform, String type,
                               String tokenAddress, String amount){
        executeTestAPIForEarn(chain, platform, type, tokenAddress, amount);
    }

    @DataProvider(name = "List EARN token for Aurora")
    public Object[][] getEarnData_008() {
        return  ComAPI.getListTokenToVerifyEarn("aurora");
    }

    @Test(dataProvider = "List EARN token for Aurora" , groups = {"API_EARN_Test"}, description = "Get Gas Limit During Perform Earn on Aurora")
    public void API_ERN_GL_008(String chain, String platform, String type,
                               String tokenAddress, String amount){
        executeTestAPIForEarn(chain, platform, type, tokenAddress, amount);
    }

    @DataProvider(name = "List EARN token for Klaytn")
    public Object[][] getEarnData_009() {
        return  ComAPI.getListTokenToVerifyEarn("klaytn");
    }

    @Test(dataProvider = "List EARN token for Klaytn" , groups = {"API_EARN_Test"}, description = "Get Gas Limit During Perform Earn on Klaytn")
    public void API_ERN_GL_009(String chain, String platform, String type,
                               String tokenAddress, String amount){
        executeTestAPIForEarn(chain, platform, type, tokenAddress, amount);
    }

    @DataProvider(name = "List EARN token for Optimism")
    public Object[][] getEarnData_010() {
        return  ComAPI.getListTokenToVerifyEarn("optimism");
    }

    @Test(dataProvider = "List EARN token for Optimism" , groups = {"API_EARN_Test"}, description = "Get Gas Limit During Perform Earn on Optimism")
    public void API_ERN_GL_010(String chain, String platform, String type,
                               String tokenAddress, String amount){
        executeTestAPIForEarn(chain, platform, type, tokenAddress, amount);
    }

    private void executeTestAPIForEarn(String chain, String platform, String type,
                                       String tokenAddress, String amount){
        Environment.Chain chain_ = Environment.getChainByName(chain);
        int gasLimit = buildStakeTxAPI.getGasLimit(chain_.chainId, platform, type, tokenAddress, amount);
        String msg = "Check Gas Limit of "+tokenAddress + " on "+ platform;
        ExtentTestManager.logMessage(Status.INFO, msg);
        msg = "GasLimit of token: "+ tokenAddress + " is "+ gasLimit;

        if (gasLimit == 0){
            String spender = optionDetail.getSpender(chain_.chainId, platform,type,tokenAddress);
            ComAPI.checkAllowance(chain, spender, tokenAddress, amount);
        }else {
            Log.info(msg);
        }
    }
}
