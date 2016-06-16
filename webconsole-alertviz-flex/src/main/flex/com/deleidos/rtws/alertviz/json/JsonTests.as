package com.deleidos.rtws.alertviz.json
{
	public class JsonTests
	{		
		public static const test1:String =
		"[ 100, 500, 300, 200, 400 ]";
		
		public static const test2:String =
		"{				\"widget\": {					\"debug\": true,					\"window\": {						\"title\": \"Sample Konfabulator Widget\",						\"name\": \"main_window\",						\"width\": 500,						\"height\": 500					},					\"Pairs\": [						{							\"src\": \"Images/Sun.png\",							\"name\": \"sun1\"						},						{							\"hOffset\": 250,							\"vOffset\": 200						},						null,						{							\"alignment\": \"center\"						}					],					\"text\": {						\"a very long item label here\": \"Click Here\",						\"size\": 36,						\"style\": null,						\"name\": \"text1\",						\"hOffset\": 250,						\"vOffset\": 100,						\"alignment\": \"center\",						\"onmouseover\": \"Hello World\"					},					\"onMouseUp\": \"sun1.opacity = (sun1.opacity / 100) * 90;\"				}			}";

		public static const test3:String =
		"{ \"color\": \"red\", \"value\": \"#f00\" }";
		
		public static const test4:String = 
		"{			\"id\": \"0001\",			\"type\": \"donut\",			\"name\": \"Cake\",			\"ppu\": 0.55,			\"batters\":				{					\"batter\":						[							{ \"id\": \"1001\", \"type\": \"Regular\" },							{ \"id\": \"1002\", \"type\": \"Chocolate\" },							{ \"id\": \"1003\", \"type\": \"Blueberry\" },							{ \"id\": \"1004\", \"type\": \"Devil's Food\" }						]				},			\"topping\":				[					{ \"id\": \"5001\", \"type\": \"None\" },					{ \"id\": \"5002\", \"type\": \"Glazed\" },					{ \"id\": \"5005\", \"type\": \"Sugar\" },					{ \"id\": \"5007\", \"type\": \"Powdered Sugar\" },					{ \"id\": \"5006\", \"type\": \"Chocolate with Sprinkles\" },					{ \"id\": \"5003\", \"type\": \"Chocolate\" },					{ \"id\": \"5004\", \"type\": \"Maple\" }				]		}";
		
		public static const test5:String =
		"[			{				\"id\": \"0001\",				\"type\": \"donut\",				\"name\": \"Cake\",				\"ppu\": 0.55,				\"batters\":					{						\"batter\":							[								{ \"id\": \"1001\", \"type\": \"Regular\" },								{ \"id\": \"1002\", \"type\": \"Chocolate\" },								{ \"id\": \"1003\", \"type\": \"Blueberry\" },								{ \"id\": \"1004\", \"type\": \"Devil's Food\" }							]					},				\"topping\":					[						{ \"id\": \"5001\", \"type\": \"None\" },						{ \"id\": \"5002\", \"type\": \"Glazed\" },						{ \"id\": \"5005\", \"type\": \"Sugar\" },						{ \"id\": \"5007\", \"type\": \"Powdered Sugar\" },						{ \"id\": \"5006\", \"type\": \"Chocolate with Sprinkles\" },						{ \"id\": \"5003\", \"type\": \"Chocolate\" },						{ \"id\": \"5004\", \"type\": \"Maple\" }					]			},			{				\"id\": \"0002\",				\"type\": \"donut\",				\"name\": \"Raised\",				\"ppu\": 0.55,				\"batters\":					{						\"batter\":							[								{ \"id\": \"1001\", \"type\": \"Regular\" }							]					},				\"topping\":					[						{ \"id\": \"5001\", \"type\": \"None\" },						{ \"id\": \"5002\", \"type\": \"Glazed\" },						{ \"id\": \"5005\", \"type\": \"Sugar\" },						{ \"id\": \"5003\", \"type\": \"Chocolate\" },						{ \"id\": \"5004\", \"type\": \"Maple\" }					]			},			{				\"id\": \"0003\",				\"type\": \"donut\",				\"name\": \"Old Fashioned\",				\"ppu\": 0.55,				\"batters\":					{						\"batter\":							[								{ \"id\": \"1001\", \"type\": \"Regular\" },								{ \"id\": \"1002\", \"type\": \"Chocolate\" }							]					},				\"topping\":					[						{ \"id\": \"5001\", \"type\": \"None\" },						{ \"id\": \"5002\", \"type\": \"Glazed\" },						{ \"id\": \"5003\", \"type\": \"Chocolate\" },						{ \"id\": \"5004\", \"type\": \"Maple\" }					]			}		]";		

		public static const test6:String =
		"{			\"id\": \"0001\",			\"type\": \"donut\",			\"name\": \"Cake\",			\"image\":				{					\"url\": \"images/0001.jpg\",					\"width\": 200,					\"height\": 200				},			\"thumbnail\":				{					\"url\": \"images/thumbnails/0001.jpg\",					\"width\": 32,					\"height\": 32				}		}";
		
		public static const test7:String = 
		"{			\"items\":				{					\"item\":						[							{								\"id\": \"0001\",								\"type\": \"donut\",								\"name\": \"Cake\",								\"ppu\": 0.55,								\"batters\":									{										\"batter\":											[												{ \"id\": \"1001\", \"type\": \"Regular\" },												{ \"id\": \"1002\", \"type\": \"Chocolate\" },												{ \"id\": \"1003\", \"type\": \"Blueberry\" },												{ \"id\": \"1004\", \"type\": \"Devil's Food\" }											]									},								\"topping\":									[										{ \"id\": \"5001\", \"type\": \"None\" },										{ \"id\": \"5002\", \"type\": \"Glazed\" },										{ \"id\": \"5005\", \"type\": \"Sugar\" },										{ \"id\": \"5007\", \"type\": \"Powdered Sugar\" },										{ \"id\": \"5006\", \"type\": \"Chocolate with Sprinkles\" },										{ \"id\": \"5003\", \"type\": \"Chocolate\" },										{ \"id\": \"5004\", \"type\": \"Maple\" }									]							}						]				}		}";
		
		public static const test8:String =
			"[ 1.2, 5.4, 3.6, 2.2, 4.9 ]";
		
		public static const test9:String =
			"{ \"array1\" : [ ], \"field1\" : { } }"; 
		
		public static const tests:Array = [ test1, test2, test3, test4, test5, test6, test7, test8, test9 ];
		
		public static function runTests():void {
			var index:int = 0;
			for each (var test:String in tests) {
				index++;
				var jsonObj:Object = OrderedJson.decode(test);
				var jsonText:String = OrderedJson.encode(jsonObj);
				if (!OrderedJson.equalJsonText(test,jsonText)) {
					throw new Error("Test " + index + "failed.");
				}
			}
		}
	}
}