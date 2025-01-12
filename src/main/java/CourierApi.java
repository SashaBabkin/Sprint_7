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

    //Проверка кода ответа и тела ответа после создания курьера
    @Step("Check Status code and Response text after creating a Courier")
    public void checkResponseAfterCreatingCourier(Response response) {
        response.then().assertThat().statusCode(201).and().body("ok", equalTo(true));
    }

    //Проверка кода ответа и тела ответа после попытки создания двух одинаковых курьеров
    @Step("Check Status code and Response text after attempt creating two identical couriers")
    public void checkResponseAfterCreatingTwoIdenticalCouriers(Response response) {
        response.then().assertThat().statusCode(409).and().body("message", equalTo("Этот логин уже используется"));
    }

    //Проверка кода ответа и тела ответа после попытки создания курьера если не переданы все обязательные поля
    @Step("Check Status code and Response text after attempt creating a Courier with login or password missing")
    public void checkResponseAfterCreatingCourierWithLoginOrPasswordMissing(Response response) {
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
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

    //Проверка кода ответа и тела ответа после успешного логина курьера
    @Step("Check Status code and Response text after successful courier login")
    public void checkResponseAfterCourierLogin(Response response) {
        response.then().assertThat().statusCode(200).and().body("id", notNullValue());
    }

    //Проверка кода ответа и тела ответа после попытки авторизации курьера если не переданы все обязательные поля
    @Step("Check Status code and Response text after attempt logging in a Courier with login or/and password missing")
    public void checkResponseAfterLoggingInCourierWithLoginOrPasswordMissing(Response response) {
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));
    }

    //Проверка кода ответа и тела ответа после попытки авторизации курьера с неправильно указанным логином и/или паролем
    @Step("Check Status code and Response text after attempt logging in a Courier with wrong login or/and password")
    public void checkResponseAfterLoggingInCourierWithWrongLoginOrPassword(Response response) {
        response.then().assertThat().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));
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
