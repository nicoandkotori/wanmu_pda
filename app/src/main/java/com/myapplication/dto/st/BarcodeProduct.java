package com.myapplication.dto.st;

import java.math.BigDecimal;
import java.util.Date;


public class BarcodeProduct {

    private String id;

    private String barcode;

    private Date barDate;
    private Date barTime;

    private String barType;

    private String invCode;

    private String invName;

    private String invStd;

    private String invUnit;

    private String invWidth;

    private String invLen;

    private String invGramWeight;

    private String invClassName;

    private String dutyPerson;

    private String invRequire;

    private Integer printCount;

    private Date printDate;

    private BigDecimal barQty;

    private BigDecimal qty;

    private String batch;

    private String rollCode;

    private String whCode;

    private String whName;

    private String workshopId;

    private String workshopName;

    private String statusId;
    private String izOrder;
    private String izIn;

    private String izOut;

    private String izPack;

    private String packCode;

    private String remark;

    private String createUser;

    private Date createDate;

    private String updateUser;

    private Date updateDate;

    private String deleteUser;

    private Date deleteDate;

    private Short izDelete;

    private String source;

    private String sourceId;

    private String planMainId;

    private String planSemiId;

    private String planProductId;

    private String resourceId;
    private String resourceName;


    private Integer knifeNum;
    private Integer rollNum;
    private Integer semiKnifeNum;

    //原始大卷条码id
    private String semiBarcodeId;

    private Integer izProduct;
    private Integer count;

    private String defWhCode;

    private String defWhName;

    private String defDepCode;

    private String defDepName;

    private String recordId;
    private String recordsId;
    private String recordType;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRecordsId() {
        return recordsId;
    }

    public void setRecordsId(String recordsId) {
        this.recordsId = recordsId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getDefWhCode() {
        return defWhCode;
    }

    public void setDefWhCode(String defWhCode) {
        this.defWhCode = defWhCode;
    }

    public String getDefWhName() {
        return defWhName;
    }

    public void setDefWhName(String defWhName) {
        this.defWhName = defWhName;
    }

    public String getDefDepCode() {
        return defDepCode;
    }

    public void setDefDepCode(String defDepCode) {
        this.defDepCode = defDepCode;
    }

    public String getDefDepName() {
        return defDepName;
    }

    public void setDefDepName(String defDepName) {
        this.defDepName = defDepName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Date getBarDate() {
        return barDate;
    }

    public void setBarDate(Date barDate) {
        this.barDate = barDate;
    }

    public Date getBarTime() {
        return barTime;
    }

    public void setBarTime(Date barTime) {
        this.barTime = barTime;
    }

    public String getBarType() {
        return barType;
    }

    public void setBarType(String barType) {
        this.barType = barType;
    }

    public String getInvCode() {
        return invCode;
    }

    public void setInvCode(String invCode) {
        this.invCode = invCode;
    }

    public String getInvName() {
        return invName;
    }

    public void setInvName(String invName) {
        this.invName = invName;
    }

    public String getInvStd() {
        return invStd;
    }

    public void setInvStd(String invStd) {
        this.invStd = invStd;
    }

    public String getInvUnit() {
        return invUnit;
    }

    public void setInvUnit(String invUnit) {
        this.invUnit = invUnit;
    }

    public String getInvWidth() {
        return invWidth;
    }

    public void setInvWidth(String invWidth) {
        this.invWidth = invWidth;
    }

    public String getInvLen() {
        return invLen;
    }

    public void setInvLen(String invLen) {
        this.invLen = invLen;
    }

    public String getInvGramWeight() {
        return invGramWeight;
    }

    public void setInvGramWeight(String invGramWeight) {
        this.invGramWeight = invGramWeight;
    }

    public String getInvClassName() {
        return invClassName;
    }

    public void setInvClassName(String invClassName) {
        this.invClassName = invClassName;
    }

    public String getDutyPerson() {
        return dutyPerson;
    }

    public void setDutyPerson(String dutyPerson) {
        this.dutyPerson = dutyPerson;
    }

    public String getInvRequire() {
        return invRequire;
    }

    public void setInvRequire(String invRequire) {
        this.invRequire = invRequire;
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
    }

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

    public BigDecimal getBarQty() {
        return barQty;
    }

    public void setBarQty(BigDecimal barQty) {
        this.barQty = barQty;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getRollCode() {
        return rollCode;
    }

    public void setRollCode(String rollCode) {
        this.rollCode = rollCode;
    }

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public String getWhName() {
        return whName;
    }

    public void setWhName(String whName) {
        this.whName = whName;
    }

    public String getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(String workshopId) {
        this.workshopId = workshopId;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getIzOrder() {
        return izOrder;
    }

    public void setIzOrder(String izOrder) {
        this.izOrder = izOrder;
    }

    public String getIzIn() {
        return izIn;
    }

    public void setIzIn(String izIn) {
        this.izIn = izIn;
    }

    public String getIzOut() {
        return izOut;
    }

    public void setIzOut(String izOut) {
        this.izOut = izOut;
    }

    public String getIzPack() {
        return izPack;
    }

    public void setIzPack(String izPack) {
        this.izPack = izPack;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(String deleteUser) {
        this.deleteUser = deleteUser;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public Short getIzDelete() {
        return izDelete;
    }

    public void setIzDelete(Short izDelete) {
        this.izDelete = izDelete;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getPlanMainId() {
        return planMainId;
    }

    public void setPlanMainId(String planMainId) {
        this.planMainId = planMainId;
    }

    public String getPlanSemiId() {
        return planSemiId;
    }

    public void setPlanSemiId(String planSemiId) {
        this.planSemiId = planSemiId;
    }

    public String getPlanProductId() {
        return planProductId;
    }

    public void setPlanProductId(String planProductId) {
        this.planProductId = planProductId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getKnifeNum() {
        return knifeNum;
    }

    public void setKnifeNum(Integer knifeNum) {
        this.knifeNum = knifeNum;
    }

    public Integer getRollNum() {
        return rollNum;
    }

    public void setRollNum(Integer rollNum) {
        this.rollNum = rollNum;
    }

    public Integer getSemiKnifeNum() {
        return semiKnifeNum;
    }

    public void setSemiKnifeNum(Integer semiKnifeNum) {
        this.semiKnifeNum = semiKnifeNum;
    }

    public String getSemiBarcodeId() {
        return semiBarcodeId;
    }

    public void setSemiBarcodeId(String semiBarcodeId) {
        this.semiBarcodeId = semiBarcodeId;
    }

    public Integer getIzProduct() {
        return izProduct;
    }

    public void setIzProduct(Integer izProduct) {
        this.izProduct = izProduct;
    }
}