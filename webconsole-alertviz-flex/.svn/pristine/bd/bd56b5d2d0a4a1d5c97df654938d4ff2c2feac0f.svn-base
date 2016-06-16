package com.deleidos.rtws.alertviz.utils
{
	import com.adobe.serialization.json.JSON;

	public class AlertParser
	{
		public function AlertParser()
		{
			throw Error("AlertParser is not instantiable");
		}
		
		public static function followsDefinition(alert:Object, alertDefinition:Object):Boolean{
			for(var param:String in alertDefinition){
				if(!(alertDefinition[param] is String)){
					if(!followsDefinition(alert[param],alertDefinition[param]))
						return false;
				}else{
					var alertTemplate:String = alertDefinition[param];
					var alertValue:String = alert[param];
					
					//see if alertValue matches the template
					if(alertTemplate.indexOf("=") != -1){
						var valid:String = alertTemplate.substring(alertTemplate.indexOf("'"), alertTemplate.lastIndexOf("'"));
						if(valid == alertValue)
							return true;
					}else if(alertTemplate.indexOf(">") != -1){
						var valid:String = alertTemplate.substring(alertTemplate.indexOf(">")+1).split(" ").join(""); //remove whitespace
						if(Number(alertValue) > Number(valid))
							return true;
					}else if(alertTemplate.indexOf(">=") != -1){
						var valid:String = alertTemplate.substring(alertTemplate.indexOf(">=")+2).split(" ").join(""); //remove whitespace
						if(Number(alertValue) >= Number(valid))
							return true;
					}else if(alertTemplate.indexOf("<") != -1){
						var valid:String = alertTemplate.substring(alertTemplate.indexOf("<")+1).split(" ").join(""); //remove whitespace
						if(Number(alertValue) < Number(valid))
							return true;
					}else if(alertTemplate.indexOf("<=") != -1){
						var valid:String = alertTemplate.substring(alertTemplate.indexOf("<=")+2).split(" ").join(""); //remove whitespace
						if(Number(alertValue) <= Number(valid))
							return true;
					}else if(alertTemplate.indexOf("LIKE ") != -1){
						var valid:String = alertTemplate.substring(alertTemplate.indexOf("'"), alertTemplate.lastIndexOf("'"));
						valid = valid.replace("*",".*");
						var regexp:RegExp = new RegExp(valid);
						return regexp.test(alertValue);
					}else if(alertTemplate.indexOf("IN ") != -1){
						var regexp:RegExp = /'(.*?)'/g;
						var values:Array = alertTemplate.match(regexp);
						if(values == null)
							return false;
						
						for each(var val:String in values){
							val = val.substring(1,val.length-1);
							if(alertValue == val) //cut off the ' marks
								return true;
						}
						
						return false;
					}
				}
			}
			return true;
		}
	}
}