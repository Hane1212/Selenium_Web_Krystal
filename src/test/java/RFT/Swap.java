package RFT;

import API.common.Environment;
import RFT.Client.BalanceClient;
import RFT.Client.SwapClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Swap {
    public Environment.Chain chain;
    public Swap() {
        this.chain = Environment.getChainByName(Environment.test_chain);
    }
    public String srcToken;
    public String destToken;
    public String amount;
    public BalanceClient balanceClient;
    public SwapClient swapClient;

    public Swap(String chainName) {
        this.chain = Environment.getChainByName(chainName);
        this.balanceClient = new BalanceClient(this.chain.chainName);
    }
    public Swap(String chainName, String srcToken, String destToken, String amount) {
        this.chain = Environment.getChainByName(chainName);
        this.srcToken = srcToken;
        this.destToken = destToken;
        this.amount = amount;
        this.balanceClient = new BalanceClient(this.chain.chainName);
        this.swapClient = new SwapClient(this.chain.chainId);
    }

    public static class PlatformList{
        public List<String> platform_NG = new ArrayList<>();
        public List<String> platform_OK = new ArrayList<>();
    }

    public void setSrcTokenAddress(String tokenAddress) {
        this.srcToken = tokenAddress;
    }
    public void setDestTokenAddress(String tokenAddress) {
        this.destToken = tokenAddress;
    }

    public PlatformList getGasLimit(){
        return this.swapClient.getGasLimit(this.chain.chainName, this.srcToken, this.destToken, this.amount);
    }
}

