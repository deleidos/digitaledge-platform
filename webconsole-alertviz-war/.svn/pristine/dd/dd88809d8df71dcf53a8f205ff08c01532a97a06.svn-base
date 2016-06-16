// Notify AlertViz that the browser is closing
if(window && window != null && typeof window !== "undefined") {
	jQuery(window).unload(function(){
		FABridge.alertVizFlexAjaxBridge.root().appClosingExternally();
	});
}