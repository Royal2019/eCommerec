package com.example.demo.conrollertest;
import com.example.demo.InjectTest;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class OrderTest {
    private OrderController orderController;
    private OrderRepository orderRepo = mock(OrderRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        InjectTest.injectObjects(orderController, "orderRepository", orderRepo);
        InjectTest.injectObjects(orderController, "userRepository", userRepo);

        Item item = new Item();
        item.setId(1L);
        item.setName("mobile");
        BigDecimal price = BigDecimal.valueOf(100);
        item.setPrice(price);
        item.setDescription("64 GB");
        List<Item> items = new ArrayList<Item>();
        items.add(item);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("nawfal");
        user.setPassword("nawfalpassword");
        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(items);
        BigDecimal total = BigDecimal.valueOf(100);
        cart.setTotal(total);
        user.setCart(cart);
        when(userRepo.findByUsername("nawfal")).thenReturn(user);
        when(userRepo.findByUsername("null")).thenReturn(null);

    }

    @Test
    public void submitOrder() {
        ResponseEntity<UserOrder> response = orderController.submit("nawfal");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(1, order.getItems().size());
    }

    @Test
    public void submitOrderUserNotExist() {
        ResponseEntity<UserOrder> response = orderController.submit("null");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getUserOrders() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("nawfal");
        assertNotNull(ordersForUser);
        assertEquals(200, ordersForUser.getStatusCodeValue());
        List<UserOrder> orders = ordersForUser.getBody();
        assertNotNull(orders);

    }

    @Test
    public void getUserOrdersNotExist() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("null");
        assertNotNull(ordersForUser);
        assertEquals(404, ordersForUser.getStatusCodeValue());

    }
}
