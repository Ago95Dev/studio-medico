package it.univaq.disim.isp.studiomedico.controller;


import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.Utente;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrazioneController implements Initializable, DataInitializable<Utente> {

    @FXML
    private TextField CodiceFiscaleTextField;
    @FXML
    private TextField NomeTextField;
    @FXML
    private TextField CognomeTextField;
    @FXML
    private DatePicker Data;
    @FXML
    private TextField PasswordTextField;
    @FXML
    private TextField EmailTextField;
    @FXML
    private TextField LuogoTextField;
    @FXML
    private TextField TelefonoTextField;
    @FXML
    private TextField CPasswordTextField;

    @FXML
    private Button registraButton;
    @FXML
    private Button annullaButton;

    private Utente utente;

    //dichiarazioni
    private final ViewDispatcher manage;
    private final UtenteService utenteservice;

    //avvio controller della vista
    public RegistrazioneController() {
        manage = ViewDispatcher.getInstance();
        StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
        utenteservice = factory.getUtenteService();
    }

    //ObservableList<String> listruolo = FXCollections.observableArrayList("medico", "paziente", "farmacista");

    //inizializzazione
    public void initialize(URL location, ResourceBundle resources) {
        registraButton.disableProperty()
                .bind(CodiceFiscaleTextField.textProperty().isEmpty().or(NomeTextField.textProperty().isEmpty()).or(CognomeTextField.textProperty().isEmpty()));

    }

    // registrazione utente
    @FXML
    public void registrazioneAction(ActionEvent event) {
        try {

            utente = utenteservice.registrazione(PasswordTextField.getText(), NomeTextField.getText(), CognomeTextField.getText(), CodiceFiscaleTextField.getText(), EmailTextField.getText(), TelefonoTextField.getText(), String.valueOf(Data), LuogoTextField.getText());


        } catch (BusinessException e) {
            manage.renderError(e);
        }
        manage.logout();
    }
}
