package socket_program;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestProcessing extends Thread {
	Socket channelSocket;

	public RequestProcessing(Socket socket) {
		this.channelSocket = socket;
	}
	
	public void run() {
		try {
			OutputStream os = channelSocket.getOutputStream();
			InputStream is = channelSocket.getInputStream();
			while (true) {
				int n = is.read();
				if (n == -1) {
					break;
				}
				System.out.println((char)n);
				os.write(n);
			}
		} catch (IOException ex) {
			System.out.println("Request processing error: " + ex);
		}
	}
}
