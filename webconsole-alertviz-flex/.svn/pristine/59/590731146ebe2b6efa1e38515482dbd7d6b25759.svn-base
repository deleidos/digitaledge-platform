import com.adobe.serialization.json.*;
import com.google.maps.InfoWindowOptions;
import com.google.maps.LatLng;
import com.google.maps.LatLngBounds;
import com.google.maps.MapEvent;
import com.google.maps.MapMouseEvent;
import com.google.maps.controls.ControlPosition;
import com.google.maps.controls.MapTypeControl;
import com.google.maps.controls.NavigationControl;
import com.google.maps.controls.ScaleControl;
import com.google.maps.overlays.Marker;
import com.google.maps.overlays.MarkerOptions;
import com.google.maps.overlays.Polygon;
import com.google.maps.overlays.PolygonOptions;
import com.google.maps.overlays.Polyline;
import com.google.maps.overlays.PolylineOptions;
import com.deleidos.rtws.alertviz.events.WatchListCommandEvent;
import com.deleidos.rtws.alertviz.googlemap.MapViewMediator;
import com.deleidos.rtws.alertviz.googlemap.iconSprite.commentIconSprite;

import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.MouseEvent;
import flash.net.navigateToURL;
import flash.utils.Dictionary;

import flexlib.mdi.containers.MDIWindow;

import mx.collections.ArrayCollection;
import mx.collections.ArrayList;
import mx.controls.Menu;
import mx.events.MenuEvent;
import mx.rpc.http.HTTPService;
import mx.utils.ArrayUtil;

[Embed(source="/assets/map/camera.png")] private var cameraIcon:Class;
[Embed(source="/assets/map/car.png")] private var carIcon:Class;
[Embed(source="/assets/map/plane.png")] private var planeIcon:Class;
[Embed(source="/assets/map/radar.png")] private var radarIcon:Class;
[Embed(source="/assets/map/default.png")] private var defaultIcon:Class;
[Embed(source="/assets/map/dot.png")] private var circleIcon:Class;
[Embed(source="/assets/map/boat.png")] private var boatIcon:Class;
[Embed(source="/assets/map/bus.png")] private var busIcon:Class;
[Embed(source="/assets/map/helicopter.png")] private var helicopterIcon:Class;
[Embed(source="/assets/map/person.png")] private var personIcon:Class;
[Embed(source="/assets/map/question.png")] private var questionIcon:Class;
[Embed(source="/assets/map/subway.png")] private var subwayIcon:Class;
[Embed(source="/assets/map/train.png")] private var trainIcon:Class;
[Embed(source="/assets/map/pen.png")] private var penIcon:Class;


private const US_CENTER_POINT:LatLng = new LatLng(39.828175, -98.579500);
private const US_ZOOM_LEVEL:int = 3;

//default colors for points, paths, and lobs (RED)
private const POINT_COLOR:uint = 0xff0000;
private const PATH_COLOR:uint = 0xff0000;
private const ELLIPSE_COLOR:uint = 0xff0000;
private const LOB_COLOR:uint = 0xff0000;

//labels for the comment button
private const COMMENT_MAKE:String = "Make Comment";
private const COMMENT_EDIT:String = "Edit Comment";
private const COMMENT_DELETE:String = "Delete Comment";

//labels and colors for drawing AoIs
private const AOI_DRAW:String = "Draw AoI";
private const AOI_FINISH:String = "Finish AoI";
private const AOI_EDIT:String = "Edit AoI";
private const AOI_RESET:String = "Delete AoI";
private const STROKE_BLUE:Number = 0x0000FF;
private const FILL_BLUE:Number = 0x0055FF;

//holds all of the comments on the map
private var commentList:ArrayCollection = new ArrayCollection();

//used to reference a comment in the list
private var commentIndex:Number;

//Markers and Polygon for creating a AoI
private var aoiMarkers:ArrayList = new ArrayList();
private var aoiArea:Polygon = null;
private var aois:ArrayList = new ArrayList();
private var aoiColors:Array = new Array(FILL_BLUE, STROKE_BLUE);

//used to send requests to the google earth servlet
private var httpService:HTTPService = new HTTPService();

//list of JSONObjects that are on the map
private var objectsOnMap:ArrayList = new ArrayList();

//list of all event types that have came in
private var eventTypes:ArrayList = new ArrayList();

//list of all colorMarkers and imgMarkers on the map
private var markers:ArrayList = new ArrayList();

//list of all path polylines on the map
private var paths:ArrayList = new ArrayList();

//list of all the short path polylines which are inside AoIs
private var aoiPaths:ArrayList = new ArrayList();

//list of all ellipse polygons on the map
private var ellipses:ArrayList = new ArrayList();

//list of all lob polygons on the map
private var lobs:ArrayList = new ArrayList();

//list of all cirMarkers on the map
private var cirMarkers:ArrayList = new ArrayList();

//list of all alertKeys that have came in
private var alertKeys:ArrayList = new ArrayList();

//given colorMarker, return the objectIdValue
//given imgMarker, return colorMarker
//given path, return objectIdValue
//given cirMarker, return colorMarker
private var dict:Dictionary = new Dictionary();

//given event type, return list of overlays with that event type
//given overlay, return event type of overlay
private var types:Dictionary = new Dictionary();

//given imgMarker or cirMarker, return list of jsonObjects at that marker
//given jsonObject, return list of imgMarker's and colorMarker's if path or point or list of polygon's if lob
private var objects:Dictionary = new Dictionary();

//given alertKey, return list of colorMarkers with that alertKey
private var keys:Dictionary = new Dictionary();

//given alertKey, return the color of all overlays with that alertKey
private var colors:Dictionary = new Dictionary();

//drop down menu for the toggle button
private var toggleMenu:Menu = new Menu();

//marker that used to have an info window open
private var oldMarker:Marker = null;

//limit of objects on the map
private var queueLimit:Number = 250;

//states whether to follow alerts that match an alertKey
private var follow:Boolean = false;

//states whether to follow a specific marker
private var followMarker:Boolean = false;

//the alertKey that is to be followed
private var alertKeyToFollow:String = "";

//the id of a marker that is to be followed
private var idToFollow:String = "";

//data model configuration object
private var config:Object;