package edu.berkeley.ims.myt

class LdapTagLib {

    static namespace = 'ldap'
    
    def attr = { attrs ->
        if (!attrs.entry) {
            return
        }
        out << attrs.entry.getAttributeValue(attrs.attr)
    }
    
    def attrAsDate = { attrs ->
        if (!attrs.entry) {
            return
        }
        out << attrs.entry.getAttributeValueAsDate(attrs.attr)
    }
    
    def attrAsDateWithFormat = { attrs ->
        if (!attrs.entry) {
            return
        }
        def date = attrs.entry.getAttributeValueAsDate(attrs.attr)
        out << g.formatDate(date:date, format:attrs.format)
    }
    
    def attrAsBoolean = { attrs ->
        if (!attrs.entry) {
            return
        }
        out << attrs.entry.getAttributeValueAsBoolean(attrs.attr)
    }
    
}