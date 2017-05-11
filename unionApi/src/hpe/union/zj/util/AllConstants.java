package hpe.union.zj.util;

public class AllConstants {

	public static final String LOG_PROPERTIES_REF = "conf/log4j.properties";
	public static final String PROPERTIES_FILE_PATH = "WEB-INF/conf/conf.properties";
	public static final String WEB_INF = "WEB-INF";

	public static final String DB_DRIVER = "dbDriver";
	public static final String DB_URL = "dbUrl";
	public static final String DB_USERNAME = "dbUsername";
	public static final String DB_PASSWORD = "dbPassword";

	public static final String UPLOADED_FILE_PATH = "uploadedFilePath";

	// sql
	public static final String EMPLOYEE_DELETE_ALL = "DELETE FROM dbo.zjUnionEmp";
	public static final String CREATE_EMPLOYEE = "INSERT INTO dbo.zjUnionEmp (eid,name,email,status,gender,comments) VALUES (?,?,?,?,?,?)";
	public static final String GET_EMP_BY_ID = "SELECT * FROM dbo.zjUnionEmp WHERE id = ?";
	public static final String UPDATE_EMP_STATUS = "UPDATE dbo.zjUnionEmp SET substitutename =?, operatorid =?, status=?, substituteid=?,submitdate=?  WHERE id = ?";
	public static final String GET_OPE_ALL = "SELECT * FROM dbo.zjUnionOperator";
	public static final String GET_OPE_BY_EMAIL = "SELECT * FROM dbo.zjUnionOperator WHERE email = ? AND status = 'Active'";
	public static final String GET_OPE_BY_ID = "SELECT * FROM dbo.zjUnionOperator WHERE id = ?";
	public static final String CREATE_OPE = "INSERT INTO dbo.zjUnionOperator(eid,firstname,lastname,email,status,comments,addedby,addedon) VALUES (?,?,?,?,?,?,?,?)";
	public static final String REMOVE_OPE = "UPDATE dbo.zjUnionOperator SET status=2 WHERE id=?";
	public static final String GET_EMP_BY_CRITERIA = "SELECT * FROM dbo.zjUnionEmp WHERE 1=1 ";
	public static final String GET_EMP_All = "SELECT * FROM dbo.zjUnionEmp";
	public static final String GET_ROLE_ALL = "SELECT * FROM dbo.zjRoles";
	public static final String UPDATE_OPE = "UPDATE dbo.zjUnionOperator SET eid = ?, firstname =? , lastname=? , email=? , status = ?, comments=?, addedby=? WHERE id=? ";
	public static final String GET_OPE_BY_CRITERIA = "SELECT * FROM dbo.zjUnionOperator WHERE 1=1 ";
	public static final String DELETE_ROLE_FOR_OPE = "DELETE FROM dbo.zjOperatorRole WHERE user_id=?";
	public static final String CREATE_OPE_ROLE = "INSERT INTO dbo.zjOperatorRole VALUES(?,?)";
	public static final String GET_ROLE_BY_OPEID = "SELECT zjRoles.* FROM dbo.zjOperatorRole,zjRoles WHERE zjRoles.id = zjOperatorRole.roles_id AND zjOperatorRole.user_id = ? ";
}
