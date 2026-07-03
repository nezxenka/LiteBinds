package org.nezxenka.litebinds.database;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DatabaseCredentials {

    private final DatabaseType type;

    private final String mysqlHost;
    private final int mysqlPort;
    private final String mysqlUser;
    private final String mysqlPassword;
    private final String mysqlDatabase;

    private final String sqliteFile;
}
