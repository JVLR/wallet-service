package com.wallet.domain.model;

import com.wallet.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTest {
    @Test
    void shouldCreateUserWithWallet() {
        User user = new User("Leo", "12345678900");
        assertNotNull(user.getId());
        assertEquals("Leo", user.getName());
        assertEquals("12345678900", user.getDocument());
        assertNotNull(user.getWallet());
    }

}
