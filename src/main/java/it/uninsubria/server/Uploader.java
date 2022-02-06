//Centore Luca 740951 VA
//Lattarulo Luca 742597 VA
//Marelli Samuele 742495 VA
//Pintonello Christian 741112 VA
package it.uninsubria.server;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import it.uninsubria.server.database.DBHelper;
import it.uninsubria.server.database.Database;
import it.uninsubria.centrivaccinali.enumerator.Qualificatore;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.enumerator.Vaccino;
import it.uninsubria.centrivaccinali.models.*;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Classe che permette il caricamento di:<br>
 *  - Nuovi centri vaccinali<br>
 *  - Nuovi cittadini vaccinati<br>
 *  - Nuovi cittadini registrati<br>
 *  - Eventi avversi
 */
@SuppressWarnings("all")
public class Uploader {
    /** Riferimento a database */
    private static Database db;
    /** Connessione a database */
    private static Connection conn;
    /** Codice operazione registra centri vaccinali */
    private static final int REGISTRA_CENTRI = 1;
    /** Codice operazione registra vaccinati */
    private static final int REGISTRA_VACCINATI = 2;
    /** Codice operazione registra cittadini */
    private static final int REGISTRA_CITTADINI = 3;
    /** Codice operazione registra eventi avversi */
    private static final int REGISTRA_EVENTI = 4;
    /** Operazione da eseguire in base alle quattro sopra riportate */
    private static final int operation = 0;

    /**
     * Metodo main per l'esecuzione dell'uploader
     * @param args args param
     */
    public static void main(String[] args) {
        conn = DBHelper.getConnection();
        db = new Database();
        db.connettiDB();

        try {
            switch (operation) {
                case REGISTRA_CENTRI:
                    registraCentri();
                    break;
                case REGISTRA_VACCINATI:
                    registraVaccinatiRandom(10000);
                    break;
                case REGISTRA_CITTADINI:
                    registraCittadini();
                    break;
                case REGISTRA_EVENTI:
                    registraEventi();
                    break;
                default:
                    System.err.println("Numero selezione errato");
                    break;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo che consente di registare centri vaccinali automaticamente (conformi ai vincoli del progetto)
     * @throws IOException eccezione input/output file.
     */
    private static void registraCentri() throws IOException {
        BufferedReader br = new BufferedReader( new FileReader(Objects.requireNonNull(new URL(CentriVaccinali.class.getResource("other/centri_lombardia.csv").toString().replaceAll("%20", " "))).getFile()));

        String currentLine;
        List<CentroVaccinale> listCV = new ArrayList<>();
        while ((currentLine = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(currentLine, ";");

            Indirizzo ind = new Indirizzo();
            CentroVaccinale cv = new CentroVaccinale(st.nextElement().toString().trim(), ind, TipologiaCentro.values()[ThreadLocalRandom.current().nextInt(0, 3)]);
            ind.setQualificatore(Qualificatore.valueOf(st.nextElement().toString().trim()));
            ind.setNome(st.nextElement().toString().trim());
            ind.setCivico(st.nextElement().toString().trim());
            ind.setCap(Integer.parseInt(st.nextElement().toString().trim()));
            ind.setComune(st.nextElement().toString().trim());
            ind.setProvincia(st.nextElement().toString().trim());

            listCV.add(cv);
        }
        br.close();

        for(CentroVaccinale cv: listCV) {
            System.out.println(cv);
            db.registraCentroVaccinale(cv);
        }
    }

    /**
     * Metodo che consente di registare vaccinati automaticamente (conformi ai vincoli del progetto)
     * @param max numero di vaccinati da registare
     * @throws IOException eccezione input/output file.
     */
    private static void registraVaccinatiRandom(int max) throws IOException {

        BufferedReader brNome = new BufferedReader(new FileReader(new URL(CentriVaccinali.class.getResource("other/nomi.txt").toString().replaceAll("%20", " ")).getFile()));
        BufferedReader brCognome = new BufferedReader(new FileReader(new URL(CentriVaccinali.class.getResource("other/cognomi.txt").toString().replaceAll("%20", " ")).getFile()));
        BufferedReader brComune = new BufferedReader(new FileReader(new URL(CentriVaccinali.class.getResource("other/soloCC.txt").toString().replaceAll("%20", " ")).getFile()));
        BufferedReader brCentri = new BufferedReader(new FileReader(new URL(CentriVaccinali.class.getResource("other/centri.txt").toString().replaceAll("%20", " ")).getFile()));
        String currentLine;

        HashMap<String, String> nomi_e_genere = new HashMap<>();
        while ((currentLine = brNome.readLine()) != null) {
            StringTokenizer stNome = new StringTokenizer(currentLine, "\t");
            nomi_e_genere.put(stNome.nextToken().trim(), stNome.nextToken().trim());
        }
        //System.out.println(nomi_e_genere);

        List<String> cognomi = new ArrayList<>();
        while ((currentLine = brCognome.readLine()) != null) {
            cognomi.add(currentLine.trim());
        }
        //System.out.println(cognomi);

        //HashMap<String, String> comuni = new HashMap<>();
        List<String> comuni = new ArrayList<>();
        while ((currentLine = brComune.readLine()) != null) {
            comuni.add(currentLine.trim());
        }
        //System.out.println(comuni);

        List<String> centri = new ArrayList<>();
        while ((currentLine = brCentri.readLine()) != null) {
            centri.add(currentLine.trim());
        }

        for (int j = 0; j < max; j++) {

            String codFis = "";

            int numero_a_caso = ThreadLocalRandom.current().nextInt(nomi_e_genere.size() - 1);

            String nomecf = nomi_e_genere.keySet().toArray()[numero_a_caso].toString();
            String genere = nomi_e_genere.get(nomecf);
            String cognomecf = cognomi.get(ThreadLocalRandom.current().nextInt(cognomi.size() - 1));

            /*calcolo prime 3 lettere */
            int cont = 0;
            /*caso cognome minore di 3 lettere*/
            if (cognomecf.length() < 3) {
                codFis += cognomecf;
                while (codFis.length() < 3) codFis += "X";
                cont = 3;
            }
            /*caso normale*/
            for (int i = 0; i < cognomecf.length(); i++) {
                if (cont == 3) break;
                if (cognomecf.charAt(i) != 'A' && cognomecf.charAt(i) != 'E' &&
                        cognomecf.charAt(i) != 'I' && cognomecf.charAt(i) != 'O' &&
                        cognomecf.charAt(i) != 'U') {
                    codFis += Character.toString(cognomecf.charAt(i));
                    cont++;
                }
            }

            /* nel caso ci siano meno di 3 consonanti*/
            while (cont < 3) {
                for (int i = 0; i < cognomecf.length(); i++) {
                    if (cont == 3) break;
                    if (cognomecf.charAt(i) == 'A' || cognomecf.charAt(i) == 'E' ||
                            cognomecf.charAt(i) == 'I' || cognomecf.charAt(i) == 'O' ||
                            cognomecf.charAt(i) == 'U') {
                        codFis += Character.toString(cognomecf.charAt(i));
                        cont++;
                    }
                }
            }

            /*lettere nome*/
            cont = 0;

            /*caso nome minore di 3 lettere*/
            if (nomecf.length() < 3) {
                codFis += nomecf;
                while (codFis.length() < 6) codFis += "X";
                cont = 3;
            }

            /*caso normale*/
            for (int i = 0; i < nomecf.length(); i++) {
                if (cont == 3) break;
                if (nomecf.charAt(i) != 'A' && nomecf.charAt(i) != 'E' &&
                        nomecf.charAt(i) != 'I' && nomecf.charAt(i) != 'O' &&
                        nomecf.charAt(i) != 'U') {
                    codFis += Character.toString(nomecf.charAt(i));
                    cont++;
                }
            }

            /* nel caso ci siano meno di 3 consonanti*/
            while (cont < 3) {
                for (int i = 0; i < nomecf.length(); i++) {
                    if (cont == 3) break;
                    if (nomecf.charAt(i) == 'A' || nomecf.charAt(i) == 'E' ||
                            nomecf.charAt(i) == 'I' || nomecf.charAt(i) == 'O' ||
                            nomecf.charAt(i) == 'U') {
                        codFis += Character.toString(nomecf.charAt(i));
                        cont++;
                    }
                }
            }


            // GENERATORE DATA
            long startMillis = new Date(1921 - 1900, Calendar.JANUARY, 1).getTime();
            long endMillis = new Date(2008 - 1900, Calendar.DECEMBER, 31).getTime();
            long randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
            Date data = new Date(randomMillisSinceEpoch);

            int anno = Integer.parseInt(String.valueOf(data.getYear()).substring(1, 2));
            int mese = data.getMonth() + 1;
            int giorno = data.getDate();
            //.out.println("anno: " + anno + " mese: " + mese + " giorno: " + giorno);

            /* anno */
            if (anno < 10) {
                codFis += "0";
            }
            codFis += anno;

            /* Mese */
            switch (mese) {
                case 1: {
                    codFis += "A";
                    break;
                }
                case 2: {
                    codFis += "B";
                    break;
                }
                case 3: {
                    codFis += "C";
                    break;
                }
                case 4: {
                    codFis += "D";
                    break;
                }
                case 5: {
                    codFis += "E";
                    break;
                }
                case 6: {
                    codFis += "H";
                    break;
                }
                case 7: {
                    codFis += "L";
                    break;
                }
                case 8: {
                    codFis += "M";
                    break;
                }
                case 9: {
                    codFis += "P";
                    break;
                }
                case 10: {
                    codFis += "R";
                    break;
                }
                case 11: {
                    codFis += "S";
                    break;
                }
                case 12: {
                    codFis += "T";
                    break;
                }
            }

            /*giorno*/
            if (Objects.equals(genere, "M")) {
                if (giorno < 10) {
                    codFis += "0";
                }
            }
            else {
                giorno += 40;
            }
            codFis += giorno;

            /* COMUNE */
            codFis += comuni.get(ThreadLocalRandom.current().nextInt(comuni.size()-1));

            /*Carattere di controllo*/
            int sommaPari = 0;
            for (int i = 1; i <= 13; i += 2) {
                switch (codFis.charAt(i)) {
                    case '0':
                    case 'A': {
                        sommaPari += 0;
                        break;
                    }
                    case '1':
                    case 'B': {
                        sommaPari += 1;
                        break;
                    }
                    case '2':
                    case 'C': {
                        sommaPari += 2;
                        break;
                    }
                    case '3':
                    case 'D': {
                        sommaPari += 3;
                        break;
                    }
                    case '4':
                    case 'E': {
                        sommaPari += 4;
                        break;
                    }
                    case '5':
                    case 'F': {
                        sommaPari += 5;
                        break;
                    }
                    case '6':
                    case 'G': {
                        sommaPari += 6;
                        break;
                    }
                    case '7':
                    case 'H': {
                        sommaPari += 7;
                        break;
                    }
                    case '8':
                    case 'I': {
                        sommaPari += 8;
                        break;
                    }
                    case '9':
                    case 'J': {
                        sommaPari += 9;
                        break;
                    }
                    case 'K': {
                        sommaPari += 10;
                        break;
                    }
                    case 'L': {
                        sommaPari += 11;
                        break;
                    }
                    case 'M': {
                        sommaPari += 12;
                        break;
                    }
                    case 'N': {
                        sommaPari += 13;
                        break;
                    }
                    case 'O': {
                        sommaPari += 14;
                        break;
                    }
                    case 'P': {
                        sommaPari += 15;
                        break;
                    }
                    case 'Q': {
                        sommaPari += 16;
                        break;
                    }
                    case 'R': {
                        sommaPari += 17;
                        break;
                    }
                    case 'S': {
                        sommaPari += 18;
                        break;
                    }
                    case 'T': {
                        sommaPari += 19;
                        break;
                    }
                    case 'U': {
                        sommaPari += 20;
                        break;
                    }
                    case 'V': {
                        sommaPari += 21;
                        break;
                    }
                    case 'W': {
                        sommaPari += 22;
                        break;
                    }
                    case 'X': {
                        sommaPari += 23;
                        break;
                    }
                    case 'Y': {
                        sommaPari += 24;
                        break;
                    }
                    case 'Z': {
                        sommaPari += 25;
                        break;
                    }
                }
            }
            int sommaDispari = 0;
            for (int i = 0; i <= 14; i += 2) {
                switch (codFis.charAt(i)) {
                    case '0':
                    case 'A': {
                        sommaDispari += 1;
                        break;
                    }
                    case '1':
                    case 'B': {
                        sommaDispari += 0;
                        break;
                    }
                    case '2':
                    case 'C': {
                        sommaDispari += 5;
                        break;
                    }
                    case '3':
                    case 'D': {
                        sommaDispari += 7;
                        break;
                    }
                    case '4':
                    case 'E': {
                        sommaDispari += 9;
                        break;
                    }
                    case '5':
                    case 'F': {
                        sommaDispari += 13;
                        break;
                    }
                    case '6':
                    case 'G': {
                        sommaDispari += 15;
                        break;
                    }
                    case '7':
                    case 'H': {
                        sommaDispari += 17;
                        break;
                    }
                    case '8':
                    case 'I': {
                        sommaDispari += 19;
                        break;
                    }
                    case '9':
                    case 'J': {
                        sommaDispari += 21;
                        break;
                    }
                    case 'K': {
                        sommaDispari += 2;
                        break;
                    }
                    case 'L': {
                        sommaDispari += 4;
                        break;
                    }
                    case 'M': {
                        sommaDispari += 18;
                        break;
                    }
                    case 'N': {
                        sommaDispari += 20;
                        break;
                    }
                    case 'O': {
                        sommaDispari += 11;
                        break;
                    }
                    case 'P': {
                        sommaDispari += 3;
                        break;
                    }
                    case 'Q': {
                        sommaDispari += 6;
                        break;
                    }
                    case 'R': {
                        sommaDispari += 8;
                        break;
                    }
                    case 'S': {
                        sommaDispari += 12;
                        break;
                    }
                    case 'T': {
                        sommaDispari += 14;
                        break;
                    }
                    case 'U': {
                        sommaDispari += 16;
                        break;
                    }
                    case 'V': {
                        sommaDispari += 10;
                        break;
                    }
                    case 'W': {
                        sommaDispari += 22;
                        break;
                    }
                    case 'X': {
                        sommaDispari += 25;
                        break;
                    }
                    case 'Y': {
                        sommaDispari += 24;
                        break;
                    }
                    case 'Z': {
                        sommaDispari += 23;
                        break;
                    }
                }
            }
            int interoControllo = (sommaPari + sommaDispari) % 26;
            String carattereControllo = "";
            switch (interoControllo) {
                case 0: {
                    carattereControllo = "A";
                    break;
                }
                case 1: {
                    carattereControllo = "B";
                    break;
                }
                case 2: {
                    carattereControllo = "C";
                    break;
                }
                case 3: {
                    carattereControllo = "D";
                    break;
                }
                case 4: {
                    carattereControllo = "E";
                    break;
                }
                case 5: {
                    carattereControllo = "F";
                    break;
                }
                case 6: {
                    carattereControllo = "G";
                    break;
                }
                case 7: {
                    carattereControllo = "H";
                    break;
                }
                case 8: {
                    carattereControllo = "I";
                    break;
                }
                case 9: {
                    carattereControllo = "J";
                    break;
                }
                case 10: {
                    carattereControllo = "K";
                    break;
                }
                case 11: {
                    carattereControllo = "L";
                    break;
                }
                case 12: {
                    carattereControllo = "M";
                    break;
                }
                case 13: {
                    carattereControllo = "N";
                    break;
                }
                case 14: {
                    carattereControllo = "O";
                    break;
                }
                case 15: {
                    carattereControllo = "P";
                    break;
                }
                case 16: {
                    carattereControllo = "Q";
                    break;
                }
                case 17: {
                    carattereControllo = "R";
                    break;
                }
                case 18: {
                    carattereControllo = "S";
                    break;
                }
                case 19: {
                    carattereControllo = "T";
                    break;
                }
                case 20: {
                    carattereControllo = "U";
                    break;
                }
                case 21: {
                    carattereControllo = "V";
                    break;
                }
                case 22: {
                    carattereControllo = "W";
                    break;
                }
                case 23: {
                    carattereControllo = "X";
                    break;
                }
                case 24: {
                    carattereControllo = "Y";
                    break;
                }
                case 25: {
                    carattereControllo = "Z";
                    break;
                }
            }
            codFis += carattereControllo;

            Vaccinato vac = new Vaccinato();
            vac.setNome(nomecf);
            vac.setCognome(cognomecf);
            vac.setCodiceFiscale(codFis);

            // GENERATORE DATA
            startMillis = new Date(2020 - 1900, Calendar.DECEMBER, 27).getTime();
            endMillis = new Date(2021 - 1900, Calendar.NOVEMBER, 26).getTime();
            randomMillisSinceEpoch = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
            data = new Date(randomMillisSinceEpoch);

            vac.setDataSomministrazione(new java.sql.Date(data.getTime()));

            // ID VAC
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
            String stringID = sdf.format(data);
            stringID=stringID.substring(0, 16);
            vac.setIdVaccino(Long.parseLong(stringID));


            vac.setVaccinoSomministrato(Vaccino.values()[ThreadLocalRandom.current().nextInt(0,4)]);

            vac.setNomeCentro(centri.get(ThreadLocalRandom.current().nextInt(0,centri.size())));

            db.registraVaccinato(vac);
        }
        brNome.close();
        brCentri.close();
        brCognome.close();
        brComune.close();
    }

    /**
     * Metodo che consente di registare cittadini automaticamente (conformi ai vincoli del progetto)
     * @throws SQLException eccezione sql database.
     * @throws IOException eccezione input/output file.
     */
    private static void registraCittadini() throws SQLException, IOException {

        BufferedReader brTab = new BufferedReader(new FileReader(new URL(CentriVaccinali.class.getResource("other/tabelleCentri.txt").toString().replaceAll("%20", " ")).getFile()));
        BufferedReader brPwd = new BufferedReader(new FileReader(new URL(CentriVaccinali.class.getResource("other/passwords.txt").toString().replaceAll("%20", " ")).getFile()));
        String currentTab;
        String currentPwd;

        int count = 0;
        List<String> listPwd = new ArrayList<>();

        while ((currentPwd = brPwd.readLine()) != null) {
            listPwd.add(currentPwd);
        }

        List<String> domini = new ArrayList<>();
        domini.add("@gmail.com");
        domini.add("@outlook.com");
        domini.add("@yahoo.it");
        domini.add("@hotmail.com");
        domini.add("@mail.com");
        domini.add("@libero.it");
        domini.add("@virgilio.com");

        while ((currentTab = brTab.readLine()) != null) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT *" +
                                            "FROM tabelle_cv.\"" + currentTab.trim() + "\"");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Cittadino c = new Cittadino();
                c.setNome(rs.getString("nome"));
                c.setCognome(rs.getString("cognome"));
                c.setCodice_fiscale(rs.getString("codice_fiscale"));
                c.setId_vaccinazione(rs.getLong("id_vaccinazione"));
                c.setEmail(c.getNome().toLowerCase() + "." + c.getCognome().toLowerCase() + domini.get(ThreadLocalRandom.current().nextInt(0, domini.size())));
                c.setUserid(c.getNome().substring(0,1).toLowerCase() + c.getCognome().toLowerCase() + ThreadLocalRandom.current().nextInt(10,100));
                c.setPassword(listPwd.get(count++));
                System.out.println(c);
                db.registraCittadino(c);
            }
        }

        brTab.close();
        brPwd.close();
    }

    /**
     * Metodo che consente di registare eventi avversi automaticamente (conformi ai vincoli del progetto)
     * @throws SQLException eccezione sql database.
     */
    private static void registraEventi() throws SQLException {
        List<String> eventi = new ArrayList<>();
        eventi.add("mal di testa");
        eventi.add("febbre");
        eventi.add("dolori muscolari e articolari");
        eventi.add("linfoadenopatia");
        eventi.add("tachicardia");
        eventi.add("crisi chipertensiva");
        PreparedStatement pstmt = conn.prepareStatement("SELECT id_vaccinazione " +
                                                            "FROM public.\"Cittadini_Registrati\"");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            long id = rs.getLong(1);
            if(ThreadLocalRandom.current().nextBoolean()) {
                List<String> eventiRandom = new ArrayList<>(eventi);
                int numeroEventi = ThreadLocalRandom.current().nextInt(eventi.size());
                for(int i=0; i < numeroEventi; i++) {
                    int eventoacaso = ThreadLocalRandom.current().nextInt(eventiRandom.size());
                    int severita = 0;
                    switch (eventoacaso) {
                        case 0:
                            severita = ThreadLocalRandom.current().nextInt(1, 3);
                            break;
                        case 1:
                            severita = ThreadLocalRandom.current().nextInt(5, 6);
                            break;
                        case 2:
                            severita = ThreadLocalRandom.current().nextInt(3, 5);
                            break;
                        case 3:
                            severita = ThreadLocalRandom.current().nextInt(3, 4);
                            break;
                        case 4:
                            severita = ThreadLocalRandom.current().nextInt(2, 5);
                            break;
                        case 5:
                            severita = ThreadLocalRandom.current().nextInt(1, 2);
                            break;
                    }
                    String nota = "";
                    if(ThreadLocalRandom.current().nextInt(100) > 70) {
                        switch (ThreadLocalRandom.current().nextInt(3)) {
                            case 0:
                                nota = "Questo evento ha severita: " + severita;
                                break;
                            case 1:
                                nota = "Evento di tipo: " + eventiRandom.get(eventoacaso);
                                break;
                            case 2:
                                nota = "Evento: " + eventiRandom.get(eventoacaso) + " ha una severita di: " + severita;
                                break;
                        }
                    }
                    EventoAvverso ea = new EventoAvverso(id, eventiRandom.get(eventoacaso), severita, nota);
                    eventiRandom.remove(eventoacaso);
                    db.registraEA(ea);
                }
            }
        }
    }
}