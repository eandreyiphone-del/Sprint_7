import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import ru.yandex.practicum.courier.Courier;
import ru.yandex.practicum.courier.CourierMethods;
import ru.yandex.practicum.orders.Order;
import ru.yandex.practicum.orders.OrderMethods;
import ru.yandex.practicum.orders.OrderSerializeOrderParam;
import ru.yandex.practicum.orders.OrderTestData;

import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Принять заказ: PUT /api/v1/orders/accept/:id")
public class OrderGetTest {

    String track;
    String courierId;
    String orderId;

    @Test
    @DisplayName("Принять существующий заказ от существующего курьера")
    @Description("Создается заказ, создается курьер, получается заказ по номеру, выделяется id ответа, принимается заказ с id курьера и id заказа - проверка кода ответа 200")
    public void acceptExistingOrderByExistingCourier() {
        Order order = OrderMethods.createNewOrder();
        Response response = OrderMethods.createOrder(order);
        //запоминаем номер трека
        track = response.then().log().all().extract().path("track").toString();
        //Получить заказ по его номеру
        response = OrderMethods.getOrderByTrack(track);
        response.then().log().all();
        //Десериализовать объект
        OrderSerializeOrderParam orderSerializeOrderParam = response.body().as(OrderSerializeOrderParam.class);
        order = orderSerializeOrderParam.getOrder();
        //Достать номер id из ответа
        orderId = String.valueOf(order.getId());
        //создаем курьера, получаем его id
        Courier courier = CourierMethods.createNewCourier();
        CourierMethods.createCourier(courier);
        courierId = CourierMethods.getId(courier);
        response = OrderMethods.getOrder(orderId, courierId);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and()
                .assertThat()
                .body("ok", equalTo(true));
    }
    @Test
    @DisplayName("Принять заказ с несуществующим номером от существующего курьера")
    @Description("Создается заказ, номер заказа меняется на несуществующий, создается курьер, принимается заказ - проверка кода ответа 404")
    public void acceptNotExistingOrderByExistingCourier() {
        Order order = OrderMethods.createNewOrder();
        Response response = OrderMethods.createOrder(order);
        //запоминаем номер трека
        track = response.then().log().all().extract().path("track").toString();
        OrderMethods.getOrderByTrack(track);
        //меняем номер трека на несуществующий
        String track2 = track + 101;
        //создаем курьера, получаем его id
        Courier courier = CourierMethods.createNewCourier();
        CourierMethods.createCourier(courier);
        // авторизуем его и получаем его Id
        courierId = CourierMethods.getId(courier);
        response = OrderMethods.getOrder(track2, courierId);
        response.then().log().all().assertThat().statusCode(404)
                .and()
                .assertThat()
                .body("message", equalTo("Заказа с таким id не существует"));
    }
    @Test
    @DisplayName("Принять заказ с существующим номером от несуществующего курьера")
    @Description("Создается заказ, номер заказа меняется на несуществующий, создается курьер, принимается заказ - проверка кода ответа 404")
    public void acceptExistingOrderByNotExistingCourier() {
        Order order = OrderMethods.createNewOrder();
        Response response = OrderMethods.createOrder(order);
        //запоминаем номер трека
        track = response.then().log().all().extract().path("track").toString();
        //Получить заказ по его номеру
        response = OrderMethods.getOrderByTrack(track);
        response.then().log().all();
        //Десериализовать объект
        OrderSerializeOrderParam orderSerializeOrderParam = response.body().as(OrderSerializeOrderParam.class);
        order = orderSerializeOrderParam.getOrder();
        //Достать номер id из ответа
        orderId = String.valueOf(order.getId());
        //создаем курьера, получаем его id
        Courier courier = CourierMethods.createNewCourier();
        CourierMethods.createCourier(courier);
        //запоминаем номер курьера
        courierId = CourierMethods.getId(courier);
        String id2 = courierId + 100;
        response = OrderMethods.getOrder(track,id2);
        response.then().log().all().assertThat().statusCode(404)
                .and()
                .assertThat()
                .body("message", equalTo("Курьера с таким id не существует"));
    }
    @Test
    @DisplayName("Заказ уже был в работе")
    @Description("Создается заказ, создается курьер, принимается заказ, повторно принимается заказ с тем же номером - проверка кода ответа 409")
    public void acceptExistingDuplicatedOrderByExistingCourier() {
        Order order = OrderMethods.createNewOrder();
        Response response = OrderMethods.createOrder(order);
//запоминаем номер трека
        track = response.then().log().all().extract().path("track").toString();
        //Получить заказ по его номеру
        response = OrderMethods.getOrderByTrack(track);
        response.then().log().all();
        //Десериализовать объект
        OrderSerializeOrderParam orderSerializeOrderParam = response.body().as(OrderSerializeOrderParam.class);
        order = orderSerializeOrderParam.getOrder();
        //Достать номер id из ответа
        orderId = String.valueOf(order.getId());
        //создаем курьера, получаем его id
        Courier courier = CourierMethods.createNewCourier();
        CourierMethods.createCourier(courier);
        //запоминаем номер курьера
        courierId = CourierMethods.getId(courier);
        response = OrderMethods.getOrder(orderId, courierId);
        response.then().log().all().assertThat().statusCode(200).and().assertThat().body("ok", equalTo(true));
        response = OrderMethods.getOrder(orderId, courierId);
        response.then().log().all().assertThat().statusCode(409)
                .and()
                .assertThat()
                .body("message", equalTo("Этот заказ уже в работе"));
    }
    @After

    public void deleteCourier() {
        if (courierId != null) {
            CourierMethods.deleteCourier(courierId);
        }
        if (track != null) {
            OrderMethods.cancelOrder(track);
        }
    }
}
