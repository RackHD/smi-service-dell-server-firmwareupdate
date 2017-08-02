/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.adapter.util;

import com.dell.isg.smi.firmwareupdate.adapter.model.ProxyConfig;
import com.dell.sm.downloader.DSMAuthenticationParameters;

/**
 * @author rahman.muhammad
 *
 */
public class ProtocolCredentialUtil {

    public static DSMAuthenticationParameters getDsmAuthentication(ProxyConfig proxyConfig) throws Exception {
        DSMAuthenticationParameters dsmAuth = null;
        if (proxyConfig == null)
            throw new Exception("Not valid login info");

        dsmAuth = new DSMAuthenticationParameters();
        dsmAuth.setUserName(proxyConfig.getUserName());
        dsmAuth.setPassword(proxyConfig.getPassword());

        return dsmAuth;
    }


    public static DSMAuthenticationParameters getDsmAuthentication(String userName, String password) throws Exception {

        DSMAuthenticationParameters auth = new DSMAuthenticationParameters();
        auth.setUserName(userName);
        auth.setPassword(password);
        return auth;

    }
}
