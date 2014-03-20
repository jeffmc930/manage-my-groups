<g:form action="save">
<g:hiddenField name="token" value="${token}" />
<fieldset>
    <!-- <legend><g:message code="wpa.setPage.legend" /></legend> -->

    <div class="control-group">
          <p><g:message code="wpa.setPage.generalMessage" /></p>
          
          <label class="control-label" for="token">Key</label>
              <p>
              <span class="token-value">${token}</span>
            </p>
              <p>
              <p><g:link action="set" class="btn" title="Generate a different key."><i class="icon-refresh"></i> <g:message code="wpa.formPage.generateKey" /></g:link>
              </p>
              <p class="help-block"><g:message code="wpa.formPage.keyHelpText" /></p>
    </div>
    
    <div class="form-actions">
        <input type="submit" class="btn btn-primary" name="save" value="Set Key"/> &nbsp;  &nbsp; <g:link controller="wpa" action="index"><g:message code="general.returnToOptions" /></g:link>
    </div>

</fieldset>
</g:form>