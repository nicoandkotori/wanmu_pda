package com.myapplication.dto.mo;

/**
 * 屠宰信息dto
 *
 * @author mijiahao
 * @date 2022/08/26
 */
public class SlaughterInfoDTO extends SlaughterInfo{

    private String invId ;

    private String invName ;

    public String getInvId() {
        return invId;
    }

    public void setInvId(String invId) {
        this.invId = invId;
    }

    @Override
    public String getInvName() {
        return invName;
    }

    @Override
    public void setInvName(String invName) {
        this.invName = invName;
    }

    @Override
    public void convertToMidSectionInfo(MidSectionInfo midSectionInfo) {
        super.convertToMidSectionInfo(midSectionInfo);
        midSectionInfo.setInvId(invId);
        midSectionInfo.setInvName(invName);
    }
}
