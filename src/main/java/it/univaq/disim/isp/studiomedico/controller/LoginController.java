package it.univaq.disim.isp.studiomedico.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.business.exceptions.UtenteNotFoundException;
import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.domain.Utente;
import it.univaq.disim.isp.studiomedico.utility.BCrypt;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import it.univaq.disim.isp.studiomedico.view.ViewException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

//classe LoginController che implementa Initializable e DataInitializable>Object>
public class LoginController implements Initializable, DataInitializable<Object> {

	@FXML
	public JFXButton Registrazione;

	@FXML
	private JFXTextField email;

	@FXML
	private JFXPasswordField password;

	@FXML
	private Label loginErrorLabel;

	@FXML
	private JFXButton loginButton;

	@FXML
	private TextField textPsw;

	@FXML
	private CheckBox showPassword;

	//dichiarazione variabili
	private ViewDispatcher manage;

	private UtenteService utenteService;

	//avvio del controller della vista
	public LoginController() {
		manage = ViewDispatcher.getInstance();
		StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
		utenteService = factory.getUtenteService();

	}

	//inizializzazione del metodo login
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//label.setVisible(false);
		loginButton.disableProperty().bind(email.textProperty().isEmpty().or(password.textProperty().isEmpty()));
		//disattiva il pulsante fino a quando nn viene scritto la user e la password
	}

	@FXML // Autenticazione
	private void loginAction(ActionEvent event) throws BusinessException, UtenteNotFoundException {
		try {
			Utente utente = utenteService.autenticazione(email.getText());
			String candidate = password.getText();
			if (BCrypt.checkpw(candidate,utente.getPassword())) {
				loginErrorLabel.setText("Email e/o password corretti!");
				manage.loggedIn(utente);
			}
			else {
				//loginErrorLabel.setText("Email e/o password errati!");
				loginErrorLabel.setVisible(true);
			}
		} catch (UtenteNotFoundException e) {
			//loginErrorLabel.setText("Email e/o password errati!");
			loginErrorLabel.setVisible(true);
		} catch (BusinessException e) {
			// e.printStackTrace();
			manage.renderError(e);
		}

	}

	//action per la registrazione dell'utente
	@FXML
	public void registraAction(ActionEvent mouseEvent) throws ViewException {
		Utente utente = new Utente();
		manage.registraView(utente);
	}

	//metodo utilizzato dalla checkbox per mostrare la password
	@FXML
	public void mostrapass(ActionEvent e) {
		if (showPassword.isSelected()) {
			String password2 = password.getText();
			textPsw.setText(password2);
			textPsw.setVisible(true);
			password.setVisible(false);
		} else {
			textPsw.setVisible(false);
			password.setVisible(true);
			password.setText(textPsw.getText());
		}


	}

}