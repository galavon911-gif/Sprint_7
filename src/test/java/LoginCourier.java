import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scooterApi.Const;
import scooterApi.client.CourierStep;
import scooterApi.model.Courier;
import scooterApi.model.Credentials;
import java.util.UUID;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Тесты ручки 'Логин курьера'")
public class LoginCourier extends Const {
    private CourierStep courierClient;
    private String login;
    private String password;
    private int courierId;

    @BeforeEach
    public void createCourierBeforeLogin() {
        courierClient = new CourierStep();
        login = "login_" + UUID.randomUUID().toString().substring(0, 8);
        password = "pass_" + UUID.randomUUID().toString().substring(0, 8);
        String name = "name_" + UUID.randomUUID().toString().substring(0, 4);

        Courier courier = new Courier(login, password, name);
        courierClient.create(courier);
    }

    @AfterEach
    public void deleteCourier() {
        if (courierId > 0) {
            courierClient.delete(courierId);
        } else {
            // Если тест проверял негативные кейсы авторизации, получаем реальный ID для очистки СУБД
            Response loginResp = courierClient.login(new Credentials(login, password));
            if (loginResp.statusCode() == 200) {
                courierClient.delete(loginResp.path("id"));
            }
        }
    }

    @Test
    @DisplayName("Успешная авторизация курьера со всеми обязательными полями")
    public void courierCanLoginSuccessfully() {
        Credentials credentials = new Credentials(login, password);

        Response response = courierClient.login(credentials);
        response.then().statusCode(200).body("id", notNullValue());

        courierId = response.path("id");
    }

    @Test
    @DisplayName("Ошибка авторизации без логина")
    public void loginWithoutLoginFieldError() {
        Credentials credentials = new Credentials(null, password);

        Response response = courierClient.login(credentials);
        response.then().statusCode(400).body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка авторизации без логина")
    public void loginWithoutPasswordFieldError() {
        Credentials credentials = new Credentials(null, password);

        Response response = courierClient.login(credentials);
        response.then().statusCode(400).body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка авторизации с неверным паролем")
    public void loginWithWrongPasswordError() {
        Credentials credentials = new Credentials(login, "incorrect_password_123");

        Response response = courierClient.login(credentials);
        response.then().statusCode(404).body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка авторизации c несуществующим курьером")
    public void loginNonExistentCourierError() {
        Credentials credentials = new Credentials("non_existent_user_xyz", password);

        Response response = courierClient.login(credentials);
        response.then().statusCode(404).body("message", is("Учетная запись не найдена"));
    }
}

