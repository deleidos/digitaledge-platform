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

define(["angular","lodash"],function(a,b){var c=a.module("kibana.factories");c.factory("storeFactory",function(){return function(a,c,d){if(!b.isFunction(a.$watch))throw new TypeError("Invalid scope.");if(!b.isString(c))throw new TypeError("Invalid name, expected a string that the is unique to this store.");if(d&&!b.isPlainObject(d))throw new TypeError("Invalid defaults, expected a simple object or nothing");d=d||{};var e=localStorage.getItem(c);if(null!=e)try{e=JSON.parse(e)}catch(f){e=null}if(null==e)e=b.clone(d);else{if(!b.isPlainObject(e))throw new TypeError("Invalid store value"+e);b.defaults(e,d)}return a[c]=e,a.$watch(c,function(e){void 0===e?(localStorage.removeItem(c),a[c]=b.clone(d)):localStorage.setItem(c,JSON.stringify(e))},!0),e}})});