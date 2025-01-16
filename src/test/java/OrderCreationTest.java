import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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

    OrderApi orderApi = new OrderApi();


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
        OrderCreationData orderCreationData = new OrderCreationData(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        Response response = orderApi.sendPostRequestToCreateOrder(orderCreationData);
        orderApi.checkResponseAfterCreationOrder(response);

    }

    @After
    public void deleteOrder() {
        orderApi.deleteOrder();
    }

}
