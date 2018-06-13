package org.sz.action.monitor.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller provides endpoint to check if the application is running. The endpoint contains version number in the path.
 */
@RestController
public class StatusController {

    private static final String APPLICATION_RUNNING_STATUS_MESSAGE = "OK";

    /**
     * Endpoint to check if the application is running.
     *
     * @return OK if the application is running
     */
    @RequestMapping("v1/status")
    public String getApplicationStatus() {
        return APPLICATION_RUNNING_STATUS_MESSAGE;
    }
}
