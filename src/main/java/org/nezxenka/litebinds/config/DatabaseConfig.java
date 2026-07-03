package org.nezxenka.litebinds.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatabaseConfig {

    private final String type;

    private final String mysqlHost;
    private final int mysqlPort;
    private final String mysqlUser;
    private final String mysqlPassword;
    private final String mysqlDatabase;

    private final String sqliteFile;
}
