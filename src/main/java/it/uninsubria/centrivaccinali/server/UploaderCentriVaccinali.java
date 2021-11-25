package it.uninsubria.centrivaccinali.server;

import it.uninsubria.centrivaccinali.database.Database;
import it.uninsubria.centrivaccinali.enumerator.Qualificatore;
import it.uninsubria.centrivaccinali.enumerator.TipologiaCentro;
import it.uninsubria.centrivaccinali.models.CentroVaccinale;
import it.uninsubria.centrivaccinali.models.Indirizzo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

public class UploaderCentriVaccinali {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader( new FileReader("C:/Users/Luca/IdeaProjects/CentriVaccinali/src/main/resources/it/uninsubria/centrivaccinali/UML/centri_lombardia.csv"));

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

        Database db = new Database();
        db.connect("123abc", "123abc");
        for(CentroVaccinale cv: listCV) {
            System.out.println(cv);
            db.registraCentroVaccinale(cv);
        }
    }
}