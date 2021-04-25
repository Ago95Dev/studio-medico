package it.univaq.disim.isp.studiomedico.controller;


import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.Utente;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import it.univaq.disim.isp.studiomedico.view.ViewException;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrazioneController implements Initializable, DataInitializable<Utente> {


    @FXML
    public Label NomeLabel;
    @FXML
    public Label registerErrorLabel;
    @FXML
    public CheckBox GDPRCheckBoxField;
    @FXML
    private TextField CodiceFiscaleTextField;
    @FXML
    private TextField NomeTextField;
    @FXML
    private TextField CognomeTextField;
    @FXML
    private DatePicker Data;
    @FXML
    private PasswordField PasswordTextField;
    @FXML
    private TextField EmailTextField;
    @FXML
    private TextField LuogoTextField;
    @FXML
    private TextField TelefonoTextField;
    @FXML
    private PasswordField CPasswordTextField;

    @FXML
    private Button registraButton;
    @FXML
    private ObservableBooleanValue checked;

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
        registerErrorLabel.setVisible(false);
        GDPRCheckBoxField.setSelected(false);
        registraButton.disableProperty()
                .bind(CodiceFiscaleTextField.textProperty().isEmpty().or(NomeTextField.textProperty().isEmpty()).or(CognomeTextField.textProperty().isEmpty())
                        .or(PasswordTextField.textProperty().isEmpty()).or(CPasswordTextField.textProperty().isEmpty()).or(TelefonoTextField.textProperty().isEmpty())
                        .or(LuogoTextField.textProperty().isEmpty()).or(Data.valueProperty().isNull())
                        .or(GDPRCheckBoxField.selectedProperty().not()));
    }

    // registrazione utente
    @FXML
    public void registrazioneAction(ActionEvent event) {
        try {
            if (CPasswordTextField.getText().equals(PasswordTextField.getText())) {
                utente = utenteservice.registrazione(PasswordTextField.getText(), NomeTextField.getText(), CognomeTextField.getText(), CodiceFiscaleTextField.getText(), EmailTextField.getText(), TelefonoTextField.getText(), Data.getValue().toString(), LuogoTextField.getText());
            }
            else {
                registerErrorLabel.setVisible(true);
                registerErrorLabel.setText("Le password inserite non combaciano!");
            }
        } catch (BusinessException e) {
            manage.renderError(e);
        }
        manage.logout();
    }

    public void annullaAction(ActionEvent actionEvent) throws ViewException {
        manage.logout();
    }
}
