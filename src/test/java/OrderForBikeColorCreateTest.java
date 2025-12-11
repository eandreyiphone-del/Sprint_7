import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.orders.Order;
import ru.yandex.practicum.orders.OrderMethods;
import ru.yandex.practicum.orders.OrderTestDataForBikeColor;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
@DisplayName("Создание заказа /цвета самоката/: POST /api/v1/orders")
public class OrderForBikeColorCreateTest {

    private String[] color;

    public OrderForBikeColorCreateTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Test {index}: Bike color(s) - {0}")
    public static Iterable<Object[]> getColors() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}, // пустой массив значит без указания цвета
        });
    }

    private String track;

    @Test
    @DisplayName("Создание заказа с разными вариантами цвета")
    @Description("Создание заказа с одним цветом, двумя цветами и без указания цвета")
    public void createOrder() {
        Order order = new Order(
                "Иван",
                "Иванов",
                "г.Москва, ул.Пушкина, д.10",
                "Краснопресненская",
                "+79161234567",
                1,
                "2024-04-15",
                "Домофон сломан, звоните соседям.",
                color
        );
        Response response = OrderMethods.createOrder(order);
        track = response.then().extract().path("track").toString();
        response.then().log().all().assertThat().statusCode(201)
                .and()
                .assertThat()
                .body("track", notNullValue()); // проверяем, что номер заказа присутствует
    }

    @After
    public void cleanUp() {
        if (track != null) {
            OrderMethods.cancelOrder(track);
        }
    }
}