import edu.berkeley.ims.mmg.CalMail

class BootStrap {

    /* Get the LdapService singleton set up here because we
       need to get its config set up, and you can't do that
       without calling an instance method on the service, and
       it would be a hack to randomly call it in a controller
       or other service class. So, we call it here in init. */
	/* Do the same for GoogleGroupsService */
	def googleGroupsService

    def ldapService

    def init = { servletContext ->
        ldapService.setConfig()	
        //googleGroupsService.doSetup()
    
        
        // Set up the calmail database rows.
        environments {
            development {
                //createAccounts()
            }
            test {
                //createAccounts()
            }
        }
    }
    def destroy = {
    }
    
    private void createAccounts() {
        CalMail.withTransaction { status ->
            [
                [3807, 'sondra', 110, 'google', 'abc', false],
                [3807, 'calnet-test', 110, 'google', 'abc', false],
                [3807, 'calnet-test1', 110, 'google', 'abc', false],
                [3807, 'calnet-test2', 110, 'google', 'abc', false],
                [3807, 'calnet-test3', 110, 'google', 'abc', false],
                [3807, 'calnet-test4', 110, 'google', 'abc', false],
                [3807, 'tokenapp', 110, 'google', 'abc', false]
            ].each { ownerUid, localpart, domainId, host, deptCode , loginDisabled->
                def account = new CalMail(
                    ownerUid: ownerUid, 
                    localpart: localpart, 
                    domainId: domainId, 
                    host: host, 
                    deptCode: deptCode,
					loginDisabled: loginDisabled
                ).save(flush: true, failOnError: true)
            }
        }
    }
    
}
