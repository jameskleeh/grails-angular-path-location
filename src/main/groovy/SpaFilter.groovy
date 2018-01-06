import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.view.InternalResourceView

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.regex.Matcher
import java.util.regex.Pattern

class SpaFilter extends OncePerRequestFilter {

    private List<String> excludedUris = []
    private List<Pattern> patterns = []

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean continueFilter = false
        String uri = request.requestURI

        for (String excludedUri: excludedUris) {
            if (uri == excludedUri) {
                continueFilter = true
                break
            }
        }
        if (!continueFilter) {
            for (Pattern pattern: patterns) {
                Matcher matcher = pattern.matcher(uri)
                if (matcher.matches()) {
                    continueFilter = true
                    break
                }
            }
        }

        if (continueFilter) {
            filterChain.doFilter(request, response)
        } else {
            new InternalResourceView("/index.html").render(Collections.emptyMap(), request, response)
        }
    }

    SpaFilter withExcludedUris(List<String> uris) {
        this.excludedUris = uris
        List<Pattern> patterns = []
        for (String uri: uris) {
            patterns.add(Pattern.compile(uri))
        }
        this.patterns = patterns
        this
    }
}
