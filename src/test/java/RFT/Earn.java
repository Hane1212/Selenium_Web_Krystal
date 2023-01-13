package RFT;

import API.common.Environment;
import RFT.Client.BalanceClient;
import RFT.Client.EarnClient;

import java.util.List;


public class Earn {
    public Environment.Chain chain;
    public String platform;

    public Earn() {
        this.chain = Environment.getChainByName(Environment.test_chain);
    }

    public String tokenAddress;
    public String earningType;
    public EarnClient earnClient;
    public BalanceClient balanceClient;

    public Earn(String chainName, String platform, String earningType) {
        this.chain = Environment.getChainByName(chainName);
        this.platform = platform;
        this.earningType = earningType;
        this.earnClient = new EarnClient(this.chain.chainId);
        this.balanceClient = new BalanceClient(this.chain.chainName);
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public static class OptionDetail {
        public String poolAddress;
        public Double minStakeAmount;
        public boolean stakeRequireApprove;
        public boolean unstakeRequireApprove;
    }
    public OptionDetail getOptionDetail() {
        return this.earnClient.getOptionDetail(this.platform, this.earningType, this.tokenAddress);
    }

    public List<String> getListOptions() {
        return this.earnClient.getListOptions(this.platform, this.earningType);
    }

}