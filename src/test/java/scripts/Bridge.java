package scripts;

import API.crosschain.serverInfoAPI;
import org.testng.annotations.BeforeGroups;

import java.util.Map;

public class Bridge extends BaseTest{

    Object maximumSwap, minimumSwap, swapFeeRatePerMillion, maximumSwapFee, minimumSwapFee;

    //  -----------------------------------------TOTAL NET WORTH--------------------------------------
    @BeforeGroups(groups = "Validate_TotalNetWorth")
    public void preDataForTotalNetWorth(){
        String token = ComPage.getCurrentAddress();
        String srcChain = "";
        String destChain = "";
        Map<String, String> dict = serverInfoAPI.getServerInfo(srcChain, token, destChain);
        maximumSwap = dict.get("maximumSwap");
        minimumSwap = dict.get("minimumSwap");
        swapFeeRatePerMillion = dict.get("swapFeeRatePerMillion");
        maximumSwapFee = dict.get("maximumSwapFee");
        minimumSwapFee = dict.get("minimumSwapFee");
    }
}
