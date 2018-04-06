package no.ntnu.mycbr.rest;

import io.swagger.annotations.*;
import no.ntnu.mycbr.rest.utils.CSVTable;

import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by kerstin on 05/08/16.
 */
@RestController
public class CBRController {

	// Need to batch below variables according to the cbr project
	private static final String CASEBASE = "support_prim_cb_latest"; 
	private static final String CONCEPT = "patient"; 
	private static final String AMALGAMATION_FUNCTION = "global_sim_1"; 

	@ApiOperation(value = GET_AMALGAMATION_FUNCTIONS, nickname = GET_AMALGAMATION_FUNCTIONS)
	@RequestMapping(method = RequestMethod.GET, path=SLASH_AMALGAMATION_FUNCTIONS, produces = APPLICATION_JSON)
	@ApiResponsesForAmalgamationFunctions
	public AmalgamationFunctions getAmalgamationFunctions(@RequestParam(value= CONCEPT_NAME_STR, defaultValue= CONCEPT) String concept) {

		return new AmalgamationFunctions(concept);
	}

	@ApiOperation(value = GET_ATTRIBUTES, nickname = GET_ATTRIBUTES)
	@RequestMapping(method = RequestMethod.GET, value = SLASH_ATTRIBUTES, headers=ACCEPT_APP_JSON)
	@ApiResponsesForApiResponse
	public Attribute getAttributes(@RequestParam(value= CONCEPT_NAME_STR, defaultValue= CONCEPT) String concept) {

		return new Attribute(concept);
	}

	@ApiOperation(value = GET_CASE, nickname = GET_CASE)
	@RequestMapping(method = RequestMethod.GET, value = SLASH_CASE, headers=ACCEPT_APP_JSON)
	@ApiResponsesForCase
	public Case getCase(@RequestParam(value=CASE_ID, defaultValue="*0") String caseID) {

		Case caze = new Case(caseID);

		return caze;
	}

	// updated
	@ApiOperation(value = GET_CASE_BASE_NAMES, nickname = GET_CASE_BASE_NAMES)
	@RequestMapping(method = RequestMethod.GET, path="/casebaseNames", produces = APPLICATION_JSON)
	@ApiResponsesForCaseBases
	public Casebase getCasebaseNames() {

		return new Casebase();
	}

	// added new
	@ApiOperation(value = GET_CASE_BASE_INSTANCES, nickname = GET_CASE_BASE_INSTANCES)
	@RequestMapping(method = RequestMethod.GET, path="/instances", produces = APPLICATION_JSON)
	@ApiResponsesForInstances
	public List<LinkedHashMap<String, String>> getInstances(@RequestParam(value= CASEBASE_NAME_STR, defaultValue= CASEBASE) String casebaseName) {

		List<LinkedHashMap<String, String>> instances = new Instances(casebaseName).getCasebaseInstances();
		return instances;
	}

	// added new for experiment - query cases vs casebase cases of are same
	@ApiOperation(value = EXEPERIMENT_DEPENDENT_CASEBASE, nickname = EXEPERIMENT_DEPENDENT_CASEBASE)
	@RequestMapping(method = RequestMethod.GET, path=SLASH_EXEPERIMENT_DEPENDENT_CASEBASE, produces = APPLICATION_JSON)
	@ApiResponsesForInstances
	public LinkedHashMap<String, LinkedHashMap<String, Double>> experimentFixedSize(
			@RequestParam(value= CASEBASE_STR, defaultValue= CASEBASE) String casebase,
			@RequestParam(value= CONCEPT_NAME_STR, defaultValue= CONCEPT) String concept,
			@RequestParam(value= AMALGAMATION_FUNCTION_STR, defaultValue= AMALGAMATION_FUNCTION) String amalFunc,
			@RequestParam(required = false, value=NO_OF_RETURNED_CASES, defaultValue = DEFAULT_NO_OF_CASES) int k,
			@RequestParam(value= "List of case IDS", defaultValue = RETRIEVAL_CASE_IDS) String caseListStr) {

		String [] caseList = caseListStr.split(",");

		List<String> caseIds = new ArrayList<String>();

		for(String caseId : caseList) 
			caseIds.add(caseId);

		return new CasebaseExperiment(casebase, concept, amalFunc,  k).dependentCaseBaseExeperiment(caseIds);
	}

	// added new for experiment - query cases vs casebase cases of are of different size
	@ApiOperation(value = EXEPERIMENT_INDEPENDENT_CASEBASE, nickname = EXEPERIMENT_INDEPENDENT_CASEBASE)
	@RequestMapping(method = RequestMethod.GET, path=SLASH_EXEPERIMENT_INDEPENDENT_CASEBASE, produces = APPLICATION_JSON)
	@ApiResponsesForInstances
	public LinkedHashMap<String, LinkedHashMap<String, Double>> experimentDynamicSize(
			@RequestParam(value= CASEBASE_STR, defaultValue= CASEBASE) String casebase,
			@RequestParam(value= CONCEPT_NAME_STR, defaultValue= CONCEPT) String concept,
			@RequestParam(value= AMALGAMATION_FUNCTION_STR, defaultValue= AMALGAMATION_FUNCTION) String amalFunc,
			@RequestParam(required = false, value=NO_OF_RETURNED_CASES, defaultValue = DEFAULT_NO_OF_CASES) int k,
			@RequestParam(value= "List of case IDS for retrival", defaultValue = RETRIEVAL_CASE_IDS) String retrievalCaseListStr,
			@RequestParam(value= "List of case IDS for casebase", defaultValue = CB_CASE_IDS) String cbCaseListStr) {

		String [] retrievalCaseList = retrievalCaseListStr.split(",");	
		List<String> retrievalCaseIds = new ArrayList<String>();	
		for(String caseId : retrievalCaseList) 
			retrievalCaseIds.add(caseId);

		String [] cbCaseList = cbCaseListStr.split(",");	
		List<String> cbCaseIds = new ArrayList<String>();	
		for(String caseId : cbCaseList) 
			cbCaseIds.add(caseId);

		return new CasebaseExperiment(casebase, concept, amalFunc,  k).independentCaseBaseExeperiment(retrievalCaseIds, cbCaseIds);
	}

	@ApiOperation(value = GET_CONCEPT, nickname = GET_CONCEPT)
	@RequestMapping(method = RequestMethod.GET, path=SLASH_CONCEPTS, produces = APPLICATION_JSON)
	@ApiResponsesForConceptName
	public ConceptName getConcept() {

		return new ConceptName();
	}

	// GET /retrival
	@ApiOperation(value = GET_SIMILAR_INSTANCES, nickname = GET_SIMILAR_INSTANCES)
	@RequestMapping(method = RequestMethod.POST, path=SLASH_RETRIEVAL, produces = APPLICATION_JSON)
	@ApiResponsesForQuery
	public Query getSimilarCases(
			@RequestParam(value= CASEBASE_STR, defaultValue= CASEBASE) String casebase,
			@RequestParam(value= CONCEPT_NAME_STR, defaultValue= CONCEPT) String concept,
			@RequestParam(value= AMALGAMATION_FUNCTION_STR, defaultValue= AMALGAMATION_FUNCTION) String amalFunc,
			@RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
			@RequestBody(required = true)  HashMap<String, Object> queryContent) {

		return new Query(casebase, concept, amalFunc, queryContent, k);
	}

	// POST /retrival
	@ApiOperation(value = GET_SIMILAR_CASES_WITH_CONTENT, nickname = GET_SIMILAR_CASES_WITH_CONTENT)
	@RequestMapping(method = RequestMethod.POST, path=RETRIEVAL_CONTENT_AS_JSON, produces = APPLICATION_JSON)

	/**@ApiImplicitParams({
        @ApiImplicitParam(name = "body_main", value = "Back", required = true, dataType = "string", paramType =
                "query"),
        @ApiImplicitParam(name = "age", value = "50", required = true, dataType = "integer", paramType =
                "query"),
        @ApiImplicitParam(name = "education", value = "High School", required = true, dataType = "string", paramType
                = "query"),
        @ApiImplicitParam(name = "gender", value = "female", required = true, dataType = "string", paramType =
                "query")})	
	 */
	@ApiResponsesDefault
	public @ResponseBody List<LinkedHashMap<String, String>> getSimilarCasesWithContent(
			@RequestParam(value= CASEBASE_STR, defaultValue= CASEBASE) String casebase,
			@RequestParam(value= CONCEPT_NAME_STR, defaultValue= CONCEPT) String concept,
			@RequestParam(value= AMALGAMATION_FUNCTION_STR, defaultValue= AMALGAMATION_FUNCTION) String amalFunc,
			@RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
			@RequestBody(required = true)  HashMap<String, Object> queryContent) {

		Query query = new Query(casebase, concept, amalFunc, queryContent, k);
		List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
		return cases;
	}

	@ApiOperation(value = GET_SIMILAR_CASES_BY_ID, nickname = GET_SIMILAR_CASES_BY_ID)
	@RequestMapping(method = RequestMethod.GET, path=SLASH_RETRIEVAL_BY_ID, produces = APPLICATION_JSON)
	@ApiResponsesForQuery
	public Query getSimilarCasesByID(
			@RequestParam(value= CASEBASE_STR, defaultValue= CASEBASE) String casebase,
			@RequestParam(value= CONCEPT_NAME_STR, defaultValue= CONCEPT) String concept,
			@RequestParam(value= AMALGAMATION_FUNCTION_STR, defaultValue= AMALGAMATION_FUNCTION) String amalFunc,
			@RequestParam(value=CASE_ID, defaultValue="144_vw") String caseID,
			@RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {

		return new Query(casebase, concept, amalFunc, caseID, k);
	}

	@ApiOperation(value = GET_SIMILAR_CASES_BY_ATTRIBUTE, nickname = "getSimilarCases")
	@RequestMapping(method = RequestMethod.GET, path=SLASH_RETRIEVAL, produces = APPLICATION_JSON)
	@ApiResponsesForQuery
	public Query getSimilarCasesByAttribute(
			@RequestParam(value= CASEBASE_STR, defaultValue= CASEBASE) String casebase,
			@RequestParam(value= CONCEPT_NAME_STR, defaultValue= CONCEPT) String concept,
			@RequestParam(value= AMALGAMATION_FUNCTION_STR, defaultValue= AMALGAMATION_FUNCTION) String amalFunc,
			@RequestParam(value= SYMBOL_ATTRIBUTE_NAME, defaultValue="Manufacturer") String attribute,
			@RequestParam(value= VALUE, defaultValue="vw") String value,
			@RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {

		return new Query(casebase, concept, amalFunc, attribute, value, k);
	}

	@ApiOperation(value = GET_SIMILAR_CASES_BY_ID_WITH_CONTENT, nickname = GET_SIMILAR_CASES_BY_ID_WITH_CONTENT)
	@RequestMapping(method = RequestMethod.GET, path=RETRIEVAL_BY_ID_CONTENT_AS_JSON, produces = APPLICATION_JSON)
	@ApiResponsesDefault
	public @ResponseBody List<LinkedHashMap<String, String>> getSimilarCasesByIDWithContent(
			@RequestParam(value= CASEBASE_STR, defaultValue= CASEBASE) String casebase,
			@RequestParam(value= CONCEPT_NAME_STR, defaultValue= CONCEPT) String concept,
			@RequestParam(value= AMALGAMATION_FUNCTION_STR, defaultValue= AMALGAMATION_FUNCTION) String amalFunc,
			@RequestParam(value=CASE_ID, defaultValue="144_vw") String caseID,
			@RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {

		Query query = new Query(casebase, concept, amalFunc, caseID, k);
		List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
		return cases;
	}

	@ApiOperation(value = GET_SIMILAR_CASES_BY_ATTRIBUTE_WITH_CONTENT, nickname = GET_SIMILAR_CASES_BY_ATTRIBUTE_WITH_CONTENT)
	@RequestMapping(method = RequestMethod.GET, path=RETRIEVAL_CONTENT_AS_JSON, produces = APPLICATION_JSON)
	@ApiResponsesDefault
	public @ResponseBody List<LinkedHashMap<String, String>> getSimilarCasesByAttributeWithContent(
			@RequestParam(value= CASEBASE_STR, defaultValue= CASEBASE) String casebase,
			@RequestParam(value= CONCEPT_NAME_STR, defaultValue= CONCEPT) String concept,
			@RequestParam(value= AMALGAMATION_FUNCTION_STR, defaultValue= AMALGAMATION_FUNCTION) String amalFunc,
			@RequestParam(value= SYMBOL_ATTRIBUTE_NAME, defaultValue="Manufacturer") String attribute,
			@RequestParam(value= VALUE, defaultValue="vw") String value,
			@RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {

		Query query = new Query(casebase, concept, amalFunc, attribute, value, k);
		List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
		return cases;
	}

	@ApiOperation(value = GET_SIMILAR_CASES_WITH_CONTENT, nickname = GET_SIMILAR_CASES_WITH_CONTENT)
	@RequestMapping(method = RequestMethod.POST, path=SLASH_RETRIEVAL_WITH_CONTENT_CSV, produces = TEXT_OR_CSV)
	@ApiResponsesDefault
	public @ResponseBody String getSimilarCasesWithContent(
			@RequestParam(value= CASEBASE_STR, defaultValue= CASEBASE) String casebase,
			@RequestParam(value= CONCEPT_NAME_STR, defaultValue= CONCEPT) String concept,
			@RequestParam(value= AMALGAMATION_FUNCTION_STR, defaultValue= AMALGAMATION_FUNCTION) String amalFunc,
			@RequestParam(value=DELIMITER, defaultValue=DELIMITER_SEMI_COLON) String delimiter,
			@RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k,
			@RequestBody(required = true)  HashMap<String, Object> queryContent) {

		Query query = new Query(casebase, concept, amalFunc, queryContent, k);
		List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
		List<Map<String, String>> cases2 = new ArrayList<Map<String, String>>(cases);
		String csvTable = new CSVTable(cases2).getTableAsString(delimiter);
		return csvTable;
	}

	@ApiOperation(value = GET_SIMILAR_CASES_BY_ID_WITH_CONTENT, nickname = GET_SIMILAR_CASES_BY_ID_WITH_CONTENT)
	@RequestMapping(method = RequestMethod.GET, path="/retrievalByIDWithContent.csv", produces = TEXT_OR_CSV)
	@ApiResponsesDefault
	public String getSimilarCasesByIDWithContentAsCSV(
			@RequestParam(value= CASEBASE_STR, defaultValue= CASEBASE) String casebase,
			@RequestParam(value= CONCEPT_NAME_STR, defaultValue= CONCEPT) String concept,
			@RequestParam(value= AMALGAMATION_FUNCTION_STR, defaultValue= AMALGAMATION_FUNCTION) String amalFunc,
			@RequestParam(value=CASE_ID, defaultValue="144_vw") String caseID,
			@RequestParam(value=DELIMITER, defaultValue=DELIMITER_SEMI_COLON) String delimiter,
			@RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {

		Query query = new Query(casebase, concept, amalFunc, caseID, k);
		List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
		List<Map<String, String>> cases2 = new ArrayList<Map<String, String>>(cases);
		String csvTable = new CSVTable(cases2).getTableAsString(delimiter);
		return csvTable;
	}

	@ApiOperation(value = GET_SIMILAR_CASES_BY_ATTRIBUTE_WITH_CONTENT, nickname = GET_SIMILAR_CASES_BY_ATTRIBUTE_WITH_CONTENT)
	@RequestMapping(method = RequestMethod.GET, path=SLASH_RETRIEVAL_WITH_CONTENT_CSV, produces = TEXT_OR_CSV)
	@ApiResponsesDefault
	public @ResponseBody String getSimilarCasesByAttributeWithContent(
			@RequestParam(value= CASEBASE_STR, defaultValue= CASEBASE) String casebase,
			@RequestParam(value= CONCEPT_NAME_STR, defaultValue= CONCEPT) String concept,
			@RequestParam(value= AMALGAMATION_FUNCTION_STR, defaultValue= AMALGAMATION_FUNCTION) String amalFunc,
			@RequestParam(value= SYMBOL_ATTRIBUTE_NAME, defaultValue="Manufacturer") String attribute,
			@RequestParam(value= VALUE, defaultValue="vw") String value,
			@RequestParam(value= DELIMITER, defaultValue=DELIMITER_SEMI_COLON) String delimiter,
			@RequestParam(required = false, value=NO_OF_RETURNED_CASES,defaultValue = DEFAULT_NO_OF_CASES) int k) {

		Query query = new Query(casebase, concept, amalFunc, attribute, value, k);
		List<LinkedHashMap<String, String>> cases = getFullResult(query, concept);
		List<Map<String, String>> cases2 = new ArrayList<Map<String, String>>(cases);
		String csvTable = new CSVTable(cases2).getTableAsString(delimiter);
		return csvTable;
	}


	@ApiOperation(value = GET_VALUE_RANGE, nickname = GET_VALUE_RANGE)
	@RequestMapping(method = RequestMethod.GET, value = SLASH_VALUES, headers=ACCEPT_APP_JSON)
	@ApiResponsesForValueRange
	public ValueRange getValueRange(
			@RequestParam(value= CONCEPT_NAME_STR, defaultValue= CONCEPT) String concept,
			@RequestParam(value= ATTRIBUTE_NAME, defaultValue="Color") String attributeName) {

		return new ValueRange(concept, attributeName);
	}


	// Private methods ----------------------------------------------------------------------------------------------------------   
	private List<LinkedHashMap<String, String>> getFullResult(Query query, String concept) {
		LinkedHashMap<String, Double> results = query.getSimilarCases();
		List<LinkedHashMap<String, String>> cases = new ArrayList<>();

		for (Map.Entry<String, Double> entry : results.entrySet()) {
			String entryCaseID = entry.getKey();
			double similarity = entry.getValue();
			Case caze = new Case(concept, entryCaseID, similarity);
			cases.add(caze.getCase());
		}

		return cases;
	}




	// All ApiResponses annotations definitions----------------------------------------------------------------------------------------------------------   

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SUCCESS),
			@ApiResponse(code = 401, message = UNAUTHORIZED),
			@ApiResponse(code = 403, message = FORBIDDEN),
			@ApiResponse(code = 404, message = NOT_FOUND),
			@ApiResponse(code = 500, message = FAILURE)
	})
	private @interface ApiResponsesDefault{}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SUCCESS, response = Case.class),
			@ApiResponse(code = 401, message = UNAUTHORIZED),
			@ApiResponse(code = 403, message = FORBIDDEN),
			@ApiResponse(code = 404, message = NOT_FOUND),
			@ApiResponse(code = 500, message = FAILURE)
	})
	private @interface ApiResponsesForCase{}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SUCCESS, response = Query.class),
			@ApiResponse(code = 401, message = UNAUTHORIZED),
			@ApiResponse(code = 403, message = FORBIDDEN),
			@ApiResponse(code = 404, message = NOT_FOUND),
			@ApiResponse(code = 500, message = FAILURE)
	})
	private @interface ApiResponsesForQuery{}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SUCCESS, response = Casebase.class),
			@ApiResponse(code = 401, message = UNAUTHORIZED),
			@ApiResponse(code = 403, message = FORBIDDEN),
			@ApiResponse(code = 404, message = NOT_FOUND),
			@ApiResponse(code = 500, message = FAILURE)
	})
	private @interface ApiResponsesForCaseBases{}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SUCCESS, response = Casebase.class),
			@ApiResponse(code = 401, message = UNAUTHORIZED),
			@ApiResponse(code = 403, message = FORBIDDEN),
			@ApiResponse(code = 404, message = NOT_FOUND),
			@ApiResponse(code = 500, message = FAILURE)
	})
	private @interface ApiResponsesForInstances{}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SUCCESS, response = AmalgamationFunctions.class),
			@ApiResponse(code = 401, message = UNAUTHORIZED),
			@ApiResponse(code = 403, message = FORBIDDEN),
			@ApiResponse(code = 404, message = NOT_FOUND),
			@ApiResponse(code = 500, message = FAILURE)
	})
	private @interface ApiResponsesForAmalgamationFunctions{}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SUCCESS, response = ConceptName.class),
			@ApiResponse(code = 401, message = UNAUTHORIZED),
			@ApiResponse(code = 403, message = FORBIDDEN),
			@ApiResponse(code = 404, message = NOT_FOUND),
			@ApiResponse(code = 500, message = FAILURE)
	})
	private @interface ApiResponsesForConceptName{}  

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SUCCESS, response = ValueRange.class),
			@ApiResponse(code = 401, message = UNAUTHORIZED),
			@ApiResponse(code = 403, message = FORBIDDEN),
			@ApiResponse(code = 404, message = NOT_FOUND),
			@ApiResponse(code = 500, message = FAILURE)
	})
	private @interface ApiResponsesForValueRange{}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = SUCCESS, response = ApiResponse.class),
			@ApiResponse(code = 401, message = UNAUTHORIZED),
			@ApiResponse(code = 403, message = FORBIDDEN),
			@ApiResponse(code = 404, message = NOT_FOUND),
			@ApiResponse(code = 500, message = FAILURE),
	})
	private @interface ApiResponsesForApiResponse{}





	// All constant definitions ------------------------------------------------------------------------------------------------------------------------    

	private static final String SLASH_RETRIEVAL_BY_ID = "/retrievalByID";
	private static final String GET_SIMILAR_CASES_BY_ID = "getSimilarCasesByID";
	private static final String SLASH_RETRIEVAL = "/retrieval";
	private static final String GET_SIMILAR_CASES_BY_ATTRIBUTE = "getSimilarCasesByAttribute";
	private static final String RETRIEVAL_BY_ID_CONTENT_AS_JSON = "/retrievalByIDWithContent.json";
	private static final String GET_SIMILAR_CASES_BY_ID_WITH_CONTENT = "getSimilarCasesByIDWithContent";
	private static final String RETRIEVAL_CONTENT_AS_JSON = "/retrievalWithContent.json";
	private static final String GET_SIMILAR_CASES_WITH_CONTENT = "getSimilarCasesWithContent";
	private static final String VALUE = "value";
	private static final String SYMBOL_ATTRIBUTE_NAME = "Symbol attribute name";
	private static final String DEFAULT_NO_OF_CASES = "-1";
	private static final String DELIMITER_SEMI_COLON = ";";
	private static final String NO_OF_RETURNED_CASES = "no of returned cases";
	private static final String DELIMITER = "delimiter";
	private static final String CASE_ID = "caseID";
	private static final String TEXT_OR_CSV = "text/csv";
	private static final String SLASH_RETRIEVAL_WITH_CONTENT_CSV = "/retrievalWithContent.csv";
	private static final String GET_SIMILAR_CASES_BY_ATTRIBUTE_WITH_CONTENT = "getSimilarCasesByAttributeWithContent";
	

	// added new
	private static final String GET_CASE_BASE_INSTANCES = "getCasebaseInstances";

	private static final String EXEPERIMENT_DEPENDENT_CASEBASE = "expDependentCasebase";
	private static final String SLASH_EXEPERIMENT_DEPENDENT_CASEBASE = "/"+EXEPERIMENT_DEPENDENT_CASEBASE;

	private static final String EXEPERIMENT_INDEPENDENT_CASEBASE = "expIndependentCasebase";
	private static final String SLASH_EXEPERIMENT_INDEPENDENT_CASEBASE = "/"+EXEPERIMENT_INDEPENDENT_CASEBASE;

	private static final String GET_CASE_BASE_NAMES = "getCasebaseNames";
	private static final String GET_CASE_BASES = "getCaseBases";
	private static final String SLASH_AMALGAMATION_FUNCTIONS = "/amalgamationFunctions";

	private static final String GET_AMALGAMATION_FUNCTIONS = "getAmalgamationFunctions";
	private static final String GET_CONCEPT = "getConcept";
	private static final String SLASH_CONCEPTS = "/concepts";
	private static final String GET_ATTRIBUTES = "getAttributes";
	private static final String SLASH_ATTRIBUTES = "/attributes";
	private static final String ATTRIBUTE_NAME = "attribute name";
	private static final String SLASH_VALUES = "/values";
	private static final String GET_VALUE_RANGE = "getValueRange";

	private static final String GET_SIMILAR_INSTANCES = "getSimilar_"+CONCEPT; 

	private static final String GET_CASE = "getCase";
	private static final String SLASH_CASE = "/case";
	private static final String ACCEPT_APP_JSON ="Accept=application/json";

	private static final String APPLICATION_JSON = "application/json";

	private static final String SUCCESS = "Success";	
	private static final String UNAUTHORIZED = "Unauthorized";
	private static final String FORBIDDEN = "Forbidden";	
	private static final String NOT_FOUND = "Not Found";
	private static final String FAILURE = "Failure";   

	private static final String CASEBASE_STR = "casebase";	
	private static final String CASEBASE_NAME_STR = "casebaseName";	

	private static final String CONCEPT_NAME_STR = "concept name";
	private static final String AMALGAMATION_FUNCTION_STR = "amalgamation function";

	//	private static ArrayList<String> defaultList = ;
	//	defaultList "patient0","patient1","patient2"};
	private static final String RETRIEVAL_CASE_IDS = "patient0,patient1,patient2";
	private static final String CB_CASE_IDS = "patient0,patient1,patient2,patient3,patient4";
}
