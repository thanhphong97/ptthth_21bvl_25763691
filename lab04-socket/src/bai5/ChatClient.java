package bai5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClient {

	public static void main(String[] args) {
		String host = args.length > 0 ? args[0] : "localhost";
		int port = args.length > 1 ? Integer.parseInt(args[1]) : 5004;

		try (Socket socket = new Socket(host, port);
				BufferedReader console = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
				BufferedReader in = new BufferedReader(
						new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				PrintWriter out = new PrintWriter(
						new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {
			System.out.println("Connected. Enter nickname, then USERS, MSG <text>, QUIT");

			Thread listener = new Thread(() -> {
				try {
					String line;
					while ((line = in.readLine()) != null) {
						System.out.println(line);
					}
				} catch (IOException e) {

				}
				System.out.println("Server closed the connection");
			});
			listener.setDaemon(true);
			listener.start();

			String line;
			while ((line = console.readLine()) != null) {
				out.println(line);
				if (line.trim().equalsIgnoreCase("QUIT"))
					break;
			}
			listener.join(500); 
		} catch (NumberFormatException e) {
			System.err.println("Port phải là số nguyên");
		} catch (IOException e) {
			System.err.println("Lỗi kết nối: " + e.getMessage());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
