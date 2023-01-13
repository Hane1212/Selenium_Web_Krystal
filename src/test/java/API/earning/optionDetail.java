package API.earning;

import API.common.Environment;
import API.common.RestUtils;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.restassured.path.json.JsonPath;
import utils.logs.Log;

public class optionDetail {
    static String optionDetail_path = "all/v1/earning/optionDetail";
//   ?chainId=137&platform=aave_v2&earningType=lending&tokenAddress=0x2791bca1f2de4661ed88a30c99a7a9449aa84174
    private static String setOptionDetailURL(long chainid, String platform, String type, String tokenAddress){
        String chainID_ = "?chainId=" + chainid;
        String platform_ = "&platform=" +platform;
        String type_ = "&earningType=" + type;
        String address_ = "&tokenAddress=" + tokenAddress;
        String path_ = Environment.uri + optionDetail_path + chainID_+ platform_  + type_ + address_;
        Log.info("setOptionDetailURL: "+  path_);
        return path_;
    }

    public static String getSpender(long chainid, String platform, String type, String tokenAddress){
        String API = setOptionDetailURL(chainid, platform, type , tokenAddress);
        JsonPath jp = RestUtils.getJsonPath(RestUtils.getResponse(API));
        String poolAddress = jp.get("poolAddress");
        return poolAddress;
    }

    public static double getMinStakeAmount(int chain, String platform, String type, String tokenAddress) {
            String API = setOptionDetailURL(chain, platform, type, tokenAddress);
            DocumentContext ctx = RestUtils.getContext(API);
        try {
            Object totalUSDvalues = ctx.read("$.validation.minStakeAmount");
            return (double) totalUSDvalues;
        } catch (Exception e) {
            return 0;
        }
    }

}
