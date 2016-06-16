<%--

    LEIDOS CONFIDENTIAL
    __________________

    (C)[2007]-[2014] Leidos
    Unpublished - All Rights Reserved.

    NOTICE:  All information contained herein is, and remains
    the exclusive property of Leidos and its suppliers, if any.
    The intellectual and technical concepts contained
    herein are proprietary to Leidos and its suppliers
    and may be covered by U.S. and Foreign Patents,
    patents in process, and are protected by trade secret or copyright law.
    Dissemination of this information or reproduction of this material
    is strictly forbidden unless prior written permission is obtained
    from Leidos.

--%>
<!DOCTYPE html>

<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="en">
<head>
  <meta charset="UTF-8" />
  
  <title>DigitalEdge &#8211; Logging Appliance</title>
  
  <spring:theme code="standard.custom.css.file" var="customCssFile" />
  <link rel="stylesheet" href="<c:url value="${customCssFile}" />" />
  <link rel="stylesheet" href="/accounts/images/font-awesome-4.1.0/font-awesome-4.1.0/css/font-awesome.css" />
  <link rel="icon" href="<c:url value="/favicon.ico" />" type="image/x-icon" />
  
  <!--[if lt IE 9]>
    <script src="//cdnjs.cloudflare.com/ajax/libs/html5shiv/3.6.1/html5shiv.js" type="text/javascript"></script>
  <![endif]-->
</head>
<body id="cas">
  <div id="container">
      <header>
        <a id="logo" href="https://www.deleidos.com" title="<spring:message code="logo.title" />"></a>
        <h1>DigitalEdge &#8211; Logging Appliance</h1>
      </header>
      <div id="content">
