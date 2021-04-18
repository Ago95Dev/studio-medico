package it.univaq.disim.isp.studiomedico.business.impl;

import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.business.exceptions.UtenteNotFoundException;
import it.univaq.disim.isp.studiomedico.domain.*;
import javafx.util.converter.DateTimeStringConverter;

import java.sql.*;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Timer;


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
                    utente.setTelefono(rs.getString("telefono"));
                    utente.setLuogoDiNascita(rs.getString("luogo_di_nascita"));
                    utente.setDataDiNascita(rs.getDate("data_di_nascita"));
                    if (utente instanceof Medico){
                        ((Medico) utente).setSpecializzazione(findSpecializzazionebyId(rs.getInt("id_specializzazione")));
                        ((Medico) utente).setContratto(findContrattobyId(rs.getInt("id_contratto")));
                       //((Medico) utente).setListaTurni("query su turni in base al medico");
                        ((Medico) utente).setNumeropresenze(rs.getInt("numeropresenze"));
                        ((Medico) utente).setNumerovisite(rs.getInt("numeroprestazioni"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new UtenteNotFoundException("Errore esecuzione query", e);
        }
        if (utente != null)
            return utente;
        throw new UtenteNotFoundException();
    }


    public Contratto findContrattobyId(int id_contratto) {
        String query = "select * from contratti where id=?";
        Contratto contratto = new Contratto();
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_contratto);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    contratto.setId(rs.getInt("id"));
                    switch (rs.getString("tipologia_contratto")) {
                        case "forfettario":
                            contratto.setTipo(TipologiaContratto.forfettario);
                            break;
                        case "presenze":
                            contratto.setTipo(TipologiaContratto.presenze);
                            break;
                        case "prestazioni":
                            contratto.setTipo(TipologiaContratto.prestazioni);
                            break;
                    }
                    contratto.setQuota(rs.getFloat("quota"));
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return  contratto;
    }

    public Specializzazione findSpecializzazionebyId(int id_specializzazione) {
        String query = "select * from specializzazioni where id=?";
        Specializzazione specializzazione = null;
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_specializzazione);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                   specializzazione = Specializzazione.valueOf(rs.getString("tipologia"));
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return specializzazione;
    }

    @Override
    public Utente registrazione(String password, String nome, String cognome, String codicef, String email, String telefono, String data, String luogo) throws BusinessException {
        Utente utente = null;
        String query = "insert into utenti(password,nome,cognome,codice_fiscale,email,telefono,data_di_nascita,luogo_di_nascita,ruolo)" + "values(?,?,?,?,?,?,?,?,?)";
        String query2 = "select * from utenti where codice_fiscale=?";

        try (PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, password);
            st.setString(2, nome);
            st.setString(3, cognome);
            st.setString(4, codicef);
            st.setString(5, email);
            st.setString(6,telefono);
            st.setDate(7, Date.valueOf(data));
            st.setString(8,luogo);
            st.setString(9,"paziente");

            int res = st.executeUpdate();


            try (PreparedStatement s1 = con.prepareStatement(query2)) {

                s1.setString(1, codicef);


                try (ResultSet rs = s1.executeQuery()) {
                    while (rs.next()) {
                        utente = new Paziente();
                        utente.setId(rs.getInt("id"));
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


    public Utente registrazioneMedico(String password, String nome, String cognome, String codicef, String email, String telefono, String data, String luogo, String specializzazione, String contratto, String turno, String oraInizio, String oraFine) throws BusinessException {
        Utente utente = null;
        String query = "insert into utenti(password,nome,cognome,codice_fiscale,email,telefono,data_di_nascita,luogo_di_nascita,id_specializzazione,id_contratto,ruolo,numeropresenze,numeroprestazioni)" + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String query2 = "select * from utenti where codice_fiscale=?";

        try (PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, password);
            st.setString(2, nome);
            st.setString(3, cognome);
            st.setString(4, codicef);
            st.setString(5, email);
            st.setString(6, telefono);
            st.setDate(7, Date.valueOf(data));
            st.setString(8, luogo);
            st.setInt(9, findIdSpecializzazione(specializzazione));
            st.setInt(10, findIdContratto(contratto));
            st.setString(11,"medico");
            st.setInt(12,0);
            st.setInt(13,0);
            int res = st.executeUpdate();


            try (PreparedStatement s1 = con.prepareStatement(query2)) {

                s1.setString(1, codicef);


                try (ResultSet rs = s1.executeQuery()) {
                    while (rs.next()) {
                        utente = new Medico();
                        utente.setId(rs.getInt("id"));
                        utente.setEmail(email);
                        utente.setCf(codicef);
                        utente.setRuolo(Ruolo.paziente);
                        utente.setNome(nome);
                        utente.setCognome(cognome);
                        utente.setPassword(password);
                        utente.setDataDiNascita(Date.valueOf(data));
                        utente.setLuogoDiNascita(luogo);
                        utente.setTelefono(telefono);
                        ((Medico) utente).setSpecializzazione(Specializzazione.valueOf(specializzazione));
                        inserisciTurno(((Medico) utente).getId(),turno,oraInizio,oraFine);
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

    public void inserisciTurno(int id_medico,String turno, String oraInizio, String oraFine) {
        String query = "insert into turni(id_medico,data,ora_inizio,ora_fine,accettato,in_corso)" + "values(?,?,?,?,?,?)";

        try (PreparedStatement st = con.prepareStatement(query)) {

            st.setInt(1, id_medico);
            st.setDate(2, Date.valueOf(turno));
            st.setString(3,oraInizio);
            st.setString(4, oraFine);
            st.setInt(5,0);
            st.setInt(6, 0);
            int res = st.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public int findIdContratto(String contratto) throws BusinessException{
        String query = "select * from contratti where tipologia_contratto=?";
        Integer idContratto = null;
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setString(1,contratto);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    idContratto = rs.getInt("id");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return idContratto;
    }

    public int findIdSpecializzazione(String specializzazione) throws BusinessException{
        String query = "select * from specializzazioni where tipologia=?";
        Integer idSpecializzazione = null;
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setString(1,specializzazione.toString());
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    idSpecializzazione = rs.getInt("id");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return idSpecializzazione;
    }
}
