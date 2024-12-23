package com.alexstk.gallery.controller;

import com.alexstk.gallery.GalleryApplicationTests;
import com.alexstk.gallery.dto.UserDto;
import com.alexstk.gallery.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIT extends GalleryApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.createTable();
    }

    @AfterEach
    public void cleanUp() {
        userRepository.deleteTable();
    }

    @Test
    public void shouldCreateUser_whenPostMethodCalled_givenUserDto() throws Exception {
        //given
        UserDto givenUserDto = new UserDto(1, "admin", "qwerty", "1");

        //when
        performPostRequest("http://localhost:8080/user", givenUserDto, null, status().is2xxSuccessful());

        //then
        Assertions.assertThat(userRepository.findById(1)).isNotNull();
    }
}
