package edu.berkeley.ims.mmg

class CalMail {

    /*
     * The ID column needs to be mapped because this is a "legacy" db.
     */
    int id

    /*
     * The CalNet UID of the user
     */
    int ownerUid
    
    /*
     * The username
     */
    String localpart

    /*
     * The host, like a CalMail server, or Google
     */
    String host

    /*
     * The domain ID, like 1, 104, etc. 1 means "berkeley.edu"
     */
    int domainId

    /*
     * Department code, which is used to determine a departmental account
     */
    String deptCode
    
    /*
     * Whether or not the account is disabled.
     */
    Boolean loginDisabled

    /*
     * Table and datasouce mappings
     */
    static mapping = {
        datasource 'calmail'
        version false
        autoTimestamp false
        table 'account'
        id name:'id'
        //enabled type: 'yes_no' 
    }

    /*
     * No constraints because we are not updating anything
     */
    static constraints = {
        
    }
}
