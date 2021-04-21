package it.univaq.disim.isp.studiomedico.controller;

import it.univaq.disim.isp.studiomedico.business.PrenotazioneService;
import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.*;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ElencoPrenotazioniController implements Initializable, DataInitializable<Turno>{
    @FXML
    public TableView<Prenotazione> PrenotazioniTableView;
    @FXML
    public TableColumn<Prenotazione,String> visitaTableColumn;
    @FXML
    public TableColumn<Prenotazione,String> pazienteTableColumn;
    @FXML
    public TableColumn<Prenotazione,LocalTime> oraInizioTableColumn;
    @FXML
    public TableColumn<Prenotazione,LocalTime> oraFineTableColumn;
    @FXML
    public Button BackButton;

    private final ViewDispatcher manage;
    private PrenotazioneService prenotazioneService;
    private UtenteService utenteService;
    private List<Prenotazione> listaPrenotazioni = new LinkedList<>();
    private ObservableList<Prenotazione> listaprenotazioni;


    //avvio controller della vista
    public ElencoPrenotazioniController() {
        manage = ViewDispatcher.getInstance();
        StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
        prenotazioneService = factory.getPrenotazioneService();
        utenteService = factory.getUtenteService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        visitaTableColumn.setStyle("-fx-alignment: CENTER;");
        pazienteTableColumn.setStyle("-fx-alignment: CENTER;");
        oraInizioTableColumn.setStyle("-fx-alignment: CENTER;");
        oraFineTableColumn.setStyle("-fx-alignment: CENTER;");
        visitaTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getVisita().getNome()));
        pazienteTableColumn.setCellValueFactory(tf -> new SimpleStringProperty(tf.getValue().getPaziente().getNome().concat(" ").concat(tf.getValue().getPaziente().getCognome())));
        oraInizioTableColumn.setCellValueFactory(new PropertyValueFactory<>("orainizio"));
        oraFineTableColumn.setCellValueFactory(new PropertyValueFactory<>("orafine"));
    }

    @Override
    public void initializeData(Turno turno) {
        stampaListaPrenotazioni(prenotazioneService.getPrenotazioniByIdTurno(turno.getId()));
    }

    public void stampaListaPrenotazioni(List<Prenotazione> listaPrenotazioni){
        listaPrenotazioni.sort(new Sort_by_Start_Time());
        listaprenotazioni = FXCollections.observableList(listaPrenotazioni);
        PrenotazioniTableView.setItems(listaprenotazioni);
    }

    //annullamento azione
/*    @FXML
    public void annullaAction(ActionEvent event) {

        manage.renderView("storicoturni", null);

    }*/
}
