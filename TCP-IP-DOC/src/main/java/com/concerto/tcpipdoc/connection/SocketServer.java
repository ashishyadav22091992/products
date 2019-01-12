package com.concerto.tcpipdoc.connection;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class SocketServer implements Runnable {

	public SocketServer() {

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public void stablisHostConnection() {
		try {
			// multiplex of select channels
			Selector selector = Selector.open();
			// listening socket
			ServerSocketChannel docServer = ServerSocketChannel.open();
			// socket address [server machine ip + doc server running port]
			InetSocketAddress docServerAddress = new InetSocketAddress("192.168.0.107", 9292);
			docServer.bind(docServerAddress);
			docServer.configureBlocking(false);
			int ops = docServer.validOps();
			// registration of channel with selector a key will generate with every channel
			// registration with selector
			SelectionKey selectKeys = docServer.register(selector, ops);

			// keep server always in listening mode
			while (true) {

				selector.select();
				Set<SelectionKey> channelKeys = selector.selectedKeys();
				Iterator<SelectionKey> keyIterator = channelKeys.iterator();
				while (keyIterator.hasNext()) {

					try {
						SelectionKey key = keyIterator.next();
						if (key.isAcceptable()) {
							SocketChannel clientSocket = docServer.accept();
							clientSocket.configureBlocking(false);
							clientSocket.register(selector, SelectionKey.OP_WRITE);
							System.out
									.println("Connection accepted for the client : " + clientSocket.getLocalAddress());

						} else if (key.isWritable()) {
							
							SocketChannel clientSocket = (SocketChannel) key.channel();
							for (int i = 0; i < 10; i++) {
								String name = "Ashish" + System.lineSeparator();
								byte[] b = name.getBytes();
								ByteBuffer buffer = ByteBuffer.wrap(b);
								try {
									clientSocket.write(buffer);
								}catch(Exception e) {
									key.cancel();
									e.printStackTrace();
									break;
								}
								

							}

							Thread.sleep(5000);
						}
						keyIterator.remove();
					} catch (Exception e) {
						keyIterator.remove();
						e.printStackTrace();
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}