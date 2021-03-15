package com.mortonstudios.fictionalmatch.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameActionsTest {

    @Autowired
    private GameActions gameActionsController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(gameActionsController).isNotNull();
    }

}