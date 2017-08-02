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
public class CredentialsConstant {

    public static Credentials getCredentials() {

        Credentials cred = new Credentials("", "root", "calvin");

        return cred;
    }

}
