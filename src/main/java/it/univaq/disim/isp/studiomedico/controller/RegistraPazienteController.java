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

public class RegistraPazienteController implements Initializable, DataInitializable<Utente>{

        @FXML
        public Button registraButton;
        @FXML
        public TextField NomeTextField;
        @FXML
        public Label NomeLabel;
        @FXML
        public Button annullaButton;
        @FXML
        public TextField CognomeTextField;
        @FXML
        public TextField LuogoTextField;
        @FXML
        public TextField EmailTextField;
        @FXML
        public TextField CodiceFiscaleTextField;
        @FXML
        public DatePicker Data;
        @FXML
        public PasswordField PasswordTextField;
        @FXML
        public PasswordField CPasswordTextField;
        @FXML
        public TextField TelefonoTextField;
        @FXML
        public Label registerErrorLabel;
        @FXML
        public CheckBox GDPRCheckBoxField;
        @FXML
        public Button proseguiButton;

        private Utente utente;


        //dichiarazioni
        private final ViewDispatcher manage;
        private final UtenteService utenteservice;

        //avvio controller della vista
        public RegistraPazienteController() {
            manage = ViewDispatcher.getInstance();
            StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
            utenteservice = factory.getUtenteService();
        }


        @Override
        public void initialize(URL location, ResourceBundle resources) {
            registerErrorLabel.setVisible(false);
            GDPRCheckBoxField.setSelected(false);
            registraButton.disableProperty()
                    .bind(CodiceFiscaleTextField.textProperty().isEmpty().or(NomeTextField.textProperty().isEmpty()).or(CognomeTextField.textProperty().isEmpty())
                            .or(PasswordTextField.textProperty().isEmpty()).or(CPasswordTextField.textProperty().isEmpty()).or(TelefonoTextField.textProperty().isEmpty())
                            .or(LuogoTextField.textProperty().isEmpty()).or(Data.valueProperty().isNull())
                            .or(GDPRCheckBoxField.selectedProperty().not()));
        }

        @Override
        public void initializeData(Utente utente) {

        }

        @FXML
        public void registrazioneAction(ActionEvent actionEvent) {
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
        }

        @FXML
        public void annullaAction(ActionEvent actionEvent) {
            manage.renderView("gestioneutenti",manage.getUtenteloggato());
        }

/*        public void prenotazioneTelefonica(ActionEvent actionEvent) {
            manage.renderView("prenotazionevisita",manage.getUtenteloggato());
        }*/
}
