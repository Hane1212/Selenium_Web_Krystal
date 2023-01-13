package pages;

import API.accounts.balancesAPI;
import API.approval.listAPI;
import API.common.Environment;
import API.common.TokenAllowance;
import API.market.overviewAPI;
import API.swap.gasPriceAPI;
import API.token.commonBaseAPI;
import API.token.tokenDetailsAPI;
import API.token.tokenListAPI;
import API.transferAPI;
import RFT.Earn;
import RFT.Swap;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import utils.Assertion;
import utils.Util;
import utils.extentreports.ExtentTestManager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComAPI extends BasePage{
    public ComAPI(WebDriver driver, Environment.Chain test_env) {
        super(driver, test_env);
    }

    /**
     * @param token
     * @return true if token address is native token
     */
    public static boolean isNative(String token){
        boolean isNative_ = false;
        String tokenAddress = tokenListAPI.getTokenAddress(test_env.nativeToken);
        if (token.equals(tokenAddress)){
            isNative_ = true;
        }
        return isNative_;
    }

//    public static boolean isNative(String chain, String token){
//        boolean isNative_ = false;
//        String nativeTokenAddress = tokenListAPI.getTokenAddress(chain, Environment.getNativeToken(chain.toLowerCase()));
//        if (token.equals(nativeTokenAddress)){
//            isNative_ = true;
//        }
//        return isNative_;
//    }

    public static BigInteger getWeiValue(String tokenAddress, double amount){
        int decimal = tokenDetailsAPI.get_decimals(tokenAddress);
        return valueToWei(amount, decimal);
    }

    public static BigInteger getWeiValue(String tokenAddress, String amount){
        int decimal = tokenDetailsAPI.get_decimals(tokenAddress);
        return valueToWei(amount, decimal);
    }

    public static BigInteger getWeiValue(String chain, String tokenAddress, String amount){
        int decimal = tokenDetailsAPI.get_decimals(chain, tokenAddress);
        return valueToWei(amount, decimal);
    }

    public static BigInteger valueToWei(String value, int decimals) {
        BigDecimal dValue = new BigDecimal(value);
        BigDecimal scale = new BigDecimal(10).pow(decimals);
        return  dValue.multiply(scale).toBigInteger();
    }

    public static BigInteger valueToWei(double value, int decimals) {
        return valueToWei(String.format("%.80f", value), decimals);
    }

    public static Double weiToValue(BigInteger wei, int decimals) {
        BigDecimal scale = new BigDecimal(10).pow(decimals);
        BigDecimal dWei = new BigDecimal(wei);
        return dWei.divide(scale).doubleValue();
    }

    public  static  Double weiToValue(String wei, int decimals) {
        BigInteger bWei = new BigInteger(wei);
        return weiToValue(bWei, decimals);
    }



    /**
     * @param chain
     * @return pair: chain, sourceToken, DestToken, Amount of source token to perform swap
     */
    public static Object[][] getListTokenToVerifySwap(String chain){
        chain = chain.toLowerCase();
        Object[][] targetTokens = new Object[0][];
        List<String> balanceTokens = balancesAPI.getListAccountBalance(chain);
        List<String> tokenLists = tokenListAPI.getTokenLists(chain);
        List<String> comLists = Util.getCommonElements(balanceTokens, tokenLists);
        List<String> marketTokens = overviewAPI.getListMarket(chain);
        for (int i=0; i<comLists.size(); i++){
            Swap swap = new Swap(chain);
//            swap.setSrcTokenAddress(comLists.get(i));
            String amount = balancesAPI.getBalance(chain, comLists.get(i));
            if(!swap.chain.isNative(comLists.get(i))){
                BigInteger allowance = TokenAllowance.getAllowance(chain, swap.chain.swapSpender, comLists.get(i));
                if ((allowance.compareTo(new BigInteger(amount)) == -1)) {
                    continue;
                }
            }
            int random = Util.getRandomNumber(marketTokens.size());
            while (marketTokens.get(random).equals(comLists.get(i))){
                random = Util.getRandomNumber(marketTokens.size());
            }
//            swap.setDestTokenAddress(marketTokens.get(random));
            Object [][]data = new Object [1][4];
            data[0][0] = chain;
            data[0][1] = comLists.get(i); //Source token
            data[0][2] = marketTokens.get(random);//Dest token
            amount = Util.divideBigInterger(amount, 2);
            data[0][3] = amount;//Amount of source token
            targetTokens = Util.merge2D(targetTokens, data);
        }
        System.out.println("Number_Of_Target_Token: "+ targetTokens.length);
        return targetTokens;
    }

    /**
     * parse from string list to object
     * @param chain of testing
     * @return
     */
    public static Object[][] getDataForTransfer(String chain) {
        List<String> listOfMaps = getListTokenToVerifyTransfer(chain);
        Object [][]data = new Object [listOfMaps.size()][];
        for(int i = 0; i < listOfMaps.size(); i++) {
            data[i]= new Object[1];
            data[i][0] = listOfMaps.get(i);
        }
        return data;
    }

    /**
     * @Author: Hane
     * @param chain: to perform test
     * @return: list of 10 tokens which combine between commonBase and account Balance token
     */
    public static List<String> getListTokenToVerifyTransfer(String chain){
        List<String> commonTokens = commonBaseAPI.getListCommonBase(chain);
        List<String> balanceTokens = balancesAPI.getListAccountBalance(chain);
        Environment.Chain chain_ = Environment.getChainByName(chain);
        String nativeToken = tokenListAPI.getTokenAddress(chain, chain_.nativeToken);
        List<String> targetTokens = new ArrayList<>();
        if(balanceTokens.size()<=commonTokens.size()){
            targetTokens = balanceTokens;
        }else {
            for (int i =0; i<commonTokens.size();i++)
            {
                if (balanceTokens.contains(commonTokens.get(i))){
                    targetTokens.add(commonTokens.get(i));
                    balanceTokens.remove(commonTokens.get(i));
                }
            }
            if (targetTokens.size()<11){
                if(targetTokens.size() + balanceTokens.size() <10){
                    targetTokens = Stream.concat(targetTokens.stream(), balanceTokens.stream())
                            .collect(Collectors.toList());
                }else {
                    targetTokens.add(nativeToken);
                    balanceTokens.remove(nativeToken);
                    int num = 10 - targetTokens.size();
                    List<String> randomPicks = Util.pickNRandom(balanceTokens, num);
                    targetTokens = Stream.concat(targetTokens.stream(), randomPicks.stream())
                            .collect(Collectors.toList());
                }
            }
        }
        return targetTokens;
    }

    /**
     * @param chain to perform test
     * @return List token include [chain, platform, type, token, amount]
     */
    public static Object[][] getListTokenToVerifyEarn(String chain){
        List<String> balanceTokens = balancesAPI.getListAccountBalance(chain);
        List<String> platforms = Util.getListPropertyValue("EARN_PLATFORM");
        List<String> type = Util.getListPropertyValue("EARN_TYPE");
        Object[][] targetTokens = new Object[0][];

        for (int i=0; i<platforms.size(); i++){
            for (int j = 0; j< type.size(); j++){
                Earn earn = new Earn(chain, platforms.get(i), type.get(j));
                List<String> tmp = earn.getListOptions();
                if (tmp.size() == 0) {
                    break;
                }
                for (int x = 0; x < tmp.size(); x++) {
                    earn.setTokenAddress(tmp.get(x));
                    if (!balanceTokens.contains(earn.tokenAddress)) {
                        continue;
                    }
                    Object[][] data = new Object[1][5];
                    String amount = balancesAPI.getBalance(chain, earn.tokenAddress);
                    if(!earn.chain.isNative(earn.tokenAddress)){
                        BigInteger allowance = TokenAllowance.getAllowance(chain, earn.getOptionDetail().poolAddress, earn.tokenAddress);
                        if ((allowance.compareTo(new BigInteger(amount)) == -1)) {
                            continue;
                        }
                    }
                    int decimals = tokenDetailsAPI.get_decimals(chain, earn.tokenAddress);
                    double amount_ = weiToValue(amount, decimals);
                    String type_ = earn.earningType;
                    //      If token is MATIC on Ankr & LIDO -> earningType is stakingMATIC
                    if (earn.tokenAddress.equals("0x7d1afa7b718fb893db30a3abc0cfc608aacfebb0")
                            && (earn.platform.equals("ankr")
                            || earn.platform.equals("lido"))) {
                        type_ = "stakingMATIC";
                    }
                    Object minStakeAmount = earn.getOptionDetail().minStakeAmount;
                    if (minStakeAmount == null) {
                        minStakeAmount = 0.0;
                    }
                    double minStakeAmount_ = Double.valueOf((Double) minStakeAmount);
                    if (Double.compare(minStakeAmount_, amount_) < 0) {
                        data[0][0] = chain;//Chain
                        data[0][1] = earn.platform; //platform
                        data[0][2] = type_;//type
                        data[0][3] = earn.tokenAddress;//tokenAddress
                        data[0][4] = amount;
                        targetTokens = Util.merge2D(targetTokens, data);
                    }
                    System.out.println(String.format("=====> \n\n\n token_address_earn: %s, amount: %f, min_amount: %f\n\n<====", tmp.get(x), amount_, minStakeAmount));
                }
            }
        }
        System.out.println("Number_Of_Target_Tokens: "+targetTokens.length);
        return targetTokens;
    }

    /**
     * @return random One UnApproval token which has balance
     */
    public static String getUnApproveToken(){
        String spender = test_env.multiSendSpender;
        List<String> balanceTokens = balancesAPI.getListAccountBalance(Environment.test_chain);
        List<String> approvalTokens = listAPI.getApprovalList(spender);
        int random = Util.getRandomNumber(balanceTokens.size());
        // TODO: infinite loop
        while (approvalTokens.contains(balanceTokens.get(random))){
            random = Util.getRandomNumber(balanceTokens.size());
        }
        return balanceTokens.get(random);
    }

    /**
     * @return random One Approval token which has balance
     */
    public static String getApproveToken(){
        String spender = test_env.multiSendSpender;
        List<String> balanceTokens = balancesAPI.getListAccountBalance(Environment.test_chain);
        List<String> approvalTokens = listAPI.getApprovalList(spender);
        int random = Util.getRandomNumber(approvalTokens.size());
        // TODO: infinite loop
        while (!balanceTokens.contains(approvalTokens.get(random))){
            random = Util.getRandomNumber(approvalTokens.size());
        }
        return approvalTokens.get(random);
    }

    public static double getBalanceBaseTokenUnit(String chain, String tokenAddress){
        String balance = balancesAPI.getBalance(chain, tokenAddress);
        int decimals = tokenDetailsAPI.get_decimals(chain, tokenAddress);
        double amount_ = Util.roundAvoidD(Double.parseDouble(balance), decimals,6) ;
        return amount_;
    }

    public static double getBalanceBaseTokenUnit(String tokenAddress){
        String balance = balancesAPI.getBalance(Environment.test_chain, tokenAddress);
        int decimals = tokenDetailsAPI.get_decimals(Environment.test_chain, tokenAddress);
        double amount_ = Util.roundAvoidD(Double.parseDouble(balance), decimals,6) ;
        return amount_;
    }

    public static String getDivideBalanceToken(String chain, String tokenAddress, int divide){
        double amount = getBalanceBaseTokenUnit(chain, tokenAddress);
        return String.valueOf(amount/divide);
    }

    public static String getDivideBalanceToken(String tokenAddress, int divide){
        double amount = getBalanceBaseTokenUnit(tokenAddress);
        return String.format("%.11f",amount/divide);
    }

    public static double calculateMaxGasFee(String srcToken, String amount){
        int gasLimit = transferAPI.getGasLimit(srcToken, Environment.recipientAddress, Double.parseDouble(amount));
        Double gasPrice = gasPriceAPI.getGasPriceStandard();
        double maxGasFee = Util.roundAvoidD(gasPrice * Double.parseDouble(String.valueOf(gasLimit)), 9, 5);
        return maxGasFee;
    }
    //------------------------------------COMMON---------------------------------------------------

    /**
     * @param chain
     * @param spender
     * @param tokenAddress
     * @return Verify allowance state of token which can not get Gas Limit
     */
    public static int checkAllowance(String chain, String spender, String tokenAddress, String amount){
        BigInteger allowance = null;
        BigInteger amount_ = new BigInteger(amount);
        String msg;
        Environment.Chain chain_ = Environment.getChainByName(chain);
        if(!chain_.isNative(tokenAddress)){
            allowance = TokenAllowance.getAllowance(chain, spender, tokenAddress);
            ExtentTestManager.logMessage(Status.INFO, "Allowance: " + allowance);
            ExtentTestManager.logMessage(Status.INFO, "Amount: " + amount_);
            if((allowance.intValue()==0) || (allowance.compareTo(amount_) ==-1)){
                msg = "Token: "+ tokenAddress + " has not approved";
                Assertion.assertWarn(msg);
            }else {
                msg = "Can not get GasLimit of token: "+ tokenAddress ;
                Assertion.assertFail(msg);
            }
        }else {
            msg = "Can not get GasLimit of NATIVE token: "+ tokenAddress;
            Assertion.assertFail(msg);
        }
        return allowance.intValue();
    }


}
