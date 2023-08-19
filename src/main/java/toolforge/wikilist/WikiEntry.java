package toolforge.wikilist;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("is_closed")
    private Boolean isClosed;

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

}
