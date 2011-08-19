<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class="Curso">
    <div class="dialog">
        <div>${curso.descripcion}</div>
    </div>
    <div class="nav">
        <portlet:renderURL var="editaUrl" >
            <portlet:param name="action" value="edita" />
            <portlet:param name="cursoId" value="${curso.id}" />
        </portlet:renderURL>

        <span class="menuButton"><a class="list" href="<portlet:renderURL portletMode="view"/>"><liferay-ui:message key="curso.regresar" /></a></span>
        <c:if test="${puedeEditar}">
            <span class="menuButton"><a class="edit" href="${editaUrl}"><liferay-ui:message key="curso.editar" /></a></span>
        </c:if>
    </div>
</div>
