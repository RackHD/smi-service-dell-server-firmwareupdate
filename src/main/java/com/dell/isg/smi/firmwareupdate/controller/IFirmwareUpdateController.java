/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.dell.isg.smi.commons.model.common.CallBackResponse;
import com.dell.isg.smi.commons.model.server.JobStatus;
import com.dell.isg.smi.commons.model.server.firmware.ApplicableUpdate;
import com.dell.isg.smi.commons.model.server.firmware.ComputerSystem;
import com.dell.isg.smi.commons.model.server.firmware.SoftwareIdentity;
import com.dell.isg.smi.firmwareupdate.controller.model.ApplicableUpdateRequest;
import com.dell.isg.smi.commons.model.server.firmware.request.CompareCatalogRequest;
import com.dell.isg.smi.commons.model.server.firmware.request.CustomRepoRequest;
import com.dell.isg.smi.firmwareupdate.common.Response;
import com.dell.isg.smi.firmwareupdate.common.UriConstants;
import com.dell.isg.smi.firmwareupdate.controller.model.CatalogComparison;
import com.dell.isg.smi.firmwareupdate.controller.model.DupRequest;
import com.dell.isg.smi.firmwareupdate.controller.model.JobStatusRequest;
import com.dell.isg.smi.firmwareupdate.controller.model.ObmRequestBase;
import com.dell.isg.smi.firmwareupdate.controller.model.UpdateRequest;
import com.dell.isg.smi.firmwareupdate.controller.model.Version;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author rahman.muhammad
 *
 */

public interface IFirmwareUpdateController {

    @ApiOperation(value = UriConstants.VERSION, nickname = "DUSE version", notes = "This Webmethod returns the deployed Firmware Update microservice version", response = Version.class)

    @RequestMapping(value = UriConstants.VERSION, method = RequestMethod.GET, produces = "application/json")
    public Version version();


    /******************************************
     * Software Inventory method
     ******************************************/

    @ApiOperation(value = UriConstants.SOFTWARE_COMPONENT_INVENTORY, nickname = "softwareInventory", notes = "This Webmethod returns installed firmware version for all possible components of a selected server, for successful return at least one input parameter is required", response = Version.class)

    @RequestMapping(value = UriConstants.SOFTWARE_COMPONENT_INVENTORY, method = RequestMethod.POST, produces = "application/json")
    public List<SoftwareIdentity> getFirmwareInventory(@RequestBody ObmRequestBase request);


    /*****************************************
     * Updateable component inventory
     ****************************************/

    @ApiOperation(value = UriConstants.UPDATEABLE_COMPONENT_INVENTORY, nickname = "updateableComponentInventory", notes = "This Webmethod returns all updateable components of a server system, webservice can update firmware version for all returned components," + "for successful return at least one valid parameter is required ", response = ComputerSystem.class, responseContainer = "List")

    @RequestMapping(value = UriConstants.UPDATEABLE_COMPONENT_INVENTORY, method = RequestMethod.POST, produces = "application/json")
    public List<ComputerSystem> getUpdateableComponentInventory(@RequestBody ObmRequestBase request);


    /****************************************************
     * Applicable updates for a requested server system
     ***************************************************/

    @ApiOperation(value = UriConstants.APPLICABLE_UPDATES, nickname = "applicableUpdates", response = ApplicableUpdate.class, responseContainer = "List", notes = "This WebMethod returns all applicable updates for a requested server system, this operation will determine if a component needs firmware update or not," + " each component is marked  with respective criticality if update is required")

    @RequestMapping(value = UriConstants.APPLICABLE_UPDATES, method = RequestMethod.POST, produces = "application/json")
    public List<ApplicableUpdate> getApplicableUpdates(@RequestBody ApplicableUpdateRequest request);


    /***************************************
     * Create custom firmware catalog
     **************************************/

    @ApiOperation(value = UriConstants.CUSTOM_CATALOG, nickname = "customeCatalog", response = Response.class, notes = "This  WebMethod creates a custom catalog & repository for firmware images, user pass in a list of all applicable updates in the request body," + " empty list in request will result a firmware repo with no image, micro service contacts ftp.dell.com and downloads DUPS to a network share to update" + " server components by firmware job")

    @RequestMapping(value = UriConstants.CUSTOM_CATALOG, method = RequestMethod.POST, produces = "application/json")
    public Response createBaseLineRepository(@RequestBody CustomRepoRequest request);


    /*****************************
     * Compare two catalog files
     ****************************/

    @ApiOperation(value = UriConstants.COMPARE_CATALOG, nickname = "compareCatalog", response = CatalogComparison.class, notes = "This WebMethod compare two firmware catalogs, user is expected to provide two accessible firmware catalog  files, microservice will compare them and " + " result is returned after comparison")

    @RequestMapping(value = UriConstants.COMPARE_CATALOG, method = RequestMethod.POST, produces = "application/json")
    public CatalogComparison CompareCatalogs(@RequestBody CompareCatalogRequest request);


    /***********************************
     * Download firmware update catalog file from ftp.dell.com or provided location
     **********************************/

    @ApiOperation(value = UriConstants.DOWNLOAD_FILE, nickname = "downloadCatalog", notes = "This Webmethod connects to provided remote location and download requested file e.g. connect ftp.dell.com and download catalog file for firmware update," + " the catalog file is downloaded to a location mentioned in URI as query Parameters, all parameters are required for operation", response = Response.class)

    @ApiImplicitParams({ @ApiImplicitParam(name = "fileName", value = "file name to be downloaded", dataType = "String", paramType = "Query", defaultValue = ""), @ApiImplicitParam(name = "fileUrl", value = "File remote URL", dataType = "String", paramType = "Query", defaultValue = ""), @ApiImplicitParam(name = "targetLocation", value = "save downloaded file to location  ", dataType = "String", paramType = "Query", defaultValue = "") })

    @RequestMapping(value = UriConstants.DOWNLOAD_FILE, method = RequestMethod.GET, produces = "application/json")
    public Response downloadFile(@RequestParam(value = "fileName", defaultValue = "") String fileName, @RequestParam(value = "fileUrl", defaultValue = "") String fileUrl, @RequestParam(value = "targetLocation", defaultValue = "") String targetLocation);


    /***********************************
     * Kickoff firmware update job
     **********************************/
    @ApiOperation(value = UriConstants.UPDATE, nickname = "update", response = JobStatus.class, responseContainer = "List", notes = "This Webmethod kickoff  firmware update job on server provided in the request body, micro service will determine and find the firmware images needed for each component" + " required firmware update,  method returns list of jobs created for each component after iDRAC server initiate LC jobs. ")

    @RequestMapping(value = UriConstants.UPDATE, method = RequestMethod.POST, produces = "application/json")
    public List<JobStatus> updateFirmware(@RequestBody UpdateRequest request);


    /***********************************
     * Kickoff single DUP firmware update job
     **********************************/
    @ApiOperation(value = UriConstants.SINGLE_UPDATE, nickname = "singleUpdate", response = JobStatus.class, responseContainer = "List", notes = "This Webmethod kickoff  Single DUP firmware update job on server provided in the request body, micro service will determine and find the firmware images needed for the component" + " required firmware update,  method returns list of jobs created for each component after iDRAC server initiate LC jobs. ")

    @RequestMapping(value = UriConstants.SINGLE_UPDATE, method = RequestMethod.POST, produces = "application/json")
    public List<JobStatus> singleFirmwareUpdate(@RequestBody DupRequest request);


    /***********************************
     * Get firmware update job Status
     **********************************/
    @ApiOperation(value = UriConstants.UPDATE_STATUS, nickname = "updateStatus", response = JobStatus.class, responseContainer = "List", notes = "This WebMethod retrieves  recent firmware update job status for all JobIDs in request body, micro service will determine and find firmware job status on hardware device." + " This method returns list of jobs created on iDRAC server.")

    @RequestMapping(value = UriConstants.UPDATE_STATUS, method = RequestMethod.POST, produces = "application/json")
    public List<JobStatus> getJobStatus(@RequestBody JobStatusRequest request);


    /***********************************
     * Test API for callback
     **********************************/

    @ApiOperation(value = UriConstants.CUSTOM_CATALOG, nickname = "verifyCallback", response = Response.class, notes = "This  is test WebMethod, verify the callback API is valid and reachable from micro service")

    @RequestMapping(value = UriConstants.UPDATE_TEST, method = RequestMethod.POST)
    public int updateFirmwareTest(@RequestBody CallBackResponse request);

}