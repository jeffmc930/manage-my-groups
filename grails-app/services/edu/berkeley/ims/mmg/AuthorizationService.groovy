package edu.berkeley.ims.mmg

class AuthorizationService {

    /**
     * Checks to see if a user is authorized to use the WPA service. This has
     * been factored into its own method so that the logic can be easily tested.
     *
     * @param affiliations    List of a person's affiliations.
     * @return true if the person is authorized based on their affiliations, or
     * false if not authorized.
     */
    def isAuthorizedWpa(affiliations) {
        def staffPattern = ~/EMPLOYEE-TYPE-.*/
        def studentPattern = ~/STUDENT-TYPE-.*/
        def affiliatePattern = ~/AFFILIATE-TYPE-.*/
        def affiliateRejectPattern = ~/AFFILIATE-TYPE-ADVCON.*/
        
        def isAuthorized = false
        
        affiliations?.each { affiliation ->
            if (affiliation.matches(staffPattern)
                || affiliation.matches(studentPattern)
                || (affiliation.matches(affiliatePattern) &&
                    (!affiliation.matches(affiliateRejectPattern)))) {
                isAuthorized = true
            }
        }
        if (isAuthorized) {
            return true
        }
        else {
            return false
        }
    }
    
    /**
     * For CalMail, as long as the person has an entry in the CalMail database,
     * and the domainId is 1, then the person is authorized. However, the
     * person may not have a Google Apps account, but that will be determined
     * later, and if not, then appropriate error message will be displayed at
     * that time.
     */
    def isAuthorizedCalMail(googleAppsAccounts) {
        if (googleAppsAccounts?.size() == 0) {
            return false
        }
        return true
    }
}
