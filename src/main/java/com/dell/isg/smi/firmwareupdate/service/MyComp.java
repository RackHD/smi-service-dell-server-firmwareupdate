/**
 * Copyright © 2017 DELL Inc. or its subsidiaries.  All Rights Reserved.
 */
/**
 * 
 */
package com.dell.isg.smi.firmwareupdate.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author rahman.muhammad
 *
 */

@Component
public class MyComp {

    @Async
    public void print() throws Exception {

        try {

            System.out.println("waiting ....>>> ");
            Thread.sleep(5 * 1000);
            System.out.println("waiting finished .... ");
        } catch (Exception exp) {
            System.out.println(exp);
        }
    }
}
