/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.adapter;

import java.util.List;

import com.dell.isg.smi.adapter.server.firmware.IServerFirmwareAdapter;
import com.dell.isg.smi.adapter.server.firmware.ServerFirmwareAdapterImpl;
import com.dell.isg.smi.adapter.server.model.WsmanCredentials;
import com.dell.isg.smi.commons.model.server.firmware.SoftwareIdentity;

/**
 * @author rahman.muhammad
 *
 */
public class WsmanFirmwareAdapterImpl implements IFirmwareAdapter {

    @Override
    public List<SoftwareIdentity> getFirmwareInventory(String address, String userName, String password) throws Exception {

        IServerFirmwareAdapter wsmanAdapter = new ServerFirmwareAdapterImpl();
        WsmanCredentials wsmanCredentials = new WsmanCredentials();
        wsmanCredentials.setAddress(address);
        wsmanCredentials.setUserName(userName);
        wsmanCredentials.setPassword(password);
        return wsmanAdapter.getSoftwareIdentity(wsmanCredentials);

    }

}
