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
public class DupRequest {

    private String serverAddress;
    private String componentId;
    private String path;
    private String fileName;


    public String getServerAddress() {
        return serverAddress;
    }


    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }


    public String getComponentId() {
        return componentId;
    }


    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }


    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public String getFileName() {
        return fileName;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
