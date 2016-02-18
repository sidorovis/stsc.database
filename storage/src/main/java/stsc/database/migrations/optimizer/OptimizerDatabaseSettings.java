package stsc.database.migrations.optimizer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

public final class OptimizerDatabaseSettings {

	public final static String configFolder = "../config/stsc_optimizer/";
	public final static String dbChangeLog = "../migrations/target/classes/stsc_optimizer/";
	public final static String dbChangeLogFile = "db.changelog.xml";

	private final String jdbcDriver;
	private final String jdbcUrl;
	private final String login;
	private final String password;

	public static OptimizerDatabaseSettings development() throws IOException {
//		stsc.cha
		
		return new OptimizerDatabaseSettings(configFolder + "development.properties");
	}

	public static OptimizerDatabaseSettings test() throws IOException {
		if (new File(configFolder + "test.properties").exists()) {
			return new OptimizerDatabaseSettings(configFolder + "test.properties");
		} else {
			// for tests from another modules we would use settings from resource folder
			return new OptimizerDatabaseSettings(OptimizerDatabaseSettings.class.getResourceAsStream("test.properties"));
		}
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

	public OptimizerDatabaseSettings migrate() throws SQLException, LiquibaseException {
		final Connection c = DriverManager.getConnection(jdbcUrl);
		final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
		final File parentPath = new File(dbChangeLog);
		final Liquibase liquibase = new Liquibase(dbChangeLogFile, new FileSystemResourceAccessor(parentPath.getAbsolutePath()), database);
		liquibase.update((String) null);
		liquibase.validate();
		database.commit();
		c.commit();
		c.close();
		return this;
	}

	public OptimizerDatabaseSettings dropAll() throws SQLException, LiquibaseException {
		final Connection c = DriverManager.getConnection(jdbcUrl);
		final Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
		final File parentPath = new File(dbChangeLog);
		final Liquibase liquibase = new Liquibase(dbChangeLogFile, new FileSystemResourceAccessor(parentPath.getAbsolutePath()), database);
		liquibase.dropAll();
		liquibase.validate();
		database.commit();
		c.commit();
		c.close();
		return this;
	}

}
