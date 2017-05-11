package hpe.union.zj.service;

import hpe.union.zj.db.DBConnectionManager;
import hpe.union.zj.pojo.Role;
import hpe.union.zj.util.AllConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

	public List<Role> getAll() throws Exception {
		Connection con = null;
		PreparedStatement state = null;
		try {
			con = DBConnectionManager.getConnection();
			con.setAutoCommit(false);
			state = con.prepareStatement(AllConstants.GET_ROLE_ALL);
			ResultSet result = state.executeQuery();
			List<Role> list = new ArrayList<Role>();
			while (result.next()) {
				Role role = new Role();
				int id = result.getInt("id");
				role.setId(id);
				String name = result.getString("name");
				if (StringUtils.isNotEmpty(name))
					role.setName(name);
				String desc = result.getString("description");
				if (StringUtils.isNotEmpty(desc))
					role.setDescription(desc);
				list.add(role);
			}
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null)
				con.close();
		}
	}

}
