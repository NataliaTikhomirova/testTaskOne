package products.interfaces;

import java.math.BigDecimal;

//Интерфейс для Кредитной карты
public interface Creditable {
    BigDecimal getDebt();
    BigDecimal getProcentRate();
}
