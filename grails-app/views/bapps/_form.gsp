
<g:form action="save" id="${account.getLogin().getUserName()}">
<!-- <g:hiddenField name="reuseGroupName" value="" /> -->
<fieldset>
    <!-- <legend><g:message code="bapps.setPage.legend" /></legend> -->
	<div>
	<g:message code="bapps.setPage.createGroupOverviewMessage"/>
	</div>
    <div class="control-group">
        <div>
			<!-- <p><g:message code="bapps.setPage.reuseMessage"/></p> -->
			<!-- <p class="help-block"><g:message code="bapps.formPage.keyHelpTextForReuse" /></p> -->
			<p class="${hasErrors(bean:googleApps,field:'reuseGroupName', 'error')}">
			<label class="control-label" for="reuseGroupName">Use Existing Name</label>
			<select id="reuseGroupName" name="reuseGroupName">
			  <option>${grailsApplication.config.mmg.setForm.nonSelected}</option>
			    <g:each in="${reuseNames}">
			        <option>${it}</option>
				</g:each>
			</select>
		</div>
	</div>
	<div class="control-group">
		<p><g:message code="bapps.setPage.createNewMessage"/></p>
        <p class="${hasErrors(bean:googleApps,field:'newGroupName', 'error')}">
        <label class="control-label" for="newGroupName">New Group Name</label>
		<p class="help-block"><g:message code="bapps.formPage.keyHelpTextForCreateNew" /></p>
        <input type="text" id="newGroupName" name="newGroupName" title="Create a new group name." value="${newGroupName}">
        </p>
    </div>


<p>


    
    <div class="form-actions">
        <input type="submit" class="btn btn-primary" name="save" value="Create Group"/> &nbsp;  &nbsp; <g:link controller="bapps" action="index" id="${account.getLogin().getUserName()}"><g:message code="general.returnToOptions" /></g:link>
    </div>

</fieldset>
</g:form>

<div class="modal hide" id="passphraseRequirements">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">×</button>
        <h3>CalNet Passphrase Requirements</h3>
      </div>
      <div class="modal-body">
          <g:message code="general.calNetPassphraseRequirements" />
      </div>
      <div class="modal-footer">
        <a href="#" class="btn btn-primary" data-dismiss="modal">Close</a>
      </div>
</div>

<script>
  $('#preTab').click(function (e) {
      // Remove any self-defined values
      $('#definedToken').val('');
      $('#definedTokenConfirmation').val('');
  })
</script>

<g:if test="${userDefined}">
<script>
$(document).ready(function() {
    $('#CreateNewTab').click();
    $("#newGroupName").focus();
});
</script>
</g:if>