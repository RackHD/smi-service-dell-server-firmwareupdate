/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.adapter.model;

import com.dell.sm.downloader.DSMProtocolEnum;

/**
 * @author rahman.muhammad
 *
 */
public class ProtocolEnum {

    public static DSMProtocolEnum getDsmProtocolEnum(String protocol) {

        DSMProtocolEnum dsmProtocol;

        if (protocol == null)
            protocol = "";

        switch (protocol) {
        case "FTP":
            dsmProtocol = DSMProtocolEnum.FTP;
            break;
        case "HTTP":
            dsmProtocol = DSMProtocolEnum.HTTP;
            break;
        case "HTTPS":
            dsmProtocol = DSMProtocolEnum.HTTP;
            break;
        case "CIFS":
            dsmProtocol = DSMProtocolEnum.CIFS;
            break;
        default:
            dsmProtocol = DSMProtocolEnum.FTP;
            break;
        }
        return dsmProtocol;
    }
}
