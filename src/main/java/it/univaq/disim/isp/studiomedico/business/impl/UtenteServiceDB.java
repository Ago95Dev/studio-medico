package it.univaq.disim.isp.studiomedico.business.impl;

import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.business.exceptions.UtenteNotFoundException;
import it.univaq.disim.isp.studiomedico.domain.*;

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
    public Utente registrazione(String password, String nome, String cognome, String codicef, String email, String telefono, String data, String luogo) throws BusinessException {
        Utente utente = null;
        String query = "insert into utenti(password,nome,cognome,codice_fiscale,email,telefono,data_di_nascita,luogo_di_nascita)" + "values(?,?,?,?,?,?,?,?)";
        String query2 = "select * from utenti where codice_fiscale=?";

        try (PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, password);
            st.setString(2, nome);
            st.setString(3, cognome);
            st.setString(4, codicef);
            st.setString(5, email);
            st.setString(6, String.valueOf(data));

            int res = st.executeUpdate();


            try (PreparedStatement s1 = con.prepareStatement(query2)) {

                s1.setString(1, codicef);


                try (ResultSet rs = s1.executeQuery()) {
                    while (rs.next()) {
                        utente = new Paziente();
                        utente.setId(rs.getInt("Id"));
                        utente.setEmail(email);
                        utente.setCf(codicef);
                        utente.setRuolo(Ruolo.paziente);
                        utente.setNome(nome);
                        utente.setCognome(cognome);
                        utente.setPassword(password);
                        utente.setDataDiNascita(Date.valueOf(data));
                        utente.setLuogoDiNascita(luogo);
                        utente.setTelefono(telefono);

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new UtenteNotFoundException("Errore esecuzione query", e);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return utente;
    }

    // errore nell'inserimento della specializzazione. nel nostro db è un id che fa riferimento in un'altra tabella, non è una stringa
    // aggiungere la query che ritorna l'id_specializzazione da aggiungere nell'insert del medico
    public Utente registrazioneMedico(String password, String nome, String cognome, String codicef, String email, String telefono, String data, String luogo, Specializzazione specializzazione) throws BusinessException {
        Utente utente = null;
        String query = "insert into utenti(password,nome,cognome,codice_fiscale,email,telefono,data_di_nascita,luogo_di_nascita,specializzazione)" + "values(?,?,?,?,?,?,?,?,?)";
        String query2 = "select * from utenti where codice_fiscale=?";

        try (PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, password);
            st.setString(2, nome);
            st.setString(3, cognome);
            st.setString(4, codicef);
            st.setString(5, email);
            st.setString(6, String.valueOf(data));
            st.setString(7, String.valueOf(specializzazione));
            int res = st.executeUpdate();


            try (PreparedStatement s1 = con.prepareStatement(query2)) {

                s1.setString(1, codicef);


                try (ResultSet rs = s1.executeQuery()) {
                    while (rs.next()) {
                        utente = new Medico();
                        utente.setId(rs.getInt("Id"));
                        utente.setEmail(email);
                        utente.setCf(codicef);
                        utente.setRuolo(Ruolo.paziente);
                        utente.setNome(nome);
                        utente.setCognome(cognome);
                        utente.setPassword(password);
                        utente.setDataDiNascita(Date.valueOf(data));
                        utente.setLuogoDiNascita(luogo);
                        utente.setTelefono(telefono);
                        ((Medico) utente).setSpecializzazione(specializzazione);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new BusinessException("Errore esecuzione query", e);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return utente;
    }
}
