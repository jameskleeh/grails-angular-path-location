import org.springframework.boot.web.servlet.FilterRegistrationBean

// Place your Spring DSL code here
beans = {

    spaFilter(FilterRegistrationBean) {
        filter = new SpaFilter().withExcludedUris(
                ['^/api.*', '.*\\.js$', '^/images.*']
        )
    }
}
