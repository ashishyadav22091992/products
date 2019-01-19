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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.revinate.guava.util.concurrent.RateLimiter;

@Service
public class DocFileReader {

	@Value("${tcpip.file.filepath}")
	private String filePath;

	@Value("${tcpip.message.message.speed}")
	private int tps;

	public static boolean repeatRead;

	RateLimiter rateLimiter;

	@Value("${tcpip.file.filemessage.readtime.multiple}")
	public void setrepeatRead(boolean readValue) {
		repeatRead = readValue;
	}

	public DocFileReader() throws IOException {

	}

	public void readFile(SocketChannel clientChannel) throws IOException {
		boolean firstRead = true;
		rateLimiter = RateLimiter.create(tps);
		File file = new File(filePath);
		while (firstRead) {
			firstRead = repeatRead;
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

}
