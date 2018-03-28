package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.rest.App;
import no.ntnu.mycbr.utils.Helper;

import static no.ntnu.mycbr.rest.utils.CommonConstants.CASE_ID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 
 * @author Amar Jaiswal
 *
 */
public class Instances {

	private List<LinkedHashMap<String, String>> cases = new ArrayList<LinkedHashMap<String, String>>();

	public Instances(String casebaseName) {

		Project project = App.getProject();		
		Collection<Instance> instances = project.getCB(casebaseName).getCases();

		for (Instance instance : instances) {

			LinkedHashMap<String, String> casecontent;

			casecontent = new LinkedHashMap<String, String>(Helper.getSortedCaseContent(instance));
			casecontent.put(CASE_ID, instance.getName());

			cases.add(casecontent);
		}		
	}

	public List<LinkedHashMap<String, String>> getCasebaseInstances(){
		return cases;
	}
}
