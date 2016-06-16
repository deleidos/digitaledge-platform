package com.deleidos.rtws.alertviz.json
{
	public class ParseString
	{
		private var string:String;
		private var _current:int;
		
		public function ParseString(value:String)
		{
			string = value;
			_current = 0;
		}
		
		private function makeSpaces(len:int):String {
			var spaces:String = "";
			while (len--) {
				spaces += " ";
			}
			return spaces;
		}
		
		private static var STATE_NORMAL:int        = 0;
		private static var STATE_QUOTED_STRING:int = 1;
		private static var STATE_ESCAPED_CHAR:int  = 2;
		private static var STATE_COMMENT_START:int = 3;
		private static var STATE_IN_COMMENT:int    = 4;
		
		public function eraseComments():void {
			var state:int = STATE_NORMAL;
			var commentStart:int = 0;
			for (var i:int = 0; i<string.length; i++) {
				var c:String = string.charAt(i);
				switch (state)
				{
					case STATE_NORMAL:
						if (c == '"') {
							state = STATE_QUOTED_STRING;
						}
						else if (c == "/") {
							state = STATE_COMMENT_START;
							commentStart = i;
						}
						break;
					case STATE_QUOTED_STRING:
						if (c == "\\")
							state = STATE_ESCAPED_CHAR;
						else if (c == "\"")
							state = STATE_NORMAL;
						break;
					case STATE_ESCAPED_CHAR:
						// Ignore the character
						state = STATE_QUOTED_STRING;
						break;
					case STATE_COMMENT_START:
						if (c == "/")
							state = STATE_IN_COMMENT;
						else
							state = STATE_NORMAL;
						break;
					case STATE_IN_COMMENT:
						if ((c == '\n') || (c == '\r')) {
							var len:int = (i - commentStart);
							var spaces:String = makeSpaces(len);
							string = string.substring(0,commentStart) + spaces + string.substr(i);
							state = STATE_NORMAL;
						}
						break;
				}
			}
		}
		
		public function get current():int {
			return _current;
		}
		
		public function get length():int {
			return string.length;
		}
		
		public function set current(value:int):void {
			_current = value;
		}
		
		public function get currentChar():String {
			return string.charAt(_current);
		}
		
		public function currentCharIsSpace():Boolean {
			var c:String = string.charAt(_current);
			return ((c == ' ') || (c == '\t') || (c == '\r') || (c == '\n'));
		}
		
		public function skipWhiteSpace():void {
			while ((_current < string.length) && currentCharIsSpace()) {
				_current += 1;
			}
		}
		
		public function charAt(index:Number = 0):String {
			return string.charAt(index);
		}
		
		public function indexOf(val:String, startIndex:Number = 0):int {
			return string.indexOf(val,startIndex);
		}

		public function substr(startIndex:Number = 0, len:Number = 0x7fffffff):String {
			return string.substr(startIndex, len);
		}
		
		public function substring(startIndex:Number = 0, endIndex:Number = 0x7fffffff):String {
			return string.substring(startIndex,endIndex);
		}
	}
}
