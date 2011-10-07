<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class="Curso">
    <h1><liferay-ui:message key="contenido.nuevo" /></h1>
    <portlet:actionURL var="actionUrl">
        <portlet:param name="action" value="creaContenido"/>
    </portlet:actionURL>
    <portlet:renderURL var="verContenido" >
        <portlet:param name="action" value="contenidoLista" />
        <portlet:param name="cursoId" value="${contenido.curso.id}" />
    </portlet:renderURL>
    
    <form:form name="contenidoForm" commandName="contenido" method="post" action="${actionUrl}" onSubmit="extractCodeFromEditor()" >
        <form:hidden path="curso.id" />
        <div class="dialog">
            <table>
                <tbody>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="nombre"><liferay-ui:message key="contenido.nombre" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:input path="nombre" maxlength="64"/>
                            <form:errors cssClass="errors" path="nombre" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="descripcion"><liferay-ui:message key="contenido.descripcion" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:textarea path="descripcion" />
                            <form:errors cssClass="errors" path="descripcion" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="texto"><liferay-ui:message key="contenido.texto" /></label>
                        </td>
                        <td valign="top" class="value">
                            <liferay-ui:input-editor width="850" />
                            <input name="<portlet:namespace />texto" type="hidden" value="" />
                            <form:errors cssClass="errors" path="texto" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="orden"><liferay-ui:message key="contenido.orden" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:input path="orden" maxlength="3"/>
                            <form:errors cssClass="errors" path="orden" cssStyle="color:red;" />
                        </td>
                    </tr>

                </tbody>
            </table>
        </div>
        <div class="nav">
            <span class="menuButton"><input type="submit" name="<portlet:namespace />_crea" class="save" value="<liferay-ui:message key='sesion.crea' />"/></span>
            <span class="menuButton"><a class="cancel" href='${verContenido}'><liferay-ui:message key="sesion.cancela" /></a></span>
        </div>
    </form:form>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        $("input#nombre").focus();
    });

    function <portlet:namespace />initEditor() { 
        return "<%= UnicodeFormatter.toString(((mx.edu.um.portlets.sgcampus.model.Contenido)request.getAttribute("contenido")).getTexto()) %>"; 
    }  

    function extractCodeFromEditor() { 
        var x = document.contenidoForm.<portlet:namespace />texto.value = window.<portlet:namespace />editor.getHTML();  
        return true;
    }
</script>
