package ru.yandex.practicum.orders;

import io.qameta.allure.Step;

public class OrderSerializeOrderParam {
    private  Order order;
    @Step("Десериализация, Выделение Id заказа из ответа")
    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
}
