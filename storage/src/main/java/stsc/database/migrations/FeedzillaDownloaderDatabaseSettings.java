package stsc.database.migrations;

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
import stsc.changelogs.feedzilla_downloader.FeedzillaDownloaderChangelog;
import stsc.config.feedzilla_downloader.FeedzillaDownloaderConfig;

public final class FeedzillaDownloaderDatabaseSettings {

	private final static FeedzillaDownloaderConfig FEEDZILLA_DOWNLOADER_CONFIG = new FeedzillaDownloaderConfig();
	private final static FeedzillaDownloaderChangelog FEEDZILLA_DOWNLOADER_CHANGELOG = new FeedzillaDownloaderChangelog();

	private final String jdbcDriver;
	private final String jdbcUrl;
	private final String login;
	private final String password;

	public static FeedzillaDownloaderDatabaseSettings development() throws IOException, URISyntaxException {
		return new FeedzillaDownloaderDatabaseSettings(FEEDZILLA_DOWNLOADER_CONFIG.getDevelopmentConfigFile());
	}

	public static FeedzillaDownloaderDatabaseSettings test() throws IOException, URISyntaxException {
		return new FeedzillaDownloaderDatabaseSettings(FEEDZILLA_DOWNLOADER_CONFIG.getTestConfigFile());
	}

	public FeedzillaDownloaderDatabaseSettings(final String filePath) throws IOException {
		this(new FileInputStream(filePath));
	}

	private FeedzillaDownloaderDatabaseSettings(InputStream sourceInputStream) throws IOException {
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

	public FeedzillaDownloaderDatabaseSettings migrate() throws SQLException, LiquibaseException, URISyntaxException {
		final Connection c = DriverManager.getConnection(jdbcUrl);
		final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
		final ClassLoaderResourceAccessor resourceAccesor = new ClassLoaderResourceAccessor();
		String packagePath = FEEDZILLA_DOWNLOADER_CHANGELOG.getClass().getPackage().getName().replace('.', '/');
		final Liquibase liquibase = new Liquibase(packagePath + "/" + FEEDZILLA_DOWNLOADER_CHANGELOG.getDbChangelogName(), resourceAccesor, database);
		liquibase.update((String) null);
		liquibase.validate();
		database.commit();
		c.commit();
		c.close();
		return this;
	}

	public FeedzillaDownloaderDatabaseSettings dropAll() throws SQLException, LiquibaseException, URISyntaxException {
		final Connection c = DriverManager.getConnection(jdbcUrl);
		final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
		final ClassLoaderResourceAccessor resourceAccesor = new ClassLoaderResourceAccessor();
		String packagePath = FEEDZILLA_DOWNLOADER_CHANGELOG.getClass().getPackage().getName().replace('.', '/');
		final Liquibase liquibase = new Liquibase(packagePath + "/" + FEEDZILLA_DOWNLOADER_CHANGELOG.getDbChangelogName(), resourceAccesor, database);
		liquibase.dropAll();
		liquibase.validate();
		database.commit();
		c.commit();
		c.close();
		return this;
	}

}
