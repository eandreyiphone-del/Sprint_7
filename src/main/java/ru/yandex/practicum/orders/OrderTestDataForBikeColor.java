package ru.yandex.practicum.orders;

import org.junit.runners.Parameterized;

public class OrderTestDataForBikeColor extends OrderTestData {


    //для параметризованного теста цвет самоката заказа POST /api/v1/orders
    private String[] color;

    public OrderTestDataForBikeColor(String[] color) {
        this.color=color;
    }

    @Parameterized.Parameters(name = "Test {index} Цвет самоката: {0}")
    public static Object[][]getColor(){
        return new Object[][]{
                {"Черный", new String[]{"BLACK"}},
                {"Серый", new String[]{"GREY"}},
                {"Черный+Серый", new String[]{"BLACK","GREY"}},
                {"Пусто", new String[]{""}}
        };
    }
    public Order order = new Order(FIRST_NAME,
            LAST_NAME,
            ADDRESS,
            METRO_STATION,
            PHONE,
            RENT_TIME,
            DELIVERY_DATE,
            COMMENT,
            color);
}
