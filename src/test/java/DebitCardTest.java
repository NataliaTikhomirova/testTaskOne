import enums.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import products.product.DebitCard;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DebitCardTest {

    private DebitCard debitCard;
    private static final String debitCardName = "Основная карта";

    @BeforeEach
    void setUp() {
        debitCard = new DebitCard(debitCardName, Currency.RUB, new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("Успешное создание дебетовой карты")
    void checkCreateDebitCard() {
        assertEquals(debitCardName, debitCard.getName());
        assertEquals(Currency.RUB, debitCard.getCurrency());
        assertEquals(0, new BigDecimal("1000.00").compareTo(debitCard.getBalance()));
    }

    @Test
    @DisplayName("Успешное пополнение баланса")
    void checkDebitCardTopUp() {
        debitCard.topUp(new BigDecimal("500.50"));
        assertEquals(0, new BigDecimal("1500.50").compareTo(debitCard.getBalance()));
    }

    @Test
    @DisplayName("Успешное списание средств")
    void checkDebitCardDebitOff() {
        debitCard.debitOff(new BigDecimal("400.00"));
        assertEquals(0, new BigDecimal("600.00").compareTo(debitCard.getBalance()));
    }

    @Test
    @DisplayName("Ошибка при создании карты с некорректными параметрами")
    void checkExceptionDebitCardWithInvalidArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new DebitCard("", Currency.RUB, BigDecimal.TEN));
        assertThrows(IllegalArgumentException.class,
                () -> new DebitCard(debitCardName, null, BigDecimal.TEN));
        assertThrows(IllegalArgumentException.class,
                () -> new DebitCard(debitCardName, Currency.RUB, new BigDecimal("-10.00")));
    }

    @Test
    @DisplayName("Ошибка при пополнении на нулевую или отрицательную сумму")
    void checkExceptionDebitCardWithNotPositiveAmount() {
        assertThrows(IllegalArgumentException.class, () -> debitCard.topUp(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> debitCard.topUp(new BigDecimal("-100")));
        assertThrows(IllegalArgumentException.class, () -> debitCard.topUp(null));
    }

    @Test
    @DisplayName("Ошибка при списании на нулевую или отрицательную сумму")
    void checkExceptionDeditCardWithNotValidAmount() {
        assertThrows(IllegalArgumentException.class, () -> debitCard.debitOff(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> debitCard.debitOff(new BigDecimal("-100")));
        assertThrows(IllegalArgumentException.class, () -> debitCard.debitOff(null));
    }

    @Test
    @DisplayName("Ошибка при попытке списать больше, чем есть на балансе")
    void checkExceptionMoreThanBalanceAmount() {
        assertThrows(IllegalStateException.class,
                () -> debitCard.debitOff(new BigDecimal("1000.01")));
    }

    @ParameterizedTest(name = "Проверка корректности списания суммы {0} при балансе 10.00 должно дать {1} " +
            "с дробной частью 3 знака")
    @CsvSource({
            "5.551, 4.449",      // 1 на конце-> округляется до четного (6)
            "5.659, 4.341",      // 9 на конце -> округляется до четного (6)
            "7.540, 2.460",      // 0 на конце -> остается четным (4)
            "0.001, 9.999"       // Мелкая дробная сумма
    })
    @DisplayName("Проверка при списании с карты")
    void checkDebitCardDeditOffAmount(String depositAmount, String expectedBalance) {
        DebitCard card = new DebitCard(debitCardName, Currency.RUB, new BigDecimal("10.00"));
        card.debitOff(new BigDecimal(depositAmount));
        assertEquals(new BigDecimal(expectedBalance), card.getBalance());
    }
}
