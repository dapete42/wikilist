package toolforge.wikilist.quarkus;

import io.quarkus.arc.Unremovable;
import io.quarkus.credentials.CredentialsProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import toolforge.wikilist.MyCnfConfig;

import java.io.IOException;
import java.util.Map;

@Named("mycnf")
@ApplicationScoped
@Unremovable
public class MyCnfCredentialsProvider implements CredentialsProvider {

    @Override
    public Map<String, String> getCredentials(String credentialsProviderName) {
        final MyCnfConfig config;
        try {
            config = new MyCnfConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Map.of(
                USER_PROPERTY_NAME, config.getUser(),
                PASSWORD_PROPERTY_NAME, config.getPassword()
        );
    }

}
