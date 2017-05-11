package hpe.union.zj.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import hpe.union.zj.db.DBConnectionManager;
import hpe.union.zj.pojo.Employee;
import hpe.union.zj.pojo.Operator;
import hpe.union.zj.pojo.Result;
import hpe.union.zj.util.AllConstants;

@Service
public class EmployeeService {

	Logger logger = Logger.getLogger(EmployeeService.class);
	OperatorService operatorService = new OperatorService();

	public Result resetEmployee(String filePath, String type) throws Exception {
		try {

			List<String> messages = new ArrayList<String>();
			if (type.equalsIgnoreCase("incremental")) {

			} else if (type.equalsIgnoreCase("full")) {
				deleteAllEmp();
			}

			InputStream input = new FileInputStream(filePath);
			Workbook wb = create(input);
			Sheet sheet = wb.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();

			List<Employee> list = new ArrayList<Employee>();

			while (rows.hasNext()) {
				Row row = rows.next();
				if (row.getRowNum() == 0) {
					continue;
				} else {
					Employee emp = new Employee();
					if (row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						DecimalFormat df = new DecimalFormat("0");
						String value = df.format(row.getCell(0).getNumericCellValue());
						emp.setEid(value);
					} else {
						emp.setEid(row.getCell(0).getStringCellValue() + "");
					}
					if (StringUtils.isNotEmpty(row.getCell(1).getStringCellValue())) {
						emp.setName(row.getCell(1).getStringCellValue());
					}
					if (StringUtils.isNotEmpty(row.getCell(2).getStringCellValue())) {
						emp.setEmail(row.getCell(2).getStringCellValue());
					}
					if (StringUtils.isNotEmpty(row.getCell(3).getStringCellValue())) {
						emp.setGender(row.getCell(3).getStringCellValue());
					}
					if (StringUtils.isNotEmpty(row.getCell(4).getStringCellValue())) {
						emp.setComment(row.getCell(4).getStringCellValue());
					}
					emp.setStatus("Unfected");

					if (type.equalsIgnoreCase("incremental")) {
						List<Employee> resultEmps = getEmpByByEid(emp);
						if (resultEmps.size() == 0) {
							list.add(emp);
						} else {
							messages.add(
									"EmployeeNo[" + emp.getEid() + "]  is exsiting in DB, not insert successfully.");
						}
					} else {
						list.add(emp);
					}

				}
			}

			batchInsertEmp(list);

			Result res = new Result();

			if (messages.size() > 0) {
				res.setStatus("failed");
			} else {
				res.setStatus("success");
			}
			res.setMessages(messages);
			return res;
		} catch (Exception ex) {
			logger.error("resetEmployee", ex);
			throw ex;
		}
	}

	public static Workbook create(InputStream in) throws IOException, InvalidFormatException {
		if (!in.markSupported()) {
			in = new PushbackInputStream(in, 8);
		}
		if (POIFSFileSystem.hasPOIFSHeader(in)) {
			return new HSSFWorkbook(in);
		}
		if (POIXMLDocument.hasOOXMLHeader(in)) {
			return new XSSFWorkbook(OPCPackage.open(in));
		}
		throw new IllegalArgumentException("你的excel版本目前poi解析不了");
	}

	private void deleteAllEmp() throws Exception {
		Connection con = null;
		PreparedStatement deleteState = null;
		try {
			con = DBConnectionManager.getConnection();
			con.setAutoCommit(false);
			deleteState = con.prepareStatement(AllConstants.EMPLOYEE_DELETE_ALL);
			deleteState.execute();
			con.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
	}

	private void batchInsertEmp(List<Employee> list) throws Exception {
		logger.info("insertEmpInBatch has " + list.size() + " employees");
		Connection con = null;
		PreparedStatement state = null;
		try {
			con = DBConnectionManager.getConnection();
			con.setAutoCommit(false);
			state = con.prepareStatement(AllConstants.CREATE_EMPLOYEE);
			for (Employee emp : list) {
				state.setString(1, emp.getEid());
				state.setString(2, emp.getName());
				state.setString(3, emp.getEmail());
				state.setString(4, emp.getStatus());
				state.setString(5, emp.getGender());
				state.setString(6, emp.getComment());
				state.addBatch();
			}
			state.executeBatch();
			con.commit();
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
	}

	public void updateEmpById(Employee emp) throws Exception {
		Connection con = null;
		PreparedStatement state = null;
		try {
			con = DBConnectionManager.getConnection();
			state = con.prepareStatement(AllConstants.UPDATE_EMP_STATUS);
			state.setString(1, emp.getSubstitutename());
			state.setInt(2, emp.getOperatorid());
			state.setString(3, emp.getStatus());
			state.setString(4, emp.getSubstituteid());
			state.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
			state.setInt(6, emp.getId());
			state.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
	}

	public List<Employee> getEmpByByCriteria(Employee emp) throws Exception {
		Connection con = null;
		PreparedStatement state = null;
		try {
			con = DBConnectionManager.getConnection();
			String sql = AllConstants.GET_EMP_BY_CRITERIA;
			if (StringUtils.isNotEmpty(emp.getEmail())) {
				sql = sql + " AND email = ?";
			}
			if (StringUtils.isNotEmpty(emp.getEid())) {
				sql = sql + " AND eid = ?";
			}
			if (emp.getOperatorid() != 0) {
				sql = sql + " AND operatorid = ?";
			}
			state = con.prepareStatement(sql);

			int i = 1;
			if (StringUtils.isNotEmpty(emp.getEmail())) {
				state.setString(i, emp.getEmail());
				i++;
			}
			if (StringUtils.isNotEmpty(emp.getEid())) {
				state.setString(i, emp.getEid());
				i++;
			}
			if (emp.getOperatorid() != 0) {
				state.setInt(i, emp.getOperatorid());
				i++;
			}
			ResultSet result = state.executeQuery();
			List<Employee> list = getEmpFromResult(result, true);
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
	}

	public List<Employee> getEmpByByEid(Employee emp) throws Exception {
		Connection con = null;
		PreparedStatement state = null;
		try {
			con = DBConnectionManager.getConnection();
			String sql = AllConstants.GET_EMP_BY_CRITERIA;
			if (StringUtils.isNotEmpty(emp.getEid())) {
				sql = sql + " AND eid = ?";
			}
			state = con.prepareStatement(sql);

			if (StringUtils.isNotEmpty(emp.getEid())) {
				state.setString(1, emp.getEid());
			}
			ResultSet result = state.executeQuery();
			List<Employee> list = getEmpFromResult(result, true);
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
	}

	private List<Employee> getEmpFromResult(ResultSet result, Boolean hasOpe) throws SQLException, Exception {
		List<Employee> list = new ArrayList<Employee>();
		while (result.next()) {
			Employee emp = new Employee();
			String name = result.getString("name");
			if (StringUtils.isNotEmpty(name))
				emp.setName(name);
			int id = result.getInt("id");
			emp.setId(id);
			String eid = result.getString("eid");
			if (StringUtils.isNotEmpty(eid))
				emp.setEid(eid);
			String email = result.getString("email");
			if (StringUtils.isNotEmpty(email))
				emp.setEmail(email);
			String gender = result.getString("gender");
			if (StringUtils.isNotEmpty(gender))
				emp.setGender(gender);
			String substitutename = result.getString("substitutename");
			if (StringUtils.isNotEmpty(substitutename))
				emp.setSubstitutename(substitutename);
			String substituteid = result.getString("substituteid");
			if (StringUtils.isNotEmpty(substituteid))
				emp.setSubstituteid(substituteid);
			String comment = result.getString("comments");
			if (StringUtils.isNotEmpty(comment))
				emp.setComment(comment);
			Timestamp submitDate = result.getTimestamp("submitdate");
			if (submitDate != null)
				emp.setSubmitdate(submitDate);
			int opeId = result.getInt("operatorid");
			emp.setOperatorid(opeId);

			if (hasOpe) {
				Operator ope = operatorService.getOpeById(opeId);
				emp.setOperator(ope);
			}

			String status = result.getString("status");
			emp.setStatus(status);
			list.add(emp);
		}
		return list;
	}

	public Employee getEmpByid(int id) throws Exception {
		Connection con = null;
		PreparedStatement state = null;
		try {
			con = DBConnectionManager.getConnection();
			state = con.prepareStatement(AllConstants.GET_EMP_BY_ID);
			state.setInt(1, id);
			ResultSet result = state.executeQuery();
			List<Employee> list = getEmpFromResult(result, true);
			Employee emp = new Employee();
			if (list != null && list.size() > 0) {
				emp = list.get(0);
			}
			return emp;
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
	}

	public File exportAllAndOpe(String fileName, String type) throws Exception {
		Connection con = null;
		PreparedStatement state = null;
		try {
			con = DBConnectionManager.getConnection();
			String sql = AllConstants.GET_EMP_All;
			state = con.prepareStatement(sql);
			ResultSet result = state.executeQuery();
			List<Employee> emplist = getEmpFromResult(result, false);

			// FileUtils.copyFile(template, newFile);
			HSSFWorkbook wb = new HSSFWorkbook();
			;
			HSSFSheet sheet = wb.createSheet("emp");

			sheet.setDefaultColumnWidth(15);

			// Create a row and put some cells in it. Rows are 0 based.
			Row row;
			Cell cell;

			int i = 0;
			row = sheet.createRow((short) i);
			cell = row.createCell((short) i++);
			cell.setCellValue("id");
			cell = row.createCell((short) i++);
			cell.setCellValue("eid");
			cell = row.createCell((short) i++);
			cell.setCellValue("name");
			cell = row.createCell((short) i++);
			cell.setCellValue("gender");
			cell = row.createCell((short) i++);
			cell.setCellValue("email");
			cell = row.createCell((short) i++);
			cell.setCellValue("status");
			cell = row.createCell((short) i++);
			cell.setCellValue("substituteid");
			cell = row.createCell((short) i++);
			cell.setCellValue("substitutename");
			cell = row.createCell((short) i++);
			cell.setCellValue("operatorid");
			cell = row.createCell((short) i++);
			cell.setCellValue("comments");
			cell = row.createCell((short) i++);
			cell.setCellValue("submitdate");

			Employee emp = null;
			if (emplist != null) {
				for (int index = 0; index < emplist.size(); index++) {
					emp = emplist.get(index);
					row = sheet.createRow((short) index + 1);
					i = 0;
					cell = row.createCell((short) i++);
					cell.setCellValue(emp.getId());
					cell = row.createCell((short) i++);
					cell.setCellValue(emp.getEid());
					cell = row.createCell((short) i++);
					cell.setCellValue(emp.getName());
					cell = row.createCell((short) i++);
					cell.setCellValue(emp.getGender());
					cell = row.createCell((short) i++);
					cell.setCellValue(emp.getEmail());
					cell = row.createCell((short) i++);
					cell.setCellValue(emp.getStatus());
					cell = row.createCell((short) i++);
					cell.setCellValue(emp.getSubstituteid());
					cell = row.createCell((short) i++);
					cell.setCellValue(emp.getSubstitutename());
					cell = row.createCell((short) i++);
					cell.setCellValue(emp.getOperatorid());
					cell = row.createCell((short) i++);
					cell.setCellValue(emp.getComment());
					if(emp.getSubmitdate()!=null){
						cell = row.createCell((short) i++);
						cell.setCellValue(emp.getSubmitdate().toLocaleString());
					}
				}
			}

			if("all".equalsIgnoreCase(type)){
				List<Operator> opeList = operatorService.getAll();

				sheet = wb.createSheet("ope");

				sheet.setDefaultColumnWidth(15);

				// Create a row and put some cells in it. Rows are 0 based.

				i = 0;
				row = sheet.createRow((short) i);
				cell = row.createCell((short) i++);
				cell.setCellValue("id");
				cell = row.createCell((short) i++);
				cell.setCellValue("eid");
				cell = row.createCell((short) i++);
				cell.setCellValue("firstname");
				cell = row.createCell((short) i++);
				cell.setCellValue("lastname");
				cell = row.createCell((short) i++);
				cell.setCellValue("email");
				cell = row.createCell((short) i++);
				cell.setCellValue("status");
				cell = row.createCell((short) i++);
				cell.setCellValue("comments");
				cell = row.createCell((short) i++);
				cell.setCellValue("addedby");
				cell = row.createCell((short) i++);
				cell.setCellValue("addedon");

				Operator ope = null;
				if (opeList != null) {
					for (int index = 0; index < opeList.size(); index++) {
						ope = opeList.get(index);
						row = sheet.createRow((short) (index + 1));
						i = 0;
						cell = row.createCell((short) i++);
						cell.setCellValue(ope.getId());
						cell = row.createCell((short) i++);
						cell.setCellValue(ope.getEid());
						cell = row.createCell((short) i++);
						cell.setCellValue(ope.getFirstname());
						cell = row.createCell((short) i++);
						cell.setCellValue(ope.getLastname());
						cell = row.createCell((short) i++);
						cell.setCellValue(ope.getEmail());
						cell = row.createCell((short) i++);
						cell.setCellValue(ope.getStatus());
						cell = row.createCell((short) i++);
						cell.setCellValue(ope.getComments());
						cell = row.createCell((short) i++);
						cell.setCellValue(ope.getAddedby());
						cell = row.createCell((short) i++);
						cell.setCellValue(ope.getAddedon() + "");
					}
				}
			}
			

			FileOutputStream fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);

		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
		return new File(fileName);
	}
}
