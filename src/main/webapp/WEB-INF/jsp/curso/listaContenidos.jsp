<%-- 
    Document   : listaContenidos
    Created on : Oct 6, 2011, 9:08:23 AM
    Author     : jdmr
--%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class="Curso">
    <h1><liferay-ui:message key="curso.contenido.lista" /> (<%= LanguageUtil.format(pageContext, "contenido.cantidad",request.getAttribute("cantidad"),false) %>)</h1>
    <c:if test="${contenidos != null}">
        <div class="list">
            <table id="<portlet:namespace />contenidos">
                <thead>
                    <tr>

                        <th><liferay-ui:message key="contenido.nombre" /></th>

                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${contenidos}" var="contenido" varStatus="status">
                        <portlet:renderURL var="verContenido" >
                            <portlet:param name="action" value="ver" />
                            <portlet:param name="contenidoId" value="${contenido.id}" />
                        </portlet:renderURL>
                        <tr class="${(status.count % 2) == 0 ? 'odd' : 'even'}">

                            <td><a href="${verContenido}">${contenido.nombre}</a></td>

                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <portlet:renderURL var="anterior" >
            <portlet:param name="max" value="${max}" />
            <portlet:param name="offset" value="${offset}" />
            <portlet:param name="direccion" value="anterior" />
        </portlet:renderURL>

        <portlet:renderURL var="siguiente" >
            <portlet:param name="max" value="${max}" />
            <portlet:param name="offset" value="${offset}" />
            <portlet:param name="direccion" value="siguiente" />
        </portlet:renderURL>

        <div class="paginateButtons">
            <c:if test="${offset > 0}">
                <a href="${anterior}" class="prevLink"><liferay-ui:message key="curso.anterior" /></a>
            </c:if>
            <c:if test="${cantidad > 5}">
                <a href="${siguiente}" class="nextLink"><liferay-ui:message key="curso.siguiente" /></a>
            </c:if>
        </div>

    </c:if>
    <div class="nav">
        <portlet:renderURL var="nuevoContenidoURL" windowState="<%= WindowState.MAXIMIZED.toString() %>" >
            <portlet:param name="action" value="nuevoContenido" />
            <portlet:param name="cursoId" value="${cursoId}" />
        </portlet:renderURL>
        <portlet:renderURL var="verCursoURL">
            <portlet:param name="action" value="verCurso" />
            <portlet:param name="cursoId" value="${cursoId}" />
        </portlet:renderURL>
        <span class="menuButton"><a class="create" href='${nuevoContenidoURL}'><liferay-ui:message key="contenido.nuevo" /></a></span>
        <span class="menuButton"><a class="list" href='${verCursoURL}'><liferay-ui:message key="curso.regresar" /></a></span>
    </div>
    <script type="text/javascript">
        <c:if test="${cursos != null}">
            highlightTableRows("<portlet:namespace />contenidos")
        </c:if>
    </script>
</div>
