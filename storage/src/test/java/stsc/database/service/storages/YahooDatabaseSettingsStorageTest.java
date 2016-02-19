package stsc.database.service.storages;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import liquibase.exception.LiquibaseException;

import org.junit.Assert;
import org.junit.Test;

import stsc.database.migrations.YahooDownloaderDatabaseSettings;
import stsc.database.service.schemas.OrmliteYahooDownloaderSettings;
import stsc.database.service.storages.YahooDownloaderDatabaseStorage;

public class YahooDatabaseSettingsStorageTest {

	@Test
	public void testYahooDatabaseSettingsStorage() throws IOException, SQLException, LiquibaseException, InterruptedException, URISyntaxException {
		final YahooDownloaderDatabaseSettings settings = YahooDownloaderDatabaseSettings.test().dropAll().migrate();
		final YahooDownloaderDatabaseStorage storage = new YahooDownloaderDatabaseStorage(settings);
		Assert.assertNotNull(storage);
		{
			final OrmliteYahooDownloaderSettings oyds = new OrmliteYahooDownloaderSettings("yahoo_downloader_test");
			oyds.setThreadAmount(6);
			Assert.assertEquals(1, storage.setSettings(oyds).getNumLinesChanged());
		}
		{
			final OrmliteYahooDownloaderSettings copy = storage.getSettings("yahoo_downloader_test");
			Assert.assertEquals(6, copy.threadAmount());
			Assert.assertEquals(1, storage.setSettings(copy).getNumLinesChanged());
		}
		settings.dropAll();
	}

}
