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
<jsp:directive.include file="includes/top.jsp" />
  <div id="msg" class="info">
    <p><spring:message code="screen.confirmation.message" arguments="${fn:escapeXml(param.service)}${fn:indexOf(param.service, '?') eq -1 ? '?' : '&'}ticket=${serviceTicketId}" /></p>
  </div>
<jsp:directive.include file="includes/bottom.jsp" />