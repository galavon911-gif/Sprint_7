import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import scooterApi.Const;
import scooterApi.client.OrderStep;
import scooterApi.model.Order;
import java.util.List;
import java.util.stream.Stream;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Тесты ручки 'Создать заказ'")
public class CreateOrder extends Const {
    private OrderStep orderClient;

    @BeforeEach
    public void initClient() {
        orderClient = new OrderStep();
    }

    static Stream<List<String>> colorVariants() {
        return Stream.of(
                List.of("BLACK"),
                List.of("GREY"),
                List.of("BLACK", "GREY"),
                List.of()
        );
    }

    @ParameterizedTest(name = "Тест создания заказа с набором цветов: {0}")
    @MethodSource("colorVariants")
    @DisplayName("Создание заказа с различными комбинациями цветов")
    public void createOrderWithVaryingColors(List<String> colors) {
        Order order = new Order("Иван", "Иванов", "Ленина, 25", "11",
                "+79998887766", 3, "2026-07-15", "У шлагбаума", colors);

        Response response = orderClient.create(order);
        response.then().statusCode(201).body("track", notNullValue());
    }
}