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
import stsc.changelogs.yahoo_downloader.YahooDownloaderChangelog;
import stsc.config.yahoo_downloader.YahooDownloaderConfig;

public final class YahooDownloaderDatabaseSettings {

	private final static YahooDownloaderConfig YAHOO_DOWNLOADER_CONFIG = new YahooDownloaderConfig();
	private final static YahooDownloaderChangelog YAHOO_DOWNLOADER_CHANGELOG = new YahooDownloaderChangelog();

	private final String jdbcDriver;
	private final String jdbcUrl;
	private final String login;
	private final String password;

	public static YahooDownloaderDatabaseSettings development() throws IOException, URISyntaxException {
		return new YahooDownloaderDatabaseSettings(YAHOO_DOWNLOADER_CONFIG.getDevelopmentConfigFile());
	}

	public static YahooDownloaderDatabaseSettings test() throws IOException, URISyntaxException {
		return new YahooDownloaderDatabaseSettings(YAHOO_DOWNLOADER_CONFIG.getTestConfigFile());
	}

	public YahooDownloaderDatabaseSettings(final String filePath) throws IOException {
		this(new FileInputStream(filePath));
	}

	private YahooDownloaderDatabaseSettings(InputStream sourceInputStream) throws IOException {
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

	public YahooDownloaderDatabaseSettings migrate() throws SQLException, LiquibaseException, URISyntaxException {
		final Connection c = DriverManager.getConnection(jdbcUrl);
		final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));

		final ClassLoaderResourceAccessor resourceAccesor = new ClassLoaderResourceAccessor();
		final String packagePath = YAHOO_DOWNLOADER_CHANGELOG.getClass().getPackage().getName().replace('.', '/');
		final Liquibase liquibase = new Liquibase(packagePath + "/" + YAHOO_DOWNLOADER_CHANGELOG.getDbChangelogName(), resourceAccesor, database);

		liquibase.update((String) null);
		liquibase.validate();
		database.commit();
		c.commit();
		c.close();
		return this;
	}

	public YahooDownloaderDatabaseSettings dropAll() throws SQLException, LiquibaseException, URISyntaxException {
		final Connection c = DriverManager.getConnection(jdbcUrl);
		final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
		final ClassLoaderResourceAccessor resourceAccesor = new ClassLoaderResourceAccessor();
		final String packagePath = YAHOO_DOWNLOADER_CHANGELOG.getClass().getPackage().getName().replace('.', '/');
		final Liquibase liquibase = new Liquibase(packagePath + "/" + YAHOO_DOWNLOADER_CHANGELOG.getDbChangelogName(), resourceAccesor, database);
		liquibase.dropAll();
		liquibase.validate();
		database.commit();
		c.commit();
		c.close();
		return this;
	}

}
