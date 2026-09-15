package socket_program;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class sTCPEchoServer {
 public final static int serverPort = 2309;
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(serverPort);
			System.out.println("Server create suceessful");
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					OutputStream os = socket.getOutputStream();
					InputStream is = socket.getInputStream();
					int ch = 0;
					while (true) {
						ch = is.read();
						if (ch == -1) {
							break;
						}
						System.out.println((char)ch);
						os.write(ch);
					}
					socket.close();
				} catch (IOException ex) {
					System.out.println("Error: Connection error | " + ex);
				}
			}
		} catch (IOException ioex) {
			System.out.println("Error: Server creation failed | " + ioex);
		}

	}

}
