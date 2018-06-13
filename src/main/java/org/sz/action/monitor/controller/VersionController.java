package org.sz.action.monitor.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller provides endpoint to check the version of the application. The endpoint contains version number in the path.
 */
@RestController
public class VersionController {

    private static final String APPLICATION_RUNNING_STATUS_MESSAGE = "OK";

    @Value("${app.version}")
    private String applicationVersion;

    /**
     * Endpoint to check the version of the application based on project.version property.
     *
     * @return project.version as the version of the application
     */
    @RequestMapping("v1/version")
    public String getApplicationStatus() {
        return applicationVersion;
    }
}

