package de.mortensenit.wsclient;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

@ClientEndpoint
public class WebSocketClient {

	private static CountDownLatch latch;

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Client: Connected to server");
		try {
			session.getBasicRemote().sendText("Hello, Server!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnMessage
	public void onMessage(String message) {
		System.out.println("Client: Received message from server: " + message);
		latch.countDown();
	}

	public static void main(String[] args) throws Exception {
		latch = new CountDownLatch(1);
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(WebSocketClient.class, URI.create("ws://localhost:8080/websocket"));
		latch.await(5, TimeUnit.SECONDS);
	}
}
