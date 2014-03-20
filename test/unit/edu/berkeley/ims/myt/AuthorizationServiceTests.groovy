package edu.berkeley.ims.myt



import grails.test.mixin.*
import org.junit.*

/**
 * Affiliations for CalNet can be found here:
 *
 *   https://wikihub.berkeley.edu/display/calnet/People+OU+Affiliations
 *
 */

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(AuthorizationService)
class AuthorizationServiceTests {
    
    /**
     * Test the various combinations of affiliations to make sure the
     * isAuthoirzedWpa() logic is correct. Basically, any affiliate except for
     * expired and ADVCON are allowed to use the WPA service.
     */
    void testIsAuthorizedWpa() {
        
        def authorizationService = new AuthorizationService()
        
        // Staff and expired student, should be true
        def affiliations = ["EMPLOYEE-TYPE-STAFF", "STUDENT-STAUTS-EXPIRED"]
        def authorized = authorizationService.isAuthorizedWpa(affiliations)
        assertTrue authorized
        
        // Staff and expired affiliate, should be true
        affiliations = ["EMPLOYEE-TYPE-STAFF", "AFFILIATE-STAUTS-EXPIRED"]
        authorized = authorizationService.isAuthorizedWpa(affiliations)
        assertTrue authorized
        
        // Expired staff and expired affiliate, should be false
        affiliations = ["EMPLOYEE-STAUTS-EXPIRED", "AFFILIATE-STAUTS-EXPIRED"]
        authorized = authorizationService.isAuthorizedWpa(affiliations)
        assertFalse authorized
        
        // Staff and student, should be true
        affiliations = ["EMPLOYEE-TYPE-STAFF", "STUDENT-TYPE-NOT REGISTERED"]
        authorized = authorizationService.isAuthorizedWpa(affiliations)
        assertTrue authorized
        
        // Student and affiliate, should be true
        affiliations = ["STUDENT-TYPE-NOT REGISTERED", "AFFILIATE-TYPE-TEMPORARY AGENCY STAFF"]
        authorized = authorizationService.isAuthorizedWpa(affiliations)
        assertTrue authorized
        
        // ADVCON Affiliate, should be false
        affiliations = ["AFFILIATE-TYPE-ADVCON-ALUMNUS"]
        authorized = authorizationService.isAuthorizedWpa(affiliations)
        assertFalse authorized
        
        // Staff and ADVCON Affiliate, should be true
        affiliations = ["EMPLOYEE-TYPE-ACADEMIC", "AFFILIATE-TYPE-ADVCON-ALUMNUS"]
        authorized = authorizationService.isAuthorizedWpa(affiliations)
        assertTrue authorized
        
        // Staff and ADVCON Affiliate, should be true (reversed, to make sure order does not matter)
        affiliations = ["AFFILIATE-TYPE-ADVCON-ALUMNUS", "EMPLOYEE-TYPE-ACADEMIC"]
        authorized = authorizationService.isAuthorizedWpa(affiliations)
        assertTrue authorized
        
        // Staff with all the rest expired, should be true
        affiliations = ["EMPLOYEE-TYPE-ACADEMIC", "STUDENT-STATUS-EXPIRED", "AFFILIATE-STATUS-EXPIRED"]
        authorized = authorizationService.isAuthorizedWpa(affiliations)
        assertTrue authorized
        
    }
}
