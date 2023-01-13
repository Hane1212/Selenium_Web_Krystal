package RFT.Client;

public class BalanceClient {
    String chainName;
    public BalanceClient(String chainName) {
        this.chainName = chainName;
    }
    public BalanceInfo getBalance(String userAddress) {
        return new BalanceInfo( Double.valueOf(this.chainName.length()));
    }
}
