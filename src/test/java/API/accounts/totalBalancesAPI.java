package API.accounts;

import API.common.Environment;
import API.common.RestUtils;
import com.jayway.jsonpath.DocumentContext;
import utils.logs.Log;

import java.util.List;

public class totalBalancesAPI {
    static String balances_path = "all/v1/account/totalBalances?address=";

    private static String setTotalBalancesURL(String address){
        String URL = new String();
        URL = Environment.uri + balances_path +address;
        Log.info("totalBalances API: "+ URL);
        return URL;
    }

    private static DocumentContext getDocOfAPI(String address){
        String API = setTotalBalancesURL(address);
        DocumentContext ctx = RestUtils.getContext(API);
        return ctx;
    }

    public static double getTotalusdValue(String tokenAddress){
        DocumentContext ctx = getDocOfAPI(tokenAddress);
        Double total = new Double(0);
        List<String> usdValues = ctx.read("$.data.balances[?(@.address == '"+tokenAddress+"')].usdValue");
        List<String> chains = ctx.read("$.data.balances[?(@.address == '"+tokenAddress+"')].chain");
        for (int i=0; i<usdValues.size();i++){
            Object out = usdValues.get(i);
            System.out.println(chains.get(i)+": "+out);
            total = total + (double) out;
        }
        return total;
    }

    public static double getBalanceOfChainBasedUSD(String chain, String tokenAddress){
        DocumentContext ctx = getDocOfAPI(tokenAddress);
        List<String> usdValues = ctx.read("$.data.balances[?(@.chain == '"+chain.toLowerCase()+"')].usdValue");
        Object usdValues_ = usdValues.get(0);
        return (double) usdValues_;
    }

    public static double getTotalBalances(String tokenAddress){
        DocumentContext ctx = getDocOfAPI(tokenAddress);
        Object totalUSDvalues = ctx.read("$.data.summary.usdValue");
        return (double) totalUSDvalues;
    }
}
