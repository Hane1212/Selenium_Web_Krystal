package API.earning;

import API.common.Environment;
import utils.logs.Log;

public class earningBalances {
    static String options_path = "all/v1/earning/earningBalances?address=";
    private static String setEarningBalancesURL(String chainName, String platform, String type){
        Environment.Chain chain = Environment.getChainByName(chainName);
        String platform_ = "?platforms=" +platform;
        String chainID_ = "&chainIds=" + chain.chainId;
        String type_ = "&types=" + type;

        String path_ = Environment.uri + options_path + platform_ + chainID_ + type_;
        Log.info("setEarningOptionsURL: "+  path_);
        return path_;
    }
}
