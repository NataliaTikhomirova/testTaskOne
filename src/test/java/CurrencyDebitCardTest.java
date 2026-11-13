import enums.Currency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import products.product.CurrencyDebitCard;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyDebitCardTest {
    private static final String currencyDebitCard = "Валютная";

    @ParameterizedTest(name = "Успешное создание валютной карты в иностранной валюте: {0}")
    @EnumSource(value = Currency.class, names = {"USD", "EUR", "CNY"})
    @DisplayName("Создание карты во всех допустимых иностранных валютах")
    void checkCreateCardInAnyForeignCurrency(Currency foreignCurrency) {
        CurrencyDebitCard card = new CurrencyDebitCard(currencyDebitCard, foreignCurrency, new BigDecimal("1000"));

        assertEquals(currencyDebitCard, card.getName());
        assertEquals(foreignCurrency, card.getCurrency());
        assertEquals(0, new BigDecimal("1000").compareTo(card.getBalance()));
    }

    @Test
    @DisplayName("Ошибка при попытке создать валютную карту в RUB")
    void checkExceptionWithNotValidCurrency() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new CurrencyDebitCard(currencyDebitCard, Currency.RUB, new BigDecimal("100")));

        assertTrue(exception.getMessage().contains("RUB"));
    }

    @Test
    @DisplayName("Ошибка при создании валютной карты с отрицательным начальным балансом")
    void checkExceptionWithNegativeBalance() {
        assertThrows(IllegalArgumentException.class,
                () -> new CurrencyDebitCard(currencyDebitCard, Currency.USD, new BigDecimal("-50")));
    }

    @Test
    @DisplayName("Успешное пополнение и списание средств с валютной карты")
    void checkTopUpAndDebitOffSuccessfully() {
        BigDecimal initialBalance = new BigDecimal("300");
        CurrencyDebitCard card = new CurrencyDebitCard(currencyDebitCard, Currency.EUR, initialBalance);

        BigDecimal topUp = new BigDecimal("150.75");
        card.topUp(topUp);
        assertEquals(0, topUp.add(initialBalance).compareTo(card.getBalance()));

        BigDecimal debitOff = new BigDecimal("200.25");
        card.debitOff(debitOff);
        assertEquals(0, topUp.add(initialBalance).subtract(debitOff).compareTo(card.getBalance()));
    }

    @Test
    @DisplayName("Ошибка при попытке списать сумму больше остатка на валютной карте")
    void checkExceptionDebitOffMoreThanLimit() {
        CurrencyDebitCard card = new CurrencyDebitCard(currencyDebitCard, Currency.CNY, new BigDecimal("100"));

        assertThrows(IllegalStateException.class,
                () -> card.debitOff(new BigDecimal("100.01")));
    }

    @Test
    @DisplayName("Ошибка при попытке пополнить или списать нулевую или отрицательную сумму")
    void checkExceptionWithNotPositiveOperations() {
        CurrencyDebitCard card = new CurrencyDebitCard(currencyDebitCard, Currency.USD, new BigDecimal("100"));

        assertThrows(IllegalArgumentException.class, () -> card.topUp(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> card.topUp(new BigDecimal("-10")));
        assertThrows(IllegalArgumentException.class, () -> card.debitOff(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> card.debitOff(null));
    }
}