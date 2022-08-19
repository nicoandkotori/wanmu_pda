package com.myapplication.dto.mo;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MoOrderInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 订单子表id
     */
    private String moDetailId;

    /**
     * 屠宰批号
     */
    private String batchCode;

    /**
     * 状态
     */
    private String statusId;
    private String statusDesc;

    /**
     * 耳标
     */
    private String cardId;
    private String cardCode;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 称重时间
     */
    private Date weighDate;

    /**
     * 供应商(耳标表信息)
     */
    private String venName;

    /**
     * 订单日期（订单子表）
     */
    private Date vouchDate;
    private Date vouchDateStart;
    private Date vouchDateEnd;

    /**
     * 单据号（订单主表）
     */
    private String vouchCode;

    /**
     * 货品名称（订单子表）
     */
    private String invName;

    /**
     * 计划数量（订单子表）
     * @return
     */
    private BigDecimal planQty;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoDetailId() {
        return moDetailId;
    }

    public void setMoDetailId(String moDetailId) {
        this.moDetailId = moDetailId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Date getWeighDate() {
        return weighDate;
    }

    public void setWeighDate(Date weighDate) {
        this.weighDate = weighDate;
    }

    public String getVenName() {
        return venName;
    }

    public void setVenName(String venName) {
        this.venName = venName;
    }

    public Date getVouchDate() {
        return vouchDate;
    }

    public void setVouchDate(Date vouchDate) {
        this.vouchDate = vouchDate;
    }

    public Date getVouchDateStart() {
        return vouchDateStart;
    }

    public void setVouchDateStart(Date vouchDateStart) {
        this.vouchDateStart = vouchDateStart;
    }

    public Date getVouchDateEnd() {
        return vouchDateEnd;
    }

    public void setVouchDateEnd(Date vouchDateEnd) {
        this.vouchDateEnd = vouchDateEnd;
    }

    public String getVouchCode() {
        return vouchCode;
    }

    public void setVouchCode(String vouchCode) {
        this.vouchCode = vouchCode;
    }

    public String getInvName() {
        return invName;
    }

    public void setInvName(String invName) {
        this.invName = invName;
    }

    public BigDecimal getPlanQty() {
        return planQty;
    }

    public void setPlanQty(BigDecimal planQty) {
        this.planQty = planQty;
    }
}
