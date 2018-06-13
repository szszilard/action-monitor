package org.sz.action.monitor.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController {

    private static final String APPLICATION_RUNNING_STATUS_MESSAGE = "OK";

    @Value("${app.version}")
    private String applicationVersion;

    @RequestMapping("v1/version")
    public String getApplicationStatus() {
        return applicationVersion;
    }
}

