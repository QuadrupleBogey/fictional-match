package com.mortonstudios.fictionalmatch.controllers;

import com.mortonstudios.fictionalmatch.multiplayer.entities.Player;
import com.mortonstudios.fictionalmatch.multiplayer.entities.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SharedAnchorsIntegrationTest {

    @LocalServerPort
    private Integer port;

    private WebSocketStompClient socketStompClient;

    @BeforeEach
    public void setup() {
        this.socketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))
        ));
    }

    private String getWsPath() {
        return String.format("ws://localhost:%d/stomp", port);
    }

    @Test
    void addingNewUser() throws InterruptedException, ExecutionException, TimeoutException {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue(1);
        this.socketStompClient.setMessageConverter(new StringMessageConverter());

        StompSession session = this.socketStompClient
                .connect(this.getWsPath(), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/topics/newPlayers", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders stompHeaders) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                System.out.println("Received message: " + o);
                blockingQueue.add((String) o);
            }
        });

        Player actual =  new Player("test", new ArrayList<Unit>(), 20, 20, 0);

        session.send("/newPlayer", actual.toString());

        assertThat(blockingQueue.poll(1, TimeUnit.SECONDS)).isEqualTo(actual.toString());
    }

    @Test
    public void gettingAllGameInformation() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        this.socketStompClient.setMessageConverter(new StringMessageConverter());

        StompSession session = this.socketStompClient
                .connect(this.getWsPath(), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/all", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                latch.countDown();
            }
        });

        if (!latch.await(1, TimeUnit.SECONDS)) {
            fail("Message not received");
        }
    }
}