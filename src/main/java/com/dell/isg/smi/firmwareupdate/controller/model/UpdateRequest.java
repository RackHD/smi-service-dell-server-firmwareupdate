/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.controller.model;

import com.dell.isg.smi.commons.model.common.CallBackRequest;

/**
 * @author rahman.muhammad
 *
 */
public class UpdateRequest extends ObmRequestBase {
    private String shareAddress;
    private String shareName;
    private String mountPoint;
    private String catalogFileName;
    private int shareType;
    private String shareUserName;
    private String sharePassword;
    private int applyUpdate;
    private String RebootNeeded;
    private CallBackRequest callBack;


    public String getShareAddress() {
        return shareAddress;
    }


    public void setShareAddress(String shareAddress) {
        this.shareAddress = shareAddress;
    }


    public String getShareName() {
        return shareName;
    }


    public void setShareName(String shareName) {
        this.shareName = shareName;
    }


    public String getMountPoint() {
        return mountPoint;
    }


    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
    }


    public String getCatalogFileName() {
        return catalogFileName;
    }


    public void setCatalogFileName(String catalogFileName) {
        this.catalogFileName = catalogFileName;
    }


    public int getShareType() {
        return shareType;
    }


    public void setShareType(int shareType) {
        this.shareType = shareType;
    }


    public String getShareUserName() {
        return shareUserName;
    }


    public void setShareUserName(String shareUserName) {
        this.shareUserName = shareUserName;
    }


    public String getSharePassword() {
        return sharePassword;
    }


    public void setSharePassword(String sharePassword) {
        this.sharePassword = sharePassword;
    }


    public int getApplyUpdate() {
        return applyUpdate;
    }


    public void setApplyUpdate(int applyUpdate) {
        this.applyUpdate = applyUpdate;
    }


    public String getRebootNeeded() {
        return RebootNeeded;
    }


    public void setRebootNeeded(String rebootNeeded) {
        RebootNeeded = rebootNeeded;
    }


    public String getServerAddress() {
        return serverAddress;
    }


    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }


    public CallBackRequest getCallBack() {
        return callBack;
    }


    public void setCallBack(CallBackRequest callBack) {
        this.callBack = callBack;
    }

}
