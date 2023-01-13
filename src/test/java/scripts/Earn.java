package scripts;

import org.testng.ITestResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Util;
import utils.logs.Log;

import java.lang.reflect.Method;

public class Earn extends BaseTest {

    @BeforeClass
    public void log(){
        Log.info("Start test for Earn function.");
    }
    //  -----------------------------------------ACCESS------------------------------------------------
    //  -----------------------------------------VALIDATION---------------------------------------------
    //  -----------------------------------------BUSINESS-----------------------------------------------
    /**
     * Author: HuongTT
     * Updated: 2022-03-24
     */
    @BeforeMethod()
    public void TearUp(ITestResult result){
        Log.info("Start test on "+ result.getMethod().getMethodName());
//        1. Switch to Ropsten chain
        ComPage.switchChain();
 //        2. Access to Earn Page
        EarnPage.getEarnPageByURL();
        EarnPage.selectTokenByNameFromEarn("ETH");
    }

    /**
     * Author: HuongTT
     * Updated: 2022-03-24
     */
    @Test()
    public void EarnETH(Method method){
//        objEarnScr.selectTokenByNameFromEarn("ETH");
//        1. Perform Earn ETH
        EarnPage.performEarnToken("ETH");
//        2. Assert Earn Success
        AssertPage.assertString(ComPage.confirm_msg, Util.getMessage("CONFIRM_003"));
//        3. Close boardcasted popup
        ComPage.closeConfirmDialog();
    }

    /**
     * Author: HuongTT
     * Updated: 2022-04-19
     */
//    @Test()
    public void EarnBySwapNow(Method method){
//        objEarnScr.selectTokenByNameFromEarn("ETH");
//        1. Perform swap now ETH from Earn
        EarnPage.performSwapNow("BAT_COMP");
//        2. Assert Earn Success
        AssertPage.assertString(pages.ComPage.confirm_msg, Util.getMessage("CONFIRM_003"));
//        3. Close boardcasted popup
        ComPage.closeConfirmDialog();
    }


}
