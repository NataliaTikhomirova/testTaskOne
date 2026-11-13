package products.product;

import enums.Currency;
import lombok.Getter;
import products.abstracts.AbstractCard;
import products.interfaces.Creditable;

import java.math.BigDecimal;

// Кредитная карта
@Getter
public class CreditCard extends AbstractCard implements Creditable {
    private final BigDecimal procentRate;
    private final BigDecimal creditLimit;

    public CreditCard(String name, Currency currency, BigDecimal creditLimit, BigDecimal procentRate) {
        super(name, currency, creditLimit);
        if (procentRate == null || procentRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Процентная ставка не может быть отрицательной");
        }
        this.creditLimit = creditLimit;
        this.procentRate = procentRate;
    }

    @Override
    //списание суммы по кредитной карте
    public void debitOff(BigDecimal amount) {
        validatePositiveAmount(amount);
        if (getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Превышен кредитный лимит");
        }
        setBalance(getBalance().subtract(amount));
    }

    @Override
    //сумма текущей задолженности
    public BigDecimal getDebt() {
        if (getBalance().compareTo(creditLimit) < 0) {
            return creditLimit.subtract(getBalance());
        }
        return BigDecimal.ZERO;
    }
}