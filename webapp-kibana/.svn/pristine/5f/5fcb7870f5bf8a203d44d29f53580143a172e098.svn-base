/*
 * LEIDOS CONFIDENTIAL
 * __________________
 *
 * (C)[2007]-[2014] Leidos
 * Unpublished - All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the exclusive property of Leidos and its suppliers, if any.
 * The intellectual and technical concepts contained
 * herein are proprietary to Leidos and its suppliers
 * and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Leidos.
 */
/*! kibana - v3.1.0 - 2014-05-15
 * Copyright (c) 2014 Rashid Khan; Licensed Apache License */

define([],function(){function a(a,b){var d=c.readFileSync(a,"utf8");0===d.indexOf("﻿")&&(d=d.substring(1)),b(d)}function b(a){return a.replace(/[\r\n]+/g," ").replace(/[\t]/g," ")}var c=require.nodeRequire("fs"),d={},e=!1,f={load:function(c,e,f,g){f(!0),a(g.baseUrl+c,function(a){d[c]=b(a)})},write:function(a,b,c){e||(e=!0,c("define('"+a+"-embed', function()\n{\n	function embed_css(content)\n	{\n		var head = document.getElementsByTagName('head')[0],\n		style = document.createElement('style'),\n		rules = document.createTextNode(content);\n		style.type = 'text/css';\n		if(style.styleSheet)\n			style.styleSheet.cssText = rules.nodeValue;\n		else style.appendChild(rules);\n			head.appendChild(style);\n	}\n	return embed_css;\n});\n")),c("define('"+a+"!"+b+"', ['"+a+"-embed'], \nfunction(embed)\n{\n	embed(\n	'"+d[b].replace(/'/g,"\\'")+"'\n	);\n	return true;\n});\n")},writeFile:function(){},onLayerEnd:function(){}};return f});