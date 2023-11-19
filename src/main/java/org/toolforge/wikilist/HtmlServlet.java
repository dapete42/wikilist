package org.toolforge.wikilist;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;

@ApplicationScoped
@WebServlet(urlPatterns = "/html")
public final class HtmlServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = -3642014253438082146L;

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        req.getRequestDispatcher("index.xhtml").forward(req, resp);
    }

}
