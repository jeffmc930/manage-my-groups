package edu.berkeley.ims.mmg

class Wpa {

    /**
     * The username
     */
    String username
    
    /**
     * The token
     */
    String password
    
    /**
     * Make sure GORM does not try to keep a version number nor create and
     * update timestamp columns. Also set the name of the table based on what
     * the real table name is. This can also be set in Config.groovy or in
     * another configuration file.
     */
    static mapping = {
        datasource 'wpa'
        version false
        autoTimestamp false
        table 'users'
        id name:'username', generator:'assigned'
    }

    /**
     * Constraints
     */
    static constraints = {
        username(blank:false,nullable:false,size:2..30)
        password(blank:false,nullable:false,size:6..10)
    }
}
