package API.earning;

import API.common.Environment;
import API.common.RestUtils;
import com.jayway.jsonpath.DocumentContext;
import utils.logs.Log;

import java.util.List;

public class options {
    static String options_path = "all/v1/earning/options";
//https://api-dev.krystal.team/all/v1/earning/options?platform=ankr&chainId=56&type=staking
    private static String setEarningOptionsURL(String chainName, String platform, String type){
        Environment.Chain chain = Environment.getChainByName(chainName);
        String platform_ = "?platforms=" +platform;
        String chainID_ = "&chainIds=" + chain.chainId;
        String type_ = "&types=" + type;

        String path_ = Environment.uri + options_path + platform_ + chainID_ + type_;
        Log.info("setEarningOptionsURL: "+  path_);
        return path_;
    }

    public static List<String> getListOptions(String chain, String platform, String type){
        String API = setEarningOptionsURL(chain, platform, type);
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = ctx.read("$.result[*].token.address");
        return tokens;
    }
}
