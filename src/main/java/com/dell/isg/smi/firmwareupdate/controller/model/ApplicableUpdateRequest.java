/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.controller.model;

/**
 * @author rahman.muhammad
 *
 */
public class ApplicableUpdateRequest extends ObmRequestBase {
    private String address;
    private String catalogPath;
    private String type;
    private String updateableComponentInventory;


    public String getCatalogPath() {
        return catalogPath;
    }


    public void setCatalogPath(String catalogPath) {
        this.catalogPath = catalogPath;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getUpdateableComponentInventory() {
        return updateableComponentInventory;
    }


    public void setUpdateableComponentInventory(String updateableComponentInventory) {
        this.updateableComponentInventory = updateableComponentInventory;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }

}
