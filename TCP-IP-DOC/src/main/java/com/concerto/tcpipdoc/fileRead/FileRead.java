/**
 * @author Ashish 
 * Modified Date Jan 13, 2019
*/
package com.concerto.tcpipdoc.fileRead;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.stereotype.Service;

import com.concerto.TcpIpDocApplication;
import com.revinate.guava.util.concurrent.RateLimiter;

@Service
public class FileRead {

	

	public FileRead() {

	}

	public void fileRead(SocketChannel clientChannel) throws IOException {
		RateLimiter rateLimiter = RateLimiter.create(TcpIpDocApplication.dataWriteTPS);
		File file = new File(TcpIpDocApplication.fileAddress);

		LineIterator it = FileUtils.lineIterator(file, "UTF-8");
		while (it.hasNext()) {
			rateLimiter.acquire();
			String lineData = it.nextLine() + System.lineSeparator();
			byte[] message = lineData.getBytes();
			ByteBuffer buffer = ByteBuffer.wrap(message);
			clientChannel.write(buffer);

		}

	}

}
