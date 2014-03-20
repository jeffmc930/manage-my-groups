package edu.berkeley.ims.mmg

class WpaService {

    def grailsApplication

    /**
     * Takes the passed in {@code person} and checks the WPA token database to
     * see if the user has a record. If the user exists the method returns true.
     * If the user does not exist the method returns false.
     *
     * @parms user          The currently logged in user, which will be an
     * UnboundID SearchResultEntry.
     * @return {@code true} if the user has a token set, and {@code false} if not.
     */
    def userHasTokenSet(person) {
        def user = Wpa.findByUsername(
            person.getAttributeValue(grailsApplication.config.myt.wpaTokenLdapUsername))
        if (user) {
            return true
        }
        return false
    }

    /**
     * Saves the passed in {@code token} for the passed in {@code person}.
     * It first tries to find the user using the
     * {@code config.myt.wpaTokenLdapUsername} of the passed in {@code person},
     * and if the a user exists, it updates the {@code token} for that user.
     * If the user does not exist then it creates a new user using the passed in
     * {@code person} and {@code token}.
     *
     * @param   person      The currently logged in user, which will be an
     * UnboundID SearchResultEntry.
     * @param   token       The token for the user.
     * @return  The Wpa domain object which is used for this operation.
     */
    def saveToken(person, token) {
        def username =
            person.getAttributeValue(grailsApplication.config.myt.wpaTokenLdapUsername)
        def wpa = Wpa.findByUsername(username)
        if (wpa) {
            wpa.password = token
        }
        else {
            wpa = new Wpa(username:username, password:token)
        }
        if (wpa.validate()) {
            wpa.save()
            log.info("Set WPA token for: [uid: ${person.getAttributeValue('uid')}], [username: ${person.getAttributeValue(grailsApplication.config.myt.calNetUsername)}].")
        }
        return wpa
    }
    
    /**
     * Deletes the token for the passed in person.
     *
     * @param person            The currently logged in user, which will be an
     * UnboundID SearchResultEntry.
     */
    def deleteToken(person) {
        def username =
            person.getAttributeValue(grailsApplication.config.myt.wpaTokenLdapUsername)
        def wpa = Wpa.findByUsername(username)
        if (wpa) {
            wpa.delete(flush: true)
            log.info("Deleted WPA token for: [uid: ${person.getAttributeValue('uid')}], [username: ${person.getAttributeValue(grailsApplication.config.myt.calNetUsername)}].")
        }
    }
    
    /**
     * Returns the existing token if the person has one, and null if not.
     *
     * @param person           The currently logged in user, which will be an
     * UnboundID SearchResultEntry.
     * @return The currently set token for the person, or null if there is no
     * token set.
     */
    def token(person) {
        def username =
            person.getAttributeValue(grailsApplication.config.myt.wpaTokenLdapUsername)
        def wpa = Wpa.findByUsername(username)
        if (wpa) {
            return wpa.password
        }
        else {
            return null
        }
    }
}
