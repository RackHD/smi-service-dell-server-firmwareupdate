/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.adapter.model;

/**
 * @author rahman.muhammad
 *
 */
public class ProxyConfig {

    private String userName = "";
    private String password = "";
    private String proxyURL;
    private int proxyPort;
    private String proxyProtocol;
    private String proxyUserName;
    private String proxyPassword;
    private String baseLocation = "ftp.dell.com";
    private String targetLocation;
    private String downloadProtocol = "FTP";
    private boolean isDellFtp = true;
    private String domainName;


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


    public String getProxyURL() {
        return proxyURL;
    }


    public void setProxyURL(String proxyURL) {
        this.proxyURL = proxyURL;
    }


    public int getProxyPort() {
        return proxyPort;
    }


    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }


    public String getProxyProtocol() {
        return proxyProtocol;
    }


    public void setProxyProtocol(String proxyProtocol) {
        this.proxyProtocol = proxyProtocol;
    }


    public String getProxyUserName() {
        return proxyUserName;
    }


    public void setProxyUserName(String proxyUserName) {
        this.proxyUserName = proxyUserName;
    }


    public String getProxyPassword() {
        return proxyPassword;
    }


    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }


    public String getBaseLocation() {
        return baseLocation;
    }


    public void setBaseLocation(String baseLocation) {
        this.baseLocation = baseLocation;
    }


    public String getTargetLocation() {
        return targetLocation;
    }


    public void setTargetLocation(String targetLocation) {
        this.targetLocation = targetLocation;
    }


    public String getDownloadProtocol() {
        return downloadProtocol;
    }


    public void setDownloadProtocol(String downloadProtocol) {
        this.downloadProtocol = downloadProtocol;
    }


    public boolean isDellFtp() {
        return isDellFtp;
    }


    public void setDellFtp(boolean isDellFtp) {
        this.isDellFtp = isDellFtp;
    }


    public String getDomainName() {
        return domainName;
    }


    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
