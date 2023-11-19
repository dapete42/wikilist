package org.toolforge.wikilist.services;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.toolforge.wikilist.WikiEntry;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ApplicationScoped
@Slf4j
public class WikilistService {

    @Inject
    private DataSource dataSource;

    private List<WikiEntry> wikiEntries;

    @Getter
    private Instant wikiEntriesFilledTime = Instant.ofEpochMilli(0);

    @PostConstruct
    void init() {
        fillWikiEntries();
    }

    public List<WikiEntry> getWikiEntries() {
        // Fill wiki entries if older than 24 hours minutes or empty
        if (wikiEntries.isEmpty()) {
            LOG.info("Wiki entries empty, filling");
            fillWikiEntries();
        } else if (Duration.between(Instant.now(), wikiEntriesFilledTime).toSeconds() > 86400) {
            LOG.info("Wiki entries older than 24 hours, refreshing");
            fillWikiEntries();
        }
        return wikiEntries;
    }

    private void fillWikiEntries() {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement("SELECT * FROM wiki WHERE is_closed=0 ORDER BY dbname");
             var rs = statement.executeQuery()) {
            List<WikiEntry> newWikiEntries = new ArrayList<>();
            while (rs.next()) {
                var entry = WikiEntry.fromResultSet(rs);
                newWikiEntries.add(fillLanguagesForDisplay(entry));
            }
            wikiEntries = newWikiEntries;
            wikiEntriesFilledTime = Instant.now();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static WikiEntry fillLanguagesForDisplay(WikiEntry entry) {
        final String lang = entry.getLang();
        final Locale entryLocale = Locale.of(lang);
        String languageDisplay = entryLocale.getDisplayLanguage(Locale.ENGLISH);
        String languageNative = entryLocale.getDisplayLanguage(entryLocale);
        if (languageNative.equalsIgnoreCase(languageDisplay)) {
            languageNative = "—";
        }
        if (lang.equals(languageDisplay)) {
            languageDisplay = "—";
        }
        entry.setLanguageDisplay(languageDisplay);
        entry.setLanguageNative(languageNative);
        return entry;
    }

}
