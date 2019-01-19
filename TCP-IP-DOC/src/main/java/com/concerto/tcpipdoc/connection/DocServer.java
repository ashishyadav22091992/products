
package com.concerto.tcpipdoc.connection;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.concerto.tcpipdoc.fileRead.DocFileReader;

@Service
public class DocServer implements Runnable {

	static Logger logger = Logger.getLogger(DocServer.class.getName());

	@Value("${tcpip.connection.server.listenip}")
	private String serverListenIp;

	@Value("${tcpip.connection.server.listenport}")
	private int serverListenPort;

	@Autowired
	private DocFileReader docFileReader;

	private SocketChannel clientChannel;

	public static boolean clientConnect = false;

	public DocServer() {

	}

	@Override
	public void run() {

		this.connectWithClient();

	}

	public void connectWithClient() {
		try {
			Selector selector = Selector.open();
			ServerSocketChannel serverSocket = ServerSocketChannel.open();
			InetSocketAddress serverAddress = new InetSocketAddress(serverListenIp, serverListenPort);
			serverSocket.bind(serverAddress);
			serverSocket.configureBlocking(false);
			int ops = serverSocket.validOps();
			serverSocket.register(selector, ops);
			while (true) {
				while (!clientConnect) {
					selector.select();
					Set<SelectionKey> selectedKeys = selector.selectedKeys();
					Iterator<SelectionKey> keysIterator = selectedKeys.iterator();
					while (keysIterator.hasNext()) {
						try {
							SelectionKey key = keysIterator.next();
							if (key.isAcceptable()) {
								clientChannel = serverSocket.accept();
								clientChannel.configureBlocking(false);
								clientChannel.register(selector, SelectionKey.OP_WRITE);
								System.out.println("Connected client address : " + clientChannel.getLocalAddress()
										+ clientChannel.keyFor(selector));
								logger.info("Connected client address : " + clientChannel.getLocalAddress());
							} else if (key.isWritable()) {
								clientChannel = (SocketChannel) key.channel();
								try {
									docFileReader.readFile(clientChannel);
									logger.info("Data writing at client socket is completed successfully!");
									clientConnect = true;
								} catch (Exception e) {
									clientConnect = false;
									key.cancel();
									logger.error("Problem while writing data on server socket : ", e);
									break;
								}

							}
							keysIterator.remove();
						} catch (Exception e) {
							clientConnect = false;
							keysIterator.remove();
							logger.error(e);
						}

					}
				}

			}

		} catch (Exception e) {
			logger.error("Problem with server socket : ", e);
		}
	}

}
