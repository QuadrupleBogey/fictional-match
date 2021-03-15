package com.mortonstudios.fictionalmatch.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SharedAnchorsTest {

    @Autowired
    private SharedAnchors sharedAnchorsController;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(sharedAnchorsController).isNotNull();
    }

    @Test
    void getAllPlayersPositions() {

    }

    @Test
    void newPlayer() {
    }

    @Test
    void editAnchor() {
    }

    @Test
    void deleteEmployee() {
    }
}