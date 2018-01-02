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
public class DupRequest extends ObmRequestBase {

    private String componentId;
    private String path;
    private String fileName;

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
