package com.deleidos.rtws.alertviz.services
{
	public interface IWatchListService
	{
		function retrieveWatchList():void;
		function createWatchListEntry(key:String):void;
		function changeWatchListEntryColor(key:String, color:uint):void;
		function deleteWatchListEntry(key:String):void;
	}
}