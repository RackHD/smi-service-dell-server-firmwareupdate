/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dell.cm.comparer.DCMCatalog;
import com.dell.cm.comparer.DCMComparer;
import com.dell.cm.comparer.DCMConsiderationEnum;
import com.dell.cm.comparer.DCMUpdateInformation;
import com.dell.cm.inventory.DCMInventory;
import com.dell.cm.repository.DCMApplicator;
import com.dell.cm.repository.DCMRepository;
import com.dell.cm.updateinformationmodel.DCMCriticality;
import com.dell.cm.updateinformationmodel.DCMI18NString;
import com.dell.cm.updateinformationmodel.DCMManifest;
import com.dell.cm.updateinformationmodel.DCMMultiSystemInventory;
import com.dell.cm.updateinformationmodel.DCMSystemInstance;
import com.dell.cm.updateinformationmodel.DCMSystemInstanceCollection;
import com.dell.cm.updateinformationmodel.DCMUpdateableComponent;
import com.dell.cm.updateinformationmodel.DCMVersionInformation;
import com.dell.isg.smi.commons.elm.exception.InvalidArgumentsException;
import com.dell.isg.smi.commons.model.server.JobStatus;
import com.dell.isg.smi.commons.model.server.firmware.ApplicableUpdate;
import com.dell.isg.smi.commons.model.server.firmware.ComputerSystem;
import com.dell.isg.smi.commons.model.server.firmware.SoftwareIdentity;
import com.dell.isg.smi.commons.model.server.firmware.Update;
import com.dell.isg.smi.commons.model.server.firmware.UpdateableComponent;
import com.dell.isg.smi.firmwareupdate.adapter.model.ProtocolEnum;
import com.dell.isg.smi.firmwareupdate.adapter.model.ProxyConfig;
import com.dell.isg.smi.firmwareupdate.adapter.util.ProtocolCredentialUtil;
import com.dell.isg.smi.firmwareupdate.adapter.util.Utility;
import com.dell.isg.smi.firmwareupdate.common.Credentials;
import com.dell.isg.smi.firmwareupdate.common.FirmwareConstants;
import com.dell.isg.smi.firmwareupdate.controller.model.UpdateRequest;
import com.dell.sm.downloader.DSMAuthenticationParameters;
import com.dell.sm.downloader.DSMFTPDownloader;
import com.dell.sm.downloader.DSMProxy;
import com.dell.sm.extracter.DSMGZipFileExtracter;

/**
 * @author rahman.muhammad
 *
 * 
 */
public class DuecAdapterImpl implements IDuecAdapter {

	private static final Logger logger = LoggerFactory.getLogger(DuecAdapterImpl.class.getName());
	private DCMInventory dcmInventory;

	public DCMInventory getDcmInventory() {
		return dcmInventory;
	}

	public void setDcmInventory(DCMInventory dcmInventory) {
		this.dcmInventory = dcmInventory;
	}

	@Override
	public List<SoftwareIdentity> getFirmwareInventory(String address, String userName, String password)
			throws Exception {

		return null;
	}

	public List<ComputerSystem> getUpdateableComponentInventory(String serverAddress, String userName, String password)
			throws Exception {
		logger.info("DuecAdapterImpl - getUpdateableComponent(..)");
		List<ComputerSystem> list = new ArrayList<ComputerSystem>();
		dcmInventory = new DCMInventory();
		DSMAuthenticationParameters auth = new DSMAuthenticationParameters();
		auth.setUserName(userName);
		auth.setPassword(password);
		DCMMultiSystemInventory mSystems = dcmInventory.createEmptyMultiSystemInventory();
		dcmInventory.addWSInventory(serverAddress, auth, mSystems);

		DCMSystemInstanceCollection dcmSystemInstanceCollection = mSystems.getSystemInstanceCollection();
		Collection<DCMSystemInstance> systemInstanceCollection = null;

		if (dcmSystemInstanceCollection != null) {
			systemInstanceCollection = dcmSystemInstanceCollection.getSystemInstances();

		}

		Collection<DCMUpdateableComponent> ucCollection = null;
		ucCollection = mSystems.getUpdateableComponentCollection().getUpdateableComponents();

		ComputerSystem cSystem = new ComputerSystem();

		if (systemInstanceCollection != null && !systemInstanceCollection.isEmpty()) {
			DCMSystemInstance item = null;
			Iterator<DCMSystemInstance> iterator = systemInstanceCollection.iterator();

			while (iterator.hasNext()) {
				item = (DCMSystemInstance) iterator.next();
				cSystem.setDeviceId(item.getServiceTag().toString());
				cSystem.setModel("");
				cSystem.setCollectionTime(item.getCollectionTime().toString());
				list.add(cSystem);
			}

		}

		if (ucCollection != null) {

			UpdateableComponent uComponent = null;
			cSystem = list.get(0);
			for (DCMUpdateableComponent item : ucCollection) {
				uComponent = new UpdateableComponent();
				uComponent.setComponentType(item.getComponentType() == null ? "" : item.getComponentType().toString());
				uComponent.setName(item.getName() == null ? "" : item.getName().getDefaultLocaleString().toString());
				uComponent.setTargetIdentifier(item.getTargetIdentifier());
				cSystem.getUpdateableComponent().add(uComponent);

			}

		}

		return list;

	}

	/*
	 * method return all applicable updates for a given inventory and catalog
	 * file
	 * 
	 */

	@Override
	public List<ApplicableUpdate> getApplicableUpdates(Credentials cred, String inventory, String catalogFile,
			String type) throws Exception {

		logger.info("DuecAdapterImpl - getApplicableUpdates(..)");
		DSMAuthenticationParameters auth = ProtocolCredentialUtil.getDsmAuthentication(cred.getUserName(),
				cred.getPassword());

		List<ApplicableUpdate> applicableUpdatesList = new ArrayList<ApplicableUpdate>();
		ApplicableUpdate systemUpdate = null;

		dcmInventory = new DCMInventory();
		DCMMultiSystemInventory mSystems = dcmInventory.createEmptyMultiSystemInventory();
		//dcmInventory.addWSInventory(cred.getAddress(), auth, mSystems);

		DCMCatalog catalog = new DCMCatalog();
		DCMManifest manifest = null;

		DCMComparer comparer = new DCMComparer();

		Collection<DCMSystemInstance> sysInstanceCollection = Utility.getSystemInstanceCollection(mSystems);
		if (sysInstanceCollection == null || sysInstanceCollection.isEmpty()) {
			InvalidArgumentsException badIpExp = new InvalidArgumentsException(com.dell.isg.smi.commons.elm.model.EnumErrorCode.ENUM_INACCESSIBLE_IP, "serverAddress");
			throw badIpExp;
		}
		HashMap<String, HashMap<DCMVersionInformation, DCMUpdateInformation>> applicableUpdates = null;

		try {
			manifest = catalog.parseCatalog(Utility.getFile(catalogFile));

		} catch (Exception exp) {
			logger.error(exp.getStackTrace().toString());
			// throw new Exception("Error parsing catalog xml file");
			InvalidArgumentsException badCatExp = new InvalidArgumentsException(com.dell.isg.smi.firmwareupdate.exception.ErrorCodeEnum.ENUM_INACCESSIBLE_CATALOG,
					"catalogPath", exp);
			throw badCatExp;
		}

		for (DCMSystemInstance system : sysInstanceCollection) {
			applicableUpdates = comparer.getApplicableUpdates(mSystems, system.getServiceTag().toString(), manifest,
					DCMConsiderationEnum.REPORT_ALL);

			systemUpdate = new ApplicableUpdate();
			systemUpdate.setDeviceId(system.getServiceTag().toString());

			if (null != applicableUpdates) {
				for (HashMap<DCMVersionInformation, DCMUpdateInformation> mapUpdate : applicableUpdates.values()) {
					for (Entry<DCMVersionInformation, DCMUpdateInformation> anUpdate : mapUpdate.entrySet()) {
						DCMVersionInformation versionInfo = anUpdate.getKey();
						DCMUpdateInformation upInfo = anUpdate.getValue();
						if (versionInfo != null && upInfo != null) {
							Update update = Utility.convergeUpdateInfo(manifest, versionInfo, upInfo);
							systemUpdate.getUpdates().add(update);
						}
					}
				}

				applicableUpdatesList.add(systemUpdate);
			}
		}

		return applicableUpdatesList;

	}

	@Override
	public int createBaseLineRepository(List<Update> updates, String catalogPath, String targetPath) throws Exception {
		logger.info("DUEC Adapter - createBaseLineRepository catalogPath= {}  , targetPath {} ", catalogPath,
				targetPath);

		if (updates == null || updates.isEmpty()) {
			throw new Exception("update list is empty");
		}

		File catalogFile = Utility.getFile(catalogPath);
		File targetDir = Utility.getDirectory(targetPath);

		DCMCatalog catalog = new DCMCatalog();
		DCMManifest manifest = catalog.parseCatalog(catalogFile);

		Collection<DCMUpdateInformation> dcmUpdates = new ArrayList<DCMUpdateInformation>();
		DCMUpdateInformation dcmUpdate = null;
		Iterator<Update> iterator = updates.iterator();

		while (iterator.hasNext()) {

			Update updateInfo = (Update) iterator.next();
			if (updateInfo.getUpdateAction().equalsIgnoreCase("UPGRADE")
					|| updateInfo.getUpdateAction().equalsIgnoreCase("DOWNGRADE")) {
				dcmUpdate = new DCMUpdateInformation();
				dcmUpdate.setCriticality(DCMCriticality.getEnumeration(updateInfo.getCriticality()));
				DCMI18NString name = new DCMI18NString();
				name.setDefaultLocaleString(updateInfo.getName());
				dcmUpdate.setName(name);
				dcmUpdate.setPath(updateInfo.getPath());
				dcmUpdate.setUniqueIdentifier(updateInfo.getUniqueIdentifier());
				dcmUpdate.setVersion(updateInfo.getVersion());
				dcmUpdates.add(dcmUpdate);
			}
		}

		DCMRepository dcmRepository = new DCMRepository();

		ProxyConfig proxyConfig = new ProxyConfig();
		DSMAuthenticationParameters auth = ProtocolCredentialUtil.getDsmAuthentication(proxyConfig);
		List<DSMProxy> proxyList = new ArrayList<DSMProxy>();
		int retVal = dcmRepository.createRepository(dcmUpdates, manifest, targetDir, auth, proxyList,
				ProtocolEnum.getDsmProtocolEnum(null));
		return retVal;
	}

	@Override
	public int compareCatalogs(String sourceCatalog, String targetCatalog) throws Exception {
		int returnValue = -1;
		File sourceFile = Utility.getDirectory(sourceCatalog);
		File targetFile = Utility.getFile(targetCatalog);
		DCMCatalog dcmCatalog = new DCMCatalog();
		dcmCatalog.getLatestUpdates(sourceFile, targetFile);

		if (dcmCatalog != null)
			returnValue = 0;

		return returnValue;
	}

	@Override
	public int downloadFile(String fileName, String fileUrl, String targetPath) throws Exception {

		logger.info("DuecAdapter - downloadFile(..)");

		DSMFTPDownloader downloader = new DSMFTPDownloader();
		int result = -1;
		try {
			result = downloader.downloadFile(fileName, targetPath, fileUrl);

			if (fileName.toLowerCase().endsWith(".gz")) {
				this.extractCatalogFile(targetPath + fileName, targetPath);
			}

		} catch (Exception exp) {
			logger.error(exp.toString());
			throw exp;
		}
		return result;
	}

	@Override
	public List<String> updateFirmware(Credentials cred, UpdateRequest request) throws Exception {

		logger.info("DuecFirmwareAdapter -update(..)");

		DSMAuthenticationParameters auth = ProtocolCredentialUtil.getDsmAuthentication(cred.getUserName(),
				cred.getPassword());
		DCMApplicator app = new DCMApplicator(request.getServerAddress(), "443", auth);

		String shareIpAddr = request.getShareAddress();
		String shareName = request.getShareName();
		String catalogFileName = request.getCatalogFileName();
		int shareType = request.getShareType();
		String shareUserName = request.getShareUserName();
		String sharePassword = request.getSharePassword();
		ArrayList<String> jobs = new ArrayList<String>();
		if (sharePassword.isEmpty())
			throw new Exception("Invalid password");

		int applyUpdate = request.getApplyUpdate();
		HashMap<Integer, String> response = null;
		boolean RebootNeeded = (request.getRebootNeeded() == null || request.getRebootNeeded().equalsIgnoreCase("YES"))
				? true : false;
		try {
			response = app.updateFromRepo(shareIpAddr, shareName, null, catalogFileName, shareType, shareUserName,
					sharePassword, applyUpdate, RebootNeeded);
		} catch (Exception exp) {
			logger.error(exp.getMessage());
			throw exp;
		}

		try {
			if (response != null && response.size() > 0) {
				logger.info("service waiting for lifecycle controller jobs....");
				Thread.sleep(FirmwareConstants.WAITE);
				List<String> tmpJobs = app.getJobsList();
				if (tmpJobs != null) {
					jobs = (ArrayList<String>) tmpJobs;
				}

			}
		} catch (Exception exp) {
			logger.error(exp.getMessage());
			throw exp;
		}
		return jobs;
	}

	/*
	 * 
	 * Continue polling for the job unless all jobs are finished OR timeout
	 */

	@Override
	public List<JobStatus> pollJob(Credentials credentials, List<String> jobs) throws Exception {
		List<JobStatus> jobsStatus = null;

		logger.info(" DEUC Adapter - Total jobs for polling ... " + jobs.size());

		DCMApplicator app = this.getDCMApplicator(credentials);

		if (jobs == null || jobs.size() == 0)
			return jobsStatus;

		jobsStatus = this.populateJobList(credentials, jobs);
		int jobsCompleted = 0;
		int TIMEOUT_IN_MINUTES = FirmwareConstants.MINUTE * 15;

		try {

			while (jobsCompleted < jobsStatus.size() && TIMEOUT_IN_MINUTES > 0) {

				for (JobStatus jobStatus : jobsStatus) {

					if (!Utility.isJobDone(jobStatus.getStatus())) {
						this.getJobsStatus(app, jobStatus);
						if (Utility.isJobDone(jobStatus.getStatus())) {
							// jobsCompleted++;
						}
					}
					Thread.sleep(FirmwareConstants.SECOND * 3);
				}

				Thread.sleep(FirmwareConstants.WAITE);
				TIMEOUT_IN_MINUTES = TIMEOUT_IN_MINUTES - FirmwareConstants.WAITE;
			}

		} catch (Exception exp) {
			logger.error(exp.getMessage());
			throw exp;
		}

		return jobsStatus;
	}

	private DCMApplicator getDCMApplicator(Credentials credentials) throws Exception {

		DSMAuthenticationParameters auth = ProtocolCredentialUtil.getDsmAuthentication(credentials.getUserName(),
				credentials.getPassword());
		DCMApplicator app = new DCMApplicator(credentials.getAddress(), "443", auth);
		return app;
	}

	/*
	 * fetch job status from hardware device update each job status in the
	 * provided list
	 */

	private void getJobsStatus(DCMApplicator dcmApplicator, List<JobStatus> jobsStatus) throws Exception {

		if (jobsStatus == null || dcmApplicator == null)
			return;
		String tmpStatus = "";
		for (JobStatus jobStatus : jobsStatus) {
			tmpStatus = dcmApplicator.getStatus(jobStatus.getJobId());
			jobStatus.setStatus(tmpStatus);
			Thread.sleep(FirmwareConstants.SECOND * 1);
		}

	}

	/*
	 * fetch job status from hardware device update job status in the orignal
	 * passed object
	 */

	private void getJobsStatus(DCMApplicator dcmApplicator, JobStatus jobStatus) throws Exception {

		if (jobStatus == null || dcmApplicator == null)
			return;
		jobStatus.setStatus(dcmApplicator.getStatus(jobStatus.getJobId()));

	}

	/*
	 * pre-populate JobStatus with details to consumed by rest of the applicatio
	 * layers
	 * 
	 */
	private List<JobStatus> populateJobList(Credentials credentials, List<String> jobs) {

		List<JobStatus> jobsStatus = new ArrayList<JobStatus>();

		if (jobs == null || jobs.size() == 0) {
			return jobsStatus;
		}
		jobs = jobs.stream().filter(item -> !item.isEmpty()).collect(Collectors.toList());

		JobStatus status = null;
		for (String jobId : jobs) {
			status = new JobStatus();
			status.setJobId(jobId);
			status.setServerAddress(credentials.getAddress());
			status.setStatus("");
			status.setMessage("firmware update job  status mentioned");
			status.setDescription("Frimware update job ");
			jobsStatus.add(status);
		}
		return jobsStatus;
	}

	/*
	 * 
	 * Extract downloaded catalog file Catalog.xml is used by firmware update
	 */

	@Override
	public boolean extractCatalogFile(String catalogFile, String targetLocation) throws Exception {
		logger.info("extracing file {}", catalogFile);

		DSMGZipFileExtracter extractor = new DSMGZipFileExtracter();

		if (catalogFile == null || targetLocation == null) {
			throw new Exception("CatalogFile or targetLocation can't be null");
		}

		boolean extractionResult = extractor.extractGZipFile(catalogFile, targetLocation);
		if (!extractionResult) {
			throw new Exception("CatalogFile extraction failed and the process for firmware update will fail");
		}

		return extractionResult;
	}

	@Override
	public List<JobStatus> getJobStatus(List<String> jobs, Credentials credentials) throws Exception {
		List<JobStatus> jobsStatus = null;
		DCMApplicator app = this.getDCMApplicator(credentials);

		if (jobs == null || jobs.size() == 0) {
			logger.info("Empty job list passed, DUEC will retrieve all current active jobs");
			jobs = (ArrayList<String>) app.getJobsList();
		}
		jobsStatus = this.populateJobList(credentials, jobs);
		this.getJobsStatus(app, jobsStatus);

		return jobsStatus;
	}

	/*
	 * Single DUP firmwre update Return list of all job created along with
	 * recent status on hardware
	 * 
	 */

	@Override
	public List<JobStatus> updateDupFirmware(String server, String component, String path, String fileName,
			Credentials credentials) throws Exception {

		List<JobStatus> jobsStatus = new ArrayList<JobStatus>();
		List<String> jobs = new ArrayList<String>();
		credentials.setAddress(server);
		DCMApplicator app = this.getDCMApplicator(credentials);

		if ((isNullEmpty(server) || isNullEmpty(component) || isNullEmpty(path) || isNullEmpty(fileName))) {
			throw new Exception("Not valid input provided for single DUP firmware update");
		}

		String fullPath = path + fileName;

		HashMap<Integer, List> result = app.updateComponentWithComponentId(fullPath, true, component);

		if (result == null)
			return jobsStatus;

		for (Map.Entry<Integer, List> entry : result.entrySet()) {
			logger.info(entry.getKey() + " " + entry.getValue());
			List jid = entry.getValue();
			Iterator<String> it = jid.iterator();
			while (it.hasNext()) {
				jobs.add(it.next());
			}
		}

		jobsStatus = this.populateJobList(credentials, jobs);
		this.getJobsStatus(app, jobsStatus);

		return jobsStatus;
	}

	private boolean isNullEmpty(String str) {

		return (str == null || str.isEmpty());
	}

}
