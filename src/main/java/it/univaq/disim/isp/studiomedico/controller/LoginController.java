package it.univaq.disim.isp.studiomedico.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.business.exceptions.UtenteNotFoundException;
import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.domain.Utente;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import it.univaq.disim.isp.studiomedico.view.ViewException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

//classe LoginController che implementa Initializable e DataInitializable>Object>
public class LoginController implements Initializable, DataInitializable<Object> {

	@FXML
	public Button Registrazione;

	@FXML
	private TextField email;

	@FXML
	private PasswordField password;

	@FXML
	private Label loginErrorLabel;

	@FXML
	private Button loginButton;

/*	@FXML
	private TextField label;*/

/*	@FXML
	private CheckBox checkbox;*/

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
			Utente utente = utenteService.autenticazione(email.getText(), password.getText());
			loginErrorLabel.setText("Email e/o password corretti!");
			manage.loggedIn(utente);
		} catch (UtenteNotFoundException e) {
			loginErrorLabel.setText("Email e/o password errati!");
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
/*	@FXML
	public void mostrapass(ActionEvent e) {
		if (checkbox.isSelected()) {
			String password2 = password.getText();
			label.setText(password2);
			label.setVisible(true);
			password.setVisible(false);
		} else {
			label.setVisible(false);
			password.setVisible(true);
			password.setText(label.getText());
		}


	}*/

}