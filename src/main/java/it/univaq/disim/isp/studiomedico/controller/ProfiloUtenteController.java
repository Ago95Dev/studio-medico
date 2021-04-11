package it.univaq.disim.isp.studiomedico.controller;

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
        }
    }

    @FXML
    public void modificaProfiloAction(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NomeTextField.setEditable(false);
    }

    public void setItemsVisibility(Utente utente){
        if (!(utente instanceof Medico)){
            SpecializzazioneLabel.setVisible(false);
            TurnoLabel.setVisible(false);
            ContrattoLabel.setVisible(false);
            NumeroPresenzeLabel.setVisible(false);
            NumeroPrestazioniLabel.setVisible(false);
            SpecializzazioneTextField.setVisible(false);
            ContrattoTextField.setVisible(false);
            NumeroPresenzeTextField.setVisible(false);
            NumeroPrestazioniTextField.setVisible(false);
            TurnoDatePicker.setVisible(false);
        }
    }
}
