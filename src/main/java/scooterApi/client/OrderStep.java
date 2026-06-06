package scooterApi.client;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import scooterApi.model.Order;
import static io.restassured.RestAssured.given;

public class OrderStep {
    private static final String ORDERS_ROOT = "/api/v1/orders";

    @Step("Отправка запроса на создание заказа с выбором цветов: {order.color}")
    public Response create(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .post(ORDERS_ROOT);
    }

    @Step("Отправка запроса на получение списка заказов")
    public Response getList() {
        return given()
                .get(ORDERS_ROOT);
    }
}