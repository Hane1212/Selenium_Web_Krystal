package API.approval;

import API.common.Environment;
import API.common.RestUtils;
import com.jayway.jsonpath.DocumentContext;
import org.openqa.selenium.WebDriver;
import pages.BasePage;
import scala.util.parsing.combinator.testing.Str;
import utils.logs.Log;

import java.util.List;

public class listAPI extends BasePage {
    static String list_path = "all/v1/approval/list?address=";

    public listAPI(WebDriver driver, Environment.Chain test_env) {
        super(driver, test_env);
    }

    private static String set_list_URL(){
        String URL = new String();
        String chainIds = "&chainIds="+ test_env.chainId;
        URL = Environment.uri + list_path + Environment.user_address + chainIds;
        Log.info("ApprovalList_URL: "+ URL);
        return URL;
    }

    public static List<String> getApprovalList(String spenderAddress){
        String API = set_list_URL();
        DocumentContext ctx = RestUtils.getContext(API);
        List<String> listAddress = ctx.read("$.data.approvals[?(@.spenderAddress == '"+spenderAddress.toLowerCase()+"')].tokenAddress");
        return listAddress;
    }
}
