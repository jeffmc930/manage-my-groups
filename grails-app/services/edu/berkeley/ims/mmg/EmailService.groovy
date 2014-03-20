package edu.berkeley.ims.mmg

class EmailService {

    /* GrailsApplication -- needed for the config. */
    def grailsApplication

    /* Instance of the MailService plugin. */
    def mailService

    /**
     * Sends a confirmation email to a user when he/she sets an AirBears2 Login
     * Token.
     *
     * @param person           The person to who the email should be sent.
     */
    def sendWpaSetConfirmation(person) {
        def email = 
            person.getAttributeValue(grailsApplication.config.myt.tokenLdapEmailAddress) ?:
            person.getAttributeValue('mail')
        if (email) {
            mailService.sendMail {
                to email
                from grailsApplication.config.grails.mail.from
                replyTo grailsApplication.config.grails.mail.replyTo
                subject grailsApplication.config.myt.wpaEmailSetSubject
                body(view:"/email/wpaSetConfirmation", 
                      model:['person':person, 'url':grailsApplication.config.grails.serverURL])
            }
        }
        else {
            def username =
                person.getAttributeValue(grailsApplication.config.myt.wpaTokenLdapUsername)
            log.info("Can't send WPA Set Confirmation; no email address for user ${username}.")
        }
    }
    
    /**
     * Sends a confirmation email to a user when he/she deletes an AirBears2
     * Login Token.
     *
     * @param person           The person to who the email should be sent.
     */
    def sendWpaDeleteConfirmation(person) {
        def email = 
            person.getAttributeValue(grailsApplication.config.myt.tokenLdapEmailAddress) ?:
            person.getAttributeValue('mail')
        if (email) {
            mailService.sendMail {
                to email
                from grailsApplication.config.grails.mail.from
                replyTo grailsApplication.config.grails.mail.replyTo
                subject grailsApplication.config.myt.wpaEmailDeleteSubject
                body(view:"/email/wpaDeleteConfirmation", 
                      model:['person':person, 'url':grailsApplication.config.grails.serverURL])
            }
        }
        else {
            def username =
                person.getAttributeValue(grailsApplication.config.myt.wpaTokenLdapUsername)
            log.info("Can't send WPA Delete Confirmation; no email address for user ${username}.")
        }
    }
    
    /**
     * Sends a confirmation email to a user when he/she sets a bApps Login
     * Token.
     *
     * @param person           The person to who the email should be sent.
     * @param username          The username for which the token was set.
     */
    def sendBappsSetConfirmation(person, username) {
        def email = 
            person.getAttributeValue(grailsApplication.config.myt.tokenLdapEmailAddress) ?:
            person.getAttributeValue('mail')
        if (email) {
            mailService.sendMail {
                to email
                from grailsApplication.config.grails.mail.from
                replyTo grailsApplication.config.grails.mail.replyTo
                subject grailsApplication.config.myt.bAppsEmailSetSubject
                body(view:"/email/bAppsSetConfirmation", 
                      model:['person':person, 'username':username,
                       'url':grailsApplication.config.grails.serverURL])
            }
        }
        else {
            log.info("Can't send Google Apps Set Confirmation; no email address for user ${username}.")
        }
    }
    
    /**
     * Sends a confirmation email to a user when he/she deletes a bApps
     * Login Token.
     *
     * @param person            The person to who the email should be sent.
     * @param username          The username for which the token was deleted.
     */
    def sendBappsDeleteConfirmation(person, username) {
        def email = 
            person.getAttributeValue(grailsApplication.config.myt.tokenLdapEmailAddress) ?:
            person.getAttributeValue('mail')
        if (email) {
            mailService.sendMail {
                to email
                from grailsApplication.config.grails.mail.from
                replyTo grailsApplication.config.grails.mail.replyTo
                subject grailsApplication.config.myt.bAppsEmailDeleteSubject
                body(view:"/email/bAppsDeleteConfirmation", 
                      model:['person':person, 'username':username,
                       'url':grailsApplication.config.grails.serverURL])
            }
        }
        else {
            log.info("Can't send Google Apps Delete Confirmation; no email address for user ${username}.")
        }
    }
    
}
