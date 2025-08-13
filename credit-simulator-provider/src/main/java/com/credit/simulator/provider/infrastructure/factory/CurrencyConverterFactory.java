package com.credit.simulator.provider.infrastructure.factory;

import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.credit.simulator.provider.domain.model.enums.CurrencyTypeEnum;
import com.credit.simulator.provider.domain.model.exception.UnsupportedCurrencyException;
import com.credit.simulator.provider.domain.model.shared.Constants;
import com.credit.simulator.provider.domain.model.shared.MathUtils;

@Component
public class CurrencyConverterFactory {

	private static final Map<CurrencyTypeEnum, Map<CurrencyTypeEnum, Double>> CONVERSION_RATES = Map.of(
			   CurrencyTypeEnum.BRL, Map.of(
			       CurrencyTypeEnum.BRL, Constants.SAME_CURRENCY_RATE,
			       CurrencyTypeEnum.USD, Constants.BRL_TO_USD,
			       CurrencyTypeEnum.EUR, Constants.BRL_TO_EUR
			   ),
			   CurrencyTypeEnum.USD, Map.of(
			       CurrencyTypeEnum.BRL, Constants.USD_TO_BRL,
			       CurrencyTypeEnum.USD, Constants.SAME_CURRENCY_RATE,
			       CurrencyTypeEnum.EUR, Constants.USD_TO_EUR
			   ),
			   CurrencyTypeEnum.EUR, Map.of(
			       CurrencyTypeEnum.BRL, Constants.EUR_TO_BRL,
			       CurrencyTypeEnum.USD, Constants.EUR_TO_USD,
			       CurrencyTypeEnum.EUR, Constants.SAME_CURRENCY_RATE
			   )
			);

	public Double convert(Double amount, String fromCurrency, String toCurrency) {
		CurrencyTypeEnum from = getCurrency(fromCurrency);
		CurrencyTypeEnum to = getCurrency(toCurrency);

		Double rate = CONVERSION_RATES.get(from).get(to);
		return MathUtils.roundToTwoDecimals(amount * rate);
	}

	public String getSymbol(String currency) {
		return getCurrency(currency).getSymbol();
	}

	public boolean isSupported(String currency) {
		return Arrays.stream(CurrencyTypeEnum.values()).anyMatch(type -> type.name().equalsIgnoreCase(currency));
	}

	private CurrencyTypeEnum getCurrency(String currency) {
		if (!isSupported(currency))
			throw new UnsupportedCurrencyException("Currency not supported: " + currency);

		return CurrencyTypeEnum.valueOf(currency.toUpperCase());
	}
}
