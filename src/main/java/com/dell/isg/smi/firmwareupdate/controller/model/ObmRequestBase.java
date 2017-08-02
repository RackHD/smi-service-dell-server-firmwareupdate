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
public class ObmRequestBase {
    protected String serverAddress;
    protected String userName;
    protected String password;


    public String getServerAddress() {
        return serverAddress;
    }


    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }
}
