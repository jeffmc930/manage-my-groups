<meta name="layout" content="ucb" />

<h1>Login</h1>

<g:if test="${flash.logout}">
    <div class="alert alert-info">
    <a class="close" data-dismiss="alert">×</a>
    ${flash.logout}
    </div>
</g:if>

<p><g:message code="auth.index" /></p>

<g:form controller="auth" action="login">
<div class="form-actions">
    <input type="submit" class="btn btn-primary" value="Login via CalNet"/>
</div>
</g:form>
