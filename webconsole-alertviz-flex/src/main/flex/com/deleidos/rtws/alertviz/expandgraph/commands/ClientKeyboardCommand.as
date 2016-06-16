package com.deleidos.rtws.alertviz.expandgraph.commands
{
	import com.deleidos.rtws.alertviz.expandgraph.models.GraphModel;
	import com.deleidos.rtws.alertviz.expandgraph.events.ClientKeyboardEvent;
	
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	
	import org.robotlegs.mvcs.Command;

	public class ClientKeyboardCommand extends Command
	{
		[Inject]
		public var event:ClientKeyboardEvent;
		
		[Inject]
		public var model:GraphModel;
		
		override public function execute():void{
			if(event.type == ClientKeyboardEvent.KEY_DOWN && event.keyCode == Keyboard.CONTROL){
				model.silent = true;
			}else if(event.type == ClientKeyboardEvent.KEY_UP && event.keyCode == Keyboard.CONTROL){
				model.silent = false;
			}
		}
	}
}