package com.example.demo.conrollertest;
import com.example.demo.InjectTest;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class CartTest {
    private CartController cartController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        InjectTest.injectObjects(cartController, "userRepository", userRepo);
        InjectTest.injectObjects(cartController, "cartRepository", cartRepo);
        InjectTest.injectObjects(cartController, "itemRepository", itemRepo);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("nawfal");
        user.setPassword("nawfalpassword");
        user.setCart(cart);
        when(userRepo.findByUsername("nawfal")).thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("mobile");
        BigDecimal price = BigDecimal.valueOf(100);
        item.setPrice(price);
        item.setDescription("64 GB");
        when(itemRepo.findById(1L)).thenReturn(java.util.Optional.of(item));

    }

    @Test
    public void addToCart() {
        ModifyCartRequest add = new ModifyCartRequest();
        add.setItemId(1L);
        add.setQuantity(1);
        add.setUsername("nawfal");
        ResponseEntity<Cart> response = cartController.addTocart(add);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart c = response.getBody();
        assertNotNull(c);
        assertEquals(BigDecimal.valueOf(100), c.getTotal());

    }

    @Test
    public void addInvalidUser() {
        ModifyCartRequest add = new ModifyCartRequest();
        add.setItemId(1L);
        add.setQuantity(1);
        add.setUsername("Invalid");
        ResponseEntity<Cart> response = cartController.addTocart(add);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addInvalidItem() {
        ModifyCartRequest add = new ModifyCartRequest();
        add.setItemId(2L);
        add.setQuantity(1);
        add.setUsername("invalid");
        ResponseEntity<Cart> response = cartController.addTocart(add);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCart() {
        // Set up test by adding two items to cart.
        ModifyCartRequest add = new ModifyCartRequest();
        add.setItemId(1L);
        add.setQuantity(2);
        add.setUsername("nawfal");
        ResponseEntity<Cart> response = cartController.addTocart(add);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        add = new ModifyCartRequest();
        add.setItemId(1L);
        add.setQuantity(1);
        add.setUsername("nawfal");
        response = cartController.removeFromcart(add);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart c = response.getBody();
        assertNotNull(c);
        assertEquals(BigDecimal.valueOf(100), c.getTotal());

    }

    @Test
    public void removeInvalidUser() {
        ModifyCartRequest add = new ModifyCartRequest();
        add.setItemId(1L);
        add.setQuantity(1);
        add.setUsername("invalid");
        ResponseEntity<Cart> response = cartController.removeFromcart(add);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeInvalidItem() {
        ModifyCartRequest add = new ModifyCartRequest();
        add.setItemId(2L);
        add.setQuantity(1);
        add.setUsername("invalid");
        ResponseEntity<Cart> response = cartController.removeFromcart(add);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
