package br.gs.signer;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {
	public static void main(String[] args) throws Exception {

		URI baseUri = getURI();
		ResourceConfig config = new ResourceConfig();
		config.packages(true, "br.gs.signer");

		config.register(JAXBContextResolver.class);
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

	private static URI getURI() {
		return UriBuilder.fromUri("http://" + getHostName() + "/").port(8080)
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
