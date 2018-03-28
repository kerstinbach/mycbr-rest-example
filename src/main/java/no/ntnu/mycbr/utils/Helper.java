package no.ntnu.mycbr.utils;

import static no.ntnu.mycbr.rest.utils.CommonConstants.EMPTY;
import static no.ntnu.mycbr.rest.utils.CommonConstants.UNDEFINED;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import de.dfki.mycbr.core.casebase.Attribute;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;

public class Helper {
	
	public static TreeMap<String, String> getSortedCaseContent(Instance aInstance) {
		HashMap<AttributeDesc, Attribute> atts = aInstance.getAttributes();
		TreeMap<String, String> sortedCaseContent = new TreeMap<String, String>();

		for(Map.Entry<AttributeDesc, Attribute> entry : atts.entrySet()) {
			AttributeDesc attDesc = entry.getKey();
			Attribute att = entry.getValue();
			String value = att.getValueAsString();
			value.trim();
			if (value.compareTo(UNDEFINED) != 0){
				if (value.compareTo(EMPTY) != 0){
					sortedCaseContent.put(attDesc.getName(), att.getValueAsString());
				}
			}
		}

		return sortedCaseContent;
	}

}
