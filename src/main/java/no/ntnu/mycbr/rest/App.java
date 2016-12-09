package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.io.CSVImporter;
import no.ntnu.mycbr.CBREngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collection;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {

    public static Project getProject() {
        return project;
    }

    private static CBREngine engine;
    private static Project project;

    public static void main(String[] args) throws Exception {


        engine = new CBREngine();
        project = engine.createProjectFromPRJ();


        Concept myconcept = project.getConceptByID("Trip");


        Collection<Instance> cases = project.getAllInstances();

        // removes all cases currently in the case_base
        for (Instance instance : cases) {
            project.removeCase(instance.getName());
        }


        // Imports the cases to the case_case
        CSVImporter csvImporter = new CSVImporter(System.getProperty("user.dir") + "/src/main/resources/cases.csv",myconcept);
        csvImporter.setSeparator(";");
        csvImporter.setSeparatorMultiple("!");


        csvImporter.readData();
        csvImporter.checkData();
        csvImporter.setCaseBase(myconcept.getProject().getCB("main_case_base"));
        csvImporter.addMissingDescriptions();
        csvImporter.addMissingValues();
        csvImporter.doImport();






        SpringApplication.run(App.class, args);
    }

}
