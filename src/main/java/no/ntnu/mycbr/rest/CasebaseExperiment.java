package no.ntnu.mycbr.rest;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.ICaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Attribute;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.similarity.AmalgamationFct;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.core.util.Pair;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctManager;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctNotChangedException;

public class CasebaseExperiment {//extends TestCase {

	//HashMap<String, ICaseBase> casebases;
	private Project project;
	// create case bases and assign the case bases that will be used for submitting a query
	DefaultCaseBase cb;
	// create a concept and get the main concept of the project;
	Concept myConcept;

	AmalgamationFct amalgamationFct;

	int k = -1;

	TemporaryAmalgamFctManager tempAmalgamFctManager;
	
	private LinkedHashMap<String, LinkedHashMap<String, Double>> allResults = new LinkedHashMap<>();

	public CasebaseExperiment(String casebase, String concept) {
		this.project = App.getProject();
		this.cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
		this.myConcept = project.getConceptByID(concept);
		this.tempAmalgamFctManager = new TemporaryAmalgamFctManager(myConcept);
	}

	public CasebaseExperiment(String casebase, String concept, String amalFunc, int k) {		
		this(casebase, concept);

		try {
			tempAmalgamFctManager.changeAmalgamFct(amalFunc);
		} catch (TemporaryAmalgamFctNotChangedException e) {
			e.printStackTrace();
		}

		this.k = k;
	}
	
	public LinkedHashMap<String, LinkedHashMap<String, Double>> dependentCaseBaseExeperiment(List<String> caseList) {
//		String cbName = "support_prim_cb_latest";
		String cbTemp = "temp_cb";

		ICaseBase cbFrom = cb;
		ICaseBase tempCB = createEmptyCasebase(cbTemp, 1000);

		teansferCases(caseList, cbFrom, tempCB);

//		System.out.println(tempCB.getCases());
		
		Collection<Instance> instances = getAllInstances(tempCB);

		for(Instance instance: instances) {
			String key = instance.getName();
			allResults.putIfAbsent(key, query(tempCB, key));
		}
		
		return allResults;
	}

	public LinkedHashMap<String, LinkedHashMap<String, Double>> independentCaseBaseExeperiment(List<String> retrivalCaseList, 
			List<String> casebaseCaseList) {
//		String cbName = "support_prim_cb_latest";
		String cbTemp = "temp_cb";

		ICaseBase cbFrom = cb;
		ICaseBase tempCB = createEmptyCasebase(cbTemp, 1000);

		teansferCases(casebaseCaseList, cbFrom, tempCB);

//		System.out.println(tempCB.getCases());
		
		for(String key: retrivalCaseList) {
			allResults.putIfAbsent(key, query(tempCB, key));
		}
		
		return allResults;
	}
	
	private Collection<Instance> getAllInstances(ICaseBase casebase) {
		return casebase.getCases(); 
	}

	private void teansferCases(List<String> caseIds, ICaseBase cbFrom, ICaseBase cbTo) {

		Collection<Instance> instances = cbFrom.getCases();

		for(Instance instance : instances) {
			if(caseIds.contains(instance.getName())) {
				cbTo.addCase(instance);
			}
		}
	}

	private ICaseBase createEmptyCasebase(String cbName, int caseCount) {
		ICaseBase cb = null;
		try {
			cb = new DefaultCaseBase(this.project, cbName, caseCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cb;
	}

	private LinkedHashMap<String, Double> query(ICaseBase casebase, String caseID) {

		LinkedHashMap<String, Double> resultList = new LinkedHashMap<>();

		Retrieval r = new Retrieval(myConcept, casebase);

		//try {
		
		Instance query = r.getQueryInstance();

		Instance caze = myConcept.getInstance(caseID);

		for (Map.Entry<AttributeDesc, Attribute> e : caze.getAttributes()
				.entrySet()) {
			query.addAttribute(e.getKey(), e.getValue());
		}

		if (k > -1) {
			r.setK(k);
			r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_K_SORTED);
		} else {
			r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_SORTED);
		}

		r.start();

		List<Pair<Instance, Similarity>> results = r.getResult();

		for (Pair<Instance, Similarity> result : results) {
			resultList.put(result.getFirst().getName(), result.getSecond().getValue());
		}

		query.reset();

		return resultList;
	}

	//		catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//	}
}
