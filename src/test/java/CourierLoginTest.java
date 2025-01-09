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
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

    private String login = "Angel";
    private String password = "1333";
    private String firstName = "Sasha";
    private String noLogin;
    private String noPassword;
    private String wrongLogin = "Demon";
    private String wrongPassword = "4321";

    @Before
    public void setUpAndCreateCourier() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        CourierCreateData courierCreateData = new CourierCreateData(login, password, firstName);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierCreateData)
                        .when()
                        .post("/api/v1/courier");
    }

    @Test
    @DisplayName("Login courier with valid data")
    @Description("Positive test of logging in a courier using valid data")
    public void loginWithValidDataTest() {
        CourierLoginData courierLoginData = new CourierLoginData(login, password);
        Response response = sendPostRequestToLoginCourier(courierLoginData);
        checkStatusCodeAfterCourierLogin(response);
    }

    @Test
    @DisplayName("Error trying to login in a courier with login missing")
    @Description("Negative test of failing to logging in with login missing")
    public void errorWhenLoginMissingTest() {
        CourierLoginData courierLoginData = new CourierLoginData(noLogin, password);
        Response response = sendPostRequestToLoginCourier(courierLoginData);
        checkStatusCodeAfterLoggingInCourierWithLoginOrPasswordMissing(response);
    }

    @Test
    @DisplayName("Error trying to login in a courier with password missing")
    @Description("Negative test of failing to logging in with password missing")
    public void errorWhenPasswordMissingTest() {
        CourierLoginData courierLoginData = new CourierLoginData(login, noPassword);
        Response response = sendPostRequestToLoginCourier(courierLoginData);
        checkStatusCodeAfterLoggingInCourierWithLoginOrPasswordMissing(response);
    }

    @Test
    @DisplayName("Error trying to login in a courier with login and password missing")
    @Description("Negative test of failing to logging in with login and password missing")
    public void errorWhenLoginAndPasswordMissingTest() {
        CourierLoginData courierLoginData = new CourierLoginData(noLogin, noPassword);
        Response response = sendPostRequestToLoginCourier(courierLoginData);
        checkStatusCodeAfterLoggingInCourierWithLoginOrPasswordMissing(response);
    }

    @Test
    @DisplayName("Error trying to login in a courier with wrong login")
    @Description("Negative test of failing to logging in with wrong login")
    public void errorWhenLoginWrongTest() {
        CourierLoginData courierLoginData = new CourierLoginData(wrongLogin, password);
        Response response = sendPostRequestToLoginCourier(courierLoginData);
        checkStatusCodeAfterLoggingInCourierWithWrongLoginOrPassword(response);
    }

    @Test
    @DisplayName("Error trying to login in a courier with wrong password")
    @Description("Negative test of failing to logging in with wrong password")
    public void errorWhenPasswordWrongTest() {
        CourierLoginData courierLoginData = new CourierLoginData(login, wrongPassword);
        Response response = sendPostRequestToLoginCourier(courierLoginData);
        checkStatusCodeAfterLoggingInCourierWithWrongLoginOrPassword(response);
    }

    @Test
    @DisplayName("Error trying to login in a courier with wrong login and password")
    @Description("Negative test of failing to logging in with wrong login and password")
    public void errorWhenLoginAndPasswordWrongTest() {
        CourierLoginData courierLoginData = new CourierLoginData(wrongLogin, wrongPassword);
        Response response = sendPostRequestToLoginCourier(courierLoginData);
        checkStatusCodeAfterLoggingInCourierWithWrongLoginOrPassword(response);
    }

    @Step("Seng POST-request to /api/v1/courier/login to login a courier")
    public Response sendPostRequestToLoginCourier(CourierLoginData courierLoginData) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierLoginData)
                        .when()
                        .post("/api/v1/courier/login");
        return response;
    }


    @Step("Check Status code and Response text after successful courier login")
    public void checkStatusCodeAfterCourierLogin(Response response) {
        response.then().assertThat().body("id", notNullValue()).and().statusCode(200);
    }

    @Step("Check Status code and Response text after attempt logging in a Courier with login or/and password missing")
    public void checkStatusCodeAfterLoggingInCourierWithLoginOrPasswordMissing(Response response) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);
    }

    @Step("Check Status code and Response text after attempt logging in a Courier with wrong login or/and password")
    public void checkStatusCodeAfterLoggingInCourierWithWrongLoginOrPassword(Response response) {
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
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
