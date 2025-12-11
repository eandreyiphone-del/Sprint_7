import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.courier.Courier;
import ru.yandex.practicum.courier.CourierMethods;
import ru.yandex.practicum.courier.CourierTestData;

import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Удаление курьера: DELETE /api/v1/courier/:id")
public class CourierDeleteTest {

    private String idCourier;

    @Before
    public void setup() {
        // Создаем нового курьера перед каждым тестом
        Courier courier = CourierMethods.createNewCourier();
        Response response = CourierMethods.createCourier(courier);
        idCourier = CourierMethods.getId(courier);
    }

    @Test
    @DisplayName("Создание курьера и успешное его удаление")
    @Description("Успешное создание курьера, получение его id и успешное удаление курьера")
    public void deleteSuccessCourier() {
        Response response = CourierMethods.deleteCourier(idCourier);
        response.then().log().all().assertThat().statusCode(200)
                .and().body("ok", equalTo(true));
        // Проверяем, что курьер удалён
        response = CourierMethods.loginCourier(new Courier("", "", ""));
        response.then().log().all().assertThat().statusCode(404)
                .and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Удаление курьера c id - пустое значение")
    @Description("Параметр id имеет пустое значение - ожидание 400 ошибки")
    public void deleteCourierWithoutId() {
        idCourier = "";
        Response response = CourierMethods.deleteCourier(idCourier);
        response.then().log().all().assertThat().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Удаление курьера c id не передан")
    @Description("Параметр id не передан - ожидание 400 ошибки")
    public void deleteCourierWithIdNull() {
        Response response = CourierMethods.deleteCourierWithoutId();
        response.then().log().all().assertThat().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Test
    @DisplayName("Удаление курьера c id=несуществующим в базе")
    @Description("Успешное создание курьера, получение его id и успешное удаление курьера")
    public void deleteCourierWithIdNotExists() {
        idCourier = "nonexistent_id"; // несуществующий id
        Response response = CourierMethods.deleteCourier(idCourier);
        response.then().assertThat().statusCode(404)
                .and().body("message", equalTo("Курьера с таким id нет."));
    }

    @After
    public void cleanup() {
        // Удаляем курьера после окончания каждого теста
        if (idCourier != null && !idCourier.isEmpty()) {
            CourierMethods.deleteCourier(idCourier);
        }
    }
}