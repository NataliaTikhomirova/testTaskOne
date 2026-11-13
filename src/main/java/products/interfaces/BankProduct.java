package products.interfaces;

import enums.Currency;

import java.math.BigDecimal;

//Базовый банковский продукт (имеет 3 параметра)
public interface BankProduct {
    String getName();
    Currency getCurrency();
    BigDecimal getBalance();
}
