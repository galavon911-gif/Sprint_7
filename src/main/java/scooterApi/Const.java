package scooterApi;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.jupiter.api.BeforeAll;

public class Const {
    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://qa-scooter.education-services.ru";
        // Настройка автоматического логирования запросов и ответов в отчет Allure
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }
}