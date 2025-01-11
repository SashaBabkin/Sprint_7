import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GetOrdersListTest {

    private String firstName = "Иван";
    private String lastName = "Иванов";
    private String address = "Тверская, 10";
    private String metroStation = "Тверская";
    private String phone = "+7 800 111 22 33";
    private int rentTime = 2;
    private String deliveryDate = "2025-01-20";
    private String comment = "Скорее";
    private String[] color = new String[]{"BLACK"};

    OrderApi orderApi = new OrderApi();

    @Before
    public void createOrder() {
        OrderCreationData orderCreationData = new OrderCreationData(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        orderApi.sendPostRequestToCreateOrder(orderCreationData);
    }

    @Test
    @DisplayName("Getting orders list")
    @Description("Positive test of getting all created orders")
    public void getOrdersListTest() {
        Response response = orderApi.sendGetRequestToGetOrdersList();
        orderApi.checkStatusCodeOfGettingOrdersList(response);
        orderApi.checkResponseTextAfterGettingOrdersList(response);
    }

    @After
    public void deleteOrder() {
        orderApi.deleteOrder();
    }


}
