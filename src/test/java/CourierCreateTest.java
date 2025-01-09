import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest {

    private String login = "Angel";
    private String password = "1333";
    private String firstName = "Sasha";
    private String noLogin;
    private String noPassword;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Create courier with valid data")
    @Description("Positive test of creating a courier using valid data")
    public void courierCreatedWithValidDataTest() {
        CourierCreateData courierCreateData = new CourierCreateData(login, password, firstName);
        Response response = sendPostRequestToCreateCourier(courierCreateData);
        checkStatusCodeAfterCreatingCourier(response);

    }

    @Test
    @DisplayName("Error trying to create two identical couriers")
    @Description("Negative test of no opportunity creating two couriers with identical login")
    public void notPossibleToCreateTwoIdenticalCouriersTest() {
        CourierCreateData courierCreateData = new CourierCreateData(login, password, firstName);
        sendPostRequestToCreateCourier(courierCreateData);
        Response response = sendPostRequestToCreateCourier(courierCreateData);
        checkStatusCodeAfterCreatingTwoIdenticalCouriers(response);

    }

    @Test
    @DisplayName("Error trying to create a courier with login missing")
    @Description("Negative test of no opportunity creating a courier with login missing")
    public void notPossibleToCreateCourierWithLoginMissingTest() {
        CourierCreateData courierCreateData = new CourierCreateData(noLogin, password, firstName);
        Response response = sendPostRequestToCreateCourier(courierCreateData);
        checkStatusCodeAfterCreatingCourierWithLoginOrPasswordMissing(response);
    }

    @Test
    @DisplayName("Error trying to create a courier with password missing")
    @Description("Negative test of no opportunity creating a courier with password missing")
    public void notPossibleToCreateCourierWithPasswordMissingTest() {
        CourierCreateData courierCreateData = new CourierCreateData(login, noPassword, firstName);
        Response response = sendPostRequestToCreateCourier(courierCreateData);
        checkStatusCodeAfterCreatingCourierWithLoginOrPasswordMissing(response);
    }

    @Test
    @DisplayName("Error trying to create a courier with login and password missing")
    @Description("Negative test of no opportunity creating a courier with login and password missing")
    public void notPossibleToCreateCourierWithLoginAndPasswordMissingTest() {
        CourierCreateData courierCreateData = new CourierCreateData(noLogin, noPassword, firstName);
        Response response = sendPostRequestToCreateCourier(courierCreateData);
        checkStatusCodeAfterCreatingCourierWithLoginOrPasswordMissing(response);
    }

    @Step("Seng POST-request to /api/v1/courier to create a courier")
    public Response sendPostRequestToCreateCourier(CourierCreateData courierCreateData) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierCreateData)
                        .when()
                        .post("/api/v1/courier");
        return response;
    }

    @Step("Check Status code and Response text after creating a Courier")
    public void checkStatusCodeAfterCreatingCourier(Response response) {
        response.then().assertThat().body("ok", equalTo(true)).and().statusCode(201);
    }

    @Step("Check Status code and Response text after attempt creating two identical couriers")
    public void checkStatusCodeAfterCreatingTwoIdenticalCouriers(Response response) {
        response.then().assertThat().statusCode(409).and().body("message", equalTo("Этот логин уже используется"));
    }

    @Step("Check Status code and Response text after attempt creating a Courier with login or password missing")
    public void checkStatusCodeAfterCreatingCourierWithLoginOrPasswordMissing(Response response) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    @After
    public void deleteCourier() {
        CourierLoginData courierLoginData = new CourierLoginData(login, password);
        Response courierId = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLoginData)
                .post("/api/v1/courier/login");
        if (courierId.then().extract().statusCode() == 200) {
        int id = courierId.then().extract().body().path("id");
        given().header("Content-type", "application/json").delete("/api/v1/courier/" + id);}
    }

}
