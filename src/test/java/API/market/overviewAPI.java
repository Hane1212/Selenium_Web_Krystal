package API.market;

import API.common.Environment;
import API.common.RestUtils;
import com.jayway.jsonpath.DocumentContext;
import utils.logs.Log;

import java.util.List;

public class overviewAPI {
    static String overview_path = "/v1/market/overview";
    static String token_overview_path = overview_path + "?tokenAddresses=";
    //   ----------------------/{chain}/v1/market/overview------------------------------------

    /**
     * @param tokenAddress
     * @return decimals of token which get from overview API
     */
    public static int getDecimals(String tokenAddress){ return (int) getData(tokenAddress, "decimals");}
    /**
     * @param tokenAddress
     * @return usd rate of token
     */
    public static double getUSD(String tokenAddress) { return Double.valueOf(getData(tokenAddress, "usd").toString()); }

    /**
     * @param tokenAddress
     * @param elem
     * @return value of token which get from overview API
     */
    public static Object getData(String tokenAddress, String elem){
        String API = Environment.uri_chain + token_overview_path + tokenAddress;
        Log.info("overview_API: "+API);
        DocumentContext ctx = RestUtils.getContext(API);
        Object out = ctx.read("$.data[0]."+elem+"");
        Log.info(elem +": "+out);
        return out;
    }

    public static List<String> getListMarket(String chain){
        String API = Environment.uri + chain + overview_path;
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = ctx.read("$.data[*].address");
        return tokens;
    }
}
