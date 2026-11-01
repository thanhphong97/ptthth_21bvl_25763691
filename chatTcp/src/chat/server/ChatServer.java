package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import chat.common.Protocol;

public class ChatServer {

	public static final int MAX_CONCURRENT_CLIENTS = 20;

	private final int listeningPort;
	private final RoomManager roomManager = new RoomManager();

	public ChatServer(int listeningPort) {
		this.listeningPort = listeningPort;
	}

	public void start() throws IOException {
		ExecutorService clientSessionPool = Executors.newFixedThreadPool(MAX_CONCURRENT_CLIENTS);
		try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {
			System.out.println("Chat server đang lắng nghe cổng " + listeningPort);
			while (true) {
				Socket clientSocket = serverSocket.accept();
				clientSessionPool.execute(new ClientHandler(clientSocket, roomManager));
			}
		} finally {
			clientSessionPool.shutdown();
		}
	}

	public static void main(String[] args) {
		try {
			int listeningPort = args.length > 0 ? Integer.parseInt(args[0]) : Protocol.DEFAULT_PORT;
			new ChatServer(listeningPort).start();
		} catch (NumberFormatException exception) {
			System.err.println("Port phải là số nguyên");
		} catch (IllegalArgumentException exception) {
			System.err.println("Port phải trong khoảng 0-65535");
		} catch (IOException exception) {
			System.err.println("Không mở được server: " + exception.getMessage());
		}
	}
}
