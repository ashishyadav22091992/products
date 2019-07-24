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
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.concerto.tcpipdoc.connection.DocClient;
import com.concerto.tcpipdoc.connection.DocServer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

@SpringBootApplication
public class TcpIpDocApplication extends Application {

	static Logger logger = Logger.getLogger(TcpIpDocApplication.class.getName());

	private ConfigurableApplicationContext context;

	@Autowired
	public DocClient docClient;

	@Value("${tcpip.connection.client.connectionType}")
	public String docType;

	@Autowired
	public DocServer docServer;

	public static void main(String[] args) {
		SpringApplication.run(TcpIpDocApplication.class, args);
		launch(args);

	}

	@Override
	public void init() throws Exception {
		super.init();
		context.getAutowireCapableBeanFactory().autowireBean(this);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		System.out.println("the object of client : " + docClient + " the object of server class : " + docServer);
//		launch(args);
//		Thread thread = null;
//		if (docType.equalsIgnoreCase("server")) {
//			thread = new Thread(docServer);
//		} else if (docType.equalsIgnoreCase("client")) {
//			thread = new Thread(docClient);
//		}
//		try {
//			thread.start();
//			logger.info("Application started successfully!");
//		} catch (Exception e) {
//			logger.error("Application failed to start!");
//		}
//
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		Label label = new Label("First Label");

		Button button = new Button("submit");
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("I got the value ");
				System.out
						.println("client value in action event : " + docClient + " value of doc server : " + docServer);

			}
		});
		StackPane stackPane = new StackPane();
		Scene secen = new Scene(stackPane, 400, 400);
		stackPane.getChildren().addAll(label, button);
		primaryStage.setScene(secen);
		primaryStage.setTitle("First title ");
		primaryStage.show();

	}

}
