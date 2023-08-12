package toolforge.wikilist;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import toolforge.wikilist.beans.WikilistBean;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
@WebServlet(urlPatterns = {"/json"})
@Slf4j
public final class JsonServlet extends HttpServlet {

    @Inject
    WikilistBean wikilistBean;

    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

        final JsonArrayBuilder builder = Json.createArrayBuilder();
        for (WikiEntry entry : wikilistBean.getWikiEntries()) {
            builder.add(entry.toJsonObject());
        }
        final JsonArray json = builder.build();

        resp.setContentType("application/json; charset=UTF8");

        try (final OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(),
                StandardCharsets.UTF_8)) {
            Json.createWriter(writer).writeArray(json);
        }

    }

    @Override
    public long getLastModified(final HttpServletRequest req) {
        return wikilistBean.getWikiEntriesFilledTime();
    }

}
