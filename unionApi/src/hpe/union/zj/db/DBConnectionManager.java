package hpe.union.zj.db;

import hpe.union.zj.util.AllConstants;
import hpe.union.zj.util.CacheUtils;

import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.log4j.Logger;

public class DBConnectionManager {

	public static DataSource dataSource;

	private static Logger logger = Logger.getLogger(DBConnectionManager.class);

	public static void setupDataSource() throws Exception {
		logger.info("DBConnectionManager.setupDataSource() start");
		Properties config = new Properties();
		config.setProperty("driverClassName",
				CacheUtils.config.getString(AllConstants.DB_DRIVER, ""));
		config.setProperty("url",
				CacheUtils.config.getString(AllConstants.DB_URL, ""));
		config.setProperty("username",
				CacheUtils.config.getString(AllConstants.DB_USERNAME, ""));
		config.setProperty("password",
				CacheUtils.config.getString(AllConstants.DB_PASSWORD, ""));
		config.setProperty("minIdle", "1");
		config.setProperty("maxWait", "2000");
		dataSource = BasicDataSourceFactory.createDataSource(config);
		logger.info("DBConnectionManager.setupDataSource() finished");
	}

	public static Connection getConnection() throws Exception {
		try {
			if (dataSource == null) {
				DBConnectionManager.setupDataSource();
			}
			return dataSource.getConnection();
		} catch (Exception e) {
			logger.error("DBConnectionManager.getConnection()", e);
			throw e;
		}
	}

}
