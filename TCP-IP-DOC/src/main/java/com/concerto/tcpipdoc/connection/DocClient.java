/**
 * @author Ashish 
 * Modified Date Jan 13, 2019
*/
package com.concerto.tcpipdoc.connection;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.concerto.tcpipdoc.fileRead.DocFileReader;

@Service
public class DocClient implements Runnable {

	static Logger logger = Logger.getLogger(DocClient.class.getName());

	private InetSocketAddress serverHostAddress;

	private SocketChannel clientChannel;

	@Value("${tcpip.connection.client.connectip}")
	private String serverIp;

	@Value("${tcpip.connection.client.connectport}")
	private int serverPort;

	private static boolean serverConnect = false;

	@Autowired
	private DocFileReader docFileReader;

	public DocClient() {

	}

	@Override
	public void run() {
		this.connectWithServer();

	}

	public void connectWithServer() {
		while (!serverConnect) {
			try {
				serverHostAddress = new InetSocketAddress(serverIp, serverPort);
				clientChannel = SocketChannel.open(serverHostAddress);
				docFileReader.readFile(clientChannel);
				logger.info("Data writing on server socket is compited successfully!");
				serverConnect = true;
			} catch (Exception e) {
				serverConnect = false;
				logger.error("Problem while writing data on server socket : ", e);
			}
		}

	}

}
