<%@ page import="com.liferay.portal.kernel.language.LanguageUtil" %>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class="Curso">
    <h1><liferay-ui:message key="javax.portlet.title" /> (<%= LanguageUtil.format(pageContext, "curso.cantidad",request.getAttribute("cantidad"),false) %>)</h1>
    <portlet:renderURL var="actionUrl" >
        <portlet:param name="action" value="busca" />
    </portlet:renderURL>

    <form name="<portlet:namespace />fm" method="post" action="${actionUrl}" >
        <div class="search">
            <table>
                <tbody>
                    <tr class="prop">
                        <td>
                            <input type="text" name="<portlet:namespace />filtro" id="<portlet:namespace />filtro" value="" />
                            <input type="submit" name="<portlet:namespace />_busca" value='<liferay-ui:message key="curso.buscar" />'/>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </form>
    <c:if test="${cursos != null}">
        <div class="list">
            <table id="<portlet:namespace />cursos">
                <thead>
                    <tr>

                        <th><liferay-ui:message key="curso.nombre" /></th>

                        <th><liferay-ui:message key="curso.inicia" /></th>

                        <th><liferay-ui:message key="curso.tipo" /></th>

                        <th><liferay-ui:message key="curso.estatus" /></th>
    
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${cursos}" var="curso" varStatus="status">
                        <portlet:renderURL var="verCurso" >
                            <portlet:param name="action" value="ver" />
                            <portlet:param name="cursoId" value="${curso.id}" />
                        </portlet:renderURL>
                        <tr class="${(status.count % 2) == 0 ? 'odd' : 'even'}">

                            <td><a href="${verCurso}">${curso.nombre}</a></td>

                            <td>${curso.inicia}</td>

                            <td><liferay-ui:message key="${curso.tipo}" /></td>

                            <td><liferay-ui:message key="${curso.estatus}" /></td>
                    
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
        <span class="menuButton"><a class="create" href='<portlet:renderURL><portlet:param name="action" value="nuevo"/></portlet:renderURL>'><liferay-ui:message key="curso.nuevo" /></a></span>
        <c:if test="${puedeAutorizar}">
            <span class="menuButton"><a class="list" href='<portlet:renderURL><portlet:param name="action" value="pendientes"/></portlet:renderURL>'><liferay-ui:message key="curso.pendientes" /></a></span>
            <span class="menuButton"><a class="list" href='<portlet:renderURL><portlet:param name="action" value="rechazados"/></portlet:renderURL>'><liferay-ui:message key="curso.rechazados" /></a></span>
        </c:if>
    </div>
    <script type="text/javascript">
        <c:if test="${cursos != null}">
            highlightTableRows("<portlet:namespace />cursos")
        </c:if>
    </script>
</div>
