package com.deleidos.rtws.alertviz.services
{
	public interface IAlertCriteriaService
	{
		function retrieveAlertCriterias():void;
		function createAlertCriteria(name:String, model:String, desc:String, def:String):void;
		function updateAlertCriteria(key:String, name:String, model:String, desc:String, def:String):void;
		function deleteAlertCriteria(key:String):void;
	}
}