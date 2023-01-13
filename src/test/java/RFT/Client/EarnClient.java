package RFT.Client;

import API.common.Environment;
import API.common.RestUtils;
import RFT.Earn;
import com.jayway.jsonpath.DocumentContext;
import utils.logs.Log;

import java.util.List;

public class EarnClient {
    String chainName;
    long chainID;

    public EarnClient(long chainID) {
        this.chainID = chainID;
    }

    public Earn.OptionDetail getOptionDetail(String platform, String earningType, String tokenAddress) {
        String optionDetail_path = "all/v1/earning/optionDetail";
//   ?chainId=137&platform=aave_v2&earningType=lending&tokenAddress=0x2791bca1f2de4661ed88a30c99a7a9449aa84174
        String chainID_ = "?chainId=" + this.chainID;
        String platform_ = "&platform=" +platform;
        String type_ = "&earningType=" + earningType;
        String address_ = "&tokenAddress=" + tokenAddress;
        String path_ = Environment.uri + optionDetail_path + chainID_+ platform_  + type_ + address_;
        Log.info("setOptionDetailURL: "+  path_);

        DocumentContext ctx = RestUtils.getContext(path_);
        Earn.OptionDetail optionDetail = new Earn.OptionDetail();
        optionDetail.poolAddress = ctx.read("$.poolAddress");
        optionDetail.stakeRequireApprove = ctx.read("$.requireApprove");
        try {
            optionDetail.minStakeAmount = ctx.read("$.validation.minStakeAmount");
            return optionDetail;
        } catch (Exception e) {
            optionDetail.minStakeAmount = 0.0;
        }
        return optionDetail;
    }



    public List<String> getListOptions( String platform, String type){
        String options_path = "all/v1/earning/options";

        String platform_ = "?platforms=" +platform;
        String chainID_ = "&chainIds=" + this.chainID;
        String type_ = "&types=" + type;

        String path_ = Environment.uri + options_path + platform_ + chainID_ + type_;
        Log.info("setEarningOptionsURL: "+  path_);
        DocumentContext ctx = RestUtils.getContext(path_);
        return ctx.read("$.result[*].token.address");
    }


}
