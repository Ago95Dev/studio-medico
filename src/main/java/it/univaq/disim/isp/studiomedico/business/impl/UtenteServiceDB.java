package it.univaq.disim.isp.studiomedico.business.impl;

import it.univaq.disim.isp.studiomedico.business.exceptions.*;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.domain.Medico;
import it.univaq.disim.isp.studiomedico.domain.Paziente;
import it.univaq.disim.isp.studiomedico.domain.Segretaria;
import it.univaq.disim.isp.studiomedico.domain.Utente;

import java.sql.*;
import java.util.Objects;


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
    public Utente autenticazione(String email, String password) throws BusinessException {
        Utente utente = null;
        String query = "select * from utenti where email=? and password=?";

        try (PreparedStatement s = con.prepareStatement(query)) {
            s.setString(1, email);
            s.setString(2, password);

            try (ResultSet rs = s.executeQuery()) {
                while (rs.next()) {
                    switch (rs.getString("ruolo")) {
                        case "medico":
                            utente = new Medico();
                            break;
                        case "segretaria":
                            utente = new Segretaria();
                            break;
                        case "paziente":
                            utente = new Paziente();
                            break;
                    }
                    Objects.requireNonNull(utente).setId(rs.getInt("id"));
                    utente.setEmail(rs.getString("email"));
                    utente.setCf(rs.getString("codice_fiscale"));
                    utente.setNome(rs.getString("nome"));
                    utente.setCognome(rs.getString("cognome"));
                    utente.setPassword("password");
                }
            }
        } catch (SQLException e) {
            throw new UtenteNotFoundException("Errore esecuzione query", e);
        }
        if (utente != null)
            return utente;
        throw new UtenteNotFoundException();
    }

    @Override
    public Utente registrazione(String username, String password, String nomeu, String cognomeu, String codicef, String email, String ruolo) throws BusinessException {
        return null;
    }
}
