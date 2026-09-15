package socket_program;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class pTCPEchoServer {
	public final static int serverPort = 2309;

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(serverPort);
			System.out.println("Parallel Server create suceessful");
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					RequestProcessing rp = new RequestProcessing(socket);
					rp.run();
					System.out.println("-> Next Session");
				} catch (IOException ex) {
					System.out.println("Error: Connection error | " + ex);
				}
			}
		} catch (IOException ioex) {
			System.out.println("Error: Server creation failed | " + ioex);
		}

	}
}
