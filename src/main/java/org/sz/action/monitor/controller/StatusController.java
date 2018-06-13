package org.sz.action.monitor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    private static final String APPLICATION_RUNNING_STATUS_MESSAGE = "OK";

    @RequestMapping("v1/status")
    public String getApplicationStatus() {
        return APPLICATION_RUNNING_STATUS_MESSAGE;
    }
}
