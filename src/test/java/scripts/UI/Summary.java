package scripts.UI;

import API.accounts.totalBalancesAPI;
import API.common.Environment;
import org.testng.ITestResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import scripts.BaseTest;
import utils.logs.Log;

public class Summary extends BaseTest {
    Object expectValue, actualValue;

    @BeforeClass(alwaysRun = true)
    public void log(){
        Log.info("Start test for Portfolio Page.");
//        2. Access to Summary Page
        SumPage.goToSumScreen();
    }
    /**
     * Author: HuongTT
     * Updated: 2022-04-12
     */
    @BeforeMethod(alwaysRun = true)
    public void TearUp(ITestResult result){
        Log.info("Start test on "+ result.getMethod().getMethodName());
        SumPage.handleSubscribe();
////        1. Switch to Ropsten chain
//        ComPage.switchChain();
////        2. Access to Summary Page
//        SumPage.goToSumScreen();
    }
    //  -----------------------------------------ACCESS------------------------------------------------
    //  -----------------------------------------VALIDATION---------------------------------------------

  @Test(groups = "Validate_Summary")
  public void SUM_ValidateTotalBalance(){
//        String user = Environment.user_address;
//        Log.info("USER_ADDRESS "+ user);
        expectValue = totalBalancesAPI.getTotalBalances(Environment.user_address);
        actualValue = SumPage.getTotalNetWorth();
        Log.info("Expect Total Net Worth: "+expectValue);
        Log.info("Actual Total Net Worth: "+actualValue);
        AssertPage.assertAlmostEqual(String.valueOf(expectValue), String.valueOf(actualValue));
  }

    /**
     * Author: HuongTT
     * Updated: 2022-03-31
     * @implSpec get data for swap
     */
    @DataProvider(name = "ListOfChain")
    public Object[][] getData() {
        return new Object[][] {{"Ethereum"}, {"Avalanche"}, {"Polygon"}, {"BSC"}, {"Arbitrum"}, {"Aurora"}, {"Fantom"}, {"Cronos"}, {"Klaytn"}};
    }

//    @Test(dataProvider = "ListOfChain", groups = "Validate_Summary")
    public void SUM_ValidateTotalNetWorth(String chain){
        expectValue = totalBalancesAPI.getBalanceOfChainBasedUSD(chain, Environment.user_address);
        actualValue = SumPage.getBalanceOfChain(chain);
        Log.info("Expect balance of "+chain+": "+expectValue);
        Log.info("Actual balance of "+chain+": "+actualValue);
        AssertPage.assertAlmostEqual(String.valueOf(expectValue), String.valueOf(actualValue));
    }

//    @Test(dataProvider = "ListOfChain", groups = "Validate_Summary")
    public void SUM_ValidateTotalBalancesOfChain(String chain){
//        ComPage.switchChain(chain);
        expectValue = totalBalancesAPI.getBalanceOfChainBasedUSD(chain, Environment.user_address);
        actualValue = SumPage.getTotalAssets(chain) + SumPage.getTotalSupply() + SumPage.getTotalLiquid();
        Log.info("Expect total balance of "+chain+": "+expectValue);
        Log.info("Actual total balance of "+chain+": "+actualValue);
        AssertPage.assertAlmostEqual(String.valueOf(expectValue), String.valueOf(actualValue));
    }

    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalNetWorth_Avalanche(){
        SumPage.ValidateTotalNetWorth("Avalanche");
    }
    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalNetWorth_Ethereum(){
        ComPage.captureImage("StartTestForEhereum");
        SumPage.ValidateTotalNetWorth("Ethereum");
    }
    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalNetWorth_Polygon(){
        SumPage.ValidateTotalNetWorth("Polygon");
    }
    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalNetWorth_BSC(){
        SumPage.ValidateTotalNetWorth("BSC");
    }
    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalNetWorth_Arbitrum(){
        SumPage.ValidateTotalNetWorth("Arbitrum");
    }
    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalNetWorth_Aurora(){
        SumPage.ValidateTotalNetWorth("Aurora");
    }
    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalNetWorth_Cronos(){
        SumPage.ValidateTotalNetWorth("Cronos");
    }
    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalNetWorth_Fantom(){
        SumPage.ValidateTotalNetWorth("Fantom");
    }
    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalNetWorth_Klaytn(){
        SumPage.ValidateTotalNetWorth("Klaytn");
    }

    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalBalancesOfChain_Ethereum(){
        SumPage.ValidateTotalBalancesOfChain("Ethereum");
    }

    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalBalancesOfChain_BSC(){
        SumPage.ValidateTotalBalancesOfChain("BSC");
    }

    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalBalancesOfChain_Polygon(){
        SumPage.ValidateTotalBalancesOfChain("Polygon");
    }

    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalBalancesOfChain_Arbitrum(){
        SumPage.ValidateTotalBalancesOfChain("Arbitrum");
    }

    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalBalancesOfChain_Aurora(){
        SumPage.ValidateTotalBalancesOfChain("Aurora");
    }

    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalBalancesOfChain_Avalanche(){
        SumPage.ValidateTotalBalancesOfChain("Avalanche");
    }

    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalBalancesOfChain_Cronos(){
        SumPage.ValidateTotalBalancesOfChain("Cronos");
    }

    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalBalancesOfChain_Fantom(){
        SumPage.ValidateTotalBalancesOfChain("Fantom");
    }

    @Test(groups = "Validate_Summary")
    public void SUM_ValidateTotalBalancesOfChain_Klaytn(){
        SumPage.ValidateTotalBalancesOfChain("Klaytn");
    }

    //  -----------------------------------------BUSINESS-----------------------------------------------
    @Test(groups = {"Business"}, description = "Execute withDraw token from Summary screen")
    public void withDrawToken(){
        String val_before = SumPage.getCurrentSupply("ETH");
        System.out.println("Value of Supply before withdraw: "+ val_before);
        SumPage.withdrawTokenByName("ETH", "0.01");
        String val_after = SumPage.getCurrentSupply("ETH");
        Log.info("Value of Supply before withdraw: "+ val_before);
        Log.info("Value of Supply after withdraw: "+ val_after);
    }
}
