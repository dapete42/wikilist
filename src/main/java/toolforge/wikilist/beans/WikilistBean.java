package toolforge.wikilist.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import toolforge.wikilist.WikiEntry;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Named("wikilist")
@ApplicationScoped
@Slf4j
public class WikilistBean {

    @Inject
    DataSource dataSource;

    @Setter
    private List<WikiEntry> wikiEntries;

    @Getter
    private long wikiEntriesFilledTime = 0;

    public synchronized List<WikiEntry> getWikiEntries() {
        final long nowTime = System.currentTimeMillis();
        // Fill wiki entries if older than 24 hours minutes or empty
        if (wikiEntries.isEmpty()) {
            LOG.info("Wiki entries empty, filling");
            fillWikiEntries();
        } else if (nowTime - this.wikiEntriesFilledTime > 86400000) {
            LOG.info("Wiki entries older than 24 hours, refreshing");
            fillWikiEntries();
        }
        return wikiEntries;
    }

    @PostConstruct
    void init() {
        fillWikiEntries();
    }

    private void fillWikiEntries() {
        try (final Connection connection = dataSource.getConnection()) {
            var rs = connection
                    .prepareStatement("SELECT * FROM wiki WHERE is_closed=0 ORDER BY dbname")
                    .executeQuery();
            List<WikiEntry> newWikiEntries = new ArrayList<>();
            while (rs.next()) {
                var entry = WikiEntry.fromResultSet(rs);
                newWikiEntries.add(fillLanguagesForDisplay(entry));
            }
            wikiEntries = newWikiEntries;
            wikiEntriesFilledTime = System.currentTimeMillis();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static WikiEntry fillLanguagesForDisplay(WikiEntry entry) {
        final String lang = entry.getLang();
        final Locale entryLocale = new Locale(lang);
        String languageDisplay = entryLocale.getDisplayLanguage(Locale.ENGLISH);
        String languageNative = entryLocale.getDisplayLanguage(entryLocale);
        if (languageNative.equalsIgnoreCase(languageDisplay)) {
            languageNative = "\u2014";
        }
        if (lang.equals(languageDisplay)) {
            languageDisplay = "\u2014";
        }
        entry.setLanguageDisplay(languageDisplay);
        entry.setLanguageNative(languageNative);
        return entry;
    }

}
