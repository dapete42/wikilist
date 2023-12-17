package org.toolforge.wikilist.rest;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.toolforge.wikilist.services.WikilistService;

@Path("/")
public class IndexResource {

    @Inject
    private Template index;

    @Inject
    private WikilistService wikilistService;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        return index.data("wikiEntries", wikilistService.getWikiEntries());
    }

}
