package toolforge.wikilist.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import toolforge.wikilist.WikiEntry;
import toolforge.wikilist.services.WikilistService;

import java.util.List;

@Named("wikilist")
@ApplicationScoped
public class WikilistBean {

    @Inject
    private transient WikilistService wikilistService;

    public synchronized List<WikiEntry> getWikiEntries() {
        return wikilistService.getWikiEntries();
    }

}
