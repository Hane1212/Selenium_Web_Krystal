package API.crosschain;

import API.common.Environment;
import API.common.RestUtils;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import utils.logs.Log;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class serverInfoAPI {
    static Response response = null;
    Response res = null; //Response object
    static JsonPath jp = null; //JsonPath object
    static String serverInfo_path = "all/v1/crosschain/serverInfo?chainId=";

    private static String getChainID(String chain){
        String chainID = new String();
        switch (chain){
            case "ethereum":
                chainID = "1";
                break;
            case "bsc":
                chainID = "56";
                break;
            case "polygon":
                chainID = "137";
                break;
            case "avalanche":
                chainID = "43114";
                break;
            case "cronos":
                chainID = "25";
                break;
            case "fantom":
                chainID = "250";
                break;
            case "arbitrum":
                chainID = "42161";
                break;
            case "aurora":
                chainID = "1313161554";
                break;
            default: chainID = "1";
        }
        return chainID;
    }

    /**
     * @param chain of source
     * @return URL of API (Ex: https://api-dev.krystal.team/all/v1/crosschain/serverInfo?chainId=1313161554)
     */
    private static String setServerInfoURL(String chain){
        String chainID = getChainID(chain);
        String URL = new String();
        URL = Environment.uri + serverInfo_path +chainID;
        Log.info("Server Info of "+chain+": "+ URL);
        return URL;
    }

    private static DocumentContext getDocOfAPI(String chain){
        String API = setServerInfoURL(chain);
        DocumentContext ctx = RestUtils.getContext(API);
        return ctx;
    }
//    Crosschain Fee is 0.1 %
//    Minimum Crosschain Fee is 0.8 AVAX
//    Maximum Crosschain Fee is 16 AVAX
//    Minimum Crosschain Amount is 0.9 AVAX
//    Maximum Crosschain Amount is 80,000 AVAX
//    Estimated Time of Crosschain Arrival is 10-30 mins
//    Crosschain amount larger than 16,000 AVAX could take up to 12 hours

    public static Object getMaximumSwap(String srcChain, String tokenAddress, String destChain){
        String destChainID = getChainID(destChain);
        DocumentContext ctx = getDocOfAPI(srcChain);
        Object out = ctx.read("$.data[?(@.address == '"+tokenAddress+"')].destChains."+destChainID+".maximumSwap");
        return out;
    }

    public static Map<String, String> getServerInfo(String srcChain, String tokenAddress, String destChain){
        String destChainID = getChainID(destChain);
        DocumentContext ctx = getDocOfAPI(srcChain);
        Map<String, String> dictionary = new HashMap<String, String>();

//      Get Maximum transfer amount
        List<String>  out = ctx.read("$.data[?(@.address == '"+tokenAddress+"')].destChains."+destChainID+".maximumSwap");
        dictionary.put("maximumSwap", out.get(0));
//      Get Minimum transfer amount
//        out = chains.read("$.minimumSwap");
        out = ctx.read("$.data[?(@.address == '"+tokenAddress+"')].destChains."+destChainID+".minimumSwap");
        dictionary.put("minimumSwap", out.get(0));
//      Get Transaction fee
        out = ctx.read("$.data[?(@.address == '"+tokenAddress+"')].destChains."+destChainID+".swapFeeRatePerMillion");
        dictionary.put("swapFeeRatePerMillion", out.get(0));
//      Get maximum Swap Fee
        out = ctx.read("$.data[?(@.address == '"+tokenAddress+"')].destChains."+destChainID+".maximumSwapFee");
        dictionary.put("maximumSwapFee", out.get(0));
//      Get minimum Swap fee
        out = ctx.read("$.data[?(@.address == '"+tokenAddress+"')].destChains."+destChainID+".minimumSwapFee");
        dictionary.put("minimumSwapFee", out.get(0));
        return dictionary;
    }

}
