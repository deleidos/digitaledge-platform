//Initializes the comment Popup Button with the Menu items
protected function initCommentPop():void {
	var commentMenu:Menu = new Menu();
	commentMenu.dataProvider = [{label:COMMENT_MAKE}, {label:COMMENT_EDIT}, {label:COMMENT_DELETE}];
	commentMenu.addEventListener(MenuEvent.CHANGE, function(e:MenuEvent):void {
		commentPop.label = e.label;
		commentPop_clickHandler(new MouseEvent(MouseEvent.CLICK));
	});
	commentPop.popUp = commentMenu;
} 


//Toggles the visibility of the comment selected
protected function commentPop_clickHandler(event:MouseEvent):void {
	aoiPop.enabled = false;
	commentPop.enabled = false;
	
	if (commentPop.label == COMMENT_MAKE) {
		for (var f:Number = 0; f < commentList.length; f++) {
			commentList[f].marker.visible = true;
		}
		inputArea.text = "Click the map where the comment should go.";
		map.addEventListener(MapMouseEvent.CLICK, addComment);
	} else if (commentPop.label == COMMENT_EDIT && commentList.length > 0) {
		inputArea.text = "Click the comment which should be edited.";
		for (var a:Number = 0; a < commentList.length; a++) {
			commentList[a].marker.addEventListener(MapMouseEvent.CLICK, editComment);
		}
	} else if (commentPop.label == COMMENT_DELETE && commentList.length > 0) {
		inputArea.text = "Click the comment which should be deleted.";
		for (var s:Number = 0; s < commentList.length; s++) {
			commentList[s].marker.addEventListener(MapMouseEvent.CLICK, deleteComment);
		}
	} else {
		inputArea.text = "No comments to edit or delete.";
		aoiPop.enabled = true;
		commentPop.enabled = true;
	}
}


//Adds a comment at the location of the click
private function addComment(e:MapMouseEvent):void {
	inputArea.text = "Enter Comment Text Here";
	commentPop.label = COMMENT_EDIT;
	inputArea.editable = true;
	doneInputBtn.visible = true;				
	map.removeEventListener(MapMouseEvent.CLICK, addComment);
	
	var options:MarkerOptions = new MarkerOptions({icon: new commentIconSprite(""), draggable:false, iconOffset:new Point(0,-20), hasShadow:false});
	options.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_TOP;
	options.icon.alpha = .7;
	var commentMarker:Marker = new Marker(e.latLng, options);
	commentList.addItem({text:"", marker:commentMarker});
	types["Comments"].addItem(commentMarker);
	map.addOverlay(commentMarker); 
	
	commentIndex = commentList.length - 1;
}


//Deletes the comment that was clicked
private function deleteComment(e:MapMouseEvent):void {
	var index:Number = -1;
	for (var a:Number = 0; a < commentList.length; a++) {
		commentList[a].marker.removeEventListener(MapMouseEvent.CLICK, deleteComment);
		if (e.latLng.lat() == commentList[a].marker.getLatLng().lat() && e.latLng.lng() == commentList[a].marker.getLatLng().lng()) {
			index = a;
		}
	}
	
	if (index != -1) {
		map.removeOverlay(commentList[index].marker);
		commentList.removeItemAt(index);
	} else {
		inputArea.text = "Error: Could not find comment";
	}
	
	aoiPop.enabled = true;
	commentPop.enabled = true;
	inputArea.text = "";
}


//Edit the comment's text after clicking on the marker
private function editComment(e:MapMouseEvent):void {
	var index:Number = -1;
	for (var a:Number = 0; a < commentList.length; a++) {
		commentList[a].marker.removeEventListener(MapMouseEvent.CLICK, editComment);
		if (e.latLng.lat() == commentList[a].marker.getLatLng().lat() && e.latLng.lng() == commentList[a].marker.getLatLng().lng()) {
			index = a;
		}
	}
	
	if (index != -1) {
		inputArea.editable = true;
		doneInputBtn.visible = true;
		inputArea.text = commentList[index].text;
		commentList[index].marker.setOptions(new MarkerOptions({draggable:true}));
		commentIndex = index;
	} else {
		inputArea.text = "Error: Could not find comment";
	}
}

//Finishes entering the input for the comment
protected function doneInput_clickHandler(event:MouseEvent):void
{
	aoiPop.enabled = true;
	commentPop.enabled = true;
	inputArea.editable = false;
	doneInputBtn.visible = false;
	
	var options:MarkerOptions = new MarkerOptions({icon: new commentIconSprite(inputArea.text), draggable:false, iconOffset:new Point(0,-20)});
	options.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_TOP;
	options.icon.alpha = .7;
	
	commentList[commentIndex].marker.setOptions(options);
	commentList[commentIndex].text = inputArea.text;
	
	inputArea.text = "";
}