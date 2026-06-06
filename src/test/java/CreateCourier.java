import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scooterApi.Const;
import scooterApi.client.CourierStep;
import scooterApi.model.Courier;
import scooterApi.model.Credentials;
import scooterApi.Const;

import java.util.UUID;
import static org.hamcrest.Matchers.is;

@DisplayName("Тесты ручки 'Создать курьера'")
public class CreateCourier extends Const {
    private CourierStep courierClient;
    private String login;
    private String password;
    private String firstName;
    private int courierId;

    @BeforeEach
    public void setupData() {
        courierClient = new CourierStep();
        login = "courier_" + UUID.randomUUID().toString().substring(0, 8);
        password = "pass_" + UUID.randomUUID().toString().substring(0, 8);
        firstName = "name_" + UUID.randomUUID().toString().substring(0, 4);
    }

    @AfterEach
    public void deleteCourier() {
        if (courierId > 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Успешное создание курьера со всеми обязательными полями")
    public void courierCanBeCreated() {
        Courier courier = new Courier(login, password, firstName);

        Response response = courierClient.create(courier);
        response.then().statusCode(201).body("ok", is(true));

        // Авторизуемся, чтобы забрать ID для AfterEach очистки
        Response loginResp = courierClient.login(new Credentials(login, password));
        if(loginResp.statusCode() == 200) {
            courierId = loginResp.path("id");
        }
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void cannotCreateDuplicateCouriers() {
        Courier courier = new Courier(login, password, firstName);

        courierClient.create(courier);

        Response duplicateResponse = courierClient.create(courier);
        duplicateResponse.then().statusCode(409)
                .body("message", is("Этот логин уже используется. Попробуйте другой."));

        Response loginResp = courierClient.login(new Credentials(login, password));
        if(loginResp.statusCode() == 200) { courierId = loginResp.path("id"); }
    }

    @Test
    @DisplayName("Ошибка создания курьера без логина")
    public void createCourierWithoutLoginReturnsError() {
        Courier courier = new Courier(null, password, firstName);

        Response response = courierClient.create(courier);
        response.then().statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Ошибка создания курьера без пароля")
    public void createCourierWithoutPasswordReturnsError() {
        Courier courier = new Courier(login, null, firstName);

        Response response = courierClient.create(courier);
        response.then().statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }
}
