<%@ include file="/WEB-INF/jsp/include.jsp" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css" type="text/css"/>
<div class="Curso">
    <div class="dialog">
        <div>${curso.descripcion}</div>
    </div>
    <div class="nav">
        <span class="menuButton"><a class="cancel" href="<portlet:renderURL portletMode="view"/>"><liferay-ui:message key="curso.regresar" /></a></span>
    </div>
</div>
