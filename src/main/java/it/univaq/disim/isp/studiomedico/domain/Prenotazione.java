package it.univaq.disim.isp.studiomedico.domain;

import java.util.Date;

public class Prenotazione {
    private Integer id;
    private Date giorno, orainizio, orafine;
    private boolean checkin, checkout;
    private Paziente paziente;
    private Visita visita;
    private Fattura fattura;

}
