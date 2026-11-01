package bai2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DigitServer {
	private static final int PORT = 5001;
	private static final String[] WORDS = { "không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín" };

	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(PORT)) {
			System.out.println("TCP digit server listening on port " + PORT);
			while (true) {
				try (Socket socket = server.accept()) {
					sever(socket);
				} catch (IOException e) {
					System.err.println("Lỗi phiên client: " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.err.println("Không mở được server: " + e.getMessage());
		}

	}

	public static void sever(Socket socket) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {
			String request;
			while ((request = in.readLine()) != null) {
				String response = process(request);
				out.println(response);
				if (request.equalsIgnoreCase("QUIT"))
					break;
			}

		}
	}

	static String process(String request) {
		if (request.equalsIgnoreCase("QUIT"))
			return "OK BYE";
		if (request.length() == 1 && request.charAt(0) >= '0' && request.charAt(0) <= '9') {
			return "OK " + WORDS[request.charAt(0) - '0'];
		}
		return "ERR INVALID_DIGIT";
	}
}
