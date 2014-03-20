// Run grails test-app SecurityFilters --functional

import grails.test.mixin.*
import org.junit.*

@TestFor(SecurityFilters)
class SecurityFiltersTests extends FiltersUnitTestCase { //functionaltestplugin.FunctionalTestCase {
    
    void testAuthorizedWpa() {
        
        def affiliations = ["EMPLOYEE-TYPE-STAFF", "STUDENT-STAUTS-EXPIRED"]
        
        def sf = new SecurityFilters()
        
        def authorized = sf.authorizedWpa(affiliations)
        
        assertTrue authorized
        
        
    }
    
    
}