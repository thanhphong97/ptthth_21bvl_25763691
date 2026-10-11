package bai5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

	private static final Map<String, PrintWriter> clients = new ConcurrentHashMap<>();
	private static final int PORT = 5004;
	private static final int MAX_CLIENTS = 10;

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);
		try (ServerSocket server = new ServerSocket(PORT)) {
			System.out.println("Chat server listening on port " + PORT);
			while (true) {
				Socket socket = server.accept();
				executor.submit(() -> handle(socket));
			}
		} catch (IOException e) {
			System.err.println("Không mở được server: " + e.getMessage());
		} finally {
			executor.shutdown();
		}
	}

	private static void handle(Socket socket) {
		String remote = String.valueOf(socket.getRemoteSocketAddress());
		String nick = null;
		try (socket;
				BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				PrintWriter out = new PrintWriter(
						new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {
			System.out.println("Connected: " + remote);
			nick = register(in, out);
			if (nick == null)
				return;
			broadcast("INFO " + nick + " joined", nick);

			String line;
			while ((line = in.readLine()) != null) {
				String[] parts = line.trim().split("\\s+", 2);
				switch (parts[0].toUpperCase(Locale.ROOT)) {
				case "QUIT":
					out.println("OK BYE");
					return;
				case "USERS":
					out.println("OK USERS " + String.join(",", clients.keySet()));
					break;
				case "MSG":
					String text = parts.length > 1 ? parts[1].trim() : "";
					if (text.isEmpty()) {
						out.println("ERR EMPTY_MESSAGE");
					} else {
						broadcast("MSG " + nick + ": " + text, nick);
						out.println("OK SENT");
					}
					break;
				default:
					out.println("ERR UNKNOWN_COMMAND");
				}
			}
		} catch (IOException e) {
			System.err.println("Client " + remote + " failed: " + e.getMessage());
		} finally {
			if (nick != null) {
				clients.remove(nick);
				broadcast("INFO " + nick + " left", nick);
			}
			System.out.println("Disconnected: " + remote);
		}
	}


	private static String register(BufferedReader in, PrintWriter out) throws IOException {
		out.println("INFO Nhap nickname:");
		String line;
		while ((line = in.readLine()) != null) {
			String nick = line.trim();
			if (!nick.matches("[A-Za-z0-9_-]{1,20}")) {
				out.println("ERR INVALID_NICKNAME");
			} else if (clients.putIfAbsent(nick, out) != null) {
				out.println("ERR NICKNAME_TAKEN");
			} else {
				out.println("OK WELCOME " + nick);
				return nick;
			}
		}
		return null;
	}

	private static void broadcast(String message, String exceptNick) {
		for (Map.Entry<String, PrintWriter> entry : clients.entrySet()) {
			if (!entry.getKey().equals(exceptNick)) {
				entry.getValue().println(message);
			}
		}
	}
}
