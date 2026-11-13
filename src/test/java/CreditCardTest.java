import enums.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import products.product.CreditCard;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreditCardTest {

    private CreditCard creditCard;
    private static final String creditCardName = "Кредитка";
    BigDecimal initialBalance = new BigDecimal("50000");

    @BeforeEach
    void setUp() {
        // Лимит 50 000, ставка 19.9%
        creditCard = new CreditCard(creditCardName, Currency.RUB, initialBalance, new BigDecimal("19.9"));
    }

    @Test
    @DisplayName("Начальная задолженность равна нулю, баланс равен лимиту")
    void checkCreditCardInitialBalance() {
        assertEquals(0, BigDecimal.ZERO.compareTo(creditCard.getDebt()));
        assertEquals(0, initialBalance.compareTo(creditCard.getBalance()));
        assertEquals(0, new BigDecimal("19.9").compareTo(creditCard.getProcentRate()));
    }

    @Test
    @DisplayName("Корректный расчет задолженности после списания и погашения")
    void checkCreditCardCalculations() {
        // Тратим 15 000
        BigDecimal debitOff = new BigDecimal("15000");
        creditCard.debitOff(initialBalance);
        assertEquals(0, initialBalance.subtract(debitOff).compareTo(creditCard.getBalance()));
        assertEquals(0, new BigDecimal("15000").compareTo(creditCard.getDebt()));

        // Частично гасим 5 000
        BigDecimal topUp = new BigDecimal("5000");
        creditCard.topUp(topUp);
        assertEquals(0, initialBalance.subtract(debitOff).add(topUp).compareTo(creditCard.getBalance()));
        assertEquals(0, new BigDecimal("10000").compareTo(creditCard.getDebt()));
    }

    @Test
    @DisplayName("Проверка, что если баланс выше лимита, долг равен нулю")
    void checkZeroDebtWhenBalanceExceedsLimit() {
        BigDecimal sum = new BigDecimal("5000");
        creditCard.topUp(sum);
        assertEquals(0, creditCard.getCreditLimit().add(sum)
                .compareTo(creditCard.getBalance()));
        assertEquals(0, BigDecimal.ZERO.compareTo(creditCard.getDebt()));
    }

    @Test
    @DisplayName("Ошибка при превышении кредитного лимита")
    void checkExceptionWithCreditLimitExceeded() {
        assertThrows(IllegalStateException.class,
                () -> creditCard.debitOff(new BigDecimal("50001")));
    }

    @Test
    @DisplayName("Ошибка при отрицательной процентной ставке")
    void checkExceptionWithNotValidProcentRate() {
        assertThrows(IllegalArgumentException.class,
                () -> new CreditCard(creditCardName, Currency.RUB, new BigDecimal("10000"), new BigDecimal("-1.5")));
    }
}
