import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierApi extends BaseHttpClient {

    //Ручка создания курьера
    private final String CREATE_COURIER = "/api/v1/courier";
    //Ручка логин курьера
    private final String LOGIN_COURIER = "/api/v1/courier/login";
    //Ручка удаления курьера
    private final String DELETE_COURIER = "/api/v1/courier/";

    //Создание курьера
    @Step("Seng POST-request to /api/v1/courier to create a courier")
    public Response sendPostRequestToCreateCourier(CourierCreateData courierCreateData) {
        Response response =
                given()
                        .spec(baseRequestSpec)
                        .body(courierCreateData)
                        .when()
                        .post(CREATE_COURIER);
        return response;
    }

    //Проверка кода ответа после создания курьера
    @Step("Check Status code after creating a Courier")
    public void checkStatusCodeAfterCreatingCourier(Response response) {
        response.then().assertThat().statusCode(201);
    }

    //Проверка тела ответа после создания курьера
    @Step("Check Response text after creating a Courier")
    public void checkResponseTextAfterCreatingCourier(Response response) {
        response.then().assertThat().body("ok", equalTo(true));
    }

    //Проверка кода ответа после попытки создания двух одинаковых курьеров
    @Step("Check Status code after attempt creating two identical couriers")
    public void checkStatusCodeAfterCreatingTwoIdenticalCouriers(Response response) {
        response.then().assertThat().statusCode(409);
    }

    //Проверка тела ответа после попытки создания двух одинаковых курьеров
    @Step("Check Response text after attempt creating two identical couriers")
    public void checkResponseTextAfterCreatingTwoIdenticalCouriers(Response response) {
        response.then().assertThat().body("message", equalTo("Этот логин уже используется"));
    }

    //Проверка кода ответа после попытки создания курьера если не переданы все обязательные поля
    @Step("Check Status code after attempt creating a Courier with login or password missing")
    public void checkStatusCodeAfterCreatingCourierWithLoginOrPasswordMissing(Response response) {
        response.then().assertThat().statusCode(400);
    }

    //Проверка тела ответа после попытки создания курьера если не переданы все обязательные поля
    @Step("Check Response text after attempt creating a Courier with login or password missing")
    public void checkResponseTextAfterCreatingCourierWithLoginOrPasswordMissing(Response response) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    //Логин курьера
    @Step("Seng POST-request to /api/v1/courier/login to login a courier")
    public Response sendPostRequestToLoginCourier(CourierLoginData courierLoginData) {
        Response response =
                given()
                        .spec(baseRequestSpec)
                        .body(courierLoginData)
                        .when()
                        .post(LOGIN_COURIER);
        return response;
    }

    //Проверка кода ответа после успешного логина курьера
    @Step("Check Status code after successful courier login")
    public void checkStatusCodeAfterCourierLogin(Response response) {
        response.then().assertThat().statusCode(200);
    }

    //Проверка тела ответа после успешного логина курьера
    @Step("Check Response text after successful courier login")
    public void checkResponseTextAfterCourierLogin(Response response) {
        response.then().assertThat().body("id", notNullValue());
    }

    //Проверка кода ответа после попытки авторизации курьера если не переданы все обязательные поля
    @Step("Check Status code after attempt logging in a Courier with login or/and password missing")
    public void checkStatusCodeAfterLoggingInCourierWithLoginOrPasswordMissing(Response response) {
        response.then().assertThat().statusCode(400);
    }

    //Проверка тела ответа после попытки авторизации курьера если не переданы все обязательные поля
    @Step("Check Response text after attempt logging in a Courier with login or/and password missing")
    public void checkResponseTextAfterLoggingInCourierWithLoginOrPasswordMissing(Response response) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    //Проверка кода ответа после попытки авторизации курьера с неправильно указанным логином и/или паролем
    @Step("Check Status code after attempt logging in a Courier with wrong login or/and password")
    public void checkStatusCodeAfterLoggingInCourierWithWrongLoginOrPassword(Response response) {
        response.then().assertThat().statusCode(404);
    }

    //Проверка тела ответа после попытки авторизации курьера с неправильно указанным логином и/или паролем
    @Step("Check Response text after attempt logging in a Courier with wrong login or/and password")
    public void checkResponseTextAfterLoggingInCourierWithWrongLoginOrPassword(Response response) {
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    //Удаление курьера
    public void deleteCourier(int courierId) {
        Response response =
                given()
                        .spec(baseRequestSpec)
                        .when()
                        .delete(DELETE_COURIER + courierId);
    }

}
