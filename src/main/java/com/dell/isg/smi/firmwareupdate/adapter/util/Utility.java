/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.adapter.util;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dell.cm.comparer.DCMUpdateInformation;
import com.dell.cm.updateinformationmodel.DCMManifest;
import com.dell.cm.updateinformationmodel.DCMMultiSystemInventory;
import com.dell.cm.updateinformationmodel.DCMSystemInstance;
import com.dell.cm.updateinformationmodel.DCMUpdatePackageInformation;
import com.dell.cm.updateinformationmodel.DCMVersionInformation;
import com.dell.isg.smi.commons.model.server.JobStatusEnum.LCJobStatus;
import com.dell.isg.smi.commons.model.server.firmware.Update;

/**
 * @author rahman.muhammad
 *
 */
public class Utility {

    private static final Logger logger = LoggerFactory.getLogger(Utility.class.getName());


    public static File getFile(String path) throws Exception {

        if (path == null || path.isEmpty()) {
            new Exception("Not a valid File or path");
        }

        File catalogFile = new File(path);
        if (!(catalogFile.isFile() && catalogFile.canRead())) {
            new Exception("Catalog.xml file doesn't exists OR not readable" + catalogFile.getAbsolutePath());
        }

        return catalogFile;
    }


    public static File getDirectory(String path) throws Exception {

        if (path == null || path.isEmpty()) {
            new Exception("Not a valid File or path");
        }

        File dir = new File(path);
        if (!(dir.isDirectory() && dir.canWrite())) {
            new Exception("Not a valid directory or OR not writable" + dir.getAbsolutePath());
        }

        return dir;
    }


    public static Collection<DCMSystemInstance> getSystemInstanceCollection(DCMMultiSystemInventory mSystem) {

        Collection<DCMSystemInstance> collection = Collections.emptyList();

        if (mSystem == null || mSystem.getSystemInstanceCollection() == null)
            return collection;

        collection = mSystem.getSystemInstanceCollection().getSystemInstances();
        return collection;
    }


    /*
     * 
     * Utility method to convert two need object for final applicable update
     */

    public static Update convergeUpdateInfo(DCMManifest manifest, DCMVersionInformation versionInfo, DCMUpdateInformation upInfo) {

        Update update = new Update();
        update.setCriticality(upInfo.getCriticality().toString());
        update.setName(upInfo.getName().getDefaultLocaleString());
        update.setPath(upInfo.getPath());
        update.setUniqueIdentifier(upInfo.getUniqueIdentifier());
        update.setVersion(upInfo.getVersion());
        update.setTargetIdentifier(versionInfo.getTargetIdentifier());
        update.setUpdateAction(upInfo.getType().toString());
        update.setSourceName(versionInfo.getTargetInstance());

        DCMUpdatePackageInformation updatePackageInformation = manifest.getUpdatePackageWithGivenIdentifier(upInfo.getUniqueIdentifier());
        logger.info("updatePackageInformation::" + updatePackageInformation.getPath());

        if (updatePackageInformation != null) {
            update.setPackageInformationPath(updatePackageInformation.getPath());
            update.setCategory(updatePackageInformation.getLUCategoryCode());
        }
        return update;

    }


    /*
     * return true when a lifecycle controller job finished either FAILED or PASSED
     */
    public static boolean isJobDone(String status) {
        return (LCJobStatus.COMPLETED.getValue().equalsIgnoreCase(status) || LCJobStatus.FAILED.getValue().equalsIgnoreCase(status));

    }

}