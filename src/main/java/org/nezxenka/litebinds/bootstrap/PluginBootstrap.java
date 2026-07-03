package org.nezxenka.litebinds.bootstrap;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;
import org.nezxenka.litebinds.LiteBinds;
import org.nezxenka.litebinds.action.ActionExecutor;
import org.nezxenka.litebinds.action.ActionRegistry;
import org.nezxenka.litebinds.action.handler.AlternativeTrapHandler;
import org.nezxenka.litebinds.action.handler.BackpackHandler;
import org.nezxenka.litebinds.action.handler.ExplosiveHandler;
import org.nezxenka.litebinds.action.handler.JakeHandler;
import org.nezxenka.litebinds.action.handler.SnowballHandler;
import org.nezxenka.litebinds.action.handler.StanHandler;
import org.nezxenka.litebinds.action.handler.TrapHandler;
import org.nezxenka.litebinds.config.ConfigLoader;
import org.nezxenka.litebinds.config.DatabaseConfig;
import org.nezxenka.litebinds.config.GuiConfig;
import org.nezxenka.litebinds.config.PluginConfig;
import org.nezxenka.litebinds.database.DatabaseConnectionFactory;
import org.nezxenka.litebinds.database.DatabaseConnectionPool;
import org.nezxenka.litebinds.database.DatabaseCredentials;
import org.nezxenka.litebinds.database.DatabaseManager;
import org.nezxenka.litebinds.database.DatabaseSchemaInitializer;
import org.nezxenka.litebinds.database.DatabaseType;
import org.nezxenka.litebinds.database.repository.PlayerActionsQueryProvider;
import org.nezxenka.litebinds.database.repository.PlayerActionsRepository;
import org.nezxenka.litebinds.database.repository.PlayerActionsResultMapper;
import org.nezxenka.litebinds.listener.PlayerEventProcessor;
import org.nezxenka.litebinds.menu.BindsMenuListener;
import org.nezxenka.litebinds.menu.builder.InventoryLayoutBuilder;
import org.nezxenka.litebinds.menu.builder.LoreBuilder;
import org.nezxenka.litebinds.menu.builder.MenuItemBuilder;
import org.nezxenka.litebinds.menu.handler.BindAssignmentProcessor;
import org.nezxenka.litebinds.menu.handler.MenuClickRouter;
import org.nezxenka.litebinds.menu.provider.ActionItemDataProvider;
import org.nezxenka.litebinds.menu.provider.ResetItemProvider;
import org.nezxenka.litebinds.model.ActionType;
import org.nezxenka.litebinds.util.ColorParser;
import org.nezxenka.litebinds.util.ServerVersionChecker;

public class PluginBootstrap {

    private final LiteBinds plugin;

    private DatabaseManager databaseManager;
    private ActionExecutor actionExecutor;
    private BindsMenuListener bindsMenuListener;
    private InventoryLayoutBuilder layoutBuilder;

    public PluginBootstrap(LiteBinds plugin) {
        this.plugin = plugin;
    }

    public void boot() {
        plugin.saveDefaultConfig();
        saveDefaultDatabaseConfig();

        PluginConfig pluginConfig = new ConfigLoader().load(
            plugin.getConfig(),
            loadDatabaseConfig()
        );

        DatabaseConfig dbConfig = pluginConfig.getDatabase();
        GuiConfig guiConfig = pluginConfig.getGui();

        DatabaseType dbType = dbConfig.getType().equalsIgnoreCase("mysql")
            ? DatabaseType.MYSQL
            : DatabaseType.SQLITE;

        DatabaseCredentials credentials = DatabaseCredentials.builder()
            .type(dbType)
            .mysqlHost(dbConfig.getMysqlHost())
            .mysqlPort(dbConfig.getMysqlPort())
            .mysqlUser(dbConfig.getMysqlUser())
            .mysqlPassword(dbConfig.getMysqlPassword())
            .mysqlDatabase(dbConfig.getMysqlDatabase())
            .sqliteFile(dbConfig.getSqliteFile())
            .build();

        DatabaseConnectionFactory connectionFactory =
            new DatabaseConnectionFactory();
        DatabaseConnectionPool connectionPool = new DatabaseConnectionPool(
            connectionFactory.create(credentials)
        );

        DatabaseSchemaInitializer schemaInitializer =
            new DatabaseSchemaInitializer(connectionPool);
        schemaInitializer.initialize();

        PlayerActionsQueryProvider queryProvider =
            new PlayerActionsQueryProvider(dbType == DatabaseType.MYSQL);
        PlayerActionsResultMapper resultMapper =
            new PlayerActionsResultMapper();
        PlayerActionsRepository repository = new PlayerActionsRepository(
            connectionPool,
            queryProvider,
            resultMapper
        );

        databaseManager = new DatabaseManager(connectionPool, repository);

        ActionRegistry actionRegistry = new ActionRegistry();
        actionRegistry.register(ActionType.SNOWBALL, new SnowballHandler());
        actionRegistry.register(ActionType.JAKE, new JakeHandler());
        actionRegistry.register(
            ActionType.ALTERNATIVE_TRAP,
            new AlternativeTrapHandler()
        );
        actionRegistry.register(ActionType.STAN, new StanHandler());
        actionRegistry.register(ActionType.TRAP, new TrapHandler());
        actionRegistry.register(ActionType.EXPLOSIVE, new ExplosiveHandler());
        actionRegistry.register(ActionType.BACKPACK, new BackpackHandler());

        actionExecutor = new ActionExecutor(actionRegistry);

        ServerVersionChecker versionChecker = new ServerVersionChecker();
        ColorParser colorParser = new ColorParser(versionChecker);

        ActionItemDataProvider actionItemDataProvider =
            new ActionItemDataProvider(guiConfig);
        LoreBuilder loreBuilder = new LoreBuilder(
            colorParser,
            actionItemDataProvider,
            guiConfig.getLore()
        );
        MenuItemBuilder menuItemBuilder = new MenuItemBuilder(
            colorParser,
            actionItemDataProvider,
            loreBuilder
        );
        ResetItemProvider resetItemProvider = new ResetItemProvider(
            colorParser,
            guiConfig.getResetButton()
        );

        layoutBuilder = new InventoryLayoutBuilder(
            colorParser,
            guiConfig.getTitle(),
            guiConfig.getRows(),
            actionItemDataProvider,
            menuItemBuilder,
            resetItemProvider,
            guiConfig.getResetButton()
        );

        BindAssignmentProcessor bindAssignmentProcessor =
            new BindAssignmentProcessor(databaseManager);
        MenuClickRouter menuClickRouter = new MenuClickRouter(
            databaseManager,
            bindAssignmentProcessor,
            actionItemDataProvider
        );

        bindsMenuListener = new BindsMenuListener(menuClickRouter);

        PlayerEventProcessor eventProcessor = new PlayerEventProcessor(
            databaseManager,
            actionExecutor
        );

        new CommandRegistration(
            plugin,
            new org.nezxenka.litebinds.command.BindsCommandExecutor(
                databaseManager,
                layoutBuilder
            ),
            new org.nezxenka.litebinds.command.BindsTabCompleter()
        ).register();

        new ListenerRegistration(
            plugin,
            eventProcessor,
            bindsMenuListener
        ).register();
    }

    private void saveDefaultDatabaseConfig() {
        File dbFile = new File(plugin.getDataFolder(), "database.yml");
        if (!dbFile.exists()) {
            plugin.saveResource("database.yml", false);
        }
    }

    private YamlConfiguration loadDatabaseConfig() {
        File dbFile = new File(plugin.getDataFolder(), "database.yml");
        return YamlConfiguration.loadConfiguration(dbFile);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public ActionExecutor getActionExecutor() {
        return actionExecutor;
    }

    public BindsMenuListener getBindsMenuListener() {
        return bindsMenuListener;
    }

    public InventoryLayoutBuilder getLayoutBuilder() {
        return layoutBuilder;
    }
}
