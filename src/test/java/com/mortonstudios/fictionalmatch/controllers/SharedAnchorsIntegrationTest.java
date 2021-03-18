package com.mortonstudios.fictionalmatch.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mortonstudios.fictionalmatch.multiplayer.entities.Player;
import com.mortonstudios.fictionalmatch.multiplayer.entities.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        BlockingQueue<Map<String, Player>> blockingQueue = new ArrayBlockingQueue(1);
        this.socketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = this.socketStompClient
                .connect(this.getWsPath(), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/topics/all", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                System.out.println("Received: " + headers.toString());
                return HashMap.class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                System.out.println("Received message: " + o);
                blockingQueue.add((Map<String, Player>) o);
            }
        });


        Player player = new Player("test", new HashMap<>(), 20, 20, 0);

        session.send("/app/join-as/player", player);
        session.send("/app/join-as/pallytwo", player);

        assertThat(blockingQueue.poll(10, TimeUnit.SECONDS).size()).isEqualTo(2);
    }

    @Test
    public void gettingAllGameInformation() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        this.socketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session = this.socketStompClient
                .connect(this.getWsPath(), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/topics/all", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                System.out.println("Received: " + headers.toString());
                return HashMap.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                latch.countDown();
            }
        });

        Player actual =  new Player("test", new HashMap<>(), 20, 20, 0);
        session.send("/app/join-as/player", actual);

        session.send("/app/join-as/player", actual);

        if (!latch.await(10, TimeUnit.SECONDS)) {
            fail("Message not received");
        }
    }

    @Test
    public void editingAnAnchor() throws Exception {
        this.socketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        BlockingQueue<Map<String, Player>> blockingQueue = new ArrayBlockingQueue(1);

        StompSession session = this.socketStompClient
                .connect(this.getWsPath(), new StompSessionHandlerAdapter() {})
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/topics/all", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                System.out.println("Received: " + headers.toString());
                return HashMap.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("Received message: " + payload);
                blockingQueue.add((Map<String, Player>) payload);
            }
        });

        Map<String, Unit>army = new HashMap<>();
        army.put("tank", new Unit.Builder().build());

        Player player =  new Player("test", army, 20, 20, 0);
        session.send("/app/join-as/player", player);
        session.send("/app/player/update/tank", new Unit.Builder().givePointValue(12).giveUnitAName("test").wPosition(2).build());

        ObjectMapper mapper = new ObjectMapper();

        Player actual = mapper.convertValue(blockingQueue.poll(10, TimeUnit.SECONDS).get("player"), new TypeReference<Player>() {});

        assertThat(actual.getArmy().get("tank").getName())
                .isEqualTo("test");
    }
}