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
public class Version {

    private String name = "Dell Update Service";
    private String description = "Microservice for Dell PowerEdge firmware update";
    private String version = "1.0";


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }


    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

}
