/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.common;

/**
 * @author rahman.muhammad
 *
 */
public class UriConstants {

    public final static String URI = "/api/1.0/server/firmware";
    public final static String VERSION = URI + "/version";
    public final static String UPDATEABLE_COMPONENT_INVENTORY = URI + "/uci";
    public final static String SOFTWARE_COMPONENT_INVENTORY = URI + "/uci/si";
    public final static String APPLICABLE_UPDATES = URI + "/comparer";
    public final static String COMPARE_CATALOG = URI + "/comparer/catalog";
    public final static String CUSTOM_CATALOG = URI + "/comparer/custom";
    public final static String DOWNLOAD_FILE = URI + "/downloader";
    public final static String UPDATE = URI + "/updater";
    public final static String SINGLE_UPDATE = URI + "/updater/dup";
    public final static String UPDATE_STATUS = URI + "/updater/status";
    public final static String UPDATE_TEST = URI + "/updater/testCallback";
}
