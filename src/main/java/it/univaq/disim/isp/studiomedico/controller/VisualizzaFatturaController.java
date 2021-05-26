package it.univaq.disim.isp.studiomedico.controller;

import com.jfoenix.controls.JFXButton;
import it.univaq.disim.isp.studiomedico.business.PrenotazioneService;
import it.univaq.disim.isp.studiomedico.business.SegretariaService;
import it.univaq.disim.isp.studiomedico.business.StudioMedicoBusinessFactory;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.Prenotazione;
import it.univaq.disim.isp.studiomedico.domain.Utente;
import it.univaq.disim.isp.studiomedico.view.ViewDispatcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class VisualizzaFatturaController implements Initializable,  DataInitializable<Prenotazione>{
    @FXML
    public JFXButton backButton;
    @FXML
    public JFXButton stampaButton;
    @FXML
    public TextArea FatturaTextArea;


    private final ViewDispatcher manage;
    private PrenotazioneService prenotazioneService;
    private SegretariaService segretariaService;
    private Prenotazione prenotazione;
    private Utente utente;


    public VisualizzaFatturaController() {
        manage = ViewDispatcher.getInstance();
        StudioMedicoBusinessFactory factory = StudioMedicoBusinessFactory.getInstance();
        prenotazioneService = factory.getPrenotazioneService();
        segretariaService = factory.getSegretariaService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    public void initializeData(Prenotazione prenotazione) throws BusinessException {
        this.prenotazione = prenotazione;
        FatturaTextArea.setText("Fattura rilasciata da: Studio Medico" + "\n"
                + "In data: " + prenotazione.getTurno().getData() + "\n"
                + "Ora: " + prenotazione.getOrainizio() + "-" + prenotazione.getOrafine() + "\n"
                + "Paziente: " + prenotazione.getPaziente().getNome() + " " + prenotazione.getPaziente().getCognome() + "\n"
                + "Medico Curante: " + prenotazione.getMedico().getNome() + " " + prenotazione.getMedico().getCognome() + "\n"
                + "Visita Sostenuta: " + prenotazione.getVisita().getNome() + "\n"
                + "Durata: " + prenotazione.getVisita().stampaDurata() + "\n"
                + "Prezzo totale: " + prenotazione.getVisita().stampaPrezzo());
    }
    @FXML
    public void back(ActionEvent actionEvent) {
        manage.renderView("riscontrovisite",manage.getUtenteloggato());
    }

    @FXML
    public void stampa(ActionEvent actionEvent) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("C:/Users/loren/OneDrive/Desktop/FattureStudioMedico/" + prenotazione.getTurno().getData() + prenotazione.getOrainizio() + prenotazione.getOrafine() + prenotazione.getPaziente().getCf()  + "pdf" ));
            document.open();
            document.add(new Paragraph("Fattura rilasciata da: Studio Medico"));
            document.add(new Paragraph("In data: " + prenotazione.getTurno().getData()));
            document.add(new Paragraph("Ora: " + prenotazione.getOrainizio() + "-" + prenotazione.getOrafine()));
            document.add(new Paragraph("Paziente: " + prenotazione.getPaziente().getNome() + " " + prenotazione.getPaziente().getCognome()));
            document.add(new Paragraph("Medico Curante: " + prenotazione.getMedico().getNome() + " " + prenotazione.getMedico().getCognome()));
            document.add(new Paragraph("Visita Sostenuta: " + prenotazione.getVisita().getNome()));
            document.add(new Paragraph("Durata: " + prenotazione.getVisita().stampaDurata()));
            document.add(new Paragraph("Prezzo totale: " + prenotazione.getVisita().stampaPrezzo()));
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
