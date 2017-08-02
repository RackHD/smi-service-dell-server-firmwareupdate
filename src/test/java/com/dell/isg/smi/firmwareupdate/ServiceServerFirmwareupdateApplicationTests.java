/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
package com.dell.isg.smi.firmwareupdate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dell.isg.smi.firmwareupdate.service.FirmwareServiceImpl;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ServiceServerFirmwareupdateApplicationTests {

    // @Test
    public void contextLoads() {
        FirmwareServiceImpl imp = new FirmwareServiceImpl();
        String address = "100.68.124.32";
        String userName = "root";
        String password = "calvin";

        try {
            // imp.getFirmwareInventory(address, userName, password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
