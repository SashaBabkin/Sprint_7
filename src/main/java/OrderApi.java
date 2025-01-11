import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderApi extends BaseHttpClient{

    private int trackNumber;

    //Ручка создания заказа
    private final String CREATE_ORDER = "/api/v1/orders";
    //Ручка получения списка заказов
    private final String GET_ORDER_LIST = "/api/v1/orders";
    //Ручка удаления заказа
    private final String DELETE_ORDER = "/api/v1/orders/cancel";

    //Создание заказа
    @Step("Seng POST-request to /api/v1/orders to create an Order")
    public Response sendPostRequestToCreateOrder(OrderCreationData orderCreationData) {
        Response response =
                given()
                        .spec(baseRequestSpec)
                        .body(orderCreationData)
                        .when()
                        .post(CREATE_ORDER);
        return response;
    }

    //Проверка кода ответа при успешном создании заказа
    @Step("Check Status code after creation an Order")
    public void checkStatusCodeAfterCreationOrder(Response response) {
        response.then().assertThat().statusCode(201);
    }

    //Проверка, что тело ответа содержит track после успешного создания заказа
    @Step("Check that Response body contains track number")
    public void checkOrderTrack(Response response) {
        response.then().assertThat().body("track", notNullValue());
        int trackNumber = response.then().extract().body().path("track");
        this.trackNumber = trackNumber;
    }

    //Отправка запроса на получение списка заказов
    @Step("Seng GET-request to /api/v1/orders to get the Orders list")
    public Response sendGetRequestToGetOrdersList() {
        Response response =
                given()
                        .spec(baseRequestSpec)
                        .when()
                        .get(GET_ORDER_LIST);
        return response;
    }

    //Проверка кода ответа после запроса на получение списка заказов
    @Step("Check Status code and Response text after getting orders list")
    public void checkStatusCodeOfGettingOrdersList(Response response) {
        response.then().assertThat().statusCode(200);
    }

    //Проверка, что тело ответа возвращает список заказов после запроса на получение списка заказов
    @Step("Check Response text after getting orders list")
    public void checkResponseTextAfterGettingOrdersList(Response response) {
        response.then().assertThat().body("orders", notNullValue());
    }

    //Удаление заказа
    public void deleteOrder() {
        given().spec(baseRequestSpec).put(DELETE_ORDER+ "?track=" + trackNumber);
    }

}
