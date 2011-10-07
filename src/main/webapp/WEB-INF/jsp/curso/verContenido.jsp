<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class="Curso">
    <h1>${contenido.nombre}</h1>
    <p>${contenido.descripcion}</p>
    <div class="contenido">${contenido.texto}</div>
    <div class="nav">
        <portlet:renderURL var="contenidoUrl" >
            <portlet:param name="action" value="contenidoLista" />
            <portlet:param name="cursoId" value="${contenido.curso.id}" />
        </portlet:renderURL>
        <span class="menuButton"><a class="list" href='${contenidoUrl}'><liferay-ui:message key="curso.regresar" /></a></span>
    </div>
</div>
