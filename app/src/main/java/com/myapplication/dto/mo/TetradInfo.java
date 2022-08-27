package com.myapplication.dto.mo;

import com.myapplication.utils.StringUrl;
import com.myapplication.utils.StringUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 四分体信息
 *
 * @author mijiahao
 * @date 2022/08/26
 */
public class TetradInfo implements Cloneable{

    private String id;

    /**
     * 订单子表ID
     */
    private String moDetailId;

    /**
     * 屠宰信息表ID
     */
    private String slaughterId;

    /**
     * 批号
     */
    private String batchCode;

    /**
     * 胴体表ID
     */
    private String midsectionId;

    /**
     * 状态ID
     */
    private Integer statusId;

    /**
     * 状态
     */
    private String statusDesc;

    /**
     * 芯片ID
     */
    private String rfid;

    /**
     * 品名 ID
     */
    private String invId;

    /**
     * 品名
     */
    private String invName;

    /**
     * 等级
     */
    private String invGrade;

    /**
     * 部位
     */
    private String invRegion;

    /**
     * 四分体称重重量
     */
    private BigDecimal tetradWeight;

    /**
     * 四分体称重时间
     */
    private Date tetradDate;

    /**
     * 排酸入库时间
     */
    private Date inStockDate;

    /**
     * 排酸出库时间
     */
    private Date outStockDate;

    /**
     * 操作人
     */
    private String operUser;

    /**
     * 操作时间
     */
    private Date operDate;

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

    public String getSlaughterId() {
        return slaughterId;
    }

    public void setSlaughterId(String slaughterId) {
        this.slaughterId = slaughterId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getMidsectionId() {
        return midsectionId;
    }

    public void setMidsectionId(String midsectionId) {
        this.midsectionId = midsectionId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getInvId() {
        return invId;
    }

    public void setInvId(String invId) {
        this.invId = invId;
    }

    public String getInvName() {
        return invName;
    }

    public void setInvName(String invName) {
        this.invName = invName;
    }

    public String getInvGrade() {
        return invGrade;
    }

    public void setInvGrade(String invGrade) {
        this.invGrade = invGrade;
    }

    public String getInvRegion() {
        return invRegion;
    }

    public void setInvRegion(String invRegion) {
        this.invRegion = invRegion;
    }

    public BigDecimal getTetradWeight() {
        return tetradWeight;
    }

    public void setTetradWeight(BigDecimal tetradWeight) {
        this.tetradWeight = tetradWeight;
    }

    public Date getTetradDate() {
        return tetradDate;
    }

    public void setTetradDate(Date tetradDate) {
        this.tetradDate = tetradDate;
    }

    public Date getInStockDate() {
        return inStockDate;
    }

    public void setInStockDate(Date inStockDate) {
        this.inStockDate = inStockDate;
    }

    public Date getOutStockDate() {
        return outStockDate;
    }

    public void setOutStockDate(Date outStockDate) {
        this.outStockDate = outStockDate;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public Date getOperDate() {
        return operDate;
    }

    public void setOperDate(Date operDate) {
        this.operDate = operDate;
    }

    @Override
    public TetradInfo clone() throws CloneNotSupportedException {
        return (TetradInfo) super.clone();
    }

    public void setOperationInfo(){
        this.operUser = StringUrl.GetUser();
        this.operDate = new Date();
    }
}
