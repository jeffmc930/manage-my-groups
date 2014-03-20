dataSource {
    pooled = true
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        // Configuration is external
		/**
		dataSource_calmail {
        	dbCreate = "update"
            url = "jdbc:h2:mem:devCalMailDb;MVCC=TRUE"
            driverClassName = "org.h2.Driver"
            username = "sa"
            password = ""
        } **/
       
    }
    test {
        dataSource_wpa {
            dbCreate = "update"
            url = "jdbc:h2:mem:testWpaDb;MVCC=TRUE"
            driverClassName = "org.h2.Driver"
            username = "sa"
            password = ""
        }

        dataSource_calmail {
            dbCreate = "update"
            url = "jdbc:h2:mem:devCalMailDb;MVCC=TRUE"
            driverClassName = "org.h2.Driver"
            username = "sa"
            password = ""
        }
    }
    production {
        // Configuration is external
    }
}
