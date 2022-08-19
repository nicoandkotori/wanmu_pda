package com.myapplication.dto.st;

import java.util.Date;

public class AllotMain {

    private Date vouchDate;
    private String id;
    private String outWhCode;
    private String outWhName;

    private String inWhCode;
    private String inWhName;
    private String outDepCode;
    private String outDepName;
    private String inDepCode;
    private String inDepName;
    private String sourceId;
    private String sourceCode;
    private String sourceType;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOutWhCode() {
        return outWhCode;
    }

    public void setOutWhCode(String outWhCode) {
        this.outWhCode = outWhCode;
    }

    public String getOutWhName() {
        return outWhName;
    }

    public void setOutWhName(String outWhName) {
        this.outWhName = outWhName;
    }

    public String getInWhCode() {
        return inWhCode;
    }

    public void setInWhCode(String inWhCode) {
        this.inWhCode = inWhCode;
    }

    public String getInWhName() {
        return inWhName;
    }

    public void setInWhName(String inWhName) {
        this.inWhName = inWhName;
    }

    public Date getVouchDate() {
        return vouchDate;
    }

    public void setVouchDate(Date vouchDate) {
        this.vouchDate = vouchDate;
    }

    public String getOutDepCode() {
        return outDepCode;
    }

    public void setOutDepCode(String outDepCode) {
        this.outDepCode = outDepCode;
    }

    public String getOutDepName() {
        return outDepName;
    }

    public void setOutDepName(String outDepName) {
        this.outDepName = outDepName;
    }

    public String getInDepCode() {
        return inDepCode;
    }

    public void setInDepCode(String inDepCode) {
        this.inDepCode = inDepCode;
    }

    public String getInDepName() {
        return inDepName;
    }

    public void setInDepName(String inDepName) {
        this.inDepName = inDepName;
    }
}
