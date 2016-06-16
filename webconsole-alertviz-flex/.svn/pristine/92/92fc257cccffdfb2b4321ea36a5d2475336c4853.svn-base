package com.deleidos.rtws.alertviz.views.popups
{		
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	
	import flash.events.MouseEvent;
	
	import mx.managers.PopUpManager;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class TestWildCardPopUpViewMediator extends Mediator
	{
		[Inject]
		public var view:TestWildCardPopUpView;
		
		public override function onRegister():void 
		{
			view.testButton.addEventListener(MouseEvent.CLICK, handleTestButton);
			view.useButton.addEventListener(MouseEvent.CLICK, handleUseButton);
			view.cancelButton.addEventListener(MouseEvent.CLICK, handleCloseButton);
			view.closeButton.addEventListener(MouseEvent.CLICK, handleCloseButton);
		}
		
		public override function onRemove():void 
		{
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleCloseButton);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleUseButton);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleTestButton);
		}
		
		/**
		 * handles when the TEST button is clicked
		 */
		protected function handleTestButton(event:MouseEvent):void
		{
			// we use regexp to search
			var regexp:String = new String();
			
			// escape special chars like: $&.- which are
			// special chars for regexp language
			// but we won't put here * and ? which are
			// our search characters
			regexp = view.wildcard.text.replace(new RegExp("([{}\(\)\^$&.\/\+\|\[\\\\]|\]|\-)","g"),"\\$1");
			
			// we transform wildcards to regexp like this:
			//   * = .*
			//   ? = .?
			regexp = regexp.replace(new RegExp("([\*\?])","g"),".$1");
			
			// the string is from the beginning to the end
			regexp = "^" + regexp + "$";
			
			// if the example matches the regexp
			if(view.example.text.search(regexp) >= 0)
			{
				view.result.text = "MATCH";
			}
			else
			{
				view.result.text = "NO MATCH";
			}
		}
		
		/**
		 * handles when the user clicks the USE EXPRESSION button
		 */
		protected function handleUseButton(event:MouseEvent):void
		{
			//sends the expression
			dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.RETURN_VAL, view.wildcard.text));
			closeView();
		}
		
		protected function handleCloseButton(event:MouseEvent):void 
		{
			closeView();
		}
		
		private function closeView():void 
		{
			PopUpManager.removePopUp(view);
			mediatorMap.removeMediator(this);
		}
	}
}