package com.concerto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.concerto.tcpipdoc.connection.SocketServer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

@SpringBootApplication
public class TcpIpDocApplication implements CommandLineRunner {

	@Autowired
	private SocketServer socketServer;

	public static String serverIP;
	public static int serverPort;
	public static String fileAddress;
	public static int dataWriteTPS;

	@Value("${tcpip.connection.server.serverip}")
	public void setServerIP(String serverIpAddress) {
		serverIP = serverIpAddress;
	}

	@Value("${tcpip.connection.server.listeningport}")
	public void setServerPort(int port) {
		serverPort = port;
	}

	@Value("${tcpip.fileread.serverclient.fileaddress}")
	public void setFileAddress(String address) {
		fileAddress = address;
	}

	@Value("${tcpip.fileread.serverclient.datawritespeed}")
	public void setTPS(int tps) {
		dataWriteTPS = tps;
	}

	public static void main(String[] args) {
		SpringApplication.run(TcpIpDocApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		socketServer.stablisHostConnection();

	}
}
