package products.abstracts;

import enums.Currency;
import lombok.Getter;
import lombok.Setter;
import products.interfaces.BankProduct;

import java.math.BigDecimal;

@Getter
// Абстрактный базовый продукт
public abstract class AbstractBankProduct implements BankProduct {
    private final String name;
    private final Currency currency;

    @Setter
    private BigDecimal balance;

    protected AbstractBankProduct(String name, Currency currency, BigDecimal initialBalance) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Валюта должна быть указана");
        }
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Начальный баланс не может быть отрицательным");
        }
        this.name = name;
        this.currency = currency;
        this.balance = initialBalance;
    }

    protected void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма операции должна быть больше нуля");
        }
    }
}
