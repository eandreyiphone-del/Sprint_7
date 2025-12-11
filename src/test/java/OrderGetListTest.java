import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import ru.yandex.practicum.courier.Courier;
import ru.yandex.practicum.courier.CourierMethods;
import ru.yandex.practicum.orders.OrderMethods;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.yandex.practicum.orders.OrderTestData.*;

@DisplayName("Получение списка заказов: GET /api/v1/orders")
public class OrderGetListTest {

    String idCourier;

    @DisplayName("Без идентификатора курьера.")
    @Description("Получение списка заказов без идентификатора курьера. В тело ответа возвращается список всех заказов")
    @Test
    public void getOrdersListNoCourierId(){
        Response response = OrderMethods.getOrderListWithoutId();
        response.then().log().all().assertThat().statusCode(200)
                .and()
                .assertThat()
                .body("orders", notNullValue());
    }
    @DisplayName("С существующим идентификатором курьера.")
    @Description("Получение списка заказов c существующим идентификатором курьера. В тело ответа возвращается список заказов")
    @Test
    public void getOrdersListWithExistCourierId(){
        Courier courier = CourierMethods.createNewCourier();
        CourierMethods.createCourier(courier);
        idCourier = CourierMethods.getId(courier);
        Response response = OrderMethods.getOrderListWithId(idCourier);
        response.then().log().all().assertThat().statusCode(200)
                .and()
                .assertThat()
                .body("orders", notNullValue());
    }
    @DisplayName("С несуществующим идентификатором курьера.")
    @Description("Получение списка заказов c несуществующим идентификатором курьера. В тело ответа возвращается список заказов")
    @Test
    public void getOrdersListWithNotExistCourierId(){
        Response response = OrderMethods.getOrderListWithId(COURIER_NOT_EXIST_ID);
        response.then().log().all().assertThat().statusCode(404)
                .and()
                .assertThat()
                .body("message", equalTo("Курьер с идентификатором " + COURIER_NOT_EXIST_ID + " не найден"));
    }
    @DisplayName("Существующий идентификатор курьера + параметр nearestStation.")
    @Description("Получение списка заказов c существующим идентификатором курьера + параметр nearestStation. В тело ответа возвращается список заказов")
    @Test
    public void getOrdersListWithExistCourierIdAndNearestStation(){
        Courier courier = CourierMethods.createNewCourier();
        CourierMethods.createCourier(courier);
        idCourier = CourierMethods.getId(courier);
        Response response = OrderMethods.getOrderListWithIdAndStations(idCourier, NEAREST_STATIONS);
        response.then().log().all().assertThat().statusCode(200)
                .and()
                .assertThat()
                .body("orders", notNullValue());
    }

    @DisplayName("Существующий идентификатор курьера + параметры limit и page.")
    @Description("Получение списка заказов c существующим идентификатором курьера + параметры limit и page. В тело ответа возвращается список заказов")
    @Test
    public void getOrdersListWithExistCourierIdAndLimitAndPage(){
        Response response = OrderMethods.getOrdersListWithExistCourierIdAndLimitAndPage(ORDER_LIMIT, ORDER_PAGE, NEAREST_STATIONS);
        response.then().log().all().assertThat().statusCode(200)
                .and()
                .assertThat()
                .body("orders", notNullValue());
    }
    @After

    public void deleteCourier() {
        if (idCourier != null) {
            CourierMethods.deleteCourier(idCourier);
        }
    }
}
