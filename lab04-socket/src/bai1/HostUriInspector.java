package bai1;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class HostUriInspector {
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("usage: java bai1.HostUriInspector <hostname> <uri>");
			return;
		}

		try {
			InetAddress[] addresses = InetAddress.getAllByName(args[0]);
			System.out.println("Host: " + args[0]);

			for (InetAddress address : addresses) {
				System.out.println("- IP: " + address.getHostAddress());
				System.out.println(" Type: " + (address instanceof Inet4Address ? "IPv4" : "IPv6"));
				System.out.println(" Loopback: " + address.isLoopbackAddress());
				System.out.println(" Site local: " + address.isSiteLocalAddress());
			}
		} catch (UnknownHostException e) {
			System.err.println("Không phân giải được host: " + args[0]);
		}

		try {
			URI uri = new URI(args[1]);
			System.out.println("URI: " + args[1]);
			System.out.println(" Scheme: " + uri.getScheme());
			System.out.println(" Host: " + uri.getHost());
			System.out.println(" Port: " + uri.getPort());
			System.out.println(" Path: " + uri.getPath());
			System.out.println(" Query: " + uri.getQuery());
			System.out.println(" Fragment: " + uri.getFragment());
		} catch (URISyntaxException e) {
			System.err.println("URI không hợp lệ: " + e.getMessage());
		}
	}
}
