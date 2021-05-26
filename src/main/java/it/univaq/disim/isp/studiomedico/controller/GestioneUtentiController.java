package it.univaq.disim.isp.studiomedico.controller;

import com.jfoenix.controls.JFXButton;
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

public class GestioneUtentiController implements Initializable, DataInitializable<Utente>{

    @FXML
    public TableView<Utente> TurniTableView;
    @FXML
    public TableColumn<Utente, String> utenteTableColumn;
    @FXML
    public TableColumn<Utente, String> ruoloTableColumn;
    @FXML
    public TableColumn<Utente, String> telefonoTableColumn;
    @FXML
    public TableColumn<Utente, String> emailTableColumn;
    @FXML
    public TableColumn prenotaTableColumn;
    @FXML
    public Label benvenutoLabel;
    @FXML
    public JFXButton registraPazienteButton;
    @FXML
    public JFXButton registraMedicoButton;
    @FXML
    public JFXButton prenotazioneTelefonicaButton;


    private Utente utente;


    //dichiarazioni
    private final ViewDispatcher manage;
    private final UtenteService utenteservice;

    //avvio controller della vista
    public GestioneUtentiController() {
        manage = ViewDispatcher.getInstance();
        StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
        utenteservice = factory.getUtenteService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void registraMedico(ActionEvent actionEvent) {
        manage.renderView("registrazioneMedico",manage.getUtenteloggato());
    }

    @FXML
    public void registraPaziente(ActionEvent actionEvent) {
        manage.renderView("registraPaziente",manage.getUtenteloggato());
    }

    @FXML
    public void prenotazioneTelefonica(ActionEvent actionEvent) {
        manage.renderView("prenotazionevisitatelefonica",manage.getUtenteloggato());
    }
}
