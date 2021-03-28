package it.univaq.disim.isp.studiomedico.controller;

import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.Utente;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RegistrazioneMedicoController<listaspecializzazioni> implements Initializable, DataInitializable<Utente>{

    @FXML
    public Label NomeLabel;
    @FXML
    public Label registerErrorLabel;
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
    private ChoiceBox SpecializzazioneChoiceBox;
    @FXML
    private ChoiceBox ContrattoChoiceBox;

    @FXML
    private Button registraButton;
    @FXML
    private Button annullaButton;


    private Utente medico;

    private final ViewDispatcher manage;
    private final UtenteService utenteservice;

    protected ObservableList<String> listaspecializzazioni = FXCollections.observableArrayList("FISIOTERAPIA", "NUTRIZIONISTA", "CARDIOLOGIA","SENOLOGIA","OTORINOLARINGOIATRIA","ORTOPEDIA","UROLOGIA","NEUROLOGIA","GASTROENTEROLOGIA","ONCOLOGIA","NEUROCHIRURGIA","MEDICINAINTERNA","GINECOLOGIA","PSICOLOGIA","CHIRURGIAVASCOLARE","OSTETRICIA","ANDROLOGIA","TRAUMATOLOGIA");
    protected ObservableList<String> listacontratti = FXCollections.observableArrayList("FORFETTARIO", "PRESENZE", "PRESTAZIONI");


    public RegistrazioneMedicoController() {
        manage = ViewDispatcher.getInstance();
        StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
        utenteservice = factory.getUtenteService();
    }

    public void initialize(URL location, ResourceBundle resources) {
        registraButton.disableProperty()
                .bind(CodiceFiscaleTextField.textProperty().isEmpty().or(NomeTextField.textProperty().isEmpty()).or(CognomeTextField.textProperty().isEmpty()));
        SpecializzazioneChoiceBox.setItems(listaspecializzazioni) ;
    }

    // registrazione utente
    public void registrazioneMedicoAction(ActionEvent event) throws BusinessException {

        medico = utenteservice.registrazioneMedico(PasswordTextField.getText(), NomeTextField.getText(), CognomeTextField.getText(), CodiceFiscaleTextField.getText(), EmailTextField.getText(), TelefonoTextField.getText(), Data.getValue().toString(), LuogoTextField.getText(), (String) SpecializzazioneChoiceBox.getValue(), (String) ContrattoChoiceBox.getValue());


        manage.logout();
    }

    public void annullaAction(ActionEvent actionEvent) {
    }

    public void registrazioneAction(ActionEvent actionEvent) {
    }
}
