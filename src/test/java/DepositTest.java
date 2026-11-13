import enums.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import products.product.Deposit;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DepositTest {

    private Deposit deposit;

    @BeforeEach
    void setUp() {
        deposit = new Deposit("Накопительный", Currency.EUR, new BigDecimal("2000"));
    }

    @Test
    @DisplayName("Успешное пополнение открытого вклада")
    void checkTopUpDepositToOpenAccount() {
        assertFalse(deposit.isClosed());
        deposit.topUp(new BigDecimal("500"));
        assertEquals(0, new BigDecimal("2500").compareTo(deposit.getBalance()));
    }

    @Test
    @DisplayName("Успешное закрытие вклада обнуляет баланс и меняет статус")
    void checkDepositClose() {
        deposit.close();
        assertTrue(deposit.isClosed());
        assertEquals(0, BigDecimal.ZERO.compareTo(deposit.getBalance()));
    }

    @Test
    @DisplayName("Ошибка при попытке пополнить закрытый вклад")
    void checkExceptionWithDepositingToClosedAccount() {
        deposit.close();
        assertThrows(IllegalStateException.class,
                () -> deposit.topUp(new BigDecimal("100")));
    }

    @Test
    @DisplayName("Ошибка при повторном закрытии вклада")
    void checkExceptionWithClosingAlreadyClosedAccount() {
        deposit.close();
        assertThrows(IllegalStateException.class, () -> deposit.close());
    }
}
