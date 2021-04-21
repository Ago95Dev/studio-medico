package it.univaq.disim.isp.studiomedico.domain;

import java.util.Comparator;

public class Sort_By_Data_Prenotazione implements Comparator<Prenotazione> {


    @Override
    public int compare(Prenotazione p1, Prenotazione p2) {
        if (p1.getTurno().getData().isBefore(p2.getTurno().getData())) return 1;
        if (p1.getTurno().getData().isAfter(p2.getTurno().getData())) return -1;
        return 0;
    }

}
