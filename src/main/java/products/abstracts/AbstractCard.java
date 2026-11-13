package products.abstracts;

import enums.Currency;
import products.interfaces.TopUp;
import products.interfaces.DebitOff;

import java.math.BigDecimal;

//Абстрактная карта
public abstract class AbstractCard extends AbstractBankProduct implements TopUp, DebitOff {

    protected AbstractCard(String name, Currency currency, BigDecimal initialBalance) {
        super(name, currency, initialBalance);
    }

    @Override
    //запрос баланса
    public void topUp(BigDecimal amount) {
        validatePositiveAmount(amount);
        setBalance(getBalance().add(amount));
    }
}
