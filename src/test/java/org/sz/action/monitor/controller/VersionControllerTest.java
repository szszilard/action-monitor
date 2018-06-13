package org.sz.action.monitor.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(VersionController.class)
@TestPropertySource(properties = {"app.version=1.0.0-TEST"})
public class VersionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void statusEndpointShouldReturnWithProjectVersion() throws Exception {
        this.mvc.perform(get("/v1/version")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("1.0.0-TEST"));
    }
}
