package chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ServerListener implements Runnable {

	private final Socket clientSocket;
	private final BufferedReader serverReader;

	public ServerListener(Socket clientSocket, BufferedReader serverReader) {
		this.clientSocket = clientSocket;
		this.serverReader = serverReader;
	}

	@Override
	public void run() {
		try {
			String serverLine;
			while ((serverLine = serverReader.readLine()) != null) {
				System.out.println(serverLine);
			}
		} catch (IOException exception) {
			if (!clientSocket.isClosed()) {
				System.err.println("Lỗi đọc từ server: " + exception.getMessage());
			}
		}
		if (!clientSocket.isClosed()) {
			System.out.println("Server đã đóng kết nối");
			closeSocketQuietly();
		}
	}

	private void closeSocketQuietly() {
		try {
			clientSocket.close();
		} catch (IOException exception) {
			System.err.println("Lỗi đóng socket: " + exception.getMessage());
		}
	}
}
