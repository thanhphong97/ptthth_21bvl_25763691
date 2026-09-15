package socket_program;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class sTCPEchoClient {
	public final static String serverIP = "localhost";
	public final static int serverPort = 2309;
	

	public static void main(String[] args) throws InterruptedException, IOException {
		Socket socket = null;
		try {
			socket = new Socket(serverIP, serverPort);
			System.out.println("Client create successful");
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			for (int i = '0'; i < '9'; i++) {
				os.write(i);
				int ch = is.read();
				System.out.println((char)ch);
				Thread.sleep(2000);
			}
		} catch (IOException ex) {
			System.out.println("Error: Can not create socket | " + ex);
		}finally {
			if (socket != null) {
				socket.close();
			}
		}

	}

}
