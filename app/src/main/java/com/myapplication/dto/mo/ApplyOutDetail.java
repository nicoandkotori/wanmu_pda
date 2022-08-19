package com.myapplication.dto.mo;


import java.math.BigDecimal;
import java.util.Date;

public class ApplyOutDetail {
    private static final long serialVersionUID = 1L;

    private String id;

    private String mainId;

    private String planCode;


    private Date planDate;

    private String invCode;

    private String invName;

    private String invStd;

//    private String unit;

    private BigDecimal qty;

    private BigDecimal outQty;

    private Integer rowNo;

    private String materialType;

    private BigDecimal wqty;
    private BigDecimal nowQty;

    public BigDecimal getNowQty() {
        return nowQty;
    }

    public void setNowQty(BigDecimal nowQty) {
        this.nowQty = nowQty;
    }

    public BigDecimal getWqty() {
        return wqty;
    }

    public void setWqty(BigDecimal wqty) {
        this.wqty = wqty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getOutQty() {
        return outQty;
    }

    public void setOutQty(BigDecimal outQty) {
        this.outQty = outQty;
    }

    public Integer getRowNo() {
        return rowNo;
    }

    public void setRowNo(Integer rowNo) {
        this.rowNo = rowNo;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }
}
