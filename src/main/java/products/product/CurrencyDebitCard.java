package products.product;

import enums.Currency;

import java.math.BigDecimal;

// Валютная дебетовая карта
public class CurrencyDebitCard extends DebitCard {

    public CurrencyDebitCard(String name, Currency currency, BigDecimal initialBalance) {
        super(name, validateForeignCurrency(currency), initialBalance);
    }

    //Проверка, что валюта отличается от базовой (иначе карта аналогична дебетовой)
    private static Currency validateForeignCurrency(Currency currency) {
        if (currency == Currency.RUB) {
            throw new IllegalArgumentException("Валютная карта не может быть открыта в базовой валюте (RUB)");
        }
        return currency;
    }
}
