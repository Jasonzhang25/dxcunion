package hpe.union.zj.servlet;

import hpe.union.zj.db.DBConnectionManager;
import hpe.union.zj.util.AllConstants;
import hpe.union.zj.util.CacheUtils;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class InitServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger;
	private static String contextPath;

	@Override
	public void init() throws ServletException {
		contextPath = getServletContext().getRealPath("/");
		PropertyConfigurator.configure(contextPath + File.separator
				+ AllConstants.WEB_INF + File.separator
				+ AllConstants.LOG_PROPERTIES_REF);
		logger = Logger.getLogger(InitServlet.class);
		try {
			CacheUtils.init(contextPath);
			DBConnectionManager.setupDataSource();
		} catch (Exception e) {
			logger.error("Initialize error", e);
		}
	}

}
