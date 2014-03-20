package edu.berkeley.ims.mmg

import org.springframework.web.servlet.ModelAndView

class WpaController {

    /* TokenService service */
    def tokenService
    
    /* WpaService service */
    def wpaService
    
    /* EmailService service */
    def emailService
    
    /* GrailsApplication */
    def grailsApplication

    /**
     * Shows the index page.
     */
    def index() {
        return new ModelAndView('/wpa/index',
            ['hasToken':wpaService.userHasTokenSet(session.person)])
    }
    
    /**
     * Displays the set page with the generated token.
     */
    def set() {
        def token = tokenService.token(8)
        session.currentToken = token
        return new ModelAndView('/wpa/set',
            ['token':token])
    }
    
    /**
     * Displays the currently set token for the person.
     */
    def view() {
        def token = wpaService.token(session.person)
        if (token) {
            return new ModelAndView('/wpa/view',
                ['token':token])
        }
        else {
            redirect(action:'index')
        }
    }
    
    /**
     * Saves the passed in token. The token is compared with what was
     * generated for the user, and if they match, then it saves it. If they
     * do not match, then the user is told they can not submit their own
     * token.
     *
     * User is redirected to the token() method if the save is successful.
     */
    def save() {
        if (request.method == 'POST') {
            
            if (!tokenService.verifyCurrentToken(session.currentToken, params.token)) {
                flash.error = message(code: 'wpa.alert.save.canNotSetError')
                flash.title = message(code: 'wpa.alert.save.canNotSetErrorForTitle')
                redirect(action:'set')
            }
            else {
                def wpa = wpaService.saveToken(session.person, params.token)
                if (wpa && wpa.hasErrors()) {
                    return new ModelAndView('/wpa/set',
                        ['token':params.token, 'wpa':wpa])
                }
                else {
                    flash.success = message(code: 'wpa.alert.save.success')
                    flash.title = message(code: 'wpa.alert.save.successForTitle')
                    emailService.sendWpaSetConfirmation(session.person)
                    redirect(action:'view')
                }
            }
        }
        else {
            redirect(action:'index')
        }
    }
    
    /**
     * Deletes the token, sets a flash.success message, and redirects the user
     * to the index page, where the flash message is shown.
     */
    def delete() {
        if (request.method == 'POST') {
            wpaService.deleteToken(session.person)
            emailService.sendWpaDeleteConfirmation(session.person)
            flash.success = message(code: 'wpa.alert.delete.success')
            flash.title = message(code: 'wpa.alert.delete.successForTitle')
            redirect(action:'index')
        }
    }
}
