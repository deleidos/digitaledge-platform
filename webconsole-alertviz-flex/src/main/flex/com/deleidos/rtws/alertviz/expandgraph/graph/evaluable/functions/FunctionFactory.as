package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.Literal;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.ObjectReference;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful.BaseFunction;

	/**
	 * A parser class that converts text to an IFunction implementing object, which can then be used to perform calculations on data.
	 */
	public class FunctionFactory
	{
		
		/**
		 * When text is surrounded by this character, the parser will interpret the contained text
		 * as text instead of as a function or a reference.
		 **/
		public static const LITERAL_QUOTE:String = "\"";
		
		/**
		 * The parser will interpet the parameters of a function to be the evaluations of the text
		 * split by this character.
		 **/
		public static const FUNCTION_SPLITTER:String = ",";
		
		/** The parser will interpet this character as the beginning of a function. **/
		public static const FUNCTION_START:String = "(";
		
		/** The parser will interpet this character as the end of a function. **/
		public static const FUNCTION_END:String = ")";
		
		/** The parser will interpet this character as the start of a reference. **/
		public static const REFERENCE_START:String = "{";
		
		/** The parser will interpet this character as the end of a reference. **/
		public static const REFERENCE_END:String = "}";
		
		/** Multi-level references will be delimited by this character. **/
		public static const REFERENCE_DELIMITER:String = "."; //note this character is not used in parsing, it is for use in the ObjectReference class
		
		/**
		 * An array of characters that have special meaning when parsing functions. For the parser to interpret
		 * these characters as text, they must be escaped with a '\' character.
		 */
		public static const SPECIAL:Array = [LITERAL_QUOTE, FUNCTION_SPLITTER, FUNCTION_START, FUNCTION_END, REFERENCE_START, REFERENCE_END];
		
		private static const QUOTE_START:String = "<!~";
		private static const QUOTE_END:String = "~!>";
		
		/** FunctionFactory is not instantiable. */
		public function FunctionFactory(){
			throw new Error("FunctionFactory not instantiable");
		}
		
		/**
		 * Creates an evaluable function
		 * @param value A valid IFunction template string
		 * @param header An object reference to be prepended to all object references 
		 */
		public static function parse(value:String, header:ObjectReference = null):IFunction{
			return parseInternal(BaseFunction.NAME, value, header);
		}
		
		/**
		 * @param fName Name of a valid, defined function
		 * @param fParams Comma-delimited set of valid IEvaluable parse strings
		 * @param header An object reference to be prepended to all object references 
		 */
		private static function parseInternal(fName:String, fParams:String, header:ObjectReference):IFunction 
		{
			var parameters:Vector.<IEvaluable> = new Vector.<IEvaluable>();
			
			//if there's no text there's no parameters to evaluate
			if(fParams != null && fParams != ""){
				fParams = quoteString(fParams);
				
				//Associates index of parens -> text contained by them
				var paramHolder:Object = new Object(); 
				var paramIndex:int = 0;
				
				//Replace the inner values of functions with an index which references their actual string parameters
				//This is done so that we can comma delimit the string to find the parameters of the outermost function
				var functionStart:int = fParams.indexOf(FUNCTION_START, 0);
				while(functionStart >= 0){ //function exists
					var openParens:int = 1;
					var closeParens:int = 0;
					var curParen:int = functionStart;
					while(closeParens != openParens){
						var nextOpen:int = fParams.indexOf(FUNCTION_START,curParen+1);
						var nextClose:int = fParams.indexOf(FUNCTION_END,curParen+1);
						if(nextOpen != -1 && (nextOpen < nextClose || nextClose == -1)){
							openParens++;
							curParen = nextOpen;
						}else if(nextClose != -1 && (nextClose < nextOpen || nextOpen == -1)){
							closeParens++;
							curParen = nextClose;
						}else{ 
							throw new Error("Malformed parameter string (unequal amount of open and closed parentheses): " + fParams);
						}
					}
					
					//get the text within the parentheses
					var functionParameters:String = fParams.substring(functionStart+1, curParen);
					
					//replace it with an index and associate that index with the parameters
					paramHolder[paramIndex] = functionParameters;
					fParams = fParams.substring(0,functionStart+1) + String(paramIndex) + fParams.substring(curParen);
					paramIndex++;
				
					functionStart = fParams.indexOf(FUNCTION_START, (curParen - functionParameters.length) + 1);
				}
				
				//Split the parameters by the function splitter character, it's safe now that all inner functions have had their potential conflicts removed
				var delimitedParams:Array = fParams.split(FUNCTION_SPLITTER);
				for each(var stringParam:String in delimitedParams){				
					var parenOpenIndex:int = stringParam.indexOf(FUNCTION_START);
					var parenEndIndex:int = stringParam.indexOf(FUNCTION_END);
					if(parenOpenIndex != -1 && parenEndIndex != -1){ //its a function
						var newFName:String = stringParam.substring(0, parenOpenIndex );
						var newFParams:String = paramHolder[int(stringParam.substring(parenOpenIndex+1,parenEndIndex))];
						parameters.push(FunctionFactory.parseInternal(newFName, newFParams, header));
						continue;
					}
				
					var bracketOpenIndex:int = stringParam.indexOf(REFERENCE_START);
					var bracketEndIndex:int = stringParam.indexOf(REFERENCE_END);
					if(bracketOpenIndex != -1 && bracketEndIndex != -1){ //its an object reference
						parameters.push(new ObjectReference(stringParam.substring(bracketOpenIndex+1, bracketEndIndex), header)); 
						continue;
					}
					
					//Okay, it's just a literal
					if(stringParam.indexOf(LITERAL_QUOTE) == 0 && stringParam.lastIndexOf(LITERAL_QUOTE) == stringParam.length-1){
						stringParam = stringParam.substring(1, stringParam.length-1);
						stringParam = unquoteString(stringParam);
						parameters.push(new Literal(stringParam));
					}else
						throw new Error("Malformed parameter string (unexpected text found): " + stringParam);
					
				}
			}
			
			//returns the appropriate function
			var func:Class = FunctionMapper.getFunction(fName);
			if(func != null)
				return new func(parameters);
			else
				throw new Error("Function " + fName + " does not exist!");
		}
		
		private static function quoteString(str:String):String{
			for(var i:int = 0; i < SPECIAL.length; i++)
				str = str.replace(new RegExp("\\\\\\" + SPECIAL[i], "g"), QUOTE_START + i + QUOTE_END);
			
			return str;
		}
		
		private static function unquoteString(str:String):String{
			for(var i:int = 0; i < SPECIAL.length; i++)
				str = str.replace(new RegExp(QUOTE_START + i + QUOTE_END, "g"), SPECIAL[i]);
			
			return str;
		}
	}
}