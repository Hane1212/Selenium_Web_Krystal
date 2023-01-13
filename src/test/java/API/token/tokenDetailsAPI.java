package API.token;

import API.accounts.balancesAPI;
import API.common.Environment;
import API.common.RestUtils;
import com.jayway.jsonpath.DocumentContext;
import utils.logs.Log;

import java.util.List;

public class tokenDetailsAPI {
    static Environment.Chain test_chain = Environment.getChainByName(Environment.test_chain);
    static String tokenDetails_path = "/v1/token/tokenDetails?address=";
    private static String setTokenListURL(String address){
        String path_ = Environment.uri_chain + tokenDetails_path + address;
        Log.info("setTokenDetails_API: "+  path_);
        return path_;
    }

    private static String setTokenListURL(String chain, String address){
        String path_ = Environment.uri+chain.toLowerCase() + tokenDetails_path + address;
        Log.info("setTokenDetails_API: "+  path_);
        return path_;
    }


    public static Double get_usd_rate(String token){
        String address = balancesAPI.getTokenAddress(token);
        String api_url = setTokenListURL(address);
        DocumentContext ctx = RestUtils.getContext(api_url);
        Object price = ctx.read("$.result.markets.usd.price");
        if(price==null){
            price = 0;
        }
        return Double.valueOf(price.toString());
    }

    public static int get_decimals(String chain, String address){
        String api_url = setTokenListURL(chain, address);
        DocumentContext ctx = RestUtils.getContext(api_url);
        Object decimals = ctx.read("$.result.decimals");
        return (int) decimals;
    }

    public static int get_decimals(String address){
        String api_url = setTokenListURL(Environment.test_chain, address);
        DocumentContext ctx = RestUtils.getContext(api_url);
        Object decimals = ctx.read("$.result.decimals");
        return (int) decimals;
    }

    public static Double get_usd_value_of_native_token(){
        return get_usd_rate(test_chain.nativeToken);
    }

}
