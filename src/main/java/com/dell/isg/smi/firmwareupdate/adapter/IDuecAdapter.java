/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.adapter;

import java.util.List;

import com.dell.isg.smi.commons.model.server.JobStatus;
import com.dell.isg.smi.commons.model.server.firmware.ApplicableUpdate;
import com.dell.isg.smi.commons.model.server.firmware.ComputerSystem;
import com.dell.isg.smi.commons.model.server.firmware.Update;
import com.dell.isg.smi.firmwareupdate.common.Credentials;
import com.dell.isg.smi.firmwareupdate.controller.model.JobStatusRequest;
import com.dell.isg.smi.firmwareupdate.controller.model.UpdateRequest;

/**
 * @author rahman.muhammad
 *
 */
public interface IDuecAdapter extends IFirmwareAdapter {

    public List<ComputerSystem> getUpdateableComponentInventory(String serverAddress, String userName, String password) throws Exception;


    public List<ApplicableUpdate> getApplicableUpdates(Credentials cred, String inventory, String catalogFile, String type) throws Exception;


    public int createBaseLineRepository(List<Update> updates, String catalogPath, String targetPath) throws Exception;


    public int compareCatalogs(String sourceCatalog, String targetCatalog) throws Exception;


    public int downloadFile(String fileName, String fileUrl, String targetPath) throws Exception;


    public List<String> updateFirmware(Credentials cred, UpdateRequest request) throws Exception;


    public List<JobStatus> pollJob(Credentials credentials, List<String> jobs) throws Exception;


    public boolean extractCatalogFile(String catalogFile, String targetLocation) throws Exception;


    public List<JobStatus> getJobStatus(List<String> jobs, Credentials credentials) throws Exception;


    public List<JobStatus> updateDupFirmware(String server, String component, String path, String fileName, Credentials credentials) throws Exception;

}
