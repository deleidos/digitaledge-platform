package com.deleidos.rtws.alertviz.events.repository
{
	import flash.events.Event;
	
	public class ModelServiceEvent extends Event
	{
		public static const MODELSRV_KEY     :String = "modelsrv";
		public static const NEW_MODEL        :String = MODELSRV_KEY + ".newModel";
		public static const NEW_MODEL_DONE   :String = MODELSRV_KEY + ".newModelDone";
		public static const LOAD_MODEL       :String = MODELSRV_KEY + ".loadModel";
		public static const MODEL_LOADED     :String = MODELSRV_KEY + ".modelLoaded";
		public static const MODEL_LOAD_FAIL  :String = MODELSRV_KEY + ".modelLoadFail";
		public static const SAVE_MODEL       :String = MODELSRV_KEY + ".saveModel";
		public static const MODEL_SAVED      :String = MODELSRV_KEY + ".modelSaved";
		public static const ENRICH_DIR_LOAD  :String = MODELSRV_KEY + ".loadEnrichDir";
		public static const ENRICH_DIR_LOADED:String = MODELSRV_KEY + ".enrichDirLoaded";
		public static const PARSER_DIR_LOAD  :String = MODELSRV_KEY + ".loadParseDir";
		public static const PARSER_DIR_LOADED:String = MODELSRV_KEY + ".parseDirLoaded";
		public static const LOAD_MODEL_DIR   :String = MODELSRV_KEY + ".loadModelDir";
		public static const MODEL_DIR_LOADED :String = MODELSRV_KEY + ".modelDirLoaded";
		public static const MODEL_DIR_LOAD_FAIL:String = MODELSRV_KEY + ".modelDirLoadFail";
		
		/* Repository visibility constants */
		public static const PUBLIC  : String = "public";
		public static const PRIVATE : String = "private";
		
		private var _nestedEvent:Event;
		private var _parameters :Object;
		
		public function ModelServiceEvent(type:String, parameters:Object, nestedEvent:Event, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			_parameters = parameters;
			_nestedEvent = nestedEvent;
		}
		
		public function get nestedEvent():Event {
			return _nestedEvent;
		}
		
		public function get parameters():Object {
			return _parameters;
		}
		
		public override function clone():Event
		{
			return new ModelServiceEvent( type, _parameters, _nestedEvent, bubbles, cancelable );
		}
	}
}