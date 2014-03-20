<meta name="layout" content="ucb" />

<h1><g:message code="bapps.updatePage.heading" args="${[gName, account.getLogin().getUserName()]}" /></h1>

<g:include controller="alert" action="flashSuccess" />
<g:include controller="alert" action="flashError" />

<p>
    <g:message code="bapps.updatePage.generalMessage" args="${[gName]}" />
</p>

<div class="alert alert-warning">
    <g:message code="bapps.updatePage.pageRefreshWarning" />
</div>

<g:form action="update" id="${account.getLogin().getUserName()}">
<g:hiddenField name="managerAccount" value="${account.getLogin().getUserName()}@${grailsApplication.config.mmg.gAppsUserDomain}" /> 
<g:hiddenField name="gName" value="${gName}" /> 

<!-- <h2><g:message code="bapps.updatePage.UpdateGroup" /></h2> -->
<fieldset>
    <!-- <legend><g:message code="bapps.setPage.legend" /></legend> -->

	 <div>
        <p class="${hasErrors(bean:googleApps,field:'description', 'error')}">
        <label class="control-label" for="description">Description</label>
		<p class="help-block"><g:message code="bapps.updatePage.descriptionText" /></p>
        <input class="input-xxlarge" type="text" id="description" name="description" title="Add a description for your group." value="${description}">
        </p>
    </div>
	<h4>Group Manager</h4>
	<div><p><g:message code="bapps.updatePage.manager" /><br>
			<span class="input-large uneditable-input">
			${account.getLogin().getUserName()}@${grailsApplication.config.mmg.gAppsUserDomain}</span></p></div>
	<div><h4>Default Settings</h4>
		<g:message code="bapps.updatePage.defaultSettingsInfo" />
	
    <div class="form-actions">
        <input type="submit" class="btn btn-primary" name="update" value="Update Group"/> &nbsp;  &nbsp; <g:link controller="bapps" 
			action="index" id="${account.getLogin().getUserName()}"><g:message code="bapps.updatePage.returnToIndexLink" /></g:link>
    </div>

</fieldset>
</g:form>

