package it.univaq.disim.isp.studiomedico.controller;

import it.univaq.disim.isp.studiomedico.business.PrenotazioneService;
import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.Medico;
import it.univaq.disim.isp.studiomedico.domain.Turno;
import it.univaq.disim.isp.studiomedico.domain.Utente;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class PrenotazioneVisitaTelefonicaController implements Initializable,  DataInitializable<Utente>{

    @FXML
    public Button cercaButton;
    @FXML
    public TableView<Turno> TurniTableView;
    @FXML
    public TableColumn<Turno, LocalDate> dataTurnoTableColumn;
    @FXML
    public TableColumn<Turno, LocalTime> oraInizioTableColumn;
    @FXML
    public TableColumn<Turno,LocalTime> oraFineTableColumn;
    @FXML
    public TableColumn<Turno,Button> prenotaTableColumn;
    @FXML
    public ChoiceBox<String> medicoChoiceBoxField;
    @FXML
    public ChoiceBox<String> specializzazioneChoiceBoxField;
    @FXML
    public Label ErroreLabel;


    private final ObservableList<String> listaVisite = FXCollections.observableArrayList(
            "Holter pressorio",
            "Laserterapia",
            "Ultrasuonoterapia",
            "Tecarterterapia",
            "Riabilitazione posturale",
            "Visita nutrizionistica",
            "Visita di controllo",
            "Chinesiterapia",
            "Elettroterapia",
            "Linfodrenaggio",
            "Visita senologica",
            "Visita urologica",
            "Visita urologica + Ecografia",
            "Visita andrologica",
            "Visita andrologia + Ecografia",
            "Ecocardiogramma",
            "ECG da sforzo",
            "Ecografia",
            "Elettromiografia",
            "Visita neurologica",
            "Visita neurochirurgica",
            "Visita gastroenterologica",
            "Visita otorinolaringoiatrica",
            "Esame audiometrico",
            "Fibrolaringoscopia",
            "Esame destibolare",
            "Visita ortopedica",
            "Visita traumatologica",
            "Visita oncologica",
            "Visita proctologica",
            "Idrocolonterapia",
            "Breath Test",
            "Visita endocrinologica",
            "Visita ematologica",
            "Visita ostetrica",
            "Visita ginecologica",
            "Ecografia ginecologica",
            "Pap Test",
            "Visita + Ecocolordoppler",
            "Visita psicologica"
    );


    protected ObservableList<String> listaspecializzazioni = FXCollections.observableArrayList(
            "Fisioterapia",
            "Nutrizionista",
            "Cardiologia",
            "Senologia",
            "Otorinolaringoiatria",
            "Ortopedia",
            "Urologia",
            "Neurologia",
            "Gastroenterologia",
            "Oncologia",
            "Neurochirurgia",
            "Medicina Interna",
            "Ginecologia",
            "Psicologia",
            "Chirurgia Vascolare",
            "Ostetricia",
            "Andrologia",
            "Traumatologia"
    );

    private ObservableList<String> listaMedici = FXCollections.observableArrayList();

    private ObservableList<Turno> listaturni;

    private final ViewDispatcher manage;

    private PrenotazioneService prenotazioneService;

    private Utente utente;

    private String visita;

    //avvio controller della vista
    public PrenotazioneVisitaTelefonicaController() {
        manage = ViewDispatcher.getInstance();
        StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
        prenotazioneService = factory.getPrenotazioneService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cercaButton.disableProperty()
                .bind(specializzazioneChoiceBoxField.valueProperty().isNull().or(medicoChoiceBoxField.valueProperty().isNull()));
        specializzazioneChoiceBoxField.setItems(listaspecializzazioni);

        specializzazioneChoiceBoxField.setOnAction(event -> {
            medicoChoiceBoxField.getItems().clear();
            setListaMedici(specializzazioneChoiceBoxField.getValue());
        });
        dataTurnoTableColumn.setStyle("-fx-alignment: CENTER;");
        oraInizioTableColumn.setStyle("-fx-alignment: CENTER;");
        oraFineTableColumn.setStyle("-fx-alignment: CENTER;");
        prenotaTableColumn.setStyle("-fx-alignment: CENTER;");
        dataTurnoTableColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        oraInizioTableColumn.setCellValueFactory(new PropertyValueFactory<>("orainizio"));
        oraFineTableColumn.setCellValueFactory(new PropertyValueFactory<>("orafine"));
        prenotaTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Turno,Button>, ObservableValue<Button>>() {
            @Override
            public ObservableValue<Button> call(TableColumn.CellDataFeatures<Turno,Button> param) {
                // pulsante visualizza
                final Button prenotaButton = new Button("Visualizza");
                prenotaButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        manage.renderView("prenotazionetelefonicaslotliberi", param.getValue());

                    }
                });
                return new SimpleObjectProperty<Button>(prenotaButton);
            }
        });
    }

    public void initializeData(Utente utente) {
    }

    public void setListaMedici(String specializzazione){
        List<Medico> lista = prenotazioneService.effettuaRicercaPrenotazione(specializzazione);
        for (Medico m: lista) {
            if (!(m.getCf().equals(manage.getUtenteloggato().getCf())))
                listaMedici.add(m.getNome() + " " + m.getCognome());
        }
        medicoChoiceBoxField.setItems(listaMedici);
    }

    @FXML
    public void effettuaRiercaTurni(ActionEvent event) throws BusinessException {
        String stringamedico = medicoChoiceBoxField.getValue();
        String[] splitStrings = stringamedico.split("\\s+");
        stampaListaTurni(prenotazioneService.getTurniByMedico(splitStrings[0],splitStrings[1]));

    }

    public void stampaListaTurni(List<Turno> listaTurni){
        listaturni = FXCollections.observableList(listaTurni);
        TurniTableView.setItems(listaturni);
    }

}
