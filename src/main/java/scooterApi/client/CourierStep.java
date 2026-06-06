package scooterApi.client;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import scooterApi.model.Courier;
import scooterApi.model.Credentials;
import static io.restassured.RestAssured.given;

public class CourierStep {
    private static final String COURIER_ROOT = "/api/v1/courier";
    private static final String COURIER_LOGIN = "/api/v1/courier/login";

    @Step("Отправка запроса на создание курьера с логином: {courier.login}")
    public Response create(Courier courier) {
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .post(COURIER_ROOT);
    }

    @Step("Отправка запроса авторизации курьера с логином: {credentials.login}")
    public Response login(Credentials credentials) {
        return given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .post(COURIER_LOGIN);
    }

    @Step("Удаление курьера с ID: {courierId}")
    public Response delete(int courierId) {
        return given()
                .delete(COURIER_ROOT + "/" + courierId);
    }
}
