package edu.berkeley.ims.mmg

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpExecuteInterceptor
import com.google.gdata.client.appsforyourdomain.UserService
import com.google.gdata.data.Link
import com.google.gdata.data.extensions.FeedLink
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException
import com.google.gdata.data.appsforyourdomain.provisioning.UserEntry
import com.google.gdata.util.InvalidEntryException
import com.google.gdata.util.ServiceException

import java.io.IOException
import java.net.URL
import java.security.MessageDigest

class GoogleAppsService {

    def grailsApplication
    
    def pwdHashFunction = 'SHA-1'
    
    //private UserService userService

    /**
     * Returns all of the Google Apps accounts for the passed in person.
     *
     * @param person        UnboundID LDAP SDK person
     * @return A list of the accounts the person has. Could be none, but it will
     * still be a list.
     */
    def googleAppsAccounts(person) {
        def uid = person.getAttributeValueAsInteger('uid')

        def calMailAccounts = CalMail.findAllByOwnerUidAndDomainId(
            uid, 
            grailsApplication.config.myt.gAppsDomainId)

        log.info "CalMail Accounts: ${calMailAccounts}"

		def gAppsAccounts = []
        	if (calMailAccounts) {
            	calMailAccounts.each { account ->
                def gAccount = googleAppsAccountFor(account.localpart) 
                if (gAccount) {
					print "gAccount: " + gAccount.getLogin().getUserName()
                    gAppsAccounts.add(gAccount)
                }
            }
        }
        return gAppsAccounts
    }

    /**
     * Gets the Google Apps entry for the provided {@code username}.
     *
     * @param username          The username of the Google Apps account.
     * @return The Google Apps account if one exists, and {@code null} if not.
     */
    def googleAppsAccountFor(String username) {
		print username
        URL retrieveUrl = 
            new URL("${grailsApplication.config.myt.gAppsUrl}/${username}")
        try {
            UserEntry entry = userService().getEntry(retrieveUrl, UserEntry.class)
            if (entry.getLogin().getSuspended() == true) {
                return null
            }
            else {
				print "entry: " + entry.getLogin().getUserName()
                return entry
            }
        }
        catch (AppsForYourDomainException e) {
            log.info("Apps exception: ${e.toString()}")
            return null
        }
    }
    
    /**
     * Takes the passed in {@code entry}, and {@code password} and sets the
     * user's Gapps Account password. The {@code isRandom} param is so that
     * the logging mechanism knows whether or not this is a set by the user,
     * or a set by the app (which is actually a delete).
     *
     * @param entry         The Google Apps entry to be updated.
     * @param password      The password to set for the provided entry.
     */
    def saveTokenForAccount(UserEntry entry, String password,
        Boolean isGenerated, person) throws Exception {
        try {
            entry.getLogin().setHashFunctionName(pwdHashFunction);
            entry.getLogin().setPassword(hashPassword(password));
            updateUser(entry)
            

            if (isGenerated) {
                log.info("Deleted (set random) Google Apps token for: [uid: ${person.getAttributeValue('uid')}], [username: ${person.getAttributeValue(grailsApplication.config.myt.calNetUsername)}], [account: ${entry.getLogin().getUserName()}].")
            }
            else {
                log.info("Set Google Apps token for: [uid: ${person.getAttributeValue('uid')}], [username: ${person.getAttributeValue(grailsApplication.config.myt.calNetUsername)}], [account: ${entry.getLogin().getUserName()}].")
            }
            
        }
        catch (AppsForYourDomainException e) {
            log.error("Could not update entry: ${e.getMessage()} - ${e.toString()}")
            return [error:"Could not save the token: ${e.getMessage()} - ${e.toString()}"]
        }
        catch (ServiceException e) {
            log.error("Google Apps Service exception: ${e.getMessage()} - ${e.toString()}")
            return [error:"Could not save the token: ${e.getMessage()} - ${e.toString()}"]
        }
        catch (InvalidEntryException e) {
            log.error("Google Apps Invalid Entry exception: ${e.getMessage()} - ${e.toString()}")
            return [error:"Could not save the token: ${e.getMessage()} - ${e.toString()}"]
        }
        return []
    }
    
    /**
     * Updates the provided {@code entry} in Google.
     *
     * @param entry         A Google Apps entry.
     */
    def updateUser(UserEntry entry) {
        URL updateUrl = 
            new URL("${grailsApplication.config.myt.gAppsUrl}/${entry.getLogin().getUserName()}")
        userService().update(updateUrl, entry)
    }
    
    /**
     * Hashes the provided {@code password} using the {@code pwdHashFunction}.
     *
     * @param password          The password to hash.
     * @return The hashed password.
     */
    def hashPassword(String password) {
        
        MessageDigest messageDigest = MessageDigest.getInstance(pwdHashFunction)
        messageDigest.update(password.getBytes())
        byte[] byteData = messageDigest.digest()
        
        StringBuffer sb = new StringBuffer()
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1))
        }
        
        return sb.toString()
    }
    
    /**
     * Creates a {@code UserService} object and returns it.
     *
     * @return The UserService object.
     */
    def userService() {
        //if (userService) {
        //    return userService
        //}
        //else {
            UserService userService = new UserService("Berkeley-Token-App")
            
            userService.setUserCredentials(
                grailsApplication.config.myt.gAppsUsername,
                grailsApplication.config.myt.gAppsPassword)
            return userService
        //}
    }
    
}
