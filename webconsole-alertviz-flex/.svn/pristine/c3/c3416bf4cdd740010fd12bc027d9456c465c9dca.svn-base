package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions
{
	import flash.utils.getDefinitionByName;
	import flash.utils.getQualifiedClassName;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful.AddFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.stateful.AverageFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful.ColorMapFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful.ConcatFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.stateful.CountFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.stateful.DcountFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful.EqualsFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful.GreaterFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful.LesserFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful.LogFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful.MapFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.stateful.MaxFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.stateful.MinFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful.MonochromeColorFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful.MultiplyFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful.RGBColorFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.stateful.SumFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful.BaseFunction;

	public class FunctionMapper
	{
		
		private static var _init:Boolean = false;
		private static var _map:Object = null;
		
		public function FunctionMapper(){
			throw new Error("FunctionMapper not instantiable");
		}
		
		public static function getFunction(fName:String):Class{
			if(!_init)
				initialize();
			
			return _map[fName][0];
		}
		
		public static function getUsage(fName:String):String{
			if(!_init)
				initialize();
			
			return _map[fName][1];
		}
		
		public static function getMap():Object{
			if(!_init)
				initialize();
			
			return _map;
		}
		
		private static function initialize():void{
			_map = new Object();
			
			_map[BaseFunction.NAME] = 		[BaseFunction as Class, BaseFunction.USAGE];
			_map[AddFunction.NAME] =		[AddFunction as Class, AddFunction.USAGE];
			_map[AverageFunction.NAME] =	[AverageFunction as Class, AverageFunction.USAGE];
			_map[CountFunction.NAME] =		[CountFunction as Class, CountFunction.USAGE];
			_map[DcountFunction.NAME] =		[DcountFunction as Class, DcountFunction.USAGE];
			_map[MinFunction.NAME] = 		[MinFunction as Class, MinFunction.USAGE];
			_map[MaxFunction.NAME] = 		[MaxFunction as Class, MaxFunction.USAGE];
			_map[MonochromeColorFunction.NAME] = [MonochromeColorFunction as Class, MonochromeColorFunction.USAGE];
			_map[RGBColorFunction.NAME] = 	[RGBColorFunction as Class, RGBColorFunction.USAGE];
			_map[MapFunction.NAME] = 		[MapFunction as Class, MapFunction.USAGE];
			_map[ColorMapFunction.NAME] = 	[ColorMapFunction as Class, ColorMapFunction.USAGE];
			_map[LesserFunction.NAME] = 	[LesserFunction as Class, LesserFunction.USAGE];
			_map[GreaterFunction.NAME] = 	[GreaterFunction as Class, GreaterFunction.USAGE];
			_map[ConcatFunction.NAME] = 	[ConcatFunction as Class, ConcatFunction.USAGE];
			_map[EqualsFunction.NAME] = 	[EqualsFunction as Class, EqualsFunction.USAGE];
			_map[LogFunction.NAME] = 		[LogFunction as Class, LogFunction.USAGE];
			_map[MultiplyFunction.NAME] = 	[MultiplyFunction as Class, MultiplyFunction.USAGE];
			_map[SumFunction.NAME] = 		[SumFunction as Class, SumFunction.USAGE];
			
			_init = true;
		}
	}
}