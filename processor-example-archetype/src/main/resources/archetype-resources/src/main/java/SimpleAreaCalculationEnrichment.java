package ${package};

import net.sf.json.JSONObject;

import com.deleidos.rtws.core.framework.EnrichmentDefinition;
import com.deleidos.rtws.core.framework.EnrichmentProperty;
import com.deleidos.rtws.core.framework.processor.AbstractEnrichmentProcessor;
import com.deleidos.rtws.core.framework.processor.EnrichmentAction;
import com.deleidos.rtws.core.framework.processor.ParameterList;

/**
 * Extremely simple example Enrichment Processor
 */
@EnrichmentDefinition(
		type="simple_area_calculation",
		description="This enrichment simply retrieves the length and width of the rectangle and calculates the area.",
		properties = {
				@EnrichmentProperty(
					name="property1Name",
					description="Example of how to declare properties for this enrichment.",
					type="java.lang.String"
				),
				@EnrichmentProperty(
					name="property1Name",
					description="Example of how to declare multiple properties for this enrichment.",
					type="java.lang.String"
				)
		}
)
public class SimpleAreaCalculationEnrichment extends AbstractEnrichmentProcessor {

	@Override
	public Object buildEnrichedElement(EnrichmentAction action, ParameterList parameters) {
		
		/* Asumming a json record such as
		{
		    "rectangle": {
		        "length": "10",
		        "width": "20"
		    }
		}
		This enrichment simply retrieves the length and width of the rectangle and calculates the area.
		The process of extracting the input parameters from and inserting the result back into the data record is handled
 		by @see AbstractEnrichmentProcessor
		*/
		
		JSONObject obj = action.getRootObject();
		Integer length = obj.getJSONObject("rectangle").getInt("length");
		Integer width = obj.getJSONObject("rectangle").getInt("width");
		Integer area = length * width;
		
		
		return area;
	}

	@Override
	public String getType() {
		return "AreaCalculation";
	}

}
