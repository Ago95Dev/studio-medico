package it.univaq.disim.isp.studiomedico.business.impl;

import it.univaq.disim.isp.studiomedico.business.PrenotazioneService;
import it.univaq.disim.isp.studiomedico.business.SegretariaService;
import it.univaq.disim.isp.studiomedico.business.UtenteService;
import it.univaq.disim.isp.studiomedico.business.exceptions.BusinessException;
import it.univaq.disim.isp.studiomedico.domain.*;
import it.univaq.disim.isp.studiomedico.utility.BCrypt;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SegretariaServiceDB extends ConnessioneDB implements SegretariaService {

    private static Connection con;

    public SegretariaServiceDB() throws BusinessException {

        try {
            con = DriverManager.getConnection(CONNECTION_STRING, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new BusinessException("Errore di connessione");
        }
    }

    @Override
    public Utente registrazioneMedico(String password, String nome, String cognome, String codicef, String email, String telefono, String data, String luogo, String specializzazione, String contratto, String turno, String oraInizio, String oraFine) throws BusinessException {
        Utente utente = null;
        String query = "insert into utenti(password,nome,cognome,codice_fiscale,email,telefono,data_di_nascita,luogo_di_nascita,id_specializzazione,id_contratto,ruolo,numeropresenze,numeroprestazioni)" + "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String query2 = "select * from utenti where codice_fiscale=?";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));

        try (PreparedStatement st = con.prepareStatement(query)) {

            st.setString(1, hashed);
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
                        utente.setPassword(rs.getString("password"));
                        utente.setDataDiNascita(Date.valueOf(data));
                        utente.setLuogoDiNascita(luogo);
                        utente.setTelefono(telefono);
                        ((Medico) utente).setSpecializzazione(Specializzazione.valueOf(specializzazione));
                        inserisciTurni(((Medico) utente).getId(),turno,oraInizio,oraFine);
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


    public void inserisciTurni(int id_medico, String turno, String oraInizio, String oraFine){
        List<Turno> listaTurni = new LinkedList<>();
        LocalDate data = LocalDate.parse(turno);
        for (int i = 0; i < 8; i++) {
            listaTurni.add(new Turno(data, LocalTime.parse(oraInizio),LocalTime.parse(oraFine)));
            data = data.plus(1, ChronoUnit.WEEKS);
        }
        for (Turno t: listaTurni) {
            inserisciTurno(id_medico,t.getData().toString(),t.getOrainizio().toString(),t.getOrafine().toString());
            //System.out.println(turno.getData().getDayOfWeek() + " " + turno.getData().getDayOfMonth() + " " + turno.getData().getMonth() + " " + turno.getData().getYear() + " " + turno.getOrainizio() + " " + turno.getOrafine());
        }
    }

    public void inserisciTurno(int id_medico,String turno, String oraInizio, String oraFine) {
        String query = "insert into turni(id_medico,data,ora_inizio,ora_fine,accettato,in_corso)" + "values(?,?,?,?,?,?)";

        try (PreparedStatement st = con.prepareStatement(query)) {

            st.setInt(1, id_medico);
            st.setDate(2, Date.valueOf(turno));
            st.setString(3,oraInizio);
            st.setString(4, oraFine);
            st.setInt(5,1);
            st.setInt(6, 0);
            int res = st.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void inserisciTurnoProvvisorio(int id_medico, String turno, String oraInizio, String oraFine) {
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

    @Override
    public List<Turno> getTurniProposti() {
        String query2 = "select * from turni where accettato=0";
        List<Turno> listaTurni = new LinkedList<>();
        try (PreparedStatement s2 = con.prepareStatement(query2)) {
            try (ResultSet rs = s2.executeQuery()) {
                while (rs.next()) {
                    Turno turno = new Turno();
                    turno.setId(rs.getInt("id"));
                    turno.setMedico(getMedicoById(rs.getInt("id_medico")));
                    turno.setData(rs.getDate("data").toLocalDate());
                    turno.setOrainizio(rs.getTime("ora_inizio").toLocalTime());
                    turno.setOrafine(rs.getTime("ora_fine").toLocalTime());
                    turno.setAccettato(rs.getBoolean("accettato"));
                    turno.setIncorso(rs.getBoolean("in_corso"));
                    listaTurni.add(turno);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return listaTurni;
    }

    @Override
    public void accettaTurnoProposto(Turno turno) {
        String query = "UPDATE turni SET accettato = '1' WHERE id = ?";

        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, turno.getId());
            int resultSet = st.executeUpdate();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        inserisciTurni(turno.getMedico().getId(),turno.getData().plusWeeks(1).toString(),turno.getOrainizio().toString(),turno.getOrafine().toString());
    }

    @Override
    public void rifiutaTurnoProposto(Turno turnostore) {
        String query = "DELETE FROM turni WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, turnostore.getId());
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Prenotazione> getPrenotazioniByNow(LocalDate now) {
        List<Prenotazione> listaPrenotazioni = new LinkedList<>();
        List<Turno> listaTurni = new LinkedList<>();
        String query = "select * from turni where data=?";
        String query2 = "select * from prenotazioni where id_turno=?";
        try (PreparedStatement st = con.prepareStatement(query)) {
                st.setDate(1, Date.valueOf(now));
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Turno turno = new Turno();
                    turno.setId(rs.getInt("id"));
                    turno.setMedico(getMedicoById(rs.getInt("id_medico")));
                    turno.setData(rs.getDate("data").toLocalDate());
                    turno.setOrainizio(rs.getTime("ora_inizio").toLocalTime());
                    turno.setOrafine(rs.getTime("ora_fine").toLocalTime());
                    turno.setAccettato(rs.getBoolean("accettato"));
                    turno.setIncorso(rs.getBoolean("in_corso"));
                    listaTurni.add(turno);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }

        for (Turno t: listaTurni) {
            try (PreparedStatement st = con.prepareStatement(query2)) {
                st.setInt(1, t.getId());
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        Prenotazione prenotazione = new Prenotazione();
                        prenotazione.setId(rs.getInt("id"));
                        prenotazione.setMedico(getMedicoById(rs.getInt("id_medico")));
                        prenotazione.setPaziente(getPazienteById(rs.getInt("id_paziente")));
                        prenotazione.setOrainizio(rs.getTime("inizio").toLocalTime());
                        prenotazione.setOrafine(rs.getTime("fine").toLocalTime());
                        prenotazione.setVisita(getVisitaById(rs.getInt("id_tipo_visita")));
                        prenotazione.setCheckin(rs.getBoolean("checkin"));
                        prenotazione.setCheckout(rs.getBoolean("checkout"));
                        prenotazione.setTurno(t);
                        listaPrenotazioni.add(prenotazione);
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    throw new BusinessException("Errore esecuzione query", e);
                }
            }catch (SQLException | BusinessException throwables) {
                throwables.printStackTrace();
            }
        }
        return listaPrenotazioni;
    }

    @Override
    public void updateCheckin(Prenotazione prenotazione) {
        String query = "UPDATE prenotazioni SET checkin = '1' WHERE id = ?";

        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, prenotazione.getId());
            int resultSet = st.executeUpdate();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateInCorsoTurno(Turno turno) {
        String query = "UPDATE turni SET in_corso = '1' WHERE id = ?";

        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, turno.getId());
            int resultSet = st.executeUpdate();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        inserisciTurno(turno.getMedico().getId(),turno.getData().plusWeeks(8).toString(),turno.getOrainizio().toString(),turno.getOrafine().toString());
    }

    @Override
    public void updatePresenzeMedico(Medico medico) {
        String query = "UPDATE utenti SET numeropresenze = ? WHERE id = ?";

        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1,medico.getNumeropresenze() + 1);
            st.setInt(2, medico.getId());
            int resultSet = st.executeUpdate();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updatePrestazioniMedico(Medico medico) {
        String query = "UPDATE utenti SET numeroprestazioni = ? WHERE id = ?";

        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1,medico.getNumerovisite() + 1);
            st.setInt(2, medico.getId());
            int resultSet = st.executeUpdate();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateCheckout(Prenotazione prenotazione) {
        String query = "UPDATE prenotazioni SET checkout = '1' WHERE id = ?";

        try (PreparedStatement st = con.prepareStatement(query)) {
            st.setInt(1, prenotazione.getId());
            int resultSet = st.executeUpdate();
        }catch (SQLException throwables) {
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



    private Medico getMedicoById(int id_medico) {
        String query = "select * from utenti where id=? and ruolo=?";
        Medico medico = new Medico();
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_medico);
            st.setString(2,"medico");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    Objects.requireNonNull(medico).setId(rs.getInt("id"));
                    medico.setNome(rs.getString("nome"));
                    medico.setCognome(rs.getString("cognome"));
                    medico.setSpecializzazione(findSpecializzazionebyId(rs.getInt("id_specializzazione")));
                    medico.setNumeropresenze(rs.getInt("numeropresenze"));
                    medico.setNumerovisite(rs.getInt("numeroprestazioni"));
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return medico;
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


    public Visita getVisitaById(int id_tipo_visita) {
        String query = "select * from tipi_visita where id=?";
        Visita visita = new Visita();
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_tipo_visita);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    Objects.requireNonNull(visita).setId(rs.getInt("id"));
                    visita.setNome(rs.getString("tipo"));
                    switch (rs.getString("durata")) {
                        case "30:00:00":
                            visita.setDurata(Duration.ofMinutes(30));
                            break;
                        case "60:00:00":
                            visita.setDurata(Duration.ofMinutes(60));
                            break;
                        case "120:00:00":
                            visita.setDurata(Duration.ofMinutes(120));
                            break;
                    }
                    visita.setPrezzo(rs.getFloat("prezzo"));
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return visita;

    }

    public Utente getPazienteById(int id_paziente) {
        String query = "select * from utenti where id=?"; /*and ruolo=?*/
        Utente paziente = new Utente();
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setInt(1,id_paziente);
            //st.setString(2,"paziente");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    Objects.requireNonNull(paziente).setId(rs.getInt("id"));
                    paziente.setEmail(rs.getString("email"));
                    paziente.setCf(rs.getString("codice_fiscale"));
                    paziente.setNome(rs.getString("nome"));
                    paziente.setCognome(rs.getString("cognome"));
                    paziente.setPassword("password");
                    paziente.setTelefono(rs.getString("telefono"));
                    paziente.setLuogoDiNascita(rs.getString("luogo_di_nascita"));
                    paziente.setDataDiNascita(rs.getDate("data_di_nascita"));
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new BusinessException("Errore esecuzione query", e);
            }
        }catch (SQLException | BusinessException throwables) {
            throwables.printStackTrace();
        }
        return paziente;
    }





}
