import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import scooterApi.Const;
import scooterApi.client.OrderStep;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты ручки 'Получить список заказов'")
public class GetOrderList extends Const {

    @Test
    @DisplayName("Запрос списка возвращает тело, содержащее массив заказов")
    public void getOrderListReturnsFilledArray() {
        OrderStep orderClient = new OrderStep();

        Response response = orderClient.getList();
        response.then().statusCode(200)
                .body("orders", is(not(empty())));
    }
}