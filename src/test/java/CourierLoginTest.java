import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import ru.yandex.practicum.courier.Courier;
import ru.yandex.practicum.courier.CourierMethods;
import ru.yandex.practicum.courier.CourierTestData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Логин курьера в системе: POST  /api/v1/courier/login")
public class CourierLoginTest {
    String idCourier;

    @Test
    @DisplayName("Успешная авторизация")
    @Description("Авторизация курьера с зарегистрированным e-mail и корректным паролем - ответ 200, возвращает id")
    public void loginCourierSuccess() {
        Courier courier = CourierMethods.createNewCourier();
        //сначала создаем курьера
        CourierMethods.createCourier(courier);
        //убираем из параметров курьера Имя
        courier.setFirstName(null);
        //регистрируем id, чтобы потом удалить
        idCourier = CourierMethods.getId(courier);
        //авторизуем курьера с данными только логин и пароль
        Response response = CourierMethods.loginCourier(courier);
        //проверяем ответ и что параметр id в ответе есть и имеет значение
        response.then().log().all().assertThat().statusCode(200).
                and().body("id", notNullValue());
    }
    @Test
    @DisplayName("Авторизация без логина")
    @Description("Авторизация курьера без указания логина и корректным паролем - ответ 400, возвращает ошибку message")
    public void loginCourierWithoutLogin() {
        Courier courier = CourierMethods.createNewCourier();
        //сначала создаем курьера
        CourierMethods.createCourier(courier);
        //убираем из параметров курьера Имя
        courier.setFirstName(null);
        //регистрируем id, чтобы потом удалить
        idCourier = CourierMethods.getId(courier);
        //значение параметра Логин - пустое значение
        courier.setLogin("");
        //авторизуем курьера с этими данными
        Response response = CourierMethods.loginCourier(courier);
        //проверяем ответ и текст сообщения об ошибке
        response.then().log().all().assertThat().statusCode(400).
                and().body("message", equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Авторизация курьера без указания пароля и корректным логином - ответ 400, возвращает ошибку message")
    public void loginCourierWithoutPassword() {
        Courier courier = CourierMethods.createNewCourier();
        //сначала создаем курьера
        CourierMethods.createCourier(courier);
        //убираем из параметров курьера Имя
        courier.setFirstName(null);
        //регистрируем id, чтобы потом удалить
        idCourier = CourierMethods.getId(courier);
        //значение параметра Пароль - пустое значение
        courier.setPassword("");
        //авторизуем курьера с этими данными
        Response response = CourierMethods.loginCourier(courier);
        //проверяем ответ и текст сообщения об ошибке
        response.then().log().all().assertThat().statusCode(400).
                and().body("message", equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Авторизация оба поля пустые")
    @Description("Авторизация курьера без указания пароля и корректным логином - ответ 400, возвращает ошибку message")
    public void loginCourierWithoutLoginAndPassword() {
        Courier courier = CourierMethods.createNewCourier();
        //сначала создаем курьера
        CourierMethods.createCourier(courier);
        //убираем из параметров курьера Имя
        courier.setFirstName(null);
        //регистрируем id, чтобы потом удалить
        idCourier = CourierMethods.getId(courier);
        //значение параметров Логин и Пароль - пустое значение
        courier.setLogin("");
        courier.setPassword("");
        //авторизуем курьера с этими данными
        Response response = CourierMethods.loginCourier(courier);
        //проверяем ответ и текст сообщения об ошибке
        response.then().log().all().assertThat().statusCode(400).
                and().body("message", equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Авторизация c несуществующим логином")
    @Description("Авторизация курьера с некорректным логином и корректным паролем - ответ 404, возвращает ошибку message")
    public void loginCourierWithNotExistingLogin() {
        Courier courier = CourierMethods.createNewCourier();
        //сначала создаем курьера
        CourierMethods.createCourier(courier);
        //убираем из параметров курьера Имя
        courier.setFirstName(null);
        //регистрируем id, чтобы потом удалить
        idCourier = CourierMethods.getId(courier);
        //значение параметра Логин меняем на несуществующее (определено в CourierTestData - могут быть ограничения на длину)
        courier.setLogin(CourierTestData.NOT_EXIST_LOGIN);
        //авторизуем курьера с этими данными
        Response response = CourierMethods.loginCourier(courier);
        //проверяем ответ и текст сообщения об ошибке
        response.then().log().all().assertThat().statusCode(404).
                and().body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("Авторизация c несуществующим паролем")
    @Description("Авторизация курьера с корректным логином и некорректным паролем - ответ 404, возвращает ошибку message")
    public void loginCourierWithNotExistingPassword() {
        Courier courier = CourierMethods.createNewCourier();
        //сначала создаем курьера
        CourierMethods.createCourier(courier);
        //убираем из параметров курьера Имя
        courier.setFirstName(null);
        //регистрируем id, чтобы потом удалить
        idCourier = CourierMethods.getId(courier);
        //значение параметра Пароль меняем на несуществующее (определено в CourierTestData - могут быть ограничения на длину)
        courier.setPassword(CourierTestData.NO_EXIST_PASSWORD);
        //авторизуем курьера с этими данными
        Response response = CourierMethods.loginCourier(courier);
        //проверяем ответ и текст сообщения об ошибке
        response.then().log().all().assertThat().statusCode(404).
                and().body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("Авторизация c несуществующими логином и паролем")
    @Description("Авторизация курьера с некорректным логином и некорректным паролем - ответ 404, возвращает ошибку message")
    public void loginCourierWithNotExistingData() {
        Courier courier = CourierMethods.createNewCourier();
        //сначала создаем курьера
        CourierMethods.createCourier(courier);
        //убираем из параметров курьера Имя
        courier.setFirstName(null);
        //регистрируем id, чтобы потом удалить
        idCourier = CourierMethods.getId(courier);
        //значение параметры Логин и Пароль меняем на несуществующие (определено в CourierTestData - могут быть ограничения на длину)
        courier.setLogin(CourierTestData.NOT_EXIST_LOGIN);
        courier.setPassword(CourierTestData.NO_EXIST_PASSWORD);
        //авторизуем курьера с этими данными
        Response response = CourierMethods.loginCourier(courier);
        //проверяем ответ и текст сообщения об ошибке
        response.then().assertThat().statusCode(404).
                and().body("message", equalTo("Учетная запись не найдена"));
    }

    @After

    public void cleaningCourier() {
        if (idCourier != null) {
            CourierMethods.deleteCourier(idCourier);
        }
    }
}
