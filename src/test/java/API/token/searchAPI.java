package API.token;

import API.common.Environment;
import API.common.RestUtils;
import com.jayway.jsonpath.DocumentContext;
import utils.logs.Log;

import java.util.ArrayList;
import java.util.List;

public class searchAPI {
    static String search_path ="/v1/token/search?query=&orderBy=usdValue&limit=1000&address=";
    //   ----------------------/{chain}/v1/token/tokenList------------------------------------
    private static String setSearchURL(){
        String path_ = Environment.uri_chain + search_path + Environment.user_address;
        Log.info("setSearch_API: "+  path_);
        return path_;
    }

    public static List<String> getListTokenWithBalance(){
        String API = setSearchURL();
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = new ArrayList<String>();

        int length = ctx.read("$.balances.length()");
        for (int i =0; i < length ;i++){
            String balance = ctx.read("$.balances["+i+"].balance");
            if (!balance.equals("0")){
                String address = ctx.read("$.balances["+i+"].token.address");
                tokens.add(address);
            }
        }
        return tokens;
    }

    public static List<String> getListTokens(){
        String API = setSearchURL();
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = new ArrayList<String>();
        return tokens = ctx.read("$.balances[*].token.address");
    }
}
