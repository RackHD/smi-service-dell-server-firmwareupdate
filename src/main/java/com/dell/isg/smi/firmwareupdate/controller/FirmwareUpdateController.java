/**
 * Copyright � 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.firmwareupdate.controller;

/**
 * @author rahman.muhammad
 *
 */

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dell.isg.smi.commons.elm.exception.InvalidArgumentsException;
import com.dell.isg.smi.commons.elm.exception.RuntimeCoreException;
import com.dell.isg.smi.commons.model.common.CallBackResponse;
import com.dell.isg.smi.commons.model.server.JobStatus;
import com.dell.isg.smi.commons.model.server.firmware.ApplicableUpdate;
import com.dell.isg.smi.commons.model.server.firmware.ComputerSystem;
import com.dell.isg.smi.commons.model.server.firmware.SoftwareIdentity;
import com.dell.isg.smi.commons.model.server.firmware.request.CompareCatalogRequest;
import com.dell.isg.smi.commons.model.server.firmware.request.CustomRepoRequest;
import com.dell.isg.smi.firmwareupdate.common.Credentials;
import com.dell.isg.smi.firmwareupdate.common.CredentialsConstant;
import com.dell.isg.smi.firmwareupdate.common.Response;
import com.dell.isg.smi.firmwareupdate.controller.model.ApplicableUpdateRequest;
import com.dell.isg.smi.firmwareupdate.controller.model.CatalogComparison;
import com.dell.isg.smi.firmwareupdate.controller.model.DupRequest;
import com.dell.isg.smi.firmwareupdate.controller.model.JobStatusRequest;
import com.dell.isg.smi.firmwareupdate.controller.model.ObmRequestBase;
import com.dell.isg.smi.firmwareupdate.controller.model.UpdateRequest;
import com.dell.isg.smi.firmwareupdate.controller.model.Version;
import com.dell.isg.smi.firmwareupdate.service.IFirmwareService;

@RestController

public class FirmwareUpdateController implements IFirmwareUpdateController {

	@Autowired
	private IFirmwareService firmwareService;

	private static final Logger logger = LoggerFactory.getLogger(FirmwareUpdateController.class.getName());

	/*
	 * webMethod to return installed firmware update Micro services version
	 * 
	 */
	@Override
	public Version version() {
		return new Version();
	}

	/*
	 * Endpoint for installed firmware version on provided server ,
	 * 
	 */
	@Override
	public List<SoftwareIdentity> getFirmwareInventory(@RequestBody ObmRequestBase request) {

		logger.info("FirmwareUpdateController - getFirmwareInventory(..) for server : {}", request.getServerAddress());

		if (isInvalid(request.getServerAddress())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("serverAddress");
			throw exp;
		} else if (isInvalid(request.getUserName())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("userName");
			throw exp;
		} else if (isInvalid(request.getPassword())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("password");
			throw exp;
		}

		try {

			logger.info("requesting firmware version for server: {}", request.getServerAddress());
			Credentials cred = new Credentials();
			cred.setAddress(request.getServerAddress());
			cred.setUserName(request.getUserName());
			cred.setPassword(request.getPassword());
			return firmwareService.getFirmwareInventory(cred.getAddress(), cred.getUserName(), cred.getPassword());
		} catch (Exception exp) {
			logger.error(exp.toString());
			RuntimeCoreException runExp = new RuntimeCoreException(exp);
			throw runExp;
		}

	}

	/*
	 * Rest API for returning all updateable component inventory for a provided
	 * system
	 * 
	 */
	@Override
	public List<ComputerSystem> getUpdateableComponentInventory(@RequestBody ObmRequestBase request) {

		logger.info("FirmwareUpdateController - getUpdateableComponentInventory(..) for server : {}",
				request.getServerAddress());

		if (isInvalid(request.getServerAddress())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("serverAddress");
			throw exp;
		} else if (isInvalid(request.getUserName())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("userName");
			throw exp;
		} else if (isInvalid(request.getPassword())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("password");
			throw exp;
		}

		try {
			logger.info("requesting firmware version for server: {} ", request.getServerAddress());
			Credentials cred = new Credentials();
			cred.setAddress(request.getServerAddress());
			cred.setUserName(request.getUserName());
			cred.setPassword(request.getPassword());
			return firmwareService.getUpdateableComponentInventory(cred.getAddress(), cred.getUserName(),
					cred.getPassword());

		} catch (Exception exp) {
			logger.error(exp.toString());
			RuntimeCoreException runExp = new RuntimeCoreException(exp);
			throw runExp;
		}

	}

	/*
	 * API for returning all applicable updates for a system
	 * 
	 */

	@Override
	public List<ApplicableUpdate> getApplicableUpdates(@RequestBody ApplicableUpdateRequest request) {

		List<ApplicableUpdate> applicableUpdates = new ArrayList<ApplicableUpdate>();
		logger.info("FirmwareUpdateController - getApplicableUpdates(..) ");

		if (request == null) {
			InvalidArgumentsException exp = new InvalidArgumentsException("request");
			throw exp;
		}

		boolean isCatalogValid = false;
		if (request.getCatalogPath() != null) {
			String extension = "";
			int i = request.getCatalogPath().lastIndexOf('.');
			if (i > 0) {
				extension = request.getCatalogPath().substring(i + 1);
			}
			if (extension.equals("xml")) {
				isCatalogValid = true;
			}
		}

		if (!isCatalogValid) {
			InvalidArgumentsException exp = new InvalidArgumentsException("catalogPath");
			throw exp;
		}

		try {
			Credentials cred = new Credentials();
			cred.setAddress(request.getServerAddress());
			cred.setUserName(request.getUserName());
			cred.setPassword(request.getPassword());
			applicableUpdates = firmwareService.getApplicableUpdates(cred, request.getUpdateableComponentInventory(),
					request.getCatalogPath(), request.getType());

		} catch (Exception exp) {
			logger.error(exp.toString());
			RuntimeCoreException runExp = new RuntimeCoreException(exp);
			throw runExp;
		}
		return applicableUpdates;
	}

	/*
	 * API for creating custom catalog based on provided applicable updates
	 */
	@Override

	public Response createBaseLineRepository(@RequestBody CustomRepoRequest request) {

		Response response = new Response();

		logger.info("firmwareUpdate controller - createBaseLineRepository(..)");

		if (request == null) {
			InvalidArgumentsException exp = new InvalidArgumentsException("request");
			throw exp;
		} else if (request.getUpdates() == null) {
			InvalidArgumentsException exp = new InvalidArgumentsException("updates");
			throw exp;
		} else if (request.getCatalogFilePath() == null) {
			InvalidArgumentsException exp = new InvalidArgumentsException("catalogPath");
			throw exp;
		} else if (request.getTargetFilePath() == null) {
			InvalidArgumentsException exp = new InvalidArgumentsException("targetFilePath");
			throw exp;
		}

		try {
			firmwareService.createBaseLineRepository(request.getUpdates(), request.getCatalogFilePath(),
					request.getTargetFilePath());
			response.setId("0");
			response.setName("createCustomeRepository");
			response.setStatus("Completed");
			response.setDescription("Firmware update on Dell PowerEdge Server required a repository of DUPs");
		} catch (Exception exp) {
			logger.error(exp.toString());
			RuntimeCoreException runExp = new RuntimeCoreException(exp);
			throw runExp;
		}

		return response;
	}

	/*
	 * API for comparing two catalogs
	 */
	@Override

	public CatalogComparison CompareCatalogs(@RequestBody CompareCatalogRequest request) {

		int returnCode = -1;
		String EMPTY_STRING = "";
		if (request == null) {
			InvalidArgumentsException exp = new InvalidArgumentsException("request");
			throw exp;
		} else if (EMPTY_STRING.equals(request.getSourceCatalog())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("sourceCatalog");
			throw exp;
		} else if (EMPTY_STRING.equals(request.getSourceCatalog())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("targetCatalog");
			throw exp;
		}

		try {
			returnCode = firmwareService.compareCatalogs(request.getSourceCatalog(), request.getTargetCatalog());

		} catch (Exception exp) {
			logger.error(exp.toString());
			RuntimeCoreException runExp = new RuntimeCoreException(exp);
			throw runExp;
		}
		CatalogComparison cmp = new CatalogComparison();
		cmp.setStatus(String.valueOf(returnCode));
		return cmp;
	}

	/*
	 * Allows to download file from specified location, to the target location.
	 * 
	 * @param sourceType - value for source type e.g NFS, CIFS, FTP etc
	 * 
	 * @param fileUrl - file location to download
	 * 
	 * @param targetLocation - where the file will be downloaded
	 */

	@Override
	public Response downloadFile(@RequestParam(value = "fileName", defaultValue = "") String fileName,
			@RequestParam(value = "fileUrl", defaultValue = "") String fileUrl,
			@RequestParam(value = "targetLocation", defaultValue = "") String targetLocation) {

		logger.info("firmwareUpdate controller - downloadFile(..)");
		Response response = new Response();

		if (fileName.isEmpty()) {
			InvalidArgumentsException exp = new InvalidArgumentsException("fileName");
			throw exp;
		} else if (fileUrl.isEmpty()) {
			InvalidArgumentsException exp = new InvalidArgumentsException("fileUrl");
			throw exp;
		} else if (targetLocation.isEmpty()) {
			InvalidArgumentsException exp = new InvalidArgumentsException("targetLocation");
			throw exp;
		}

		try {
			firmwareService.downloadFile(fileName, fileUrl, targetLocation);
			response.setId("0");
			response.setName(fileName);
			response.setStatus("Download completed successfully");
			response.setDescription("Firmware update on Dell PowerEdge Server required a catalog file");

		} catch (Exception exp) {
			logger.error(exp.toString());
			RuntimeCoreException runExp = new RuntimeCoreException(exp);
			throw runExp;
		}

		return response;
	}

	/*
	 * API for execuring the firmware update on target server
	 */
	@Override
	public List<JobStatus> updateFirmware(@RequestBody UpdateRequest request) {
		logger.info("FirmwareUpdate Controller - update(.....) starts");

		List<JobStatus> jobStatusList = new ArrayList<>();

		if (request == null) {
			InvalidArgumentsException exp = new InvalidArgumentsException("request");
			throw exp;
		}

		try {

			Credentials cred = new Credentials();
			cred.setAddress(request.getServerAddress());
			cred.setUserName(request.getUserName());
			cred.setPassword(request.getPassword());

			List<String> jobs = firmwareService.updateFirmware(cred, request);
			jobs.forEach(item -> {
				JobStatus status = new JobStatus();
				status.setServerAddress(request.getServerAddress());
				status.setJobId(item);
				jobStatusList.add(status);
			});

			if (request.getCallBack() != null && request.getCallBack().getCallbackUri() != null) {
				firmwareService.updateFirmwareViaCallback(cred, request, jobs);

			}

		} catch (Exception exp) {
			logger.error(exp.toString());
			RuntimeCoreException runExp = new RuntimeCoreException(exp);
			throw runExp;
		}

		logger.info("FirmwareUpdate Controller - update(.....) ends ");

		return jobStatusList;

	}

	/*
	 * (non-Javadoc) Test method for CallBack funtions
	 * 
	 */
	@Override
	public int updateFirmwareTest(@RequestBody CallBackResponse request) {

		logger.info("test REST API called succesfully {} ", request.getData());

		return 0;
	}

	@Override
	public List<JobStatus> getJobStatus(@RequestBody JobStatusRequest request) {

		if (request == null) {
			InvalidArgumentsException exp = new InvalidArgumentsException("request");
			throw exp;
		} else if (isInvalid(request.getServerAddress())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("serverAddress");
			throw exp;
		} else if (isInvalid(request.getUserName())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("userName");
			throw exp;
		} else if (isInvalid(request.getPassword())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("password");
			throw exp;
		}

		try {

			Credentials cred = new Credentials();
			cred.setUserName(request.getUserName());
			cred.setPassword(request.getPassword());
			cred.setAddress(request.getServerAddress());
			return firmwareService.getJobStatus(request.getJobs(), cred);
		} catch (Exception exp) {
			logger.error(exp.toString());
			RuntimeCoreException runExp = new RuntimeCoreException(exp);
			throw runExp;
		}

	}

	@Override
	public List<JobStatus> singleFirmwareUpdate(@RequestBody DupRequest request) {
		logger.info("firmware update controller - singleFirmwareUpdate()");

		if (request == null) {
			InvalidArgumentsException exp = new InvalidArgumentsException("request");
			throw exp;
		} else if (isInvalid(request.getServerAddress())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("serverAddress");
			throw exp;
		} else if (isInvalid(request.getComponentId())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("componentId");
			throw exp;
		} else if (isInvalid(request.getFileName())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("fileName");
			throw exp;
		} else if (isInvalid(request.getPath())) {
			InvalidArgumentsException exp = new InvalidArgumentsException("path");
			throw exp;
		}

		Credentials cred = CredentialsConstant.getCredentials();
		cred.setAddress(request.getServerAddress());
		try {
			return firmwareService.updateDupFirmware(request.getServerAddress(), request.getComponentId(),
					request.getPath(), request.getFileName(), cred);

		} catch (Exception exp) {
			logger.error(exp.toString());
			RuntimeCoreException runExp = new RuntimeCoreException(exp);
			throw runExp;
		}
	}

	private boolean isInvalid(String data) {

		return data == null || data.isEmpty();
	}

}
