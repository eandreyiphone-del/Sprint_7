import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import ru.yandex.practicum.orders.Order;
import ru.yandex.practicum.orders.OrderMethods;
import ru.yandex.practicum.orders.OrderTestData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Принять заказ: PUT /api/v1/orders/accept/:id")
public class OrderGetTrackTest extends OrderTestData {
    String track;

    @Test
    @DisplayName("Запрос с существующим номером - 200 возвращается объект заказа")
    @Description("Создается заказ - проверка кода ответа 200 и что в теле есть объект order")
    public void getExistingOrderTest() {
        Order order = OrderMethods.createNewOrder();
        Response response = OrderMethods.createOrder(order);
        //запоминаем номер трека
        track = response.then().extract().path("track").toString();
        // вызываем эндпоинт GET /api/v1/orders/track
        response = OrderMethods.getOrderByTrack(track);
        response.then()
                .assertThat().statusCode(200)
                .and()
                .assertThat()
                .body("order", notNullValue());
    }
    @Test
    @DisplayName("Запрос без номера заказа  - 400 возвращается ошибка")
    @Description("Создается заказ - проверка кода ответа 200 и что в теле есть объект order")
    public void getWithoutOrderTest() {
        // вызываем эндпоинт GET /api/v1/orders/track
        Response response = OrderMethods.getOrderByTrackWithoutTrack();
        response.then()
                .assertThat().statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }
    @Test
    @DisplayName("Запрос c несуществующим номером заказа  - 404 возвращается ошибка")
    @Description("Создается заказ, меняется номер на несуществующий - проверка кода ответа 404 и что в теле есть сообщение об ошибке")
    public void getNotExistingOrderTest() {
        Order order = OrderMethods.createNewOrder();
        Response response = OrderMethods.createOrder(order);
        //запоминаем номер трека, чтоб удалить его потом
        track = response.then().extract().path("track").toString();
        //меняем номер заказа на несуществующий
        String track2 = track + 100;
        // вызываем эндпоинт GET /api/v1/orders/track
        response = OrderMethods.getOrderByTrack(track2);
        response.then()
                .assertThat().statusCode(404)
                .and()
                .assertThat()
                .body("message", equalTo("Заказ не найден"));
    }

    @After

    public void deleteCourier() {
        if (track != null) {
            OrderMethods.cancelOrder(track);
        }
    }

}
