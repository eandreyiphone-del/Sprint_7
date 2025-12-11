package ru.yandex.practicum.constants;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class UrlApi {
    public static final String MAIN_URL = "https://qa-scooter.praktikum-services.ru";
    public static final String COURIER = "api/v1/courier/";
    public static final String LOGIN = "api/v1/courier/login/";
    public static final String ORDER = "api/v1/orders";
    public static final String ORDER_ACCEPT = "api/v1/orders/accept/";
    public static final String ORDER_BY_TRACK = "api/v1/orders/track";
    public static final String CANCEL_ORDER = "api/v1/orders/cancel";

    //указываем, что нам надо иметь в спецификации URL и Content-Type Json.
    public static RequestSpecification spec() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(MAIN_URL)
                .log()
                .all();

    }
}
