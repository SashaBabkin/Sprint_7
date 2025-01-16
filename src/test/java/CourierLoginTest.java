import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CourierLoginTest {

    private String login = "Angel";
    private String password = "1333";
    private String firstName = "Sasha";
    private String noLogin;
    private String noPassword;
    private String wrongLogin = "Demon";
    private String wrongPassword = "4321";

    CourierApi courierApi = new CourierApi();

    @Before
    public void createCourier() {
        CourierCreateData courierCreateData = new CourierCreateData(login, password, firstName);
        courierApi.sendPostRequestToCreateCourier(courierCreateData);
    }

    @Test
    @DisplayName("Login courier with valid data")
    @Description("Positive test of logging in a courier using valid data")
    public void loginWithValidDataTest() {
        CourierLoginData courierLoginData = new CourierLoginData(login, password);
        Response response = courierApi.sendPostRequestToLoginCourier(courierLoginData);
        courierApi.checkResponseAfterCourierLogin(response);
    }

    @Test
    @DisplayName("Error trying to login in a courier with login missing")
    @Description("Negative test of failing to logging in with login missing")
    public void errorWhenLoginMissingTest() {
        CourierLoginData courierLoginData = new CourierLoginData(noLogin, password);
        Response response = courierApi.sendPostRequestToLoginCourier(courierLoginData);
        courierApi.checkResponseAfterLoggingInCourierWithLoginOrPasswordMissing(response);
    }

    @Test
    @DisplayName("Error trying to login in a courier with password missing")
    @Description("Negative test of failing to logging in with password missing")
    public void errorWhenPasswordMissingTest() {
        CourierLoginData courierLoginData = new CourierLoginData(login, noPassword);
        Response response = courierApi.sendPostRequestToLoginCourier(courierLoginData);
        courierApi.checkResponseAfterLoggingInCourierWithLoginOrPasswordMissing(response);
    }

    @Test
    @DisplayName("Error trying to login in a courier with login and password missing")
    @Description("Negative test of failing to logging in with login and password missing")
    public void errorWhenLoginAndPasswordMissingTest() {
        CourierLoginData courierLoginData = new CourierLoginData(noLogin, noPassword);
        Response response = courierApi.sendPostRequestToLoginCourier(courierLoginData);
        courierApi.checkResponseAfterLoggingInCourierWithLoginOrPasswordMissing(response);
    }

    @Test
    @DisplayName("Error trying to login in a courier with wrong login")
    @Description("Negative test of failing to logging in with wrong login")
    public void errorWhenLoginWrongTest() {
        CourierLoginData courierLoginData = new CourierLoginData(wrongLogin, password);
        Response response = courierApi.sendPostRequestToLoginCourier(courierLoginData);
        courierApi.checkResponseAfterLoggingInCourierWithWrongLoginOrPassword(response);
    }

    @Test
    @DisplayName("Error trying to login in a courier with wrong password")
    @Description("Negative test of failing to logging in with wrong password")
    public void errorWhenPasswordWrongTest() {
        CourierLoginData courierLoginData = new CourierLoginData(login, wrongPassword);
        Response response = courierApi.sendPostRequestToLoginCourier(courierLoginData);
        courierApi.checkResponseAfterLoggingInCourierWithWrongLoginOrPassword(response);
    }

    @Test
    @DisplayName("Error trying to login in a courier with wrong login and password")
    @Description("Negative test of failing to logging in with wrong login and password")
    public void errorWhenLoginAndPasswordWrongTest() {
        CourierLoginData courierLoginData = new CourierLoginData(wrongLogin, wrongPassword);
        Response response = courierApi.sendPostRequestToLoginCourier(courierLoginData);
        courierApi.checkResponseAfterLoggingInCourierWithWrongLoginOrPassword(response);
    }


    @After
    public void deleteCourier() {
        CourierLoginData courierLoginData = new CourierLoginData(login, password);
        Response response = courierApi.sendPostRequestToLoginCourier(courierLoginData);
        if (response.then().extract().statusCode() == 200) {
            int id = response.then().extract().body().path("id");
            courierApi.deleteCourier(id);
        }

    }
}
