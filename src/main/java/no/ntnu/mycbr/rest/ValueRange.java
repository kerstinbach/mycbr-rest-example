package no.ntnu.mycbr.rest;

import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.model.AttributeDesc;
import de.dfki.mycbr.core.model.FloatDesc;
import de.dfki.mycbr.core.model.IntegerDesc;
import de.dfki.mycbr.core.model.DoubleDesc;
import de.dfki.mycbr.core.model.SymbolDesc;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static no.ntnu.mycbr.rest.utils.CommonConstants.*;

/**
 * @date 05/08/16
 * @author Kerstin Bach
 * @refactored_by Amar Jaiswal
 * @refactored_on 08/03/2018
 */
public class ValueRange {

	private HashMap<String, Object> attributes = new HashMap<String, Object>();

    public ValueRange(String concept, String attributeName) {

        Project project = App.getProject();
        // create a concept and get the main concept of the project;
        Concept myConcept = project.getConceptByID(concept);
        attributes.clear();

        AttributeDesc attdesc = myConcept.getAttributeDesc(attributeName);
        String AttributeDescName = attdesc.getClass().getSimpleName();
        if (AttributeDescName.equalsIgnoreCase(FLOAT_DESC)){
            FloatDesc aFloatAtt = (FloatDesc) attdesc;
            Set<Float> range = new HashSet<>();
            range.add(aFloatAtt.getMin());
            range.add(aFloatAtt.getMax());
            attributes.put(attdesc.getName(),range);
        }
        else if (AttributeDescName.equalsIgnoreCase(INTEGER_DESC)){
            IntegerDesc aIntegerAtt = (IntegerDesc) attdesc;
            Set<Integer> range = new HashSet<>();
            range.add(aIntegerAtt.getMin());
            range.add(aIntegerAtt.getMax());
            attributes.put(attdesc.getName(),range);
        }
        else if (AttributeDescName.equalsIgnoreCase(DOUBLE_DESC)){
            DoubleDesc aIntegerAtt = (DoubleDesc) attdesc;
            Set<Double> range = new HashSet<>();
            range.add(aIntegerAtt.getMin());
            range.add(aIntegerAtt.getMax());
            attributes.put(attdesc.getName(),range);
        }
        else if (AttributeDescName.equalsIgnoreCase(SYMBOL_DESC)){
            SymbolDesc aSymbolAtt = (SymbolDesc) attdesc;
            attributes.put(attdesc.getName(),aSymbolAtt.getAllowedValues());
        }
        else {
            attributes.put(attdesc.getName(),N_A);
        }
    }

    public HashMap<String, Object> getValueRange() {
        return attributes;
    }
}
