<meta name="layout" content="ucb" />

<h1><g:message code="bapps.setPage.heading" args="${[account.getLogin().getUserName()]}"/></h1>

<g:include controller="alert" action="formErrors" params="['item':googleApps]" />
<g:include controller="alert" action="flashError" />

<div id="form">
    <g:render template="form" />
</div>
