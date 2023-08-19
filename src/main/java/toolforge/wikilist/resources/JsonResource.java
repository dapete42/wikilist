package toolforge.wikilist.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import toolforge.wikilist.services.WikilistService;

import java.util.Date;

@Path("/json")
public class JsonResource {

    @Inject
    private WikilistService wikilistService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        return Response
                .ok(wikilistService.getWikiEntries(), MediaType.APPLICATION_JSON)
                .lastModified(Date.from(wikilistService.getWikiEntriesFilledTime()))
                .build();
    }

}
