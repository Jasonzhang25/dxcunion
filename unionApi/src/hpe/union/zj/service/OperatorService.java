package hpe.union.zj.service;

import hpe.union.zj.controller.InterfaceController;
import hpe.union.zj.db.DBConnectionManager;
import hpe.union.zj.pojo.Operator;
import hpe.union.zj.pojo.Role;
import hpe.union.zj.util.AllConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class OperatorService {

	Logger logger = Logger.getLogger(OperatorService.class);

	public List<Operator> getAll() throws Exception {
		Connection con = null;
		PreparedStatement state = null;
		try {
			con = DBConnectionManager.getConnection();
			state = con.prepareStatement(AllConstants.GET_OPE_ALL);
			ResultSet result = state.executeQuery();
			List<Operator> list = new ArrayList<Operator>();
			list = getOpeFromResult(result);
			if (con != null)
				con.close();
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
	}

	public void createOpe(Operator ope) throws Exception {
		Connection con = null;
		PreparedStatement state = null;
		try {
			con = DBConnectionManager.getConnection();
			state = con.prepareStatement(AllConstants.CREATE_OPE,
					PreparedStatement.RETURN_GENERATED_KEYS);
			state.setString(1, ope.getEid());
			state.setString(2, ope.getFirstname());
			state.setString(3, ope.getLastname());
			state.setString(4, ope.getEmail());
			state.setString(5, ope.getStatus());
			state.setString(6, ope.getComments());
			state.setString(7, ope.getAddedby());
			state.setDate(8, new java.sql.Date(new Date().getTime()));
			state.executeUpdate();
			ResultSet rs = state.getGeneratedKeys();
			int id = 0;// 保存生成的ID
			if (rs != null && rs.next()) {
				id = rs.getInt(1);
			}

			if (con != null)
				con.close();

			for (Role role : ope.getRoles()) {
				con = DBConnectionManager.getConnection();
				state = con.prepareStatement(AllConstants.CREATE_OPE_ROLE);
				state.setInt(1, id);
				state.setInt(2, role.getId());
				state.execute();
				if (con != null)
					con.close();
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
	}

	public void removeOperator(int id) throws Exception {
		Connection con = null;
		PreparedStatement state = null;
		try {
			con = DBConnectionManager.getConnection();
			state = con.prepareStatement(AllConstants.REMOVE_OPE);
			state.setInt(1, id);
			state.execute();
			if (con != null)
				con.close();
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
	}

	public Operator getOpeByEmail(String email) throws Exception {
		Connection con = null;
		PreparedStatement state = null;
		Operator ope = new Operator();
		try {
			con = DBConnectionManager.getConnection();
			state = con.prepareStatement(AllConstants.GET_OPE_BY_EMAIL);
			state.setString(1, email);
			ResultSet result = state.executeQuery();
			

			List<Operator> list = getOpeFromResult(result);
			if (list != null & list.size() > 0) {
				ope = list.get(0);
			}
			
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
		return ope;
	}

	public void updateOpe(Operator ope) throws Exception {
		Connection con = null;
		PreparedStatement state = null;
		try {
			con = DBConnectionManager.getConnection();
			state = con.prepareStatement(AllConstants.UPDATE_OPE);
			state.setString(1, ope.getEid());
			state.setString(2, ope.getFirstname());
			state.setString(3, ope.getLastname());
			state.setString(4, ope.getEmail());
			state.setString(5, ope.getStatus());
			state.setString(6, ope.getComments());
			state.setString(7, ope.getAddedby());
			state.setInt(8, ope.getId());
			state.execute();
			if (con != null)
				con.close();

			con = DBConnectionManager.getConnection();
			state = con.prepareStatement(AllConstants.DELETE_ROLE_FOR_OPE);
			state.setInt(1, ope.getId());
			logger.info("delete role for ope, sql = "
					+ AllConstants.DELETE_ROLE_FOR_OPE + " user_id = "
					+ ope.getId());
			state.execute();
			if (con != null)
				con.close();

			for (Role role : ope.getRoles()) {
				con = DBConnectionManager.getConnection();
				state = con.prepareStatement(AllConstants.CREATE_OPE_ROLE);
				state.setInt(1, ope.getId());
				state.setInt(2, role.getId());
				logger.info("create role for ope, sql = "
						+ AllConstants.CREATE_OPE_ROLE + " user_id = "
						+ ope.getId() + " role_id = " + role.getId());
				state.execute();
				if (con != null)
					con.close();
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
	}

	public Operator getOpeById(int id) throws Exception {
		Connection con = null;
		PreparedStatement state = null;
		try {
			con = DBConnectionManager.getConnection();
			state = con.prepareStatement(AllConstants.GET_OPE_BY_ID);
			state.setInt(1, id);
			ResultSet result = state.executeQuery();
			Operator ope = new Operator();
			List<Operator> list = getOpeFromResult(result);
			if (con != null)
				con.close();
			if (list != null & list.size() > 0) {
				ope = list.get(0);
			}
			return ope;
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
	}

	public List<Operator> getOpeByCriteria(Operator ope) throws Exception {
		Connection con = null;
		PreparedStatement state = null;
		try {
			con = DBConnectionManager.getConnection();
			String sql = AllConstants.GET_OPE_BY_CRITERIA;

			if (StringUtils.isNotEmpty(ope.getEid())) {
				sql = sql + " AND eid = ?";
			}
			if (StringUtils.isNotEmpty(ope.getEmail())) {
				sql = sql + " AND email = ?";
			}
			if (StringUtils.isNotEmpty(ope.getAddedby())) {
				sql = sql + " AND addedby = ?";
			}
			state = con.prepareStatement(sql);
			int index = 1;
			if (StringUtils.isNotEmpty(ope.getEid())) {
				state.setString(index, ope.getEid());
				index++;
			}
			if (StringUtils.isNotEmpty(ope.getEmail())) {
				state.setString(index, ope.getEmail());
				index++;
			}
			if (StringUtils.isNotEmpty(ope.getAddedby())) {
				state.setString(index, ope.getAddedby());
			}
			ResultSet result = state.executeQuery();
			List<Operator> list = getOpeFromResult(result);
			if (con != null)
				con.close();
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
	}

	private List<Operator> getOpeFromResult(ResultSet result) throws Exception {
		List<Operator> list = new ArrayList<Operator>();
		while (result.next()) {
			Operator ope = new Operator();
			String lastname = result.getString("lastname");
			if (StringUtils.isNotEmpty(lastname))
				ope.setLastname(lastname);
			String firstname = result.getString("firstname");
			if (StringUtils.isNotEmpty(firstname))
				ope.setFirstname(firstname);
			String email = result.getString("email");
			if (StringUtils.isNotEmpty(email))
				ope.setEmail(email);
			String status = result.getString("status");
			if (StringUtils.isNotEmpty(status))
				ope.setStatus(status);
			String comments = result.getString("comments");
			if (StringUtils.isNotEmpty(comments))
				ope.setComments(comments);
			String addedby = result.getString("addedby");
			if (StringUtils.isNotEmpty(addedby))
				ope.setAddedby(addedby);
			Date addedon = result.getDate("addedon");
			ope.setAddedon(addedon);

			int id = result.getInt("id");
			ope.setId(id);
			String eid = result.getString("eid");
			if (StringUtils.isNotEmpty(eid))
				ope.setEid(eid);

			Connection con = null;
			PreparedStatement state = null;
			try {
				con = DBConnectionManager.getConnection();
				state = con.prepareStatement(AllConstants.GET_ROLE_BY_OPEID);
				state.setInt(1, id);
				ResultSet resultRole = state.executeQuery();
				List<Role> roles = new ArrayList<Role>();
				while (resultRole.next()) {
					Role role = new Role();
					int roleId = resultRole.getInt("id");
					role.setId(roleId);
					String name = resultRole.getString("name");
					if (StringUtils.isNotEmpty(name))
						role.setName(name);
					String description = resultRole.getString("description");
					if (StringUtils.isNotEmpty(description))
						role.setDescription(description);
					roles.add(role);
				}
				ope.setRoles(roles);
				if (con != null)
					con.close();
			} catch (Exception e) {
				throw e;
			} finally {
				if (con != null)
					con.close();
			}

			list.add(ope);
		}
		return list;
	}

}
