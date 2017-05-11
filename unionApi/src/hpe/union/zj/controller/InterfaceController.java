package hpe.union.zj.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import hpe.union.zj.pojo.Credentials;
import hpe.union.zj.pojo.Employee;
import hpe.union.zj.pojo.Operator;
import hpe.union.zj.pojo.Result;
import hpe.union.zj.pojo.Role;
import hpe.union.zj.service.EmployeeService;
import hpe.union.zj.service.OperatorService;
import hpe.union.zj.service.RoleService;
import hpe.union.zj.util.AllConstants;
import hpe.union.zj.util.CacheUtils;
import hpe.union.zj.util.JsonUtils;

@Controller
public class InterfaceController {

	Logger logger = Logger.getLogger(InterfaceController.class);

	@Autowired
	private OperatorService operatorService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private RoleService roleService;

	@RequestMapping("gotoUploadPage")
	public String gotoUploadPage(HttpServletRequest request) {
		return "uploadFile";
	}

	@RequestMapping("test")
	public String test(HttpServletRequest request) {
		return "test";
	}

	@ResponseBody
	@RequestMapping("uploadEmpPkg")
	public String uploadEmpPkg(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "file", required = true) MultipartFile file, HttpServletRequest request,
			ModelMap model) throws Exception {
		String fileName = new Date().getTime() + "_" + file.getName();
		String filePath = CacheUtils.config.getString(AllConstants.UPLOADED_FILE_PATH) + fileName;
		try {
			file.transferTo(new File(filePath));
			Result res = employeeService.resetEmployee(filePath, type);
			return JsonUtils.objectToJson(res);
		} catch (Exception e) {
			logger.error("uploadEmpPkg", e);
			throw e;
		}
	}

	@RequestMapping("download")
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		OutputStream os = response.getOutputStream();
		try {
			String type = request.getParameter("type");
			String fileName = request.getRealPath("/") + "file/" + new Date().getTime() + ".xls";
			File file = employeeService.exportAllAndOpe(fileName, type);
			InputStream inStream = new FileInputStream(file);
			response.reset();
			response.setContentType("bin");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			byte[] b = new byte[100];
			int len;
			try {
				while ((len = inStream.read(b)) > 0)
					response.getOutputStream().write(b, 0, len);
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			file.delete();
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	@ResponseBody
	@RequestMapping(value = "getEmpById", method = RequestMethod.POST, headers = { "content-type=application/json",
			"content-type=application/xml" })
	public String getEmpById(@RequestBody Employee emp) throws Exception {
		try {
			Employee empResult = employeeService.getEmpByid(emp.getId());
			return JsonUtils.objectToJson(empResult);
		} catch (Exception e) {
			logger.error("getEmpById", e);
			throw e;
		}
	}

	@ResponseBody
	@RequestMapping(value = "getEmpByCriteria", method = RequestMethod.POST, headers = {
			"content-type=application/json", "content-type=application/xml" })
	public String getEmpByCriteria(@RequestBody Employee emp) throws Exception {
		List<Employee> empList = new ArrayList<Employee>();
		try {
			if (StringUtils.isNotEmpty(emp.getEmail()) || StringUtils.isNotEmpty(emp.getEid())
					|| emp.getOperatorid() != 0) {
				empList = employeeService.getEmpByByCriteria(emp);
				return JsonUtils.objectToJson(empList);
			} else {
				throw new Exception("must has email or employeeNo");
			}

		} catch (Exception e) {
			logger.error("getEmpByCriteria", e);
			throw e;
		}
	}

	@ResponseBody
	@RequestMapping(value = "updateEmp", method = RequestMethod.POST, headers = { "content-type=application/json",
			"content-type=application/xml" })
	public String updateEmp(@RequestBody Employee emp) throws Exception {
		try {
			employeeService.updateEmpById(emp);
			return JsonUtils.objectToJson(new String("update success"));
		} catch (Exception e) {
			logger.error("updateEmpStatus", e);
			throw e;
		}
	}

	@ResponseBody
	@RequestMapping("getRoleAll")
	public String getRoleAll(HttpServletRequest request) throws Exception {
		List<Role> roleList = new ArrayList<Role>();
		try {
			roleList = roleService.getAll();
			return JsonUtils.objectToJson(roleList);
		} catch (Exception e) {
			logger.error("getRoleAll", e);
			throw e;
		}
	}

	@ResponseBody
	@RequestMapping("getOpeAll")
	public String getOpeAll(HttpServletRequest request) throws Exception {
		List<Operator> opeList = new ArrayList<Operator>();
		try {
			opeList = operatorService.getAll();
			return JsonUtils.objectToJson(opeList);
		} catch (Exception e) {
			logger.error("getOpeAll", e);
			throw e;
		}
	}

	@ResponseBody
	@RequestMapping(value = "authorization", method = RequestMethod.POST, headers = { "content-type=application/json",
			"content-type=application/xml" })
	public String authorization(@RequestBody Credentials credentials, HttpServletResponse response) throws Exception {
		Operator resOpe = new Operator();
		try {
			String email = credentials.getUseremail();
			if (StringUtils.isNotEmpty(email)) {
				resOpe = operatorService.getOpeByEmail(email);
				if (resOpe != null) {
					return JsonUtils.objectToJson(resOpe);
				} else {
					response.setStatus(402);
					return JsonUtils.objectToJson("user not exsit");
				}

			} else {
				response.setStatus(402);
				return JsonUtils.objectToJson("no email input");
			}

		} catch (Exception e) {
			logger.error("authentication", e);
			throw e;
		}
	}

	@ResponseBody
	@RequestMapping(value = "authentication", method = RequestMethod.POST, headers = { "content-type=application/json",
			"content-type=application/xml" })
	public String getOpeByEmail(@RequestBody Credentials credentials, HttpServletResponse response) throws Exception {
		Operator resOpe = new Operator();
		try {
			String email = credentials.getUseremail();
			if (StringUtils.isNotEmpty(email)) {
				resOpe = operatorService.getOpeByEmail(email);
				if (resOpe != null) {
					if (authenricate(credentials.getUseremail(), credentials.getPassword())) {
						return JsonUtils.objectToJson(resOpe);
					} else {
						response.setStatus(401);
						return JsonUtils.objectToJson("username or password incorrect");
					}
				} else {
					response.setStatus(402);
					return JsonUtils.objectToJson("user not exsit");
				}

			} else {
				response.setStatus(402);
				return JsonUtils.objectToJson("no email input");
			}

		} catch (Exception e) {
			logger.error("authentication", e);
			throw e;
		}
	}

	public boolean authenricate(String username, String password) {
		String dn = "uid=" + username + ",OU=People,o=hp.com";
		Hashtable<String, String> authEnv = new Hashtable<String, String>();
		String ldapURL = "ldaps://ldap.hp.com";
		authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		authEnv.put(Context.PROVIDER_URL, ldapURL);
		authEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
		authEnv.put(Context.SECURITY_PRINCIPAL, dn);
		authEnv.put(Context.SECURITY_CREDENTIALS, password);
		authEnv.put("java.naming.security.protocol", "ssl");

		try {
			DirContext authContext = new InitialDirContext(authEnv);
			// authContext.b
			return true;
		} catch (NamingException namEx) {
			logger.error("auth", namEx);
			return false;
		}
	}

	@ResponseBody
	@RequestMapping(value = "getOpeByCriteria", method = RequestMethod.POST, headers = {
			"content-type=application/json", "content-type=application/xml" })
	public String getOpeByCriteria(@RequestBody Operator ope) throws Exception {
		List<Operator> opeList = new ArrayList<Operator>();
		try {
			opeList = operatorService.getOpeByCriteria(ope);
			return JsonUtils.objectToJson(opeList);
		} catch (Exception e) {
			logger.error("getOpeByCriteria", e);
			throw e;
		}
	}

	@ResponseBody
	@RequestMapping(value = "getOpeById", method = RequestMethod.POST, headers = { "content-type=application/json",
			"content-type=application/xml" })
	public String getOpeById(@RequestBody Operator ope) throws Exception {
		Operator resOpe = new Operator();
		try {
			resOpe = operatorService.getOpeById(ope.getId());
			return JsonUtils.objectToJson(resOpe);
		} catch (Exception e) {
			logger.error("getOpeById", e);
			throw e;
		}
	}

	@ResponseBody
	@RequestMapping(value = "addOperator", method = RequestMethod.POST, headers = { "content-type=application/json",
			"content-type=application/xml" })
	public String addOperator(@RequestBody Operator ope) throws Exception {
		try {
			operatorService.createOpe(ope);
			return JsonUtils.objectToJson(new String("addOperator success"));
		} catch (Exception e) {
			logger.error("addOperator", e);
			throw e;
		}
	}

	@ResponseBody
	@RequestMapping(value = "updateOperator", method = RequestMethod.POST, headers = { "content-type=application/json",
			"content-type=application/xml" })
	public String updateOperator(@RequestBody Operator ope) throws Exception {
		try {
			operatorService.updateOpe(ope);
			return JsonUtils.objectToJson(new String("update success"));
		} catch (Exception e) {
			logger.error("updateOperator", e);
			throw e;
		}
	}

	@ResponseBody
	@RequestMapping(value = "removeOperator", method = RequestMethod.POST, headers = { "content-type=application/json",
			"content-type=application/xml" })
	public String removeOperator(@RequestBody Operator ope) throws Exception {
		try {
			operatorService.removeOperator(ope.getId());
			return JsonUtils.objectToJson(new String("removeOperator success"));
		} catch (Exception e) {
			logger.error("removeOperator", e);
			throw e;
		}
	}
}
