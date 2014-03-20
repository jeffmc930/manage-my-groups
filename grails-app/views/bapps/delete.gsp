<meta name="layout" content="ucb" />

<h1><g:message code="bapps.delete.heading" args="${[account.getLogin().getUserName(), params.gName]}" /></h1>
<p><g:message code="bapps.delete.generalMessage" args="${[account.getLogin().getUserName(), params.gName]}" /></p>

<p>
<!-- <div class="alert alert-info">
    <g:message code="bapps.delete.deleteInfo" />
</div> -->
</p>

<g:include controller="alert" action="flashError" />


<g:form action="delete" id="${account.getLogin().getUserName()}">
<g:hiddenField name="gName" value="${params.gName}"/>
<div class="form-actions">
	<button type="submit" class="btn btn-small btn-danger" title="Delete group ${params.gName}">
        <i class="icon-trash icon-white"></i>  <g:message code="bapps.index.optionsDeleteLink" />
    </button>
	&nbsp;  &nbsp; 
<g:link controller="bapps" action="index" id="${account.getLogin().getUserName()}"><g:message code="general.returnToOptions" /></g:link>
</div>
</g:form>
