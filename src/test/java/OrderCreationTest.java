import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTest {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;


    public OrderCreationTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][] {
                {"Иван", "Иванов", "Тверская, 10", "Тверская", "+7 800 111 22 33", 2, "2025-01-20", "Скорее", new String[]{"BLACK"}},
                {"Иван", "Иванов", "Тверская, 10", "Тверская", "+7 800 111 22 33", 2, "2025-01-20", "Скорее", new String[]{"BLACK", "GREY"}},
                {"Иван", "Иванов", "Тверская, 10", "Тверская", "+7 800 111 22 33", 2, "2025-01-20", "Скорее", new String[]{"GREY"}},
                {"Иван", "Иванов", "Тверская, 10", "Тверская", "+7 800 111 22 33", 2, "2025-01-20", "Скорее", new String[]{}}
        };
    }
    @Test
    @DisplayName("Order creation")
    @Description("Positive test of creating an order with different scooter color")
    public void possibleToMakeOrderTest() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        OrderCreationData orderCreationData = new OrderCreationData(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = sendPostRequestToCreateOrder(orderCreationData);
        checkStatusCodeAfterCreationOrder(response);

    }

    @Step("Seng POST-request to /api/v1/orders to create an Order")
    public Response sendPostRequestToCreateOrder(OrderCreationData orderCreationData) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(orderCreationData)
                        .when()
                        .post("/api/v1/orders");
        return response;
    }

    @Step("Check Status code and Response text after creation an Order")
    public void checkStatusCodeAfterCreationOrder(Response response) {
        response.then().assertThat().body("track", notNullValue()).and().statusCode(201);
    }

}
