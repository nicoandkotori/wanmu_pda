package com.myapplication.dto.mo;

/**
 * 四分体信息dto
 *
 * @author mijiahao
 * @date 2022/08/26
 */
public class TetradInfoDTO extends TetradInfo implements Cloneable{

    private String midSectionInfoRfid;


    public String getMidSectionInfoRfid() {
        return midSectionInfoRfid;
    }

    public void setMidSectionInfoRfid(String midSectionInfoRfid) {
        this.midSectionInfoRfid = midSectionInfoRfid;
    }

    @Override
    public TetradInfoDTO clone() throws CloneNotSupportedException {
        return (TetradInfoDTO) super.clone();
    }
}
