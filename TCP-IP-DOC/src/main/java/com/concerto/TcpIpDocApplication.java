package com.concerto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.concerto.tcpipdoc.connection.SocketServer;

@SpringBootApplication
public class TcpIpDocApplication implements CommandLineRunner{
	
	@Autowired
	private SocketServer socketServer;

	public static void main(String[] args) {
		SpringApplication.run(TcpIpDocApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		socketServer.stablisHostConnection();
		
	}

}

