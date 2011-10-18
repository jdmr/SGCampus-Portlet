<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class="Curso">
    <h1><liferay-ui:message key="curso.nuevo.titulo" /></h1>
    <portlet:actionURL var="actionUrl">
        <portlet:param name="action" value="crea"/>
    </portlet:actionURL>

    <form:form name="cursoForm" commandName="curso" method="post" action="${actionUrl}" onSubmit="extractCodeFromEditor()" >
        <input type="hidden" id="<portlet:namespace />maestroId" name="<portlet:namespace />maestro.id" value="${curso.maestro.id}" />
        <div class="dialog">
            <table>
                <tbody>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="codigo"><liferay-ui:message key="curso.codigo" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:input path="codigo" maxlength="32"/>
                            <form:errors cssClass="errors" path="codigo" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="nombre"><liferay-ui:message key="curso.nombre" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:input path="nombre" maxlength="128"/>
                            <form:errors cssClass="errors" path="nombre" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="descripcion"><liferay-ui:message key="curso.descripcion" /></label>
                        </td>
                        <td valign="top" class="value">
                            <liferay-ui:input-editor name='<%= renderResponse.getNamespace() + "structure_el_TextAreaField_content" %>' 
                                                     editorImpl="editor.wysiwyg.portal-web.docroot.html.portlet.journal.edit_article_content.jsp" 
                                                     toolbarSet="liferay-article" 
                                                     width="100%" 
                                                     />
                            <input name="<portlet:namespace />descripcion" type="hidden" value="" />
                            <form:errors cssClass="errors" path="descripcion" cssStyle="color:red;" />
                            <!--input type="button" onclick="javascript:verContenido();" value="Ver Contenido" /-->
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="<portlet:namespace />maestroNombre"><liferay-ui:message key="curso.maestro" /></label>
                        </td>
                        <td valign="top" class="value">
                            <div id="<portlet:namespace />maestroDiv">
                                <c:if test="${curso.maestro != null}">
                                    <table>
                                        <thead>
                                            <tr>
                                                <th><liferay-ui:message key="nombre" /></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>${curso.maestro.nombreCompleto}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </c:if>
                            </div>
                            <input type="text" name="maestroNombre" id="<portlet:namespace />maestroNombre" value="" />
                            <form:errors cssClass="errors" path="maestro" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="<portlet:namespace />inicia"><liferay-ui:message key="curso.inicia" /></label>
                        </td>
                        <td valign="top" class="value">
                            <input type="text" name="inicia" id="<portlet:namespace />inicia" value="${inicia}" />
                            <form:errors cssClass="errors" path="inicia" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="<portlet:namespace />termina"><liferay-ui:message key="curso.termina" /></label>
                        </td>
                        <td valign="top" class="value">
                            <input type="text" name="termina" id="<portlet:namespace />termina" value="${termina}" />
                            <form:errors cssClass="errors" path="termina" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="<portlet:namespace />url"><liferay-ui:message key="curso.url" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:input id="<portlet:namespace />url" path="url" maxlength="254"/>
                            <form:errors cssClass="errors" path="url" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td colspan="2" style="padding:0;">
                            <table style="border:0;">
                                <tbody>
                                    <tr class="prop">
                                        <td valign="top" class="name">
                                            <label for="tipo"><liferay-ui:message key="curso.tipo" /></label>
                                        </td>
                                        <td valign="top" class="value">
                                            <form:select path="tipo" items="${tipos}" />
                                            <form:errors cssClass="errors" path="tipo" />
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                            <div id="tipoDiv"
                                 <c:if test="${curso.tipo == 'PATROCINADO'}">style="display:none;"</c:if>
                                 >
                                <table style="border:0;">
                                    <tbody>
                                        <tr class="prop">
                                            <td valign="top" class="name">
                                                <label for="precio"><liferay-ui:message key="curso.precio" /></label>
                                            </td>
                                            <td valign="top" class="value">
                                                <form:input path="precio" />
                                                <form:errors cssClass="errors" path="precio" />
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="comunidadId"><liferay-ui:message key="curso.comunidad" /></label>
                        </td>
                        <td valign="top" class="value">
                            <form:select path="comunidadId" items="${comunidades}" />
                            <form:errors cssClass="errors" path="comunidadId" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="tags"><liferay-ui:message key="tags" /></label>
                        </td>
                        <td valign="top" class="value">
                            <liferay-ui:asset-tags-selector hiddenInput="tags" />
                            <aui:input type="hidden" name="tags" />
                            <form:errors cssClass="errors" path="tags" />
                        </td>
                    </tr>

                </tbody>
            </table>
        </div>
        <div class="dialog">
            <table>
                <tbody>
                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="<portlet:namespace />telefono"><liferay-ui:message key="curso.telefono" /></label>
                        </td>
                        <td valign="top" class="value">
                            <input type="text" name="telefono" id="<portlet:namespace />telefono" value="${curso.telefono}" />
                            <form:errors cssClass="errors" path="telefono" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="<portlet:namespace />estado"><liferay-ui:message key="curso.estado" /></label>
                        </td>
                        <td valign="top" class="value">
                            <input type="text" name="estado" id="<portlet:namespace />estado" value="${curso.estado}" />
                            <form:errors cssClass="errors" path="estado" cssStyle="color:red;" />
                        </td>
                    </tr>

                    <tr class="prop">
                        <td valign="top" class="name">
                            <label for="<portlet:namespace />pais"><liferay-ui:message key="curso.pais" /></label>
                        </td>
                        <td valign="top" class="value">
                            <input type="text" name="pais" id="<portlet:namespace />pais" value="${curso.pais}" />
                            <form:errors cssClass="errors" path="pais" cssStyle="color:red;" />
                        </td>
                    </tr>

                </tbody>
            </table>
        </div>

        <div class="nav">
            <span class="menuButton"><input type="submit" name="<portlet:namespace />_crea" class="save" value="<liferay-ui:message key='curso.crea' />"/></span>
            <span class="menuButton"><a class="cancel" href="<portlet:renderURL portletMode="view"/>"><liferay-ui:message key="curso.cancela" /></a></span>
        </div>
    </form:form>
    <script type="text/javascript">
        $(document).ready(function() {
            $("input#<portlet:namespace />maestroNombre")
            .autocomplete({
                source: "<portlet:resourceURL id='buscaMaestro'/>",
                select: function(event, ui) {
                    $("input#<portlet:namespace />maestroId").val(ui.item.id);
                    $("#<portlet:namespace />maestroDiv").load('<portlet:resourceURL id="asignaMaestro" />',{id:ui.item.id},function() {
                        $("input#<portlet:namespace />maestroNombre").val(ui.item.nombre);
                        $("input#<portlet:namespace />inicia").focus();
                    });
                    return false;
                }
            });
            $("input#<portlet:namespace />inicia").datepicker({dateFormat: 'dd/mm/yy'});
            $("input#<portlet:namespace />termina").datepicker({dateFormat: 'dd/mm/yy'});
            $("select#tipo").change(function() {
                if($(this).val() == 'PATROCINADO') {
                    $("div#tipoDiv").slideUp();
                } else {
                    $("div#tipoDiv").slideDown();
                }
            });
            
            $("input#codigo").focus();
        });

        function <portlet:namespace />initEditor() { 
            return "<%= UnicodeFormatter.toString(((mx.edu.um.portlets.sgcampus.model.Curso)request.getAttribute("curso")).getDescripcion()) %>"; 
        }  

        function extractCodeFromEditor() { 
            var x = document.cursoForm.<portlet:namespace />descripcion.value = window.<portlet:namespace />structure_el_TextAreaField_content.getHTML();  
            return true;
        }
        
        function verContenido() {
            alert("HTML: "+window.<portlet:namespace />structure_el_TextAreaField_content.getHTML());
            alert("HTML2:"+ window.<portlet:namespace />structure_el_TextAreaField_content.getHTML(true));
            alert("XHTML:"+window.<portlet:namespace />structure_el_TextAreaField_content.getXHTML());
        }
    </script>
</div>
