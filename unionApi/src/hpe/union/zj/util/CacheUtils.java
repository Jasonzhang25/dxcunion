package hpe.union.zj.util;

import java.io.File;
import java.net.MalformedURLException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

public class CacheUtils {

	private static Logger logger = Logger.getLogger(CacheUtils.class);

	public static PropertiesConfiguration config = null;

	public static PropertiesConfiguration attributesMapping = null;

	public static String contextPath = "";

	public static void init(String contextPath) throws ConfigurationException,
			NumberFormatException, MalformedURLException {
		config = new PropertiesConfiguration(contextPath + File.separator
				+ AllConstants.PROPERTIES_FILE_PATH);
		CacheUtils.contextPath = contextPath;
		
		logger.info("init completed");
	}

	public static String getAttributesProperty(String key) {
		try {
			return String.valueOf(attributesMapping.getProperty(key));
		} catch (Exception e) {
			return "";
		}
	}
}
