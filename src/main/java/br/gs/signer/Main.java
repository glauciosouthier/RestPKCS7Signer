package br.gs.signer;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {
	private static int port = 8080;

	public static void main(String[] args) throws Exception {
		loadParams(args);
		URI baseUri = getURI();
		ResourceConfig config = new ResourceConfig();
		config.packages(true, "br.gs.signer");
		config.packages(true, "br.gs.signer.api");

		config.register(JsonMoxyConfigurationContextResolver.class);
		config.register(FilesEntry.class);
		// Closeable server = SimpleContainerFactory.create(getURI(), config);
		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri,
				config);

		try {
			System.out.println("--Press Enter to STOP the server--");
			System.in.read();
		} finally {
			// server.stop(0);
			// server.close();
			server.shutdown();
			System.out.println("Server stoped!");
		}

	}

	private static void loadParams(String[] args) {
		if (args == null || args.length == 0)
			return;

		
		for (int i=0;i<args.length;i++) {
			switch (args[i]) {
			case "-port":
				port=Integer.valueOf(args[i+1]);
				break;

			default:
				break;
			}
		}
	}

	private static URI getURI() {

		return UriBuilder.fromUri("http://" + getHostName() + "/").port(port)
				.build();
	}

	private static String getHostName() {
		String hostName = "localhost";
		try {
			hostName = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostName;
	}
}
