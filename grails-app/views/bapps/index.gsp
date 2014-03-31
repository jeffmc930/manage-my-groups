<meta name="layout" content="ucb" />

<h1><g:message code="bapps.index.heading" args="${[account.getLogin().getUserName()]}" /></h1>

<g:if test="${calMailAccount.loginDisabled == true}">
<div class="alert alert-info">
    <h3><g:message code="bapps.index.disabledHeader" /></h3>
    <g:message code="bapps.index.disabledMessage" />
</div>
</g:if>
<g:else>
    <g:include controller="alert" action="flashSuccess" />
    <g:include controller="alert" action="flashError" />
    

    <p><g:message code="bapps.index.generalMessage" /></p>

    <!-- <div class="alert alert-info"><p><g:message code="bapps.index.deleteNote" /></p></div> -->

    <h2><g:message code="bapps.index.optionsHeading" /></h2>
    <hr>
    <ul class="options-list buttons">
        <li>
            <g:form controller="bapps" action="set" id="${account.getLogin().getUserName()}" method="get" name="set">
                <button type="submit" class="btn btn-success" title="Create bConnected group for account ${account.getLogin().getUserName()}...">
                    <i class="icon-pencil icon-white"></i> <g:message code="bapps.index.optionsSetLink" />
                </button>
            </g:form>
        </li>
    </ul>

	<h2>&nbsp&nbsp&nbsp<g:message code="bapps.index.groupsOwnedHeading" /></h2>
	<p>
	<table class="table">
	  <thead>
	    <tr>
	      <th><h4>Group Name</h4></th>
	      <th><h4>Delete</h4></th>
	    </tr>
	  </thead>
	  <tbody>
		<g:if test="${existingGroupNames.size() == 0}">
			<tr>
				<td><g:message code="bapps.index.noGroupsYet" /></td><td></td>
			</tr>
		</g:if>
		<g:each in="${existingGroupNames}">
			<tr>
				<td>
				<strong>
				<a href="${grailsApplication.config.gGroups.addressLinkPrefix}${it}${grailsApplication.config.gGroups.addressLinkSuffix}">
				${it}</a>
				</strong>
				</td>
				<td class="controls">
					<g:form controller="bapps" action="delete" id="${account.getLogin().getUserName()}" method="get" name="delete">
						<g:hiddenField name="gName" value="${it}"/>
						<button type="submit" class="btn btn-small btn-danger" title="Delete group ${it}">
		                    <i class="icon-trash icon-white"></i> <g:message code="bapps.index.optionsDeleteLink" />
		                </button>
					</g:form>
				</td>
			</tr>
		</g:each>
	  </tbody>
	</table>
</g:else>

<g:if test="${session.googleAppsAccounts.size() > 1}">
<p><g:link controller="bapps" action="choose"><g:message code="bapps.index.returnToAccountsLink" /></g:link>
</g:if>