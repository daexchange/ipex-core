package ai.turbochain.ipex.constant;

public enum AccountType {
	
	ExchangeAccount (1), LegalCurrencyAccount (2);

    // 定义私有变量
    private int nCode ;

    // 构造函数，枚举类型只能为私有
    private AccountType(int _nCode) {
        this.nCode = _nCode;
    }

    @Override
    public String toString() {
        return String.valueOf(this.nCode);
    }
 }
