/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.adapter;

import java.util.List;

import com.dell.isg.smi.commons.model.server.firmware.ComputerSystem;
import com.dell.isg.smi.commons.model.server.firmware.SoftwareIdentity;

/**
 * @author rahman.muhammad
 *
 */
public interface IFirmwareAdapter {

    public List<SoftwareIdentity> getFirmwareInventory(String address, String userName, String password) throws Exception;
}
