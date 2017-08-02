/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.common;

/**
 * @author rahman.muhammad
 *
 */

public class Credentials {

    private String address;
    private String userName;
    private String password;


    public Credentials() {

    }


    public Credentials(String address, String userName, String password) {
        this.address = address;
        this.userName = userName;
        this.password = password;

    }


    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }


    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }


    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }


    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }


    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }


    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
