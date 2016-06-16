package com.deleidos.rtws.alertviz.expandgraph.graph
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.*;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.FunctionFactory;
	import com.deleidos.rtws.alertviz.expandgraph.graph.properties.*;

	public class IPGraphSettings extends GraphSettings
	{
		public function IPGraphSettings()
		{
			var dataModel:String = "cef";
			var properties:Object = new Object();
			
			properties["Source"] = new Vector.<GraphProperties>();
			properties["Source"].push(new GraphProperties("Root Level", sourceRootNodeProperties(), sourceRootEdgeProperties()));
			properties["Source"].push(new GraphProperties("Network Level", sourceNetworkLevelNodeProperties(), sourceRootEdgeProperties()));
			properties["Source"].push(new GraphProperties("IP Level", sourceIPLevelNodeProperties(), sourceRootEdgeProperties()));
			
			properties["Destination"] = new Vector.<GraphProperties>();
			properties["Destination"].push(new GraphProperties("Root Level", destinationRootNodeProperties(), destinationRootEdgeProperties()));
			properties["Destination"].push(new GraphProperties("Network Level", destinationNetworkLevelNodeProperties(), destinationRootEdgeProperties()));
			properties["Destination"].push(new GraphProperties("IP Level", destinationIPLevelNodeProperties(), destinationRootEdgeProperties()));
			
			super(dataModel, properties);
		}
		
		private function sourceRootNodeProperties():Properties{
			var cK:CustomKey;
			var cW:IEvaluable;
			var cC:IEvaluable;
			var cL:IEvaluable;
			
			//Making the custom key
			var header:String = "0";
			var functionList:Vector.<IEvaluable> = new Vector.<IEvaluable>();
			
			cK = new CustomKey(header, functionList);
			cW = FunctionFactory.parse("15");
			cC = FunctionFactory.parse("colormap(avg(map({arcsightHeader.severity},Low,0,Medium,1,High,2,Very-High,3)),0,3,rgbcolor(green),rgbcolor(yellow),rgbcolor(red))");
			cL = FunctionFactory.parse("Source");
			return new Properties(cK, cW, cC, cL);
		}
		
		private function sourceNetworkLevelNodeProperties():Properties{
			var cK:CustomKey;
			var cW:IEvaluable;
			var cC:IEvaluable;
			var cL:IEvaluable;
			
			//Making the custom key
			var header:String = "0";
			var functionList:Vector.<IEvaluable> = new Vector.<IEvaluable>();
			functionList.push(FunctionFactory.parse("{networkData.source.network.network}"));
			
			cK = new CustomKey(header, functionList);
			cW = FunctionFactory.parse("greater(lesser(count(),50),10)");
			cC = FunctionFactory.parse("colormap(avg(map({arcsightHeader.severity},Low,0,Medium,1,High,2,Very-High,3)),0,3,rgbcolor(green),rgbcolor(yellow),rgbcolor(red))");
			cL = FunctionFactory.parse("{networkData.source.network.network}");
			return new Properties(cK, cW, cC, cL);
		}
		
		private function sourceIPLevelNodeProperties():Properties{
			var cK:CustomKey;
			var cW:IEvaluable;
			var cC:IEvaluable;
			var cL:IEvaluable;
			
			//Making the custom key
			var header:String = "0";
			var functionList:Vector.<IEvaluable> = new Vector.<IEvaluable>();
			functionList.push(FunctionFactory.parse("{networkData.source.network.network}"));
			functionList.push(FunctionFactory.parse("{networkData.source.ip}"));
				
			cK = new CustomKey(header, functionList);
			cW = FunctionFactory.parse("greater(lesser(count(),50),10)");
			cC = FunctionFactory.parse("colormap(avg(map({arcsightHeader.severity},Low,0,Medium,1,High,2,Very-High,3)),0,3,rgbcolor(green),rgbcolor(yellow),rgbcolor(red))");
			cL = FunctionFactory.parse("{networkData.source.ip}");
			
			return new Properties(cK, cW, cC, cL);
		}
		
		private function destinationRootNodeProperties():Properties{
			var cK:CustomKey;
			var cW:IEvaluable;
			var cC:IEvaluable;
			var cL:IEvaluable;
			
			//Making the custom key
			var header:String = "1";
			var functionList:Vector.<IEvaluable> = new Vector.<IEvaluable>();
			
			cK = new CustomKey(header, functionList);
			cW = FunctionFactory.parse("15");
			cC = FunctionFactory.parse("colormap(avg(map({arcsightHeader.severity},Low,0,Medium,1,High,2,Very-High,3)),0,3,rgbcolor(green),rgbcolor(yellow),rgbcolor(red))");
			cL = FunctionFactory.parse("Destination");
			return new Properties(cK, cW, cC, cL);
		}
		
		private function destinationNetworkLevelNodeProperties():Properties{
			var cK:CustomKey;
			var cW:IEvaluable;
			var cC:IEvaluable;
			var cL:IEvaluable;
			
			//Making the custom key
			var header:String = "1";
			var functionList:Vector.<IEvaluable> = new Vector.<IEvaluable>();
			functionList.push(FunctionFactory.parse("{networkData.destination.network.network}"));
			
			cK = new CustomKey(header, functionList);
			cW = FunctionFactory.parse("greater(lesser(count(),50),10)");
			cC = FunctionFactory.parse("colormap(avg(map({arcsightHeader.severity},Low,0,Medium,1,High,2,Very-High,3)),0,3,rgbcolor(green),rgbcolor(yellow),rgbcolor(red))");
			cL = FunctionFactory.parse("{networkData.destination.network.network}");
			return new Properties(cK, cW, cC, cL);
		}
		
		private function destinationIPLevelNodeProperties():Properties{
			var cK:CustomKey;
			var cW:IEvaluable;
			var cC:IEvaluable;
			var cL:IEvaluable;
			
			//Making the custom key
			var header:String = "1";
			var functionList:Vector.<IEvaluable> = new Vector.<IEvaluable>();
			functionList.push(FunctionFactory.parse("{networkData.destination.network.network}"));
			functionList.push(FunctionFactory.parse("{networkData.destination.ip}"));
			
			cK = new CustomKey(header, functionList);
			cW = FunctionFactory.parse("greater(lesser(count(),50),10)");
			cC = FunctionFactory.parse("colormap(avg(map({arcsightHeader.severity},Low,0,Medium,1,High,2,Very-High,3)),0,3,rgbcolor(green),rgbcolor(yellow),rgbcolor(red))");
			cL = FunctionFactory.parse("{networkData.destination.ip}");
			return new Properties(cK, cW, cC, cL);
		}
		
		private function sourceRootEdgeProperties():Properties{
			var cK:CustomKey;
			var cW:IEvaluable;
			var cC:IEvaluable;
			var cL:IEvaluable;
			
			//Making the custom key
			var header:String = "0";
			var functionList:Vector.<IEvaluable> = new Vector.<IEvaluable>();
			
			cK = new CustomKey(header, functionList);
			cW = FunctionFactory.parse("count()");
			//cC = FunctionFactory.parse("colormap(avg(map({arcsightHeader.severity},Low,0,Medium,1,High,2,Very-High,3)),0,3,rgbcolor(green),rgbcolor(yellow),rgbcolor(red))");
			//cL = FunctionFactory.parse("count()");
			return new Properties(cK, cW, cC, cL);
		}
		
		private function destinationRootEdgeProperties():Properties{
			var cK:CustomKey;
			var cW:IEvaluable;
			var cC:IEvaluable;
			var cL:IEvaluable;
			
			//Making the custom key
			var header:String = "1";
			var functionList:Vector.<IEvaluable> = new Vector.<IEvaluable>();
			
			cK = new CustomKey(header, functionList);
			cW = FunctionFactory.parse("count()");
			//cC = FunctionFactory.parse("colormap(avg(map({arcsightHeader.severity},Low,0,Medium,1,High,2,Very-High,3)),0,3,rgbcolor(green),rgbcolor(yellow),rgbcolor(red))");
			//cL = FunctionFactory.parse("count()");
			return new Properties(cK, cW, cC, cL);
		}
		
		
	}
}