package ru.yandex.practicum.orders;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static ru.yandex.practicum.constants.UrlApi.*;

public class OrderMethods extends OrderTestData{
    public static Order createNewOrder() {
        return new Order(FIRST_NAME,LAST_NAME,ADDRESS,METRO_STATION,PHONE,RENT_TIME,DELIVERY_DATE,COMMENT,COLOR);
    }
    @Step("Создание заказа")
    public static Response createOrder(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDER);
    }
    @Step("Отмена заказа")
    public static Response cancelOrder(String trackId) {
        return spec()
                .queryParams("track",trackId)
                .when()
                .put(CANCEL_ORDER);
    }
    @Step("Получение списка заказов без Id курьера")
    public static Response getOrderListWithoutId() {
        return spec()
                .when()
                .get(ORDER);
    }
    @Step("Получение списка заказов c идентификатором курьера")
    public static Response getOrderListWithId(String Id) {
        return spec()
                .when()
                .queryParams("courierId", Id)
                .get(ORDER);
    }
    @Step("Получение списка заказов c идентификатором курьера и станциями метро")
    public static Response getOrderListWithIdAndStations(String Id, String[] stations) {
        return spec()
                .when()
                .queryParam("courierId", Id)
                .queryParams("nearestStation", stations)
                .get(ORDER);
    }

    @Step("Получение списка заказов c идентификатором курьера и станциями метро")
    public static Response getOrdersListWithExistCourierIdAndLimitAndPage(Integer limit, Integer page, String[] stations) {
        return spec()
                .when()
                .queryParam("limit", limit)
                .queryParam("page", page)
                .queryParams("nearestStation", stations)
                .get(ORDER);
    }

    @Step("Принять заказ курьером")
    public static Response getOrder(String orderId, String courierId) {
        return spec()
                .when()
                .queryParams("courierId",courierId)
                .put(ORDER_ACCEPT+orderId);
    }
    @Step("Получить заказ по номеру")
    public static Response getOrderByTrack(String track) {
        return spec()
                .when()
                .queryParams("t",track)
                .get(ORDER_BY_TRACK);
    }
    @Step("Получить заказ по номеру без номера трека")
    public static Response getOrderByTrackWithoutTrack() {
        return spec()
                .when()
                .get(ORDER_BY_TRACK);
    }
}
