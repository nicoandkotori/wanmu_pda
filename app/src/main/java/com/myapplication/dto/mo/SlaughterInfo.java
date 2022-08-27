package com.myapplication.dto.mo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mijiahao
 * @TableName mo_slaughter_info
 */

public class SlaughterInfo implements Serializable {

    /**
     * 
     */
    private String id;

    /**
     * 订单子表ID
     */
    private String moDetailId;

    /**
     * 批次
     */
    private String batchCode;

    /**
     * 
     */
    private Integer statusId;

    /**
     * 
     */
    private String statusDesc;

    /**
     * 耳标ID
     */
    private String cardId;

    /**
     * 耳标编号
     */
    private String cardCode;

    /**
     * 
     */
    private BigDecimal slaughterWeight;

    /**
     * 过磅日期
     */
    private Date slaughterDate;

    /**
     * 过磅操作人
     */
    private String slaughterOperName;

    /**
     * 吊挂日期
     */
    private Date hookDate;

    /**
     * 芯片ID
     */
    private String rfid;

    /**
     *
     */
    private String createUser;

    /**
     *
     */
    private Date createDate;

    /**
     *
     */
    private String updateUser;

    /**
     *
     */
    private Date updateDate;

    /**
     *
     */
    private String deleteUser;

    /**
     *
     */
    private Date deleteDate;

    /**
     *
     */
    private Integer izDelete;


    private static final long serialVersionUID = 1L;

    /**
     * 供应商(耳标表信息)
     */
    private String venName;

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

    /**
     * 订单日期（订单主表）
     */
    private Date vouchDate;

    public void transformFromMoOrderInfo(MoOrderInfo orderInfo){
        setCardCode(orderInfo.getCardCode());
        setCardId(orderInfo.getCardId());
        setMoDetailId(orderInfo.getMoDetailId());
        setSlaughterWeight(orderInfo.getWeight());
        setBatchCode(orderInfo.getBatchCode());
        setVouchDate(orderInfo.getVouchDate());
        setPlanQty(orderInfo.getPlanQty());
        setVouchCode(orderInfo.getVouchCode());
        setInvName(orderInfo.getInvName());
    }

    public String getVenName() {
        return venName;
    }

    public void setVenName(String venName) {
        this.venName = venName;
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

    public Date getVouchDate() {
        return vouchDate;
    }

    public void setVouchDate(Date vouchDate) {
        this.vouchDate = vouchDate;
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

    public BigDecimal getSlaughterWeight() {
        return slaughterWeight;
    }

    public void setSlaughterWeight(BigDecimal slaughterWeight) {
        this.slaughterWeight = slaughterWeight;
    }

    public Date getSlaughterDate() {
        return slaughterDate;
    }

    public void setSlaughterDate(Date slaughterDate) {
        this.slaughterDate = slaughterDate;
    }

    public String getSlaughterOperName() {
        return slaughterOperName;
    }

    public void setSlaughterOperName(String slaughterOperName) {
        this.slaughterOperName = slaughterOperName;
    }

    public Date getHookDate() {
        return hookDate;
    }

    public void setHookDate(Date hookDate) {
        this.hookDate = hookDate;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
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

    public Integer getIzDelete() {
        return izDelete;
    }

    public void setIzDelete(Integer izDelete) {
        this.izDelete = izDelete;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SlaughterInfo other = (SlaughterInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMoDetailId() == null ? other.getMoDetailId() == null : this.getMoDetailId().equals(other.getMoDetailId()))
            && (this.getBatchCode() == null ? other.getBatchCode() == null : this.getBatchCode().equals(other.getBatchCode()))
            && (this.getStatusId() == null ? other.getStatusId() == null : this.getStatusId().equals(other.getStatusId()))
            && (this.getStatusDesc() == null ? other.getStatusDesc() == null : this.getStatusDesc().equals(other.getStatusDesc()))
            && (this.getCardId() == null ? other.getCardId() == null : this.getCardId().equals(other.getCardId()))
            && (this.getCardCode() == null ? other.getCardCode() == null : this.getCardCode().equals(other.getCardCode()))
            && (this.getSlaughterWeight() == null ? other.getSlaughterWeight() == null : this.getSlaughterWeight().equals(other.getSlaughterWeight()))
            && (this.getSlaughterDate() == null ? other.getSlaughterDate() == null : this.getSlaughterDate().equals(other.getSlaughterDate()))
            && (this.getSlaughterOperName() == null ? other.getSlaughterOperName() == null : this.getSlaughterOperName().equals(other.getSlaughterOperName()))
            && (this.getHookDate() == null ? other.getHookDate() == null : this.getHookDate().equals(other.getHookDate()))
            && (this.getRfid() == null ? other.getRfid() == null : this.getRfid().equals(other.getRfid()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getUpdateUser() == null ? other.getUpdateUser() == null : this.getUpdateUser().equals(other.getUpdateUser()))
            && (this.getUpdateDate() == null ? other.getUpdateDate() == null : this.getUpdateDate().equals(other.getUpdateDate()))
            && (this.getDeleteUser() == null ? other.getDeleteUser() == null : this.getDeleteUser().equals(other.getDeleteUser()))
            && (this.getDeleteDate() == null ? other.getDeleteDate() == null : this.getDeleteDate().equals(other.getDeleteDate()))
            && (this.getIzDelete() == null ? other.getIzDelete() == null : this.getIzDelete().equals(other.getIzDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMoDetailId() == null) ? 0 : getMoDetailId().hashCode());
        result = prime * result + ((getBatchCode() == null) ? 0 : getBatchCode().hashCode());
        result = prime * result + ((getStatusId() == null) ? 0 : getStatusId().hashCode());
        result = prime * result + ((getStatusDesc() == null) ? 0 : getStatusDesc().hashCode());
        result = prime * result + ((getCardId() == null) ? 0 : getCardId().hashCode());
        result = prime * result + ((getCardCode() == null) ? 0 : getCardCode().hashCode());
        result = prime * result + ((getSlaughterWeight() == null) ? 0 : getSlaughterWeight().hashCode());
        result = prime * result + ((getSlaughterDate() == null) ? 0 : getSlaughterDate().hashCode());
        result = prime * result + ((getSlaughterOperName() == null) ? 0 : getSlaughterOperName().hashCode());
        result = prime * result + ((getHookDate() == null) ? 0 : getHookDate().hashCode());
        result = prime * result + ((getRfid() == null) ? 0 : getRfid().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getUpdateUser() == null) ? 0 : getUpdateUser().hashCode());
        result = prime * result + ((getUpdateDate() == null) ? 0 : getUpdateDate().hashCode());
        result = prime * result + ((getDeleteUser() == null) ? 0 : getDeleteUser().hashCode());
        result = prime * result + ((getDeleteDate() == null) ? 0 : getDeleteDate().hashCode());
        result = prime * result + ((getIzDelete() == null) ? 0 : getIzDelete().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", moDetailId=").append(moDetailId);
        sb.append(", batchCode=").append(batchCode);
        sb.append(", statusId=").append(statusId);
        sb.append(", statusDesc=").append(statusDesc);
        sb.append(", cardId=").append(cardId);
        sb.append(", cardCode=").append(cardCode);
        sb.append(", slaughterWeight=").append(slaughterWeight);
        sb.append(", slaughterDate=").append(slaughterDate);
        sb.append(", slaughterOperName=").append(slaughterOperName);
        sb.append(", hookDate=").append(hookDate);
        sb.append(", rfid=").append(rfid);
        sb.append(", createUser=").append(createUser);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateUser=").append(updateUser);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", deleteUser=").append(deleteUser);
        sb.append(", deleteDate=").append(deleteDate);
        sb.append(", izDelete=").append(izDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    /**
     * 转换为二分体信息
     *
     * @param midSectionInfo 二分体信息
     */
    public void convertToMidSectionInfo(MidSectionInfo midSectionInfo){
        midSectionInfo.setBatchCode(batchCode);
        midSectionInfo.setMoDetailId(moDetailId);
        midSectionInfo.setSlaughterId(id);
    }
}