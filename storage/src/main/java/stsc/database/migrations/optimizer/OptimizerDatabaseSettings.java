package stsc.database.migrations.optimizer;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import stsc.changelogs.stsc_optimizer.StscOptimizerChangelog;
import stsc.config.stsc_optimizer.StscOptimizerConfig;

public final class OptimizerDatabaseSettings {

	private static final StscOptimizerConfig STSC_OPTIMIZER_CONFIG = new StscOptimizerConfig();
	private static final StscOptimizerChangelog STSC_OPTIMIZER_CHANGELOG = new StscOptimizerChangelog();

	private final String jdbcDriver;
	private final String jdbcUrl;
	private final String login;
	private final String password;

	public static OptimizerDatabaseSettings development() throws IOException, URISyntaxException {
		return new OptimizerDatabaseSettings(STSC_OPTIMIZER_CONFIG.getDevelopmentConfigFile());
	}

	public static OptimizerDatabaseSettings test() throws IOException, URISyntaxException {
		return new OptimizerDatabaseSettings(STSC_OPTIMIZER_CONFIG.getTestConfigFile());
	}

	public OptimizerDatabaseSettings(final String filePath) throws IOException {
		this(new FileInputStream(filePath));
	}

	private OptimizerDatabaseSettings(InputStream sourceInputStream) throws IOException {
		try (DataInputStream inputStream = new DataInputStream(sourceInputStream)) {
			final Properties properties = new Properties();
			properties.load(inputStream);
			jdbcDriver = properties.getProperty("jdbc.driver");
			jdbcUrl = properties.getProperty("jdbc.url");
			login = properties.getProperty("jdbc.login");
			password = properties.getProperty("jdbc.password");
		}

	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public OptimizerDatabaseSettings migrate() throws SQLException, LiquibaseException, URISyntaxException {
		final Connection c = DriverManager.getConnection(jdbcUrl);
		final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
		final ClassLoaderResourceAccessor resourceAccesor = new ClassLoaderResourceAccessor();
		final String packagePath = STSC_OPTIMIZER_CHANGELOG.getClass().getPackage().getName().replace('.', '/');
		final Liquibase liquibase = new Liquibase(packagePath + "/" + STSC_OPTIMIZER_CHANGELOG.getDbChangelogName(), resourceAccesor, database);
		liquibase.update((String) null);
		liquibase.validate();
		database.commit();
		c.commit();
		c.close();
		return this;
	}

	public OptimizerDatabaseSettings dropAll() throws SQLException, LiquibaseException, URISyntaxException {
		final Connection c = DriverManager.getConnection(jdbcUrl);
		final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
		final ClassLoaderResourceAccessor resourceAccesor = new ClassLoaderResourceAccessor();
		final String packagePath = STSC_OPTIMIZER_CHANGELOG.getClass().getPackage().getName().replace('.', '/');
		final Liquibase liquibase = new Liquibase(packagePath + "/" + STSC_OPTIMIZER_CHANGELOG.getDbChangelogName(), resourceAccesor, database);
		liquibase.dropAll();
		liquibase.validate();
		database.commit();
		c.commit();
		c.close();
		return this;
	}

}
