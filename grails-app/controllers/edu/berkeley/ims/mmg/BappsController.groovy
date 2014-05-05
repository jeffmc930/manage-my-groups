package edu.berkeley.ims.mmg

import org.springframework.web.servlet.ModelAndView

import org.springframework.web.context.request.RequestContextHolder as RCH

class BappsController {

    /* Make sure the supplied account ID is valid for this user. */    
    def beforeInterceptor = [action:this.&accountFromId, except:['choose']]

    /* GrailsApplication -- needed for the config. */
    def grailsApplication

    def messageSource // inject the messageSource

    /* TokenService service */
    def tokenService
    
    /* EmailService service */
    def emailService

    /** GoogleAppsService service */
    def googleAppsService
    
    def calMailService

	/* NameSpace Service */
	def nameSpaceService
	
	/* Google Groups Service */
	def googleGroupsService

	/** LDAP service */
	def ldapService
    
    /* Keeps track of the account that is set using params.id */
    def currentAccount
    
    /* Since we use this a lot, we pull it out when setting currentAccount */
    def currentUsername
    
    /* The underlying CalMail account for the selected account */
    def calMailAccount

    /**
     * Shows the index page with the provided account. If no account
     * is provided and the user only has one account, then the account
     * is set to that one account. If the user has more than one account,
     * then the user is sent to the 'choose' action.
     *
     * If the user only has one account, then accountFromId() should set it,
     * so we can assume that if it is not set here, the user needs to choose.
     */
    def index() { 
		// GroupNames are owned by the person's UID
		// This will change when department names have their own UID
		def existingGroupNames = nameSpaceService.getExistingGroupNames(session.person)
		def groupsIsManager = googleGroupsService.findGroupsForManager(currentAccount, existingGroupNames)
       return new ModelAndView('/bapps/index',
            ['account':currentAccount, 'calMailAccount':calMailAccount, 'existingGroupNames': groupsIsManager])
    }
    
    /**
     * Shows a list of IDs for the person to choose. If an ID has been chosen,
     * i.e., params.id will be set, then it redirects to 'index' with that
     * account set as the id.
     */
    def choose() {
        if (session.googleAppsAccounts?.size() == 1) {
            redirect(action:'index')
        }
        
        if (params.id) {
            redirect(action:'index', id:params.id)
        }
        else {
            return new ModelAndView('/bapps/choose',
                ['accounts':session.googleAppsAccounts])
        }
    }
    
    /**
     * Displays the 'set' (form) page for the token.
     */
    def set() {
        // You can not get this from the config because the GoogleAppsCommand
        // also needs to know about the token length, and you can't inject that
        // into the CommandObject.
 		def reuseNames = nameSpaceService.getReuseNames(session.person)
		session.reuseNames = reuseNames
        
        return new ModelAndView('/bapps/set',
            ['account':currentAccount, 'reuseNames': reuseNames.sort()])
    }
    
    /**
     * Creates a group in Google. This makes use of the GoogleAppsCommand object which is
     * defined at the bottom of this class.
     */
    def save(GoogleAppsCommand cmd) {
        if (request.method == 'POST') {
            def userDefined = (params.newGroupName) ? true : false
            if (cmd.hasErrors()) {
                def errorsForTitle = ''
              	cmd.errors.allErrors.each { 
                    errorsForTitle = "${errorsForTitle} ${messageSource.getMessage(it, request.getLocale())}"
                }
				
                flash.title = message(code: 'bapps.alert.save.errorForTitle') + errorsForTitle
                return new ModelAndView('/bapps/set',
                    ['account':currentAccount,
                     'googleApps':cmd, 'newGroupName':params.newGroupName, 'possibleEntries':session.reuseNames])
            }
            else {
				def result = nameSpaceService.addName(params.newGroupName ?: params.reuseGroupName, session.person, session.reuseNames)
				def result2 = googleGroupsService.createGroup(params.newGroupName ?: params.reuseGroupName)
				def result3 = calMailService.addGroupName(params.newGroupName ?: params.reuseGroupName, session.person)
				if (result && result2 && result3) {
                  	flash.success = message(code: 'bapps.alert.save.success', 
                          args:[params.newGroupName ?: params.reuseGroupName])
					flash.title = message(code: 'bapps.alert.save.successForTitleDefined', 
                          args:[currentUsername])
                      // Redirect to update page
					def existingGroupNames = nameSpaceService.getExistingGroupNames(session.person)
	                return new ModelAndView('/bapps/update',
						['account':currentAccount, 
						'calMailAccount':calMailAccount, 
						'existingGroupNames': existingGroupNames,
						'gName': params.newGroupName ?: params.reuseGroupName])
                }
                else { // there was an error. Let them know.
                    flash.error = message(code: 'bapps.alert.save.error.retry')
                    flash.title = message(code: 'bapps.alert.save.errorForTitle')
					def existingGroupNamesIfError = nameSpaceService.getExistingGroupNames(session.person)
	                return new ModelAndView('/bapps/index',
	                    ['account':currentAccount, 'calMailAccount':calMailAccount, 'existingGroupNames': existingGroupNamesIfError])
                }
            }
        }
        else if (currentAccount) {
            redirect(action:'index')
        }
        // The final else would have already been handled by accountFromId()
    }
    
    /**
     * Displays the token if the user chooses the pre-generated token.
     */
    def view() {
        if (!flash.token && currentAccount) {
            redirect(action:'index')
        }
        else {
            return new ModelAndView('/bapps/view',
                ['account':currentAccount])
        }
    }

	/**
     * Allows the user to add themselves as manager, add a description and other settings
     */
    def update(GoogleAppsCommand cmd) {
		if (cmd.hasErrors()) {
             def errorsForTitle = ''
           	cmd.errors.allErrors.each { 
                 errorsForTitle = "${errorsForTitle} ${messageSource.getMessage(it, request.getLocale())}"
             }				
             flash.title = message(code: 'bapps.alert.update.errorForTitle') + errorsForTitle
			 flash.error = errorsForTitle
			def existingGroupNamesIfDescError = nameSpaceService.getExistingGroupNames(session.person)
			def groupsIsManagerIfDescError = googleGroupsService.findGroupsForManager(currentAccount, existingGroupNamesIfDescError)
	        return new ModelAndView('/bapps/update',
				['account':currentAccount, 
				'calMailAccount':calMailAccount, 
				'existingGroupNames': groupsIsManagerIfDescError,
				'description': params.description,
				'gName': params.gName])
        }
		def settings = ['description': params.description,
						'manager': params.managerAccount
						]
		flash.error = ""
		def results = googleGroupsService.updateGroupAll(params.gName, settings)
		if (results) {
			def existingGroupNames = nameSpaceService.getExistingGroupNames(session.person)
			def groupsIsManager = googleGroupsService.findGroupsForManager(currentAccount, existingGroupNames)
	        return new ModelAndView('/bapps/index',
				['account':currentAccount, 
				'calMailAccount':calMailAccount, 
				'existingGroupNames': groupsIsManager,
				'gName': params.gName])
		} else {
			flash.error = message(code: 'bapps.alert.save.error.retry')
            flash.title = message(code: 'bapps.alert.save.errorForTitle')
			def existingGroupNamesIfError = nameSpaceService.getExistingGroupNames(session.person)
			def groupsIsManagerIfError = googleGroupsService.findGroupsForManager(currentAccount, existingGroupNamesIfError)
	        return new ModelAndView('/bapps/update',
				['account':currentAccount, 
				'calMailAccount':calMailAccount, 
				'existingGroupNames': groupsIsManagerIfError,
				'description': params.description,
				'gName': params.gName])
			
		}
    }
    
    
    /**
     * Deletes the token, sets a flash.success message, and redirects the user
     * to either the choose page or the index page, where the flash message is
     * shown.
     *
     * Actually, it does not delete the token, but it sets the passphrase to a
     * random, 30 character string.
     */
    def delete() {
        if (request.method == 'POST') {
			def result = nameSpaceService.deleteNameOrService(params.gName)
			def result2 = googleGroupsService.deleteGroup(params.gName)
			def result3 = calMailService.deleteGroupName(params.gName)
            
            if (result && result2 && result3) {
                flash.success = message(code:'bapps.alert.delete.success',
                    args:[params.gName])
                flash.title = message(code:'bapps.alert.delete.successForTitle',
                    args:[currentUsername])

				def existingGroupNames = nameSpaceService.getExistingGroupNames(session.person)
	            return new ModelAndView('/bapps/index',
	                    ['account':currentAccount, 
						'calMailAccount':calMailAccount, 
						'existingGroupNames': existingGroupNames,
						'gName': params.gName])
            }
            else {
                flash.error = message(code: 'bapps.alert.delete.error', args:[params.gName])
                flash.title = message(code: 'bapps.alert.delete.errorForTitle')
                return new ModelAndView('/bapps/delete',
                    ['account':currentAccount])
            }
        }
        else {
            return new ModelAndView('/bapps/delete',
                ['account':currentAccount, gName:params.gName])
        }
    }
    
    /**
     * Checks to see if the supplied ID is valid for the logged in user.
     * If not, then it either sends the user to the index or choose page,
     * depending on whether or not the user has one or more Google accounts.
     *
     * It keeps the user in the bApps section because if the user was not
     * authorized to be in the section at all, the user would have been stopped
     * by the SecurityFilters.
     */
    protected accountFromId() {
        // Only do this if the user has any Google apps. If not, then the
        // SecurityFilter will pick it up.
        if (session.googleAppsAccounts) {
			print "in accountFromId session"
            if (params.id) {
                session.googleAppsAccounts.each {
                    if (it.getLogin().getUserName() == params.id) {
                        currentAccount = it
                        currentUsername = it.getLogin().getUserName()
                    }
                }
            }
            else if (session.googleAppsAccounts.size() == 1) {
                currentAccount = session.googleAppsAccounts[0]
                currentUsername = currentAccount.getLogin().getUserName()
            }
        
            if (!currentAccount) {
				currentAccount = session.googleAppsAccounts[0]
                currentUsername = currentAccount.getLogin().getUserName()
                session.googleAppsAccounts.size() == 1 ? redirect(action:'index') : redirect(action:'choose')
				return false
            }
        }
        // If the currentAccount is set, now pull out the actual account from
        // the CalMail database
        if (currentAccount) {
            calMailAccount = calMailService.account(
                session.person, currentAccount.getLogin().getUserName())
        }
        
        // Now, if this is any of the actions below and the login is disabled,
        // then we need to redirect to index.
        if (actionName == 'set' || actionName == 'save' ||
            actionName == 'view' || actionName == 'delete') {
            if (calMailAccount?.loginDisabled == true) {
                redirect(action: 'index')
            }
        }
    }
    
    
}


/**
 * Command Object for interacting with the GoogleAppsService.
 */
class GoogleAppsCommand {

	def session = RCH.requestAttributes.session

	def messageSource // inject the messageSource
	
    /* GrailsApplication -- needed for the config. */
	def grailsApplication
		
	/* Name Space Service */
	def nameSpaceService
	
	String newGroupName
	
	String reuseGroupName
	
	String description
	    
    static constraints = {
		newGroupName(blank:true,nullable:true,size:1..255,matches:/[a-zA-Z0-9\-_.]+/, validator: { val, obj ->
            if (!val && obj.reuseGroupName.equals(obj.grailsApplication.config.mmg.setForm.nonSelected)) {
                return 'googleAppsCommand.newGroupName.cannotbeblank'
			} else {
				if (val.equals(obj.grailsApplication.config.mmg.setForm.nonSelected)) {
					return 'googleAppsCommand.newGroupName.cannotUseName'
				}
				if (val && !obj.reuseGroupName.equals(obj.grailsApplication.config.mmg.setForm.nonSelected)) {
					if (!val.equals(obj.reuseGroupName))
					return 'googleAppsCommand.newGroupName.useEitherOneNotBoth'
				}
				if (obj.reuseGroupName.equals(obj.grailsApplication.config.mmg.setForm.nonSelected) && val){
					if (!obj.nameSpaceService.validateName(val, obj.session.person)) {
						return 'googleAppsCommand.newGroupName.cannotUseName'
					}
				}
			
			}
            })

		reuseGroupName(blank:true,nullable:true,size:1..255,matches:/[a-zA-Z0-9\-_.]+/)
		description(blank:true,nullable:true,size:1..300,matches:/[a-zA-Z0-9\-_. #\+=\(\)\&\^\~]+/)

    }
    
}

