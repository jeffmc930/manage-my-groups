package edu.berkeley.ims.mmg

import org.springframework.web.servlet.ModelAndView

class AuthController {

    /* GrailsApplication -- needed for the config. */
    def grailsApplication
    
    /* LdapService */
    def ldapService
    
    /**
     * Displays the base login page for the app.
     */
    def index() {
        if (session.person) {
            redirect(controller:'main', action:'index')
        }
    }

    /**
     * Logs a user in by checking the REMOTE_USER header.
     */
    def login() {
        if (session.person) {
            redirect(controller:'main', action:'index')
        }
        else if (grails.util.Environment.current.name == "development" &&
            grailsApplication.config.myt.autoLoginUserAs) {
           def person = ldapService.find(
                grailsApplication.config.ldap.personUsernameAttr,
                grailsApplication.config.myt.autoLoginUserAs)
			print "person: " + person
            setSessionAndRedirect(person)
        }
        else {
            def user = request.getHeader('REMOTE_USER') ?: request.getRemoteUser()
            if (!user) {
                session?.invalidate()
                redirect(action:'failure')
            }
            else {
                def person = ldapService.find(
                    grailsApplication.config.ldap.personUsernameAttr, user)
				log.info "User = ${user}, person = ${person}"
                setSessionAndRedirect(person)
            }
        }
    }

    /**
     * Sets the session and redirects to the correct location. Also checks to
     * see if the person is a test ID, and if so, also checks to make sure
     * test IDs are being allowed. If not, it sends the to the notAuthorized
     * action.
     *
     * @param person            The person object from the directory.
     */
    private setSessionAndRedirect(person) {
        if (person) {
			log.info "in set session"
            def isTestID = person.getAttributeValueAsBoolean(grailsApplication.config.ldap.testIdAttr)
            if (!grailsApplication.config.myt.allowTestIds && isTestID) {
                session?.invalidate()
                redirect(action:'notAuthorized')
            }
            else {
                session.person = person
				log.info "near intended controller call"
                // Get these from the session. They were set by the
                // isAuthenticated SecurityFilter.
                if (session.intendedController) {
                    redirect(
                        controller:session.intendedController,
                        action:session.intendedAction,
                        params: session.intendedParams)
                }
                else {
					print "redirecting to index"
                    redirect(controller:'main', action:'index')
                }
            }
        }
        else {
            session?.invalidate()
            redirect(action:'notAuthorized')
        }
    }

    /**
     * Invalidate the session, and send the user to the config.myt.logoutURL, if one
     * is specified.
     */
    def logout() {
        session.invalidate()
        if (grailsApplication.config.myt?.logoutURL) {
            redirect(url:grailsApplication.config.myt.logoutURL)
        }
        else {
            flash.logout = message(code:'auth.logout')
            redirect(action:'index')
        }
    }

    def notAuthorized() { }
    
    def failure() { }
    
    def notEligibleWpa() { }
    
    def notEligibleBApps() { }
}
