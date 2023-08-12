package toolforge.wikilist;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class WikiEntry {

    private final String dbname;

    private final String lang;

    private final String name;

    private final String family;

    private final String url;

    public Boolean isClosed;

    private String languageDisplay;

    private String languageNative;

    public static WikiEntry fromResultSet(ResultSet rs) throws SQLException {
        return WikiEntry.builder()
                .dbname(rs.getString("dbname"))
                .lang(rs.getString("lang"))
                .name(rs.getString("name"))
                .family(rs.getString("family"))
                .url(rs.getString("url"))
                .isClosed(rs.getInt("is_closed") != 0)
                .build();
    }

    public JsonObject toJsonObject() {
        final JsonObjectBuilder builder = Json.createObjectBuilder();
        if (dbname != null) {
            builder.add("dbname", dbname);
        }
        if (lang != null) {
            builder.add("lang", lang);
        }
        if (name != null) {
            builder.add("name", name);
        }
        if (family != null) {
            builder.add("family", family);
        }
        if (url != null) {
            builder.add("url", url);
        }
        if (isClosed != null) {
            builder.add("is_closed", isClosed);
        }
        return builder.build();
    }

}
