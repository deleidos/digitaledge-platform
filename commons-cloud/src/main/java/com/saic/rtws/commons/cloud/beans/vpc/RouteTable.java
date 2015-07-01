package com.saic.rtws.commons.cloud.beans.vpc;

import java.util.ArrayList;
import java.util.List;

public class RouteTable {

	private String routeTableId;
	private List<Route> routes;
	private List<String> associationIds;
	
	public RouteTable(){
		
	}
	
	public void setRouteTableId(String routeTableId){
		this.routeTableId = routeTableId;
	}
	
	public String getRouteTableId(){
		return routeTableId;
	}
	
	public void setRoutes(List<Route> routes){
		this.routes = routes;
	}
	
	public List<Route> getRoutes(){
		return routes;
	}
	
	public void addRoute(Route route){
		if(routes == null){
			routes = new ArrayList<Route>();
		}
		
		routes.add(route);
	}
	
	public List<String> getAssociationIds() {
		return this.associationIds;
	}
	
	public void addAssociationId(String associationId) {
		if (associationIds == null) {
			associationIds = new ArrayList<String>();
		}
		
		associationIds.add(associationId);
	}
}
