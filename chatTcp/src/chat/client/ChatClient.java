package chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import chat.common.Protocol;

public class ChatClient {

	private static final long LISTENER_FINISH_WAIT_MILLIS = 500;

	public static void main(String[] args) {
		try {
			String serverHost = args.length > 0 ? args[0] : Protocol.DEFAULT_HOST;
			int serverPort = args.length > 1 ? Integer.parseInt(args[1]) : Protocol.DEFAULT_PORT;
			runChatSession(serverHost, serverPort);
		} catch (NumberFormatException exception) {
			System.err.println("Port phải là số nguyên");
		} catch (IllegalArgumentException exception) {
			System.err.println("Port phải trong khoảng 0-65535");
		} catch (UnknownHostException exception) {
			System.err.println("Không tìm thấy máy chủ: " + exception.getMessage());
		} catch (IOException exception) {
			System.err.println("Lỗi kết nối: " + exception.getMessage());
		} catch (InterruptedException exception) {
			Thread.currentThread().interrupt();
		}
	}

	private static void runChatSession(String serverHost, int serverPort) throws IOException, InterruptedException {
		try (ExecutorService serverListenerExecutor = Executors.newSingleThreadExecutor();
				Socket clientSocket = new Socket(serverHost, serverPort);
				BufferedReader consoleReader = new BufferedReader(
						new InputStreamReader(System.in, StandardCharsets.UTF_8));
				BufferedReader serverReader = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
				PrintWriter serverWriter = new PrintWriter(
						new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true)) {
			Future<?> serverListenerTask = serverListenerExecutor.submit(new ServerListener(clientSocket, serverReader));

			String consoleInput;
			while (!clientSocket.isClosed() && (consoleInput = consoleReader.readLine()) != null) {
				serverWriter.println(consoleInput);
				if (consoleInput.strip().equalsIgnoreCase(Protocol.COMMAND_EXIT)) {
					break;
				}
			}
			waitBrieflyForListener(serverListenerTask);
			clientSocket.close();
		}
	}

	private static void waitBrieflyForListener(Future<?> serverListenerTask) throws InterruptedException {
		try {
			serverListenerTask.get(LISTENER_FINISH_WAIT_MILLIS, TimeUnit.MILLISECONDS);
		} catch (TimeoutException exception) {
			serverListenerTask.cancel(true);
		} catch (ExecutionException exception) {
			System.err.println("Lỗi luồng nhận: " + exception.getCause());
		}
	}
}
