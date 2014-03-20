package edu.berkeley.ims.mmg

import org.springframework.web.servlet.ModelAndView

class AlertController {

    def formErrors() {
        if (params.item?.hasErrors()) {
            flash.success = null
            flash.ajaxSuccess = null
        }
        return new ModelAndView('/alert/formErrors', ['item':params.item])
    }
    
    def flashSuccess() { }
    
    def flashAjaxSuccess() { }
    
    def flashError() { }
    
    def flashWarning() { }
    
    def flashAjaxWarning() { }
    
}
