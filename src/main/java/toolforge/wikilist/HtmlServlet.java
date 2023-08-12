package toolforge.wikilist;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serial;

@ApplicationScoped
@WebServlet(urlPatterns = {"/html"})
@Slf4j
public final class HtmlServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 7039174169351226580L;

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        req.getRequestDispatcher("index.xhtml").forward(req, resp);
    }

}
