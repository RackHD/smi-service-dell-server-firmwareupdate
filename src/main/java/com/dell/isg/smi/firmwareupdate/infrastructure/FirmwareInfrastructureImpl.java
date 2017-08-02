/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.infrastructure;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dell.isg.smi.commons.model.server.JobStatus;
import com.dell.isg.smi.commons.model.server.firmware.ApplicableUpdate;
import com.dell.isg.smi.commons.model.server.firmware.ComputerSystem;
import com.dell.isg.smi.commons.model.server.firmware.SoftwareIdentity;
import com.dell.isg.smi.commons.model.server.firmware.Update;
import com.dell.isg.smi.firmwareupdate.adapter.DuecAdapterImpl;
import com.dell.isg.smi.firmwareupdate.adapter.IDuecAdapter;
import com.dell.isg.smi.firmwareupdate.adapter.IFirmwareAdapter;
import com.dell.isg.smi.firmwareupdate.adapter.WsmanFirmwareAdapterImpl;
import com.dell.isg.smi.firmwareupdate.common.Credentials;
import com.dell.isg.smi.firmwareupdate.controller.model.UpdateRequest;

/**
 * @author rahman.muhammad
 *
 */

public class FirmwareInfrastructureImpl implements IFirmwareInfrastructure {

    private IFirmwareAdapter firmwareAdapter;
    IDuecAdapter duecAdapter;
    private static final Logger logger = LoggerFactory.getLogger(FirmwareInfrastructureImpl.class);


    public FirmwareInfrastructureImpl() {
        duecAdapter = new DuecAdapterImpl();
        firmwareAdapter = new WsmanFirmwareAdapterImpl();
    }


    @Override
    public List<SoftwareIdentity> getFirmwareInventory(String address, String userName, String password) throws Exception {

        return firmwareAdapter.getFirmwareInventory(address, userName, password);

    }


    @Override
    public List<ComputerSystem> getUpdateableComponentInventory(String serverAddress, String userName, String password) throws Exception {

        return duecAdapter.getUpdateableComponentInventory(serverAddress, userName, password);
    }


    @Override
    public List<ApplicableUpdate> getApplicableUpdates(Credentials cred, String inventory, String catalogFile, String type) throws Exception {

        return duecAdapter.getApplicableUpdates(cred, inventory, catalogFile, type);
    }


    @Override
    public int createBaseLineRepository(List<Update> updates, String catalogPath, String targetPath) throws Exception {
        return duecAdapter.createBaseLineRepository(updates, catalogPath, targetPath);
    }


    @Override
    public int compareCatalogs(String sourceCatalog, String targetCatalog) throws Exception {

        return duecAdapter.compareCatalogs(sourceCatalog, targetCatalog);
    }


    @Override
    public int downloadFile(String fileName, String fileUrl, String targetPath) throws Exception {

        return duecAdapter.downloadFile(fileName, fileUrl, targetPath);
    }


    @Override
    public List<String> updateFirmware(Credentials cred, UpdateRequest request) throws Exception {
        return duecAdapter.updateFirmware(cred, request);
    }


    @Override
    public List<JobStatus> pollJob(Credentials credentials, List<String> jobs) throws Exception {

        return duecAdapter.pollJob(credentials, jobs);
    }


    @Override
    public List<JobStatus> getJobStatus(List<String> jobs, Credentials credentials) throws Exception {

        return duecAdapter.getJobStatus(jobs, credentials);
    }


    @Override
    public List<JobStatus> updateDupFirmware(String server, String component, String path, String fileName, Credentials credentials) throws Exception {

        return duecAdapter.updateDupFirmware(server, component, path, fileName, credentials);
    }

}
