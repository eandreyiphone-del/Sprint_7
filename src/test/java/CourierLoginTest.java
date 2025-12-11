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
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Логин курьера в системе: POST  /api/v1/courier/login")
public class CourierLoginTest {

    private String idCourier;

    @Before
    public void setup() {
        // Создаем нового курьера перед каждым тестом
        Courier courier = CourierMethods.createNewCourier();
        Response response = CourierMethods.createCourier(courier);
        idCourier = CourierMethods.getId(courier);
    }

    @Test
    @DisplayName("Успешная авторизация")
    @Description("Авторизация курьера с зарегистрированным e-mail и корректным паролем - ответ 200, возвращает id")
    public void loginCourierSuccess() {
        Courier courier = CourierMethods.createNewCourier(); // обновляем только логин и пароль
        courier.setFirstName(null); // очищаем ненужные поля
        Response response = CourierMethods.loginCourier(courier);
        response.then().log().all().assertThat().statusCode(200)
                .and().body("id", equalTo(idCourier)); // сравниваем с id нашего тестового курьера
    }

    @Test
    @DisplayName("Авторизация без логина")
    @Description("Авторизация курьера без указания логина и корректным паролем - ответ 400, возвращает ошибку message")
    public void loginCourierWithoutLogin() {
        Courier courier = CourierMethods.createNewCourier();
        courier.setLogin(""); // устанавливаем пустое значение логина
        Response response = CourierMethods.loginCourier(courier);
        response.then().log().all().assertThat().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Авторизация курьера без указания пароля и корректным логином - ответ 400, возвращает ошибку message")
    public void loginCourierWithoutPassword() {
        Courier courier = CourierMethods.createNewCourier();
        courier.setPassword(""); // устанавливаем пустое значение пароля
        Response response = CourierMethods.loginCourier(courier);
        response.then().log().all().assertThat().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация c несуществующим логином")
    @Description("Авторизация курьера с некорректным логином и корректным паролем - ответ 404, возвращает ошибку message")
    public void loginCourierWithNotExistingLogin() {
        Courier courier = CourierMethods.createNewCourier();
        courier.setLogin("invalid_login");
        Response response = CourierMethods.loginCourier(courier);
        response.then().log().all().assertThat().statusCode(404)
                .and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация c несуществующим паролем")
    @Description("Авторизация курьера с корректным логином и некорректным паролем - ответ 404, возвращает ошибку message")
    public void loginCourierWithNotExistingPassword() {
        Courier courier = CourierMethods.createNewCourier();
        courier.setPassword("invalid_password");
        Response response = CourierMethods.loginCourier(courier);
        response.then().log().all().assertThat().statusCode(404)
                .and().body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void cleanup() {
        // Удаляем курьера после окончания каждого теста
        if (idCourier != null && !idCourier.isEmpty()) {
            CourierMethods.deleteCourier(idCourier);
        }
    }
}