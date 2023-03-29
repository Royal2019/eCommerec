package com.example.demo.conrollertest;
import com.example.demo.InjectTest;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class UserTest {
    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController(null, null, null);
        InjectTest.injectObjects(userController, "userRepository", userRepo);
        InjectTest.injectObjects(userController, "cartRepository", cartRepo);
        InjectTest.injectObjects(userController, "bCryptPasswordEncoder", encoder);
        User user = new User();
        Cart cart = new Cart();
        user.setId(0);
        user.setUsername("nawfal");
        user.setPassword("nawfalpassword");
        user.setCart(cart);
        when(userRepo.findByUsername("nawfal")).thenReturn(user);
        when(userRepo.findById(0L)).thenReturn(java.util.Optional.of(user));
        when(userRepo.findByUsername("null")).thenReturn(null);

    }

    @Test
    public void createUser() {
        when(encoder.encode("nawfalpassword")).thenReturn("encode");
        CreateUserRequest add = new CreateUserRequest();
        add.setUsername("nawfal");
        add.setPassword("nawfalpassword");
        add.setConfirmPassword("nawfalpassword");
        final ResponseEntity<User> response = userController.createUser(add);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("nawfal", u.getUsername());
        assertEquals("encode", u.getPassword());

    }

    @Test
    public void createUserShortPassword() {
        CreateUserRequest add = new CreateUserRequest();
        add.setUsername("nawfal");
        add.setPassword("short");
        add.setConfirmPassword("short");
        final ResponseEntity<User> response = userController.createUser(add);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void createUserPasswordMismatch() {
        CreateUserRequest add = new CreateUserRequest();
        add.setUsername("nawfal");
        add.setPassword("nawfalpassword");
        add.setConfirmPassword("awfalPassword");
        final ResponseEntity<User> response = userController.createUser(add);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findUser() {
        final ResponseEntity<User> response = userController.findByUserName("nawfal");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals("nawfal", u.getUsername());
    }

    @Test
    public void findUserDoesNotExist() {
        final ResponseEntity<User> response = userController.findByUserName("null");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findUserById() {
        final ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());;
    }

    @Test
    public void findUserByIdDoesNotExist() {
        final ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
