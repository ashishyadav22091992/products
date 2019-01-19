/**
 * @author Ashish 
 * Modified Date Jan 17, 2019
*/
package com.concerto;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.concerto.tcpipdoc.connection.DocClient;
import com.concerto.tcpipdoc.connection.DocServer;

@SpringBootApplication
public class TcpIpDocApplication implements CommandLineRunner {

	static Logger logger = Logger.getLogger(TcpIpDocApplication.class.getName());

	@Autowired
	private DocClient docClient;

	@Value("${tcpip.connection.client.connectionType}")
	private String docType;

	@Autowired
	private DocServer docServer;

	public static void main(String[] args) {
		SpringApplication.run(TcpIpDocApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		Thread thread = null;
		if (docType.equalsIgnoreCase("server")) {
			thread = new Thread(docServer);
		} else if (docType.equalsIgnoreCase("client")) {
			thread = new Thread(docClient);
		}
		try {
			thread.start();
//            logger.info("Application started successfully!");
		} catch (Exception e) {
//            logger.error("Application failed to start!");
		}

	}

}
