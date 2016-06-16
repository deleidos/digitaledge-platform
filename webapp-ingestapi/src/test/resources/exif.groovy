import com.deleidos.rtws.commons.util.scripting.annotation.ScriptSignature
import java.util.ArrayList
import java.lang.reflect.Method
import java.lang.reflect.Modifier 


@ScriptSignature(signature="convertDegreesToDecimals(degree, direction)", 
		description="Converts latitude and longitude degrees to decimal")
def String convertDegreesToDecimals(degree, direction) {
	if (degree == null) {
		if (!degree.matches("(-)?[0-6][0-9]\"[0-6][0-9]\'[0-6][0-9](.[0-9]{1,16})?")) {
		        throw new IllegalArgumentException("Degree " + degree + " is not in the proper format.");
		}        
		throw new IllegalArgumentException("Unable to convert a null degree");
	}
		
	String[] strArray=degree.split("[\"']");
	double value =  Double.parseDouble(strArray[0])+Double.parseDouble(strArray[1])/60+Double.parseDouble(strArray[2])/3600;
	
	if (direction != null) {
		if ((direction.trim().equalsIgnoreCase("W")) || (direction.trim().equalsIgnoreCase("S"))) {
			value = 0 - value;
		}
	}
		
	return value;
}







