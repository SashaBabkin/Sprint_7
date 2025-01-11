import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

public class CourierCreateTest {

    private String login = "Angel";
    private String password = "1333";
    private String firstName = "Sasha";
    private String noLogin;
    private String noPassword;

    CourierApi courierApi = new CourierApi();

    @Test
    @DisplayName("Create courier with valid data")
    @Description("Positive test of creating a courier using valid data")
    public void courierCreatedWithValidDataTest() {
        CourierCreateData courierCreateData = new CourierCreateData(login, password, firstName);
        Response response = courierApi.sendPostRequestToCreateCourier(courierCreateData);
        courierApi.checkStatusCodeAfterCreatingCourier(response);
        courierApi.checkResponseTextAfterCreatingCourier(response);

    }

    @Test
    @DisplayName("Error trying to create two identical couriers")
    @Description("Negative test of no opportunity creating two couriers with identical login")
    public void notPossibleToCreateTwoIdenticalCouriersTest() {
        CourierCreateData courierCreateData = new CourierCreateData(login, password, firstName);
        courierApi.sendPostRequestToCreateCourier(courierCreateData);
        Response response = courierApi.sendPostRequestToCreateCourier(courierCreateData);
        courierApi.checkStatusCodeAfterCreatingTwoIdenticalCouriers(response);
        courierApi.checkResponseTextAfterCreatingTwoIdenticalCouriers(response);

    }

    @Test
    @DisplayName("Error trying to create a courier with login missing")
    @Description("Negative test of no opportunity creating a courier with login missing")
    public void notPossibleToCreateCourierWithLoginMissingTest() {
        CourierCreateData courierCreateData = new CourierCreateData(noLogin, password, firstName);
        Response response = courierApi.sendPostRequestToCreateCourier(courierCreateData);
        courierApi.checkStatusCodeAfterCreatingCourierWithLoginOrPasswordMissing(response);
        courierApi.checkResponseTextAfterCreatingCourierWithLoginOrPasswordMissing(response);
    }

    @Test
    @DisplayName("Error trying to create a courier with password missing")
    @Description("Negative test of no opportunity creating a courier with password missing")
    public void notPossibleToCreateCourierWithPasswordMissingTest() {
        CourierCreateData courierCreateData = new CourierCreateData(login, noPassword, firstName);
        Response response = courierApi.sendPostRequestToCreateCourier(courierCreateData);
        courierApi.checkStatusCodeAfterCreatingCourierWithLoginOrPasswordMissing(response);
        courierApi.checkResponseTextAfterCreatingCourierWithLoginOrPasswordMissing(response);
    }

    @Test
    @DisplayName("Error trying to create a courier with login and password missing")
    @Description("Negative test of no opportunity creating a courier with login and password missing")
    public void notPossibleToCreateCourierWithLoginAndPasswordMissingTest() {
        CourierCreateData courierCreateData = new CourierCreateData(noLogin, noPassword, firstName);
        Response response = courierApi.sendPostRequestToCreateCourier(courierCreateData);
        courierApi.checkStatusCodeAfterCreatingCourierWithLoginOrPasswordMissing(response);
        courierApi.checkResponseTextAfterCreatingCourierWithLoginOrPasswordMissing(response);
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
