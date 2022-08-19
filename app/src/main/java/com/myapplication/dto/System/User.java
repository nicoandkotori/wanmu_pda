package com.myapplication.dto.System;

import java.util.Date;

public class User {

	private String id;

	private String userCode;

	private String userName;

	private String pwd;

	private String mobile;

	private String depId;//所属部门

	private String depName;

	private Boolean groupId;//是否管理员

	private Boolean isEnable;

	private String lastLoginIp;

	private Date lastLoginDate;

	private String createUser;

	private Date createDate;

	private String updateUser;

	private Date updateDate;

	private String ip;

	private String izVen;//是否供应商

	private String venCode;//供应商编码

	private String venName;//供应商名称

	private String venSymbol;//供应商代号

	private String workCode;
	private Integer izWork;
	private String procedureId;
	private String procedureName;
	private String whCode;

	public String getWhCode() {
		return whCode;
	}

	public void setWhCode(String whCode) {
		this.whCode = whCode;
	}

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	public Integer getIzWork() {
		return izWork;
	}

	public void setIzWork(Integer izWork) {
		this.izWork = izWork;
	}

	public String getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(String procedureId) {
		this.procedureId = procedureId;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public Boolean getEnable() {
		return isEnable;
	}

	public void setEnable(Boolean enable) {
		isEnable = enable;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public Boolean getGroupId() {
		return groupId;
	}

	public void setGroupId(Boolean groupId) {
		this.groupId = groupId;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIzVen() {
		return izVen;
	}

	public void setIzVen(String izVen) {
		this.izVen = izVen;
	}

	public String getVenCode() {
		return venCode;
	}

	public void setVenCode(String venCode) {
		this.venCode = venCode;
	}

	public String getVenName() {
		return venName;
	}

	public void setVenName(String venName) {
		this.venName = venName;
	}

	public String getVenSymbol() {
		return venSymbol;
	}

	public void setVenSymbol(String venSymbol) {
		this.venSymbol = venSymbol;
	}
}