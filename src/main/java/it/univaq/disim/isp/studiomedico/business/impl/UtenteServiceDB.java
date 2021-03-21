package it.univaq.disim.isp.studiomedico.business.impl;

import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.domain.Utente;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class UtenteServiceDB extends ConnessioneDB implements UtenteService {

    private static Connection con;

    public UtenteServiceDB() throws BusinessException {

        try {
            con = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new BusinessException("Errore di connessione");
        }
    }

    @Override
    public Utente autenticazione(String username, String password) throws BusinessException {
        return null;
    }

    @Override
    public Utente registrazione(String username, String password, String nomeu, String cognomeu, String codicef, String email, String ruolo) throws BusinessException {
        return null;
    }
}
