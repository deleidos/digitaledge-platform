package com.deleidos.rtws.alertviz.json
{
	public class OrderedJson
	{
		public static const quote:String = "\"";
		
		/**
		 * Returns true if the two strings are textually equal json strings after removing
		 * all whitespace.
		 */
		public static function equalJsonText(jsonText1:String, jsonText2:String):Boolean {
			var jsonCompare1:String = jsonText1.replace(/\s+/g, ''); // remove all spaces
			var jsonCompare2:String = jsonText2.replace(/\s+/g, ''); // remove all spaces
			return jsonCompare1 == jsonCompare2;
		}
		
		/**
		 * Converts the given object to a json string.
		 */
		public static function encode(input:Object):String {
			var result:String = null;
			if (input is String) {
				var s:String = input as String;
				result = quote + s + quote;
			}
			else if (input is Number) {
				var n:Number = input as Number;
				result = n.toString();
			}
			else if (input is JsonLiteral) {
				var literal:JsonLiteral = input as JsonLiteral;
				result = literal.label;
			}
			else if (input is JsonObject) {
				result = toJsonObjectText(input as JsonObject);
			}
			else if (input is Array) {
				result = toJsonArrayText(input as Array);
			}
			else {
				throw Error("Invalid object - cannot convert to json text");
			}
			return result;
		}
		
		private static function toJsonArrayText(input:Array):String {
			var result:String = "[ ";
			var comma:String = "";
			for each (var element:Object in input) {
				result = result.concat(comma, encode(element));
				comma = ", ";
			}
			result = result + " ]";
			return result;
		}
		
		private static function toJsonObjectText(input:JsonObject):String {
			var result:String = "{ ";
			var comma:String = "";
			for each (var item:JsonItem in input) {
				result = result.concat(comma,quote,item.name,quote," : ",encode(item.value));
				comma = ", ";
			}
			result = result + " }";
			return result;
		}
		
		/**
		 * Parse the given JSON text. Will throw an Error exception when an
		 * error is detected.
		 */
		public static function decode(input:String):Object {
			
			var text:ParseString = new ParseString(input);
			var result:Object;
			
			text.eraseComments();
			skipTo(text, "{[", "Expecting '{' or '['");
			
			if (text.currentChar == "{") {
				result = getObjectItem(text); 
			}
			else {
				result = getArrayItem(text);
			}
			
			return result;
		}
		
		/**
		 * Gets the named item from the JsonObject and returns its value.
		 */
		public static function getValue(name:String, obj:JsonObject):Object {
			for each (var item:JsonItem in obj) {
				if (item.name == name) {
					return item.value;
				}
			}
			return null;
		}

		/**
		 * Skip past white space to the next character, and check if that character is
		 * one of the targets. If not, return false, and optionally throw an exception
		 * with the passed error.
		 */
		private static function skipTo(text:ParseString, targets:String, errmsg:String = null):Boolean {
			
			text.skipWhiteSpace();
			
			if (targets.indexOf(text.currentChar) >= 0) {
				return true;
			}
			
			if (errmsg != null) {
				throw new Error(makeErrorMessage(errmsg,text));				
			}
			
			return false;
		}
		
		/**
		 * Get and return the value in quotes. Expects the next token
		 * to be a quoted string.
		 */
		private static function getStringToken(text:ParseString):String {
			var result:String = "";
			
			skipTo(text,quote,"Expecting '\"'");
			var beginQuote:int = text.current;
			var endQuote:int = beginQuote;
			do {
				endQuote = text.indexOf(quote,endQuote+1);
			}
			while ((endQuote > 0) && (text.charAt(endQuote-1) == "\\"));

			if (endQuote < 0) {
				throw new Error(makeErrorMessage("End quote not found",text));
			}
			else {
				result = text.substring(beginQuote+1, endQuote);
				text.current = endQuote + 1;
			}
			
			return result;
		}
		
		/**
		 * Get the next "item" in an object, which is a name/value pair,
		 * where the value can be any valid JSON value.
		 * { }
		 * { "f1" : "v1" }
		 * { "f1" : "v1", "f2", "v2" }
		 */
		private static function getObjectItem(text:ParseString):JsonObject {
			var result:JsonObject = new JsonObject();
			
			skipTo(text,"{","Expecting '{'");
			
			do {
				text.current += 1;
				text.skipWhiteSpace();
				if (text.currentChar == "}") {
					break; // Empty object
				}
				
				var name:String = getStringToken(text);
				skipTo(text,":","Expecting ':'");
				text.current += 1;
				var value:Object = getNextToken(text);
				result.push(new JsonItem(name,value));				
				skipTo(text,",}", "Expecting ',' or '}'");
			} while (text.currentChar == ",");
			
			text.current += 1;
			
			return result;
		}

		/**
		 * Get and return an array. Expects the next character is a [,
		 * followed by a set of values separated by commas, and ending
		 * in a ].
		 */
		private static function getArrayItem(text:ParseString):Array {
			var result:Array = new Array();

			skipTo(text,"[","Expecting '['");
			do {
				text.current += 1;
				var element:Object = getNextToken(text);
				if (element != null) {
					result.push(element);
				}
				skipTo(text, ",]", "Expecting ',' or ']'");
			} while (text.currentChar != ']');
			
			text.current += 1;
			
			return result;
		}
		
		/**
		 * Gets and returns the next token as a number. Expects the
		 * next token is a valid JSON number.
		 */
		private static function getNumberToken(text:ParseString):Number {
			// Skip to the next non-number related character
			const numberChars:String = "-+0123456789.eE";
			var numberStart:int = text.current;
			var numberEnd:int = numberStart;
			while (numberChars.indexOf(text.charAt(numberEnd)) >= 0) {
				numberEnd++;
			}
			var numString:String = text.substring(numberStart,numberEnd);
			var jsonNumberRegEx:RegExp = /^-?(?:0|[1-9]\d*)(?:\.\d+)?(?:[eE][+\-]?\d+)?$/;
			if (!numString.match(jsonNumberRegEx)) {
				throw new Error(makeErrorMessage("Invalid number: '" + numString + "'", text));
			}
			text.current = numberEnd;
			return new Number(numString);
		}
		
		/**
		 * Gets and returns a literal, which can be 'true', 'false', 'null'.
		 */
		private static function getLiteralToken(text:ParseString, expectedLiteral:JsonLiteral):JsonLiteral {
			var label:String = expectedLiteral.label;
			if (text.substr(text.current,label.length) == label) {
				text.current += label.length;
				return expectedLiteral;
			}
			else {
				throw new Error(makeErrorMessage("Expecting '" + expectedLiteral.label + "'",text));
			}
		}
		
		/**
		 * Gets the next token, which can be an object, array, string, number,
		 * the literals 'true', 'false', and 'null'.
		 */
		private static function getNextToken(text:ParseString):Object {
			var result:Object = null;
			
			text.skipWhiteSpace();
			
			var nextChar:String = text.currentChar;
			if (nextChar == "{") {
				result = getObjectItem(text);
			}
			else if (nextChar == "[") {
				result = getArrayItem(text);
			}
			else if (nextChar == quote) {
				result = getStringToken(text);
			}
			else if (nextChar.match(/[\-0-9]/)) {
				result = getNumberToken(text);
			}
			else if (nextChar == "t") {
				result = getLiteralToken(text,JsonLiteral.jsonTrue);
			}
			else if (nextChar == 'f') {
				result = getLiteralToken(text,JsonLiteral.jsonFalse);
			}
			else if (nextChar == 'n') {
				result = getLiteralToken(text,JsonLiteral.jsonNull);
			}
			else if ((nextChar == '}') || (nextChar == ']')) {
				result = null;
			}
			else {
				throw new Error(makeErrorMessage("Expecting one of '{', '[', string, number, true, false, null",text));
			}
			return result;
		}


		/**
		 * Format an error message
		 */
		private static function makeErrorMessage(prefix:String, text:ParseString):String {
			
			// Figure out which line and character within that line the error is
			var lineCount:int = 0;
			var lastLineEnd:int = -1;
			for (var i:int=0; i<text.current; i++) {
				if (text.charAt(i) == "\n") {
					lineCount++;
					lastLineEnd = i;
				}
			}
			
			var position:int = text.current - lastLineEnd;
			lineCount++;
			
			return prefix + " at line " + lineCount + ", at char " + position; 
		}
	}
}
