package it.univaq.disim.isp.studiomedico.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

	@FXML
	private Label loginErrorLabel;
	
	@FXML
	private TextField username;
	
	@FXML
	private TextField password;
	
	@FXML
	private Button loginButton;
	
	private ViewDispatcher dispatcher;
	
	public LoginController() {
		dispatcher = ViewDispatcher.getInstance();
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loginButton.disableProperty().bind(username.textProperty().isEmpty().or(password.textProperty().isEmpty()));
	}

	@FXML
	private void loginAction(ActionEvent event) {
		if (!("amleto".equals(username.getText()) && ("amleto".equals(password.getText())))) {
			loginErrorLabel.setText("Username e/o password errati!");
		} else {
			dispatcher.loggedIn();
		}
	}

}
