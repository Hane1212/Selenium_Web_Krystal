package API.token;

import API.common.Environment;
import API.common.RestUtils;
import com.jayway.jsonpath.DocumentContext;
import utils.logs.Log;

import java.util.List;

public class commonBaseAPI {
    static String commonBase_path ="/v1/token/commonBase";
    //   ----------------------/{chain}/v1/token/tokenList------------------------------------
    private static String setCommonBaseURL(String chain){
        String path_ = Environment.uri + chain + commonBase_path;
        Log.info("setCommonBase_API: "+  path_);
        return path_;
    }

    public static List<String> getListCommonBase(String chain){
        String API = setCommonBaseURL(chain);
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> tokens = ctx.read("$.tokens[*].address");
        return tokens;
    }
}
