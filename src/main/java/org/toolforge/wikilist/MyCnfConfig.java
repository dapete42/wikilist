package org.toolforge.wikilist;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Slf4j
public class MyCnfConfig {

    private static final String MY_CNF = "replica.my.cnf";

    @Getter
    private String password;

    @Getter
    private String user;

    public MyCnfConfig() throws IOException {

        final String userHome = System.getProperty("user.home");
        final String toolDataDir = System.getenv("TOOL_DATA_DIR");

        // Primary: from $HOME
        Path myCnfFile = Paths.get(userHome, MY_CNF);
        if (Files.exists(myCnfFile)) {
            LOG.info("Using {} from {}", MY_CNF, myCnfFile);
        } else {
            // Secondary: from $TOOL_DATA_DIR (within container built by Build Service)
            if (toolDataDir != null) {
                myCnfFile = Paths.get(toolDataDir, MY_CNF);
                if (Files.exists(myCnfFile)) {
                    LOG.info("Using {} from {}", MY_CNF, myCnfFile);
                } else {
                    throw new IOException(
                            MessageFormatter.format("{} not found in home or $TOOL_DATA_DIR", MY_CNF).getMessage());
                }
            } else {
                throw new IOException(
                        MessageFormatter.format("{} not found in home", MY_CNF).getMessage());
            }
        }

        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(myCnfFile);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            properties.load(bufferedReader);
        } catch (IOException e) {
            throw new IOException(
                    MessageFormatter.format("Error reading file '{}'", myCnfFile.toAbsolutePath()).getMessage(), e);
        }

        this.user = properties.getProperty("user");
        if (this.user == null || this.user.isEmpty()) {
            LOG.error(String.format("Property '%s' missing or empty", "user"));
        } else {
            if (this.user.startsWith("'") && this.user.endsWith("'")) {
                this.user = this.user.substring(1, this.user.length() - 1);
            }
        }

        this.password = properties.getProperty("password");
        if (this.password == null || this.password.isEmpty()) {
            LOG.error(String.format("Property '%s' missing or empty", "password"));
        } else {
            if (this.password.startsWith("'") && this.password.endsWith("'")) {
                this.password = this.password.substring(1, this.password.length() - 1);
            }
        }

    }

}
