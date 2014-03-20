package edu.berkeley.ims.mmg

//import grails.plugins.rest.client.RestBuilder

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.URLENC

class CalNetService {

    /* GrailsApplication -- needed for the config. */
    def grailsApplication
	
	/* LDAP Service */
	def ldapService

    /**
     * Takes the passed in {@code person} and tries to authenticate
     * as that user using the passed in {@code token}.
     *
     * @param passphrase    The token/password to use for the authN attempt
     * @param person        UnboundID LDAP SDK person
     * @return true if the authN attempt is successful, and false otherwise.
     */
    def testAuthenticationFor(passphrase, person) {
        def kps = person.getAttributeValue(grailsApplication.config.myt.calNetUsername)
        def url = "${grailsApplication.config.myt.krbURL}authN"
        
        def params =  [
            appid: grailsApplication.config.myt.krbAppId,
            authkey: grailsApplication.config.myt.krbAuthKey,
            id: kps,
            passphrase: passphrase
        ]
        
        def response
        
        def http = new HTTPBuilder(url)
        
        http.post(body: params) { resp, reader ->
            response = reader.toString().split(/\n/)
        }
        
        return response[0] == "0" ? true : false
        
    }
    
    /*
     * Regex test for CalNet passphrase rules 1, 2, and the ID part of 3.
     *
     * Rules and methodology are below:
     *
     * 1) A minimum length of 9 characters (maximum 255). It may also include spaces
     *    (which is why we call it a passphrase).
     * 2) It must contain characters from at least three of the following four
     *    character groups:
     *    a) English uppercase (A through Z)
     *    b) English lowercase (a through z)
     *    c) numeric digits (0 through 9)
     *    d) non-alphanumeric characters (such as !, $, #, or %)
     * 3) Without regard to case, the passphrase may not contain your first name,
     *    middle name, last name, or your CalNet ID itself if any of these are three
     *    characters or longer.
     * 4) Any time you change your passphrase, the new one may not be the same as
     *    the current or previous passphrase.
     *
     * Again, we are only testing 1 and 2 with this regex.
     *
     * Methodology:
     *  1) Test to make sure the passphrase does not contain the ID
     *  2) Length: .{9,255}
     *  3) Permutations of a, b, c, and d
     *      i)      a, b, c     (?=.*[a-z])(?=.*[A-Z])(?=.*\d)
     *      ii)     a, b, d     (?=.*[a-z])(?=.*[A-Z])(?=.*\p{Punct})
     *      iii)    b, c, d     (?=.*[A-Z])(?=.*\d)(?=.*\p{Punct})
     *      iv)     a, c, d     (?=.*[a-z])(?=.*\d)(?=.*\p{Punct})
     *  
    */
    def validatePassphraseComplexityFor(passphrase, person) {
        
        // Get the ID and displayName of the person
        def kps = person.getAttributeValue(grailsApplication.config.myt.calNetUsername)
        def displayName = person.getAttributeValue('displayName')
        
        // Make sure the ID is not contained in the passphrase
        if (kps?.size() > 2 &&
            passphrase?.toLowerCase().matches(/.*${kps?.toLowerCase()}.*/)) {
            return false
        }
        
        def names = displayName?.split(/\s/)
        def nameMatches = false
        names.each { name ->
            if (name?.size() > 2 && passphrase?.toLowerCase().matches(/.*${name?.toLowerCase()}.*/)) {
                nameMatches = true
            }
        }
        if (nameMatches) {
            return false
        }
        
        // The permutations
        def i   = ~/(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{9,255}/
        def ii  = ~/(?=.*[a-z])(?=.*[A-Z])(?=.*\p{Punct}).{9,255}/
        def iii = ~/(?=.*[A-Z])(?=.*\d)(?=.*\p{Punct}).{9,255}/
        def iv  = ~/(?=.*[a-z])(?=.*\d)(?=.*\p{Punct}).{9,255}/

        // If the passphrase exists, check each permutation
        if (passphrase && (
            i.matcher(passphrase).matches() ||
            ii.matcher(passphrase).matches() ||
            iii.matcher(passphrase).matches() ||
            iv.matcher(passphrase).matches())) {
            return true
        }
        
        return false
    }


	// we check the name in the LDAP NameSpace
	// if it exists we need to confirm it is owned by the current user
	// otherwise we return false. If it doesn't exist, they 
	// we return true, and the user is good to go.
	// @param {@code name, person} - the name to check, the current user
    // @return boolean 
    
    def validateName(name, person) {
		// look for the name
		def entry = ldapService.findOwnerOfName(name)
		if (entry) {
			if (entry.getAttributeValue(grailsApplication.config.ldap.personIdAttr) == 		
					person.getAttributeValue(grailsApplication.config.ldap.personIdAttr)){
				return true
			} else {
				return false
			}
    	} else {
			return true
		}
	}

}
