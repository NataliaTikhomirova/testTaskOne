package products.product;

import enums.Currency;
import lombok.Getter;
import products.abstracts.AbstractBankProduct;
import products.interfaces.Closable;
import products.interfaces.TopUp;

import java.math.BigDecimal;

// Вклад
@Getter
public class Deposit extends AbstractBankProduct implements TopUp, Closable {
    private boolean closed = false;

    public Deposit(String name, Currency currency, BigDecimal initialBalance) {
        super(name, currency, initialBalance);
    }

    @Override
    //пополнение вклада
    public void topUp(BigDecimal amount) {
        if (closed) {
            throw new IllegalStateException("Невозможно пополнить закрытый вклад");
        }
        validatePositiveAmount(amount);
        setBalance(getBalance().add(amount));
    }

    @Override
    //закрытие вклада
    public void close() {
        if (closed) {
            throw new IllegalStateException("Вклад уже закрыт");
        }
        setBalance(BigDecimal.ZERO);
        this.closed = true;
    }
}
