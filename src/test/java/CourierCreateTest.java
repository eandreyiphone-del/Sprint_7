import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import ru.yandex.practicum.courier.Courier;
import ru.yandex.practicum.courier.CourierMethods;

import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Создание курьера: POST /api/v1/courier")
public class CourierCreateTest {
    String idCourier;

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Создание курьера со всеми заполненными полями")
    public void createSuccessCourier() {
        Courier courier = CourierMethods.createNewCourier();
        Response response = CourierMethods.createCourier(courier);
        idCourier = CourierMethods.getId(courier);
        response.then().log().all().assertThat().statusCode(201).
                and().body("ok", equalTo(true));
    }
    @Test
    @DisplayName("Создание курьера с данными уже существующими в БД")
    @Description("Создание второго курьера со существующими данными - ожидание 409 ошибки")
    public void createCourierDouble() {
        Courier courier = CourierMethods.createNewCourier();
        //первый создан
        CourierMethods.createCourier(courier);
        idCourier = CourierMethods.getId(courier);
        //второй такой же
        Response response = CourierMethods.createCourier(courier);
        response.then().log().all().assertThat().statusCode(409).
                and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Создание курьера без логина - ожидание 400 ошибки")
    public void createCourierWithLoginNull() {
        Courier courier = CourierMethods.createNewCourier();
        courier.setLogin(null);
        Response response = CourierMethods.createCourier(courier);
        idCourier = CourierMethods.getId(courier);
        response.then().assertThat().statusCode(400).
                and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создание курьера логина - пустое значение")
    @Description("Создание курьера c логин - пустое значение - ожидание 400 ошибки")
    public void createCourierWithoutLogin() {
        Courier courier = CourierMethods.createNewCourier();
        courier.setLogin("");
        Response response = CourierMethods.createCourier(courier);
        idCourier = CourierMethods.getId(courier);
        response.then().log().all().assertThat().statusCode(400).
                and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Создание курьера без пароля - ожидание 400 ошибки")
    public void createCourierWithPasswordNull() {
        Courier courier = CourierMethods.createNewCourier();
        courier.setPassword(null);
        Response response = CourierMethods.createCourier(courier);
        idCourier = CourierMethods.getId(courier);
        response.then().log().all().assertThat().statusCode(400).
                and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создание курьера пароль - пустое поле")
    @Description("Создание курьера пароль - пустое поле - ожидание 400 ошибки")
    public void createCourierWithoutPassword() {
        Courier courier = CourierMethods.createNewCourier();
        courier.setPassword("");
        Response response = CourierMethods.createCourier(courier);
        idCourier = CourierMethods.getId(courier);
        response.then().log().all().assertThat().statusCode(400).
                and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создание курьера без имени")
    @Description("Создание курьера без имени - ожидание 400 ошибки")
    public void createCourierWithNameNull() {
        Courier courier = CourierMethods.createNewCourier();
        courier.setFirstName(null);
        Response response = CourierMethods.createCourier(courier);
        idCourier = CourierMethods.getId(courier);
        response.then().log().all().assertThat().statusCode(400).
                and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Создание курьера имя - пустое поле")
    @Description("Создание курьера имя - пустое поле - ожидание 400 ошибки")
    public void createCourierWithoutName() {
        Courier courier = CourierMethods.createNewCourier();
        courier.setFirstName("");
        Response response = CourierMethods.createCourier(courier);
        idCourier = CourierMethods.getId(courier);
        response.then().log().all().assertThat().statusCode(400).
                and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера - в запросе передан пустой JSON")
    @Description("Создание курьера без имени - ожидание 400 ошибки")
    public void createCourierWithoutParameters() {
        Courier courier = CourierMethods.createNewCourier();
        courier.setLogin(null);
        courier.setPassword(null);
        courier.setFirstName(null);
        Response response = CourierMethods.createCourier(courier);
        idCourier = CourierMethods.getId(courier);
        response.then().log().all().assertThat().statusCode(400).
                and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After

    public void deleteCourier() {
        if (idCourier !=null) {
            CourierMethods.deleteCourier(idCourier);
        }
    }
}
