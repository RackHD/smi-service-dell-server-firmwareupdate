/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dell.isg.smi.commons.model.common.CallBackResponse;
import com.dell.isg.smi.commons.model.common.Defaults;
import com.dell.isg.smi.commons.model.common.InventoryCallbackResponse;
import com.dell.isg.smi.commons.model.common.Options;
import com.dell.isg.smi.commons.model.server.JobStatus;
import com.dell.isg.smi.commons.model.server.firmware.ApplicableUpdate;
import com.dell.isg.smi.commons.model.server.firmware.ComputerSystem;
import com.dell.isg.smi.commons.model.server.firmware.SoftwareIdentity;
import com.dell.isg.smi.commons.model.server.firmware.Update;
import com.dell.isg.smi.firmwareupdate.adapter.util.Utility;
import com.dell.isg.smi.firmwareupdate.common.Credentials;
import com.dell.isg.smi.firmwareupdate.common.CredentialsConstant;
import com.dell.isg.smi.firmwareupdate.common.FirmwareConstants;
import com.dell.isg.smi.firmwareupdate.controller.model.JobStatusRequest;
import com.dell.isg.smi.firmwareupdate.controller.model.UpdateRequest;
import com.dell.isg.smi.firmwareupdate.infrastructure.FirmwareInfrastructureImpl;
import com.dell.isg.smi.firmwareupdate.infrastructure.IFirmwareInfrastructure;
import com.dell.isg.smi.commons.model.server.firmware.ComputerSystem;

/**
 * @author rahman.muhammad
 *
 */
@Component
public class FirmwareServiceImpl implements IFirmwareService {

    private IFirmwareInfrastructure iFirmwareinfrastructure;
    private static final Logger logger = LoggerFactory.getLogger(FirmwareServiceImpl.class.getName());


    public FirmwareServiceImpl() {
        iFirmwareinfrastructure = new FirmwareInfrastructureImpl();
    }


    @Override
    public List<SoftwareIdentity> getFirmwareInventory(String address, String userName, String password) throws Exception {
        logger.info("firmwareupdate service, getFirmwareInventory(..)");

        return iFirmwareinfrastructure.getFirmwareInventory(address, userName, password);

    }


    @Override
    public List<ComputerSystem> getUpdateableComponentInventory(String serverAddress, String userName, String password) throws Exception {
        logger.info("firmwareupdate service - getUpdateableComponentInventory(..)");
        return iFirmwareinfrastructure.getUpdateableComponentInventory(serverAddress, userName, password);

    }


    @Override
    public List<ApplicableUpdate> getApplicableUpdates(Credentials cred, String inventory, String catalogFile, String type) throws Exception {
        logger.info("firmware service - getApplicableUpdates(..)");
        return iFirmwareinfrastructure.getApplicableUpdates(cred, inventory, catalogFile, type);
    }


    @Override
    public int createBaseLineRepository(List<Update> updates, String catalogPath, String targetPath) throws Exception {

        return iFirmwareinfrastructure.createBaseLineRepository(updates, catalogPath, targetPath);
    }


    @Override
    public int compareCatalogs(String sourceCatalog, String targetCatalog) throws Exception {

        return iFirmwareinfrastructure.compareCatalogs(sourceCatalog, targetCatalog);
    }


    @Override
    public int downloadFile(String fileName, String fileUrl, String targetPath) throws Exception {

        return iFirmwareinfrastructure.downloadFile(fileName, fileUrl, targetPath);
    }


    @Override
    public List<String> updateFirmware(Credentials cred, UpdateRequest request) throws Exception {

        return iFirmwareinfrastructure.updateFirmware(cred, request);

    }


    @Async
    public void updateFirmwareViaCallback(Credentials cred, UpdateRequest request, List<String> jobs) throws Exception {
        List<JobStatus> jobsStatus = new ArrayList<JobStatus>();

        if (jobs == null || jobs.size() == 0) {
            logger.info("Empty job list, callback function will not execute");
            return;
        }

        try {
            logger.info("calling callback function " + request.getCallBack().getCallbackUri());
            List<String> tmpJobs = new ArrayList<String>();
            ;
            int TIMEOUT_IN_MINUTES = FirmwareConstants.MINUTE * 15;
            Iterator<String> iterator = null;

            while (jobs.size() > 0 && TIMEOUT_IN_MINUTES > 0) {
                iterator = jobs.iterator();
                while (iterator.hasNext()) {
                    tmpJobs.add(iterator.next());
                    jobsStatus = this.getJobStatus(tmpJobs, cred);
                    if (Utility.isJobDone(jobsStatus.get(0).toString())) {
                        iterator.remove();
                        try {
                            this.executeCallBack(request.getCallBack().getCallbackUri(), jobsStatus);
                        } catch (Exception exp) {
                            logger.error(exp.getMessage());
                        }
                    }
                    tmpJobs.clear();
                    Thread.sleep(FirmwareConstants.SECOND * 5);
                }
                Thread.sleep(FirmwareConstants.WAITE);
                TIMEOUT_IN_MINUTES = TIMEOUT_IN_MINUTES - FirmwareConstants.WAITE;
            }
        }

        catch (Exception exp) {
            logger.error(exp.getMessage());
            throw exp;
        }

    }


    /*
     * API to send back response to provided URI
     */
    private void executeCallBack(String url, List<JobStatus> jobsStatus) throws Exception {

        if (url == null || url.isEmpty()) {
            throw new Exception("Provided URL can't be NULL or Empty");
        }

        InventoryCallbackResponse response = this.getCallBackResponse(jobsStatus);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<InventoryCallbackResponse> entity = new HttpEntity<InventoryCallbackResponse>(response, headers);

        try {
            logger.info("executing callback function");
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
            logger.info("Response Received from endpoint {} ", url);
        } catch (Exception exp) {
            logger.error("Error Sending Response to endpoint {} ", url);
            logger.error(exp.getMessage());

        }

    }


    private InventoryCallbackResponse getCallBackResponse(List<JobStatus> jobsStatus) {

        InventoryCallbackResponse invResponse = new InventoryCallbackResponse();
        Options option = new Options();
        Defaults def = new Defaults();

        invResponse.setName("FirmwareUpdate");
        invResponse.setOptions(option);

        option.setDefaults(def);
        def.setType("WSMAN");
        def.setData(jobsStatus);
        return invResponse;
    }


    @Override
    public List<JobStatus> getJobStatus(List<String> jobs, Credentials credentials) throws Exception {

        return iFirmwareinfrastructure.getJobStatus(jobs, credentials);
    }


    @Override
    public List<JobStatus> updateDupFirmware(String server, String component, String path, String fileName, Credentials credentials) throws Exception {

        return iFirmwareinfrastructure.updateDupFirmware(server, component, path, fileName, credentials);
    }

}
