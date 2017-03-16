package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Attribute;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.*;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.util.Pair;
import no.ntnu.mycbr.CBREngine;
import no.ntnu.mycbr.rest.util.ResultPair;

import java.util.*;
import java.util.stream.Collectors;
import static java.util.Collections.reverseOrder;

/**
 * Created by kerstin on 05/08/16.
 */
public class Query {

    private static HashMap<String, Double> resultList = new HashMap<String, Double>();
    private ArrayList<ResultPair> resultList2 = new ArrayList<>();

    public Query(String casebase, String concept, String attribute, String value) {

        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
        // create a concept and get the main concept of the project;
        Concept myConcept = project.getConceptByID(concept) ;

        Retrieval r = new Retrieval(myConcept, cb);

        try {
            Instance query = r.getQueryInstance();

            SymbolDesc queryDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get(attribute);
            query.addAttribute(queryDesc,queryDesc.getAttribute(value));

            r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_SORTED);
            r.start();
            List<Pair<Instance, Similarity>> results = r.getResult();

            for (Pair<Instance, Similarity> result : results) {
                this.resultList.put(result.getFirst().getName(), result.getSecond().getValue());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
}

    public Query(String casebase, String concept, String caseID) {

        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
        // create a concept and get the main concept of the project;
        Concept myConcept = project.getConceptByID(concept) ;

        Retrieval r = new Retrieval(myConcept, cb);

        try {
            Instance query = r.getQueryInstance();

            Instance caze = myConcept.getInstance(caseID);

            for (Map.Entry<AttributeDesc, Attribute> e : caze.getAttributes()
                    .entrySet()) {
                query.addAttribute(e.getKey(), e.getValue());
            }

            r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_SORTED);
            r.start();
            List<Pair<Instance, Similarity>> results = r.getResult();

            for (Pair<Instance, Similarity> result : results) {
                this.resultList.put(result.getFirst().getName(), result.getSecond().getValue());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Query(String casebase, String concept, HashMap<String, Object> queryContent) {

        Project project = App.getProject();
        // create case bases and assign the case bases that will be used for submitting a query
        DefaultCaseBase cb = (DefaultCaseBase)project.getCaseBases().get(casebase);
        // create a concept and get the main concept of the project;
        Concept myConcept = project.getConceptByID(concept) ;

        Retrieval r = new Retrieval(myConcept, cb);

        try {
            Instance query = r.getQueryInstance();

            for (Map.Entry<String, Object> att : queryContent.entrySet()) {
                String name = att.getKey();
                AttributeDesc attdesc = myConcept.getAttributeDesc(name);
                if (attdesc.getClass().getSimpleName().equalsIgnoreCase("FloatDesc")){
                    FloatDesc aFloatAtt = (FloatDesc) attdesc;
                    query.addAttribute(attdesc, Float.parseFloat(att.getValue().toString()));
                }
                if (attdesc.getClass().getSimpleName().equalsIgnoreCase("IntegerDesc")){
                    if (!att.getValue().toString().equals("")) {
                        IntegerDesc aIntegerAtt = (IntegerDesc) attdesc;
                        query.addAttribute(attdesc, Integer.parseInt(att.getValue().toString()));
                    }
                }
                if (attdesc.getClass().getSimpleName().equalsIgnoreCase("DoubleDesc")){
                    DoubleDesc aIntegerAtt = (DoubleDesc) attdesc;
                    query.addAttribute(attdesc, Double.parseDouble(att.getValue().toString()));
                }
                if (attdesc.getClass().getSimpleName().equalsIgnoreCase("SymbolDesc")){
                    SymbolDesc aSymbolAtt = (SymbolDesc) attdesc;
                    query.addAttribute(attdesc, (String) att.getValue());
                }
                if (attdesc.getClass().getSimpleName().equalsIgnoreCase("StringDesc")){
                    StringDesc aSymbolAtt = (StringDesc) attdesc;
                    query.addAttribute(attdesc, (String) att.getValue());
                }
            }

            r.setRetrievalMethod(Retrieval.RetrievalMethod.RETRIEVE_SORTED);
            r.start();
            List<Pair<Instance, Similarity>> results = r.getResult();

            for (Pair<Instance, Similarity> result : results) {
                Case c = new Case(result.getFirst().getName());



                //this.resultList.put(result.getFirst().getName(), result.getSecond().getValue());

                ResultPair pair = new ResultPair(c.getCase(), result.getSecond().getValue());

                if (resultList2.size() > 0) {
                    System.out.println(resultList2.get(0).getContent());
                }
                this.resultList2.add(pair);
                System.out.println(resultList2.get(resultList2.size()-1).getContent());

            }
            //this.resultList = sortAndReduceResult(this.resultList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LinkedHashMap<String, Double> sortAndReduceResult(HashMap<String, Double> fullResultList) {
        LinkedHashMap<String, Double> reducedSortedResultList = new LinkedHashMap<>();
        LinkedHashMap<String, Double> sortedResultList = fullResultList.entrySet().stream()
                .sorted(reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        int counter = 0;
        Iterator it = sortedResultList.entrySet().iterator();

        // Ajust number to how many results you want back to the client
        while (it.hasNext() && counter < 10000) {
            HashMap.Entry pair = (Map.Entry)it.next();
            reducedSortedResultList.put((String)pair.getKey(), (Double)pair.getValue());
            counter++;
        }


        return reducedSortedResultList;

    }

    public ArrayList<ResultPair> getSimilarCases() {
        /*
        for(int i = 0; i < resultList2.size(); i++) {
            System.out.println(resultList2.get(i).getContent());
        }
        */
        return this.resultList2;
    }
}