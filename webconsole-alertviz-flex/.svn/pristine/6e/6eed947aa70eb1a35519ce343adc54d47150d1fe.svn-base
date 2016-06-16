package com.deleidos.rtws.alertviz.utils.repository
{
	import com.deleidos.rtws.commons.util.StringUtils;
	
	import mx.validators.RegExpValidator;
	import mx.validators.ValidationResult;
	
	public class ValidationUtils
	{
		/** constant that represents an identifier */
		public static const identifierRegEx:String = "^\\w+$";
		
		/** Regular expressions for valid data mapping commands */
		public static const getFieldRegEx:RegExp    = /get\([\w]+(,.+){0,1}\)/;
		public static const setConstantRegEx:RegExp = /=.*/;
		public static const runScriptRegEx:RegExp   = /script\([\w]+(,\s*[\w]+)*?\)/;
		public static const doConvertRegEx:RegExp   = /convert\([\w]+(\s*\+\s*[\w]+)*?,.+\)/;
		
		/**
		 *  Regular expressions for valid input model data types
		 *  Note the date/time format string will have to be validated separately
		 */
		public static const dataTypeRegEx:String = "^string$|^number$|^datestring\\s+.+$";
		
		/** custom date/time format specifiers with comments in parens */
		public static const customDateTimeFormatters:Array = [
			"G (era, BC/AD)",
			"yy (two digit year)",
			"yyyy (four digit year)",
			"MM (two digit month)",
			"MMM (month name abbreviated)",
			"MMMM (month name full)",
			"w (week # in the year)",
			"W (week # in the month)",
			"D (day in the year)",
			"dd (two digit month)",
			"F (day # of week in month)",
			"E (day name of week)",
			"EEEE (day name full)",
			"a (AM/PM)",
			"HH (two digit hour 0-23)",
			"hh (two digit hour am/pm 1-12)",
			"mm (two digit minute)",
			"ss (two digit second)",
			"SSS (three digit millisecond)",
			"z (general timezone)",
			"Z (RFC822 timezone)"
		];

		/** Removes the comments in the date/time specifier strings */
		public static function removeComments(s:String):String {
			var i:int = s.indexOf(" (");
			if (i>0)
				return s.substr(0,i);
			else
				return s;
		}
		
		/** Returns true if the custom format specifier exists */
		public static function formatSpecifierExists(spec:String):Boolean {
			for each (var item:String in customDateTimeFormatters) {
				if (removeComments(item) == spec) {
					return true;
				}
			}
			return false;
		}
		
		/** Validates the format string, returns a ValidationResult object */ 
		public static function validateDateFormat(format:String):ValidationResult {
			var result:ValidationResult = new ValidationResult(false);
			if (format.length == 0) {
				result.isError = true;
				result.errorMessage = "Date/time format string is empty.";
			}
			else if ((StringUtils.countOf(format, "'") % 2) != 0) {
				result.isError = true;
				result.errorMessage = "Quotes not matched.";
			}
			else {
				var noQuotes:String = format.replace(/'.*'/,""); // Remove quoted section(s)
				var formatStrings:Array = noQuotes.split(/[^0-9a-zA-Z]+/); // Break on nondigit, nonalpha
				for each (var spec:String in formatStrings) {
					if (!formatSpecifierExists(spec)) {
						result.isError = true;
						result.errorMessage = "Invalid format string '" + spec + "'";
						break;
					}
				}
			}
			return result;
		}

		/** Returns a generic validator for an identifier */
		public static function getIdentifierValidator():RegExpValidator {
			var validator:RegExpValidator = new RegExpValidator();
			validator.flags = "gmi";
			validator.expression = identifierRegEx;
			validator.required = true;
			validator.noMatchError = "Valid names consist of numbers, letters, and underscores";
			validator.requiredFieldError = "Value is required";
			return validator;
		}

		/** Returns a validator for the data type in the input model */
		public static function dataTypeValidator():RegExpValidator {
			var validator:RegExpValidator = new RegExpValidator();
			validator.flags = "gmi";
			validator.expression = dataTypeRegEx;
			validator.required = true;
			validator.noMatchError = "Type must be one of string, number, or datestring <format>";
			validator.requiredFieldError = "Value is required";
			return validator;
		}
		
		/** This is a failure validator - it always fails with the given error message */
		public static function failValidator(errorMsg:String):RegExpValidator {
			var validator:RegExpValidator = new RegExpValidator();
			validator.flags = "gmi";
			validator.expression = "^\\0$"; // Indended as a pattern that will never match
			validator.required = true;
			validator.noMatchError = errorMsg;
			validator.requiredFieldError = "Value is required";
			return validator;
		}
		
	}
}
