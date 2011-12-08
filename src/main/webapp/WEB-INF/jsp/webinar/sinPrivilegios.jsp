<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class="Curso">
    <h1><liferay-ui:message key="webinar.sin.privilegios.titulo" /></h1>
    <p>
        <liferay-ui:message key="webinar.sin.privilegios.mensaje" />
    </p>
    <p>
        <a class="cancel" href="<portlet:renderURL portletMode="view"/>"><liferay-ui:message key="webinar.regresar" /></a>
    </p>
</div>
