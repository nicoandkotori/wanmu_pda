package com.myapplication.dto.mo;

import java.util.Date;

public class MidSectionInfo {

    /**
     * ID
     */
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
     * 屠宰批号
     */
    private String batchCode;

    /**
     * 状态，枚举，0转挂、1胴体过磅、2流转
     */
    private String statusId,statusDesc;

    /**
     * 芯片ID
     */
    private String rfid;

    /**
     * 产品ID，档案
     */
    private String invId;

    /**
     * 品名
     */
    private String invName;

    /**
     * 等级，枚举
     */
    private String invGrade;

    /**
     * 部位，枚举，L/R
     */
    private String invRegion;

    /**
     * 转挂时间
     */
    private Date transHookDate;

    /**
     * 过磅时间
     */
    private String midsectionDate;

    /**
     * 过磅重量
     */
    private String midsectionWeight;

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

    public Date getTransHookDate() {
        return transHookDate;
    }

    public void setTransHookDate(Date transHookDate) {
        this.transHookDate = transHookDate;
    }

    public String getMidsectionDate() {
        return midsectionDate;
    }

    public void setMidsectionDate(String midsectionDate) {
        this.midsectionDate = midsectionDate;
    }

    public String getMidsectionWeight() {
        return midsectionWeight;
    }

    public void setMidsectionWeight(String midsectionWeight) {
        this.midsectionWeight = midsectionWeight;
    }

    @Override
    public String toString() {
        return "MidSectionInfo{" +
                "id='" + id + '\'' +
                ", moDetailId='" + moDetailId + '\'' +
                ", slaughterId='" + slaughterId + '\'' +
                ", batchCode='" + batchCode + '\'' +
                ", statusId='" + statusId + '\'' +
                ", statusDesc='" + statusDesc + '\'' +
                ", rfid='" + rfid + '\'' +
                ", invId='" + invId + '\'' +
                ", invName='" + invName + '\'' +
                ", invGrade='" + invGrade + '\'' +
                ", invRegion='" + invRegion + '\'' +
                ", transHookDate=" + transHookDate +
                ", midsectionDate='" + midsectionDate + '\'' +
                ", midsectionWeight='" + midsectionWeight + '\'' +
                '}';
    }

    /**
     * 转换四分体信息
     */
    public void convertTetrad(TetradInfo tetradInfo){
        tetradInfo.setBatchCode(batchCode);
        tetradInfo.setMoDetailId(moDetailId);
        tetradInfo.setInvName(invName);
        tetradInfo.setInvId(invId);
        tetradInfo.setSlaughterId(slaughterId);
        tetradInfo.setMidsectionId(id);
        tetradInfo.setInvRegion(invRegion);
    }
}
