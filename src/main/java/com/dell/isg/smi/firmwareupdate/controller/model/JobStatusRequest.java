/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.controller.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rahman.muhammad
 *
 */
public class JobStatusRequest extends ObmRequestBase {
    private List<String> jobs;


    public List<String> getJobs() {

        if (jobs == null) {
            jobs = new ArrayList<String>();
        }
        return jobs;
    }


    public void setJobs(List<String> jobs) {
        this.jobs = jobs;
    }

}
