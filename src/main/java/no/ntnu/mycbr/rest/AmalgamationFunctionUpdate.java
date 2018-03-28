package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.similarity.AmalgamationFct;

/**
 * 
 * @author Amar Jaiswal
 *
 */

public class AmalgamationFunctionUpdate {
	
	public AmalgamationFunctionUpdate(String amalgamationFunctionName) {
		Project project = App.getProject();
		
		for (AmalgamationFct func : project.getAvailableAmalgamFcts()) {
			if(func.getName().equalsIgnoreCase(amalgamationFunctionName)) {
				project.setActiveAmalgamFct(func);
			}
		}
		
		//Concept concept = project.getConceptByID("");
		//concept.removeInstance("");
		
		//project.getCB("").removeCase("");	
	}
}
