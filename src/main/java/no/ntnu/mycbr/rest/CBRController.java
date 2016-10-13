package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.casebase.Instance;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.HashMap;


/**
 * Created by kerstin on 05/08/16.
 */
@RestController
public class CBRController {

    @CrossOrigin(origins = "http://localhost:8000")
    @ApiOperation(value = "getConcept", nickname = "getConcept")
    @RequestMapping(method = RequestMethod.GET, path="/concepts", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ConceptName.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ConceptName getConcept() {
        return new ConceptName();
    }

    @CrossOrigin(origins = "http://localhost:8000")
    @ApiOperation(value = "getSimilarCases", nickname = "getSimilarCases")
    @RequestMapping(method = RequestMethod.GET, path="/retrieval", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Query.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Query getSimilarCases(@RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
                          @RequestParam(value="concept name", defaultValue="Car") String concept,
                          @RequestParam(value="Symbol attribute name", defaultValue="Manufacturer") String attribute,
                          @RequestParam(value="value", defaultValue="vw") String value) {
        return new Query(casebase, concept, attribute, value);
    }


    @CrossOrigin(origins = "http://localhost:8000")
    @ApiOperation(value = "getSimilarCases", nickname = "getSimilarCases")
    @RequestMapping(method = RequestMethod.POST, path="/retrieval", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Query.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Query getSimilarCases(@RequestParam(value="casebase", defaultValue="CaseBase0") String casebase,
                                 @RequestParam(value="concept name", defaultValue="Car") String concept,
                                 @RequestBody(required = true)  HashMap<String, Object> queryContent) {
        System.out.println("=============================================");
        System.out.println("THS IS HOW THE QUERY CONTENT LOOKS LIKE: ");
        System.out.println(queryContent.keySet());
        System.out.println(queryContent.values());
        System.out.println("=============================================");
        return new Query(casebase, concept, queryContent);
    }

    @CrossOrigin(origins = "http://localhost:8000")
    @ApiOperation(value = "getSimilarCasesByID", nickname = "getSimilarCasesByID")
    @RequestMapping(method = RequestMethod.GET, path="/retrievalByID", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Query.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Query getSimilarCases(@RequestParam(value="casebase", defaultValue="main_case_base") String casebase,
                                     @RequestParam(value="concept", defaultValue="Trip") String concept,
                                     @RequestParam(value="caseID", defaultValue="Trip0") String caseID) {
        return new Query(casebase, concept, caseID);
    }

    @CrossOrigin(origins = "http://localhost:8000")
    @ApiOperation(value = "getCase", nickname = "getCase")
    @RequestMapping(method = RequestMethod.GET, value = "/case", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Case.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Case getCase(@RequestParam(value="caseID", defaultValue="Trip0") String caseID) {
        return new Case(caseID);
    }

    @CrossOrigin(origins = "http://localhost:8000")
    @ApiOperation(value = "getCaseBases", nickname = "getCaseBases")
    @RequestMapping(method = RequestMethod.GET, path="/casebase", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = CaseBases.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public CaseBases getCaseBases() {
        return new CaseBases();
    }

    @CrossOrigin(origins = "http://localhost:8000")
    @ApiOperation(value = "getAttributes", nickname = "getAttributes")
    @RequestMapping(method = RequestMethod.GET, value = "/attributes", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ApiResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Attribute getAttributes(@RequestParam(value="concept name", defaultValue="Trip") String concept) {
        return new Attribute(concept);
    }

    @CrossOrigin(origins = "http://localhost:8000")
    @ApiOperation(value = "getValueRange", nickname = "getValueRange")
    @RequestMapping(method = RequestMethod.GET, value = "/values", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ValueRange.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ValueRange getValueRange(@RequestParam(value="concept name", defaultValue="Trip") String concept,
                                    @RequestParam(value="attribute name", defaultValue="University") String attributeName) {
        return new ValueRange(concept, attributeName);
    }

}
