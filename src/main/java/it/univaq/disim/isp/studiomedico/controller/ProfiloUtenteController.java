package it.univaq.disim.isp.studiomedico.controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.domain.Medico;
import it.univaq.disim.isp.studiomedico.domain.Paziente;
import it.univaq.disim.isp.studiomedico.domain.Specializzazione;
import it.univaq.disim.isp.studiomedico.domain.Utente;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfiloUtenteController implements Initializable, DataInitializable<Utente>{

    @FXML
    public TextField DataNascitaTextField;
    @FXML
    public Label NomeLabel;
    @FXML
    public DatePicker TurnoDatePicker;
    @FXML
    public Label TurnoLabel;
    @FXML
    public Label ContrattoLabel;
    @FXML
    public Label SpecializzazioneLabel;
    @FXML
    public Label NumeroPresenzeLabel;
    @FXML
    public Label NumeroPrestazioniLabel;
    @FXML
    public JFXDatePicker DatePickerTurno;
    @FXML
    public JFXTimePicker TimePickerOraInizio;
    @FXML
    public JFXTimePicker TimePickerOraFine;
    @FXML
    public Label proponiTurnoLabel;
    @FXML
    public Label OrafineLabel;
    @FXML
    public Label OrainizioLabel;
    @FXML
    public Button proponiTurnoButton;
    @FXML
    public TextField pagamentoTextField;
    @FXML
    public Label pagamentoLabel;
    @FXML
    private Button SalvaModificheButton;
    @FXML
    private Button AnnullaModificheButton;
    @FXML
    private TextField NumeroPresenzeTextField;
    @FXML
    private TextField NumeroPrestazioniTextField;
    @FXML
    private TextField CodiceFiscaleTextField;
    @FXML
    private TextField NomeTextField;
    @FXML
    private TextField CognomeTextField;
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
    private TextField SpecializzazioneTextField;
    @FXML
    private TextField ContrattoTextField;

    private Utente utente;

    //dichiarazioni
    private final ViewDispatcher manage;
    private final UtenteService utenteservice;

    //avvio controller della vista
    public ProfiloUtenteController() {
        manage = ViewDispatcher.getInstance();
        StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
        utenteservice = factory.getUtenteService();
    }

    public void initializeData(Utente utente) {
        pagamentoTextField.clear();
        setItemsVisibility(utente);
        NomeTextField.setText(utente.getNome());
        CognomeTextField.setText(utente.getCognome());
        EmailTextField.setText(utente.getEmail());
        LuogoTextField.setText(utente.getLuogoDiNascita());
        CodiceFiscaleTextField.setText(utente.getCf());
        TelefonoTextField.setText(utente.getTelefono());
        DataNascitaTextField.setText(utente.getDataDiNascita().toString());
        if (utente instanceof Medico){
            SpecializzazioneTextField.setText(((Medico) utente).getSpecializzazione().toString());
            ContrattoTextField.setText(((Medico) utente).getContratto().getTipo().toString());
            NumeroPresenzeTextField.setText(((Medico) utente).getNumeropresenze().toString());
            NumeroPrestazioniTextField.setText(((Medico) utente).getNumerovisite().toString());
            pagamentoTextField.setText(((Medico) utente).getContratto().stampaContratto(((Medico) utente).getNumeropresenze(),((Medico) utente).getNumerovisite()));
        }
    }

    @FXML
    public void proponiTurnoAction(ActionEvent actionEvent) {
        utenteservice.inserisciTurnoProvvisorio(manage.getUtenteloggato().getId(),DatePickerTurno.getValue().toString(),TimePickerOraInizio.getValue().toString(),TimePickerOraFine.getValue().toString());
        manage.renderView("profiloutente",manage.getUtenteloggato());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NomeTextField.setEditable(false);
    }

    public void setItemsVisibility(Utente utente){
        if (!(utente instanceof Medico)){
            SpecializzazioneLabel.setVisible(false);
            ContrattoLabel.setVisible(false);
            NumeroPresenzeLabel.setVisible(false);
            NumeroPrestazioniLabel.setVisible(false);
            SpecializzazioneTextField.setVisible(false);
            ContrattoTextField.setVisible(false);
            NumeroPresenzeTextField.setVisible(false);
            NumeroPrestazioniTextField.setVisible(false);
            proponiTurnoLabel.setVisible(false);
            OrafineLabel.setVisible(false);
            OrainizioLabel.setVisible(false);
            DatePickerTurno.setVisible(false);
            TimePickerOraFine.setVisible(false);
            TimePickerOraInizio.setVisible(false);
            proponiTurnoButton.setVisible(false);
            AnnullaModificheButton.setVisible(false);
            pagamentoTextField.setVisible(false);
            pagamentoLabel.setVisible(false);
        }
    }

    @FXML
    public void resetTurnoProposto(ActionEvent actionEvent) {
        DatePickerTurno.getEditor().clear();
        TimePickerOraInizio.getEditor().clear();
        TimePickerOraFine.getEditor().clear();
    }
}
