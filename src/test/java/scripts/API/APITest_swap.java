package scripts.API;

import API.common.Environment;

import API.swap.allRatesAPI;
import RFT.Swap;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ComAPI;
import utils.Assertion;
import utils.extentreports.ExtentTestManager;
import utils.logs.Log;

import java.util.List;

public class APITest_swap {
    @BeforeClass
    public void log(){
        Log.info("API_TEST: Start test for Get GasLimit");
    }
    //------------------------------------TRANSFER---------------------------------------------------

    //------------------------------------SWAP---------------------------------------------------

    @DataProvider(name = "List SWAP token for Ethereum")
    public Object[][] getSwapData_001() {
        return  ComAPI.getListTokenToVerifySwap("ethereum");
    }

    @Test(dataProvider = "List SWAP token for Ethereum" , groups = {"API_SWAP_Test"}, description = "Get Gas Limit During Perform Swap on Ethereum")
    public void API_SWP_GL_001(String chain, String srcToken, String desToken, String amount){
        executeTestAPIForSwap(chain, srcToken, desToken, amount);
    }

    @DataProvider(name = "List SWAP token for Cronos")
    public Object[][] getSwapData_002() {
        return  ComAPI.getListTokenToVerifySwap("cronos");
    }

    @Test(dataProvider = "List SWAP token for Cronos" , groups = {"API_SWAP_Test"}, description = "Get Gas Limit During Perform Swap on Cronos")
    public void API_SWP_GL_002(String chain, String srcToken, String desToken, String amount){
        executeTestAPIForSwap(chain, srcToken, desToken, amount);
    }

    @DataProvider(name = "List SWAP token for BSC")
    public Object[][] getSwapData_003() {
        return  ComAPI.getListTokenToVerifySwap("bsc");
    }

    @Test(dataProvider = "List SWAP token for BSC" , groups = {"API_SWAP_Test"}, description = "Get Gas Limit During Perform Swap on BSC")
    public void API_SWP_GL_003(String chain, String srcToken, String desToken, String amount){
        executeTestAPIForSwap(chain, srcToken, desToken, amount);
    }

    @DataProvider(name = "List SWAP token for Polygon")
    public Object[][] getSwapData_004() {
        return  ComAPI.getListTokenToVerifySwap("polygon");
    }

    @Test(dataProvider = "List SWAP token for Polygon" , groups = {"API_SWAP_Test"}, description = "Get Gas Limit During Perform Swap on Polygon")
    public void API_SWP_GL_004(String chain, String srcToken, String desToken, String amount){
        executeTestAPIForSwap(chain, srcToken, desToken, amount);
    }

    @DataProvider(name = "List SWAP token for Fantom")
    public Object[][] getSwapData_005() {
        return  ComAPI.getListTokenToVerifySwap("fantom");
    }

    @Test(dataProvider = "List SWAP token for Fantom" , groups = {"API_SWAP_Test"}, description = "Get Gas Limit During Perform Swap on Fantom")
    public void API_SWP_GL_005(String chain, String srcToken, String desToken, String amount){
        executeTestAPIForSwap(chain, srcToken, desToken, amount);
    }

    @DataProvider(name = "List SWAP token for Arbitrum")
    public Object[][] getSwapData_006() {
        return  ComAPI.getListTokenToVerifySwap("arbitrum");
    }

    @Test(dataProvider = "List SWAP token for Arbitrum" , groups = {"API_SWAP_Test"}, description = "Get Gas Limit During Perform Swap on Arbitrum")
    public void API_SWP_GL_006(String chain, String srcToken, String desToken, String amount){
        executeTestAPIForSwap(chain, srcToken, desToken, amount);
    }

    @DataProvider(name = "List SWAP token for Avalanche")
    public Object[][] getSwapData_007() {
        return  ComAPI.getListTokenToVerifySwap("avalanche");
    }

    @Test(dataProvider = "List SWAP token for Avalanche" , groups = {"API_SWAP_Test"}, description = "Get Gas Limit During Perform Swap on Avalanche")
    public void API_SWP_GL_007(String chain, String srcToken, String desToken, String amount){
        executeTestAPIForSwap(chain, srcToken, desToken, amount);
    }

    @DataProvider(name = "List SWAP token for Aurora")
    public Object[][] getSwapData_008() {
        return  ComAPI.getListTokenToVerifySwap("Aurora");
    }

    @Test(dataProvider = "List SWAP token for Aurora" , groups = {"API_SWAP_Test"}, description = "Get Gas Limit During Perform Swap on Aurora")
    public void API_SWP_GL_008(String chain, String srcToken, String desToken, String amount){
        executeTestAPIForSwap(chain, srcToken, desToken, amount);
    }

    @DataProvider(name = "List SWAP token for Klaytn")
    public Object[][] getSwapData_009() {
        return  ComAPI.getListTokenToVerifySwap("Klaytn");
    }

    @Test(dataProvider = "List SWAP token for Klaytn" , groups = {"API_SWAP_Test"}, description = "Get Gas Limit During Perform Swap on Klaytn")
    public void API_SWP_GL_009(String chain, String srcToken, String desToken, String amount){
        executeTestAPIForSwap(chain, srcToken, desToken, amount);
    }

    @DataProvider(name = "List SWAP token for Optimism")
    public Object[][] getSwapData_010() {
        return  ComAPI.getListTokenToVerifySwap("Optimism");
    }

    @Test(dataProvider = "List SWAP token for Optimism" , groups = {"API_SWAP_Test"}, description = "Get Gas Limit During Perform Swap on Optimism")
    public void API_SWP_GL_010(String chain, String srcToken, String desToken, String amount){
        executeTestAPIForSwap(chain, srcToken, desToken, amount);
    }

    private void executeTestAPIForSwap(String chain, String srcToken, String desToken, String amount){
        Swap swap = new Swap(chain, srcToken, desToken, amount);
        String msg = "Check Gas Limit of "+srcToken + " and "+ desToken;
        ExtentTestManager.logMessage(Status.INFO, msg);
        List<String> platform_NG = swap.getGasLimit().platform_NG;
        List<String> platform_OK = swap.getGasLimit().platform_OK;

        if (platform_OK.size()>0){
            msg = "Get GasLimit success!";
            ExtentTestManager.logMessage(Status.INFO, msg);
            if (platform_NG.size()>0){
                if(platform_NG.get(0).equals("NO_RATE")){
                    msg = "Pair "+srcToken + " and "+ desToken +" does not have rate";
                    Assertion.assertWarn(msg);
                }else {
                    for (int i=0; i<platform_NG.size();i++){
                        msg = "Platform does not have rate: "+ platform_NG.get(i);
                        Assertion.assertWarn(msg);
                    }
                }
            }
            Assert.assertTrue(true, msg);
        }else {
            msg = "Can not get Gas Limit of this pair!";
            Assertion.assertFail(msg);
        }
    }

//    private void executeTestAPIForSwap(String chain, String srcToken, String desToken, String amount){
//        Swap swap = new Swap(chain, srcToken, desToken, amount);
//        String msg = "Check Gas Limit of "+srcToken + " and "+ desToken;
//        ExtentTestManager.logMessage(Status.INFO, msg);
//        List<String> listFail = allRatesAPI.getGasLimit500000(swap.chain.chainName, swap.srcToken, swap.destToken, swap.amount);
//        if (listFail.size()>0){
//            if(listFail.get(0).equals("NO_RATE")){
//                msg = "Pair "+srcToken + " and "+ desToken +" does not have rate";
//                Assertion.assertWarn(msg);
//            }else {
//                msg = "Can not get Gas Limit of this pair!";
//                Assertion.assertFail(msg);
////                ComAPI.checkAllowance(chain, swap.chain.swapSpender, srcToken, amount);
//            }
////        }else if(listFail.size()>1){
////            for (int i=0;i<listFail.size();i++){
////                ExtentTestManager.logMessage(Status.INFO, "Platform: "+listFail.get(i));
////                int allow = ComAPI.checkAllowance(chain, swap.chain.swapSpender, srcToken, amount);
////                if(allow ==0) break;
////            }
//        }else {
//            msg = "Get GasLimit success!";
//            Assert.assertTrue(true, msg);
//        }
//    }
}
