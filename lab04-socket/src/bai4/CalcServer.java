package bai4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CalcServer {
	private static final int PORT = 5003;

	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(PORT)) {
			System.out.println("Calc server listening on port " + PORT);
			while (true) {
				try (Socket socket = server.accept()) {
					serve(socket);
				} catch (IOException e) {
					System.err.println("Lỗi phiên client: " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.err.println("Không mở được server: " + e.getMessage());
		}
	}

	public static void serve(Socket socket) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {
			String request;
			while ((request = in.readLine()) != null) {
				String response = process(request);
				out.println(response);
				if (request.trim().equalsIgnoreCase("QUIT"))
					break;
			}

		}
	}

	private static String process(String request) {
		String trimmed = request.trim();
		if (trimmed.equalsIgnoreCase("QUIT"))
			return "OK BYE";
		try {
			String[] parts = trimmed.split("\\s+");
			if (parts.length != 4) {
				return "ERR INVALID_FORMAT";
			}

			if (!parts[0].equalsIgnoreCase("CALC")) {
				return "ERR INVALID_FORMAT";
			}

			String op = parts[1];
			if (!op.matches("[+\\-*/]")) {
				return "ERR UNSUPPORTED_OPERATOR";
			}

			String operator = parts[1];
			var a = Long.parseLong(parts[2]);
			var b = Long.parseLong(parts[3]);
			Long result;
			switch (operator) {
				case "+":
					result = a + b;
					break;
				case "-":
					result = a - b;
					break;
				case "*":
					result = a * b;
					break;
				case "/":
					if (b == 0) {
						return "ERR DIVIDE_BY_ZERO";
					}
					result = a / b;
					break;
				default:
					return "ERR UNKNOWN_OPERATOR";
			}
			return "OK " + result;
		} catch (NumberFormatException e) {
			return "ERR INVALID_NUMBER";
		}
	}

}
