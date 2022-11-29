package ru.job4j.cinema.filter;

import org.springframework.stereotype.Component;
import ru.job4j.cinema.enumeration.UnfilteredUrn;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class AuthFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (check(uri)) {
            chain.doFilter(req, res);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/" + UnfilteredUrn.LOGIN_PAGE.getValue());
            return;
        }
        chain.doFilter(req, res);
    }

    private boolean check(String uri) {
        return Arrays.stream(UnfilteredUrn.values())
                .anyMatch(inst -> uri.endsWith(inst.getValue()));
    }
}
