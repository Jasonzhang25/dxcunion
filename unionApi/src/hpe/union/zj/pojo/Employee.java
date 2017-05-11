package hpe.union.zj.pojo;

import java.sql.Timestamp;

public class Employee {

	private String eid;
	private int id;
	private String name;
	private String email;
	private String status;
	private String gender;
	private Operator operator;
	private String substituteid = "";
	private String substitutename = "";
	private int operatorid = 0;
	private String comment;
	private Timestamp submitdate;
	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	
	public String getSubstituteid() {
		return substituteid;
	}
	public void setSubstituteid(String substituteid) {
		this.substituteid = substituteid;
	}
	public String getSubstitutename() {
		return substitutename;
	}
	public void setSubstitutename(String substitutename) {
		this.substitutename = substitutename;
	}
	public int getOperatorid() {
		return operatorid;
	}
	public void setOperatorid(int operatorid) {
		this.operatorid = operatorid;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Timestamp getSubmitdate() {
		return submitdate;
	}
	public void setSubmitdate(Timestamp submitdate) {
		this.submitdate = submitdate;
	}
	
	
	
}
