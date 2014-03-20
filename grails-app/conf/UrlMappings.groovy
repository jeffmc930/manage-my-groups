class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:"main")
        //"/"(view:"/index")
        "500"(view: grails.util.Environment.current.name == 'production' ? '/errorProd' : '/error')
        "404"(view: '/error404')
    }
}
