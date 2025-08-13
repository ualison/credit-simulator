package com.credit.simulator.provider.domain.model.enums;

import com.credit.simulator.provider.domain.model.shared.MathUtils;

public enum CurrencyTypeEnum {
    BRL("R$", 1.0),
    USD("$", 0.18),
	EUR("€", 0.16);
    
    private final String symbol;
    private final Double rate;
    
    CurrencyTypeEnum(String symbol, Double rate) {
        this.symbol = symbol;
        this.rate = rate;
    }
    
    public Double convert(Double amount) {
        return MathUtils.roundToTwoDecimals(amount * rate);
    }
    
    public String getSymbol() {
        return symbol;
    }
}