package bai3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUdpServer {
	private static final int PORT = 5003;
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MM yyyy");
	private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH mm ss");

	public static void main(String[] args) {
		byte[] buffer = new byte[4096];
		try (DatagramSocket socket = new DatagramSocket(PORT)) {
			System.out.println("UDP server listening on port " + PORT);
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);
				String message = new String(request.getData(), request.getOffset(), request.getLength(),
						StandardCharsets.UTF_8);
				String text = process(message);
				byte[] responseData = text.getBytes(StandardCharsets.UTF_8);
				DatagramPacket response = new DatagramPacket(responseData, responseData.length,
						request.getAddress(), request.getPort());
				socket.send(response);
			}
		} catch (IOException e) {
			System.err.println("UDP server error: " + e.getMessage());
		}
	}

	static String process(String request) {
		String trimmed = request.trim();
		LocalDateTime now = LocalDateTime.now();
		if (trimmed.equalsIgnoreCase("DATE"))
			return "OK " + now.format(DATE_FORMAT);
		if (trimmed.equalsIgnoreCase("TIME"))
			return "OK " + now.format(TIME_FORMAT);
		if (trimmed.equalsIgnoreCase("DATETIME"))
			return "OK " + now.format(DATE_FORMAT) + " " + now.format(TIME_FORMAT);
		return "ERR UNKNOWN_COMMAND";
	}
}
