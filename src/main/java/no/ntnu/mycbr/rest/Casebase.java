package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.ICaseBase;
import no.ntnu.mycbr.rest.App;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by kerstin on 05/08/16.
 */
public class Casebase {

    private final List<String> casebaseNames = new LinkedList<>();


    public Casebase() {

        de.dfki.mycbr.core.Project project = App.getProject();

        for (Map.Entry<String, ICaseBase> cb : project.getCaseBases().entrySet()) {
            casebaseNames.add(cb.getKey());
        }
    }
    
    public List<String> getCasebaseNames() {
        return casebaseNames;
    }
}
