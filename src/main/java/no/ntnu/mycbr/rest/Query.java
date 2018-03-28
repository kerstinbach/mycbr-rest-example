package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Attribute;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.casebase.MultipleAttribute;
import de.dfki.mycbr.core.model.*;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.core.util.Pair;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctNotChangedException;
import no.ntnu.mycbr.rest.utils.TemporaryAmalgamFctManager;

import java.text.ParseException;
import java.util.*;

import static no.ntnu.mycbr.rest.utils.CommonConstants.*;

/**
 * @date 05/08/16
 * @author Kerstin Bach
 * @refactored_by Amar Jaiswal
 * @refactored_on 08/03/2018
 */
public class Query {

	private LinkedHashMap<String, Double> resultList = new LinkedHashMap<>();

	Project project;
	// create case bases and assign the case bases that will be used for submitting a query
	DefaultCaseBase cb;
	// create a concept and get the main concept of the project;
	Concept myConcept;

	TemporaryAmalgamFctManager tempAmalgamFctManager;

	public Query(String casebase, String concept) {
		this.project = App.getProject();
		this.cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
		this.myConcept = project.getConceptByID(concept);
		this.tempAmalgamFctManager = new TemporaryAmalgamFctManager(myConcept);
	}

	public Query(String casebase, String concept, String amalFunc, HashMap<String, Object> queryContent, int k) {

		this(casebase, concept);

		try {
			tempAmalgamFctManager.changeAmalgamFct(amalFunc);

			Retrieval r = new Retrieval(myConcept, cb);

			try {
				Instance query = r.getQueryInstance();

				for (Map.Entry<String, Object> att : queryContent.entrySet()) {
					String name = att.getKey();
					createRelevantQuery(myConcept, query, name, att.getValue().toString());
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
					this.resultList.put(result.getFirst().getName(), result.getSecond().getValue());
				}

				query.reset();

			}
			catch (Exception e) {
				e.printStackTrace();
			}

		} catch (TemporaryAmalgamFctNotChangedException e) {
			// Return empty result
			return;
		} finally {
			tempAmalgamFctManager.rollBack();
		}
	}

	public Query(String casebase, String concept, String amalFunc, String caseID, int k) {

		this(casebase, concept);

		try {
			tempAmalgamFctManager.changeAmalgamFct(amalFunc);

			Retrieval r = new Retrieval(myConcept, cb);

			try {
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
					this.resultList.put(result.getFirst().getName(), result.getSecond().getValue());
				}

				query.reset();
			}

			catch (Exception e) {
				e.printStackTrace();
			}

		} catch (TemporaryAmalgamFctNotChangedException e) {
			// Return empty result
			return;
		} finally {
			//tempAmalgamFctManager.rollBack();
		}
	}

	public Query(String casebase, String concept, String amalFunc, String attribute, String value, int k) {

		this(casebase, concept);

		try {
			tempAmalgamFctManager.changeAmalgamFct(amalFunc);

			Retrieval r = new Retrieval(myConcept, cb);

			try {
				Instance query = r.getQueryInstance();

				createRelevantQuery(myConcept, query, attribute, value);

				if (k > -1) {
					r.setK(k);
					r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_K_SORTED);
				} else {
					r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_SORTED);
				}

				r.start();
				List<Pair<Instance, Similarity>> results = r.getResult();

				for (Pair<Instance, Similarity> result : results) {
					this.resultList.put(result.getFirst().getName(), result.getSecond().getValue());
				}

				query.reset();

			}
			catch (Exception e) {
				e.printStackTrace();
			}

		} catch (TemporaryAmalgamFctNotChangedException e) {
			// Return empty result
			return;
		} finally {
			tempAmalgamFctManager.rollBack();
		}
	}

	public LinkedHashMap<String, Double> getSimilarCases() {
		return resultList;
	}

	/**
	 * @param myConcept
	 * @param query
	 * @param att
	 * @param name
	 * @throws ParseException
	 */
	private void createRelevantQuery(Concept myConcept, Instance query, String desc, String value )
			throws ParseException {

		AttributeDesc attdesc = myConcept.getAttributeDesc(desc);
		
		try {

			if (attdesc.getClass().getSimpleName().equalsIgnoreCase(FLOAT_DESC)){
				
				query.addAttribute(attdesc, Float.parseFloat(value));
			}
			if (attdesc.getClass().getSimpleName().equalsIgnoreCase(INTEGER_DESC)){
				query.addAttribute(attdesc, Integer.parseInt(value));
			}
			if (attdesc.getClass().getSimpleName().equalsIgnoreCase(DOUBLE_DESC)){
				query.addAttribute(attdesc, Double.parseDouble(value));
			}
			if (attdesc.getClass().getSimpleName().equalsIgnoreCase(SYMBOL_DESC)){
				SymbolDesc aSymbolAtt = (SymbolDesc) attdesc;
				if (!aSymbolAtt.isMultiple()) {
					query.addAttribute(attdesc, value);
				}
				else {
					LinkedList<Attribute> llAtts = new LinkedList<Attribute>();
					StringTokenizer st = new StringTokenizer(value, COMMA);
					while (st.hasMoreElements()) {
						String symbolName = st.nextElement().toString().trim();
						llAtts.add(aSymbolAtt.getAttribute(symbolName));
					}

					MultipleAttribute<SymbolDesc> muliSymbol = new MultipleAttribute<SymbolDesc>(aSymbolAtt, llAtts);
					query.addAttribute(attdesc, muliSymbol);
				}
			}
		} catch (Exception e) {
			System.err.println(e);
			System.err.println(desc +" : "+ value + " : "+attdesc);
		}
	}

//	private Instance adaptQuery(Instance query) {
//
//		Map<AttributeDesc, Attribute> map = query.getAttributes();
//
//		Set<AttributeDesc> set = map.keySet();
//
//		for (AttributeDesc attributeDesc : set) {
//			if(map.get(attributeDesc).getValueAsString().equals(Project.UNKNOWN_SPECIAL_VALUE))
//				map.replace(attributeDesc, new Attribute() {
//
//					@Override
//					public String getValueAsString() {
//						return Project.UNDEFINED_SPECIAL_ATTRIBUTE;
//					}
//				} );
//			System.out.println(attributeDesc+" : "+ map.get(attributeDesc));
//		}
//
//		System.out.println(map);
//
//		System.out.println(map.get("age"));
//
//		return query;
//	}
}



































