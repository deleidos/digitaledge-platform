package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable
{
	import com.adobe.serialization.json.JSON;
	
	import mx.utils.ObjectUtil;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.FunctionFactory;

	public class ObjectReference implements IEvaluable
	{
		
		private var references:Array;
		private var header:ObjectReference;
		
		private var _value:String = null;
		public function get value():String{
			return _value;
		}
		
		public function ObjectReference(obj:String, header:ObjectReference = null)
		{
			if(obj == null)
				references = null;
			else{
				references = obj.split(FunctionFactory.REFERENCE_DELIMITER);
				this.header = header;
			}
		}
		
		public function evaluate(o:Object):String{
			if(o == null || references == null)
				return null;
			else{
				var curObj:Object = o; var ref:String; var tmp:Object;
				if(header != null){
					for each(ref in header.references){
						tmp = curObj[ref];
						if(tmp == null)
							return null;
						curObj = tmp;
					}
				}
				
				for each(ref in references){
					tmp = curObj[ref];
					if(tmp == null)
						return null;
					curObj = tmp;
				}
				
				return ObjectUtil.isSimple(curObj) ? curObj.toString() : JSON.encode(curObj);
			}
		}
		
		public function addItem(o:Object):String{
			_value = evaluate(o);
			return _value;
		}
		
		public function deleteItem(o:Object):String{
			return _value;
		}
		
		public function reset():IEvaluable{
			return this; //ObjectReference is immutable
		}
		
		public function toString():String{
			var definition:String = FunctionFactory.REFERENCE_START;
			
			for( var i:int = 0; i < references.length; i++ ){
				if(i != 0) definition += FunctionFactory.REFERENCE_DELIMITER;
				definition += references[i];
			}
			
			definition += FunctionFactory.REFERENCE_END;
			
			return definition;
		}
	}
}