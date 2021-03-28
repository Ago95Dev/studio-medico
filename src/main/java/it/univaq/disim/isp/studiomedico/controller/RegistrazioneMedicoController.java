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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RegistrazioneMedicoController<listaspecializzazioni> implements Initializable, DataInitializable<Utente>{

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
    private Button registraButton;
    @FXML
    private Button annullaButton;


    private Utente medico;

    private final ViewDispatcher manage;
    private final UtenteService utenteservice;

    protected ObservableList<String> listaspecializzazioni = FXCollections.observableArrayList("FISIOTERAPIA", "NUTRIZIONISTA", "CARDIOLOGIA","SENOLOGIA","OTORINOLARINGOIATRIA","ORTOPEDIA","UROLOGIA","NEUROLOGIA","GASTROENTEROLOGIA","ONCOLOGIA","NEUROCHIRURGIA","MEDICINAINTERNA","GINECOLOGIA","PSICOLOGIA","CHIRURGIAVASCOLARE","OSTETRICIA","ANDROLOGIA","TRAUMATOLOGIA");

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
/*    public void registrazioneMedicoAction(ActionEvent event) throws BusinessException {

        medico = utenteservice.registrazioneMedico(PasswordTextField.getText(), NomeTextField.getText(), CognomeTextField.getText(), CodiceFiscaleTextField.getText(), EmailTextField.getText(), TelefonoTextField.getText(), String.valueOf(Data), LuogoTextField.getText(), (String) SpecializzazioneChoiceBox.getValue());


        manage.logout();
    }*/

}
