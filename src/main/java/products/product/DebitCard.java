package products.product;

import enums.Currency;
import products.abstracts.AbstractCard;

import java.math.BigDecimal;

// Дебетовая карта
public class DebitCard extends AbstractCard {

    public DebitCard(String name, Currency currency, BigDecimal initialBalance) {
        super(name, currency, initialBalance);
    }

    @Override
    //списание средств с дебетовой карты
    public void debitOff(BigDecimal amount) {
        validatePositiveAmount(amount);
        if (getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Недостаточно средств на карте");
        }
        setBalance(getBalance().subtract(amount));
    }
}
