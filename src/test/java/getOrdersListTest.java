import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class getOrdersListTest {

    @Test
    @DisplayName("Getting orders list")
    @Description("Positive test of getting all created orders")
    public void getOrdersListTest() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        Response response = sendGetRequestToGetOrdersList();
        checkStatusCodeOfGettingOrdersList(response);
    }

    @Step("Seng GET-request to /api/v1/orders to get the Orders list")
    public Response sendGetRequestToGetOrdersList() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/v1/orders");
        return response;
    }

    @Step("Check Status code and Response text after getting oreders list")
    public void checkStatusCodeOfGettingOrdersList(Response response) {
        response.then().assertThat().body("orders", notNullValue()).and().statusCode(200);
    }


}
