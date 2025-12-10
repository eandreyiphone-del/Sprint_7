import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.orders.OrderMethods;
import ru.yandex.practicum.orders.OrderTestDataForBikeColor;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
@DisplayName("Создание заказа /цвета самоката/: POST /api/v1/orders")
public class OrderForBikeColorCreateTest extends OrderTestDataForBikeColor {

    public OrderForBikeColorCreateTest(String name, String[] color) {
        super(color);
    }
    String track;
    @Test
    //@DisplayName("Создание заказа") - убрано для красоты в отчетах
    //@Description("Заказ можно создать с указанием только одного цвета, обоих цветов, либо без их указания в принципе")
    public void createOrder() {
        Response response = OrderMethods.createOrder(order);
        //запоминаем номер трека, чтобы удалить его потом
        track = response.then().extract().path("track").toString();
        //проверка кода ответа и что в теле вернулся параметр track и он не пустой
        response.then().log().all().assertThat().statusCode(201)
                .and()
                .assertThat()
                .body("track", notNullValue());
    }

    //удаляем тестовые заказы - хоть ручка и не работает, вычитала, что надо подчищать за собой, дабы не засорять БД
    @After
    public void deleteTestOrder() {
        if (track != null) {
            OrderMethods.cancelOrder(track);
            //это проверка, как она действительно работает - искала причину падений - расхождения с требованиями
            //response.then().log().all().assertThat().statusCode(200);
        }
    }

}
