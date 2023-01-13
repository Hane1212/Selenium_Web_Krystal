package API.token;

import API.common.Environment;
import API.common.RestUtils;
import com.jayway.jsonpath.DocumentContext;
import scala.util.parsing.combinator.testing.Str;
import utils.logs.Log;

import java.util.List;

public class tokenListAPI {
    static String tokenList_path ="/v1/token/tokenList";
    //   ----------------------/{chain}/v1/token/tokenList------------------------------------
    private static String setTokenListURL(){
        String path_ = Environment.uri_chain + tokenList_path;
        Log.info("setTokenList_API: "+  path_);
        return path_;
    }

    private static String setTokenListURL(String chain){
        String path_ = Environment.uri + chain.toLowerCase() + tokenList_path;
        Log.info("setTokenList_API: "+  path_);
        return path_;
    }

    public static int getDecimals(String tokenAddress){
        String API = setTokenListURL();
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = ctx.read("$.tokens[?(@.address == '"+tokenAddress+"')].decimals");
//        if (tokens.size()>1){
////          TODO
//        }
        Object out = tokens.get(0);
        return (int) out;
    }

    public static int getDecimals(String chain, String tokenAddress){
        String API = setTokenListURL(chain);
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = ctx.read("$.tokens[?(@.address == '"+tokenAddress+"')].decimals");
//        if (tokens.size()>1){
////          TODO
//        }
        Object out = tokens.get(0);
        return (int) out;
    }

    public static String getTokenAddress(String tokenSymbol){
        String API = setTokenListURL();
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = ctx.read("$.tokens[?(@.symbol == '"+tokenSymbol+"')].address");
//        if (tokens.size()>1){
////          TODO
//        }
        System.out.println("tokenSymbol: " + tokenSymbol);
        Object out = tokens.get(0);
        return (String) out;
    }


    public static String getTokenAddress(String chain, String tokenSymbol){
        String API = setTokenListURL(chain);
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = ctx.read("$.tokens[?(@.symbol == '"+tokenSymbol+"')].address");
//        if (tokens.size()>1){
////          TODO
//        }
        System.out.println("tokenSymbol: " + tokenSymbol);
        Object out = tokens.get(0);
        return (String) out;
    }

    public static String getTokenSymbol(String chain, String tokenAddress){
        String API = setTokenListURL(chain);
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = ctx.read("$.tokens[?(@.address == '"+tokenAddress+"')].symbol");
//        if (tokens.size()>1){
////          TODO
//        }
        Object out = tokens.get(0);
        return (String) out;
    }

    public static String getTokenSymbol(String tokenAddress){
        String API = setTokenListURL();
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = ctx.read("$.tokens[?(@.address == '"+tokenAddress+"')].symbol");
//        if (tokens.size()>1){
////          TODO
//        }
        Object out = tokens.get(0);
        return (String) out;
    }

    public static List<String> getTokenLists(String chain){
        String API = setTokenListURL(chain);
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = ctx.read("$.tokens[*].address");
        return tokens;
    }

}
