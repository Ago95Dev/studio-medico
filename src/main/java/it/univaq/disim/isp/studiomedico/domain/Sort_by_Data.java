package it.univaq.disim.isp.studiomedico.domain;

import java.util.Comparator;

public class Sort_by_Data implements Comparator<Turno> {

    @Override
    public int compare(Turno t1, Turno t2) {
        if (t1.getData().isBefore(t2.getData())) return -1;
        if (t1.getData().isAfter(t2.getData())) return 1;
        return 0;
    }




}