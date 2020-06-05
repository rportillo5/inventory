package com.banderasmusic.rest.inventory;

import com.banderasmusic.rest.inventory.model.Inventory;
import com.banderasmusic.rest.inventory.service.InventoryService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
class InventoryApplicationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private InventoryService inventoryService;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Test
    public void getShouldReturnOne() throws Exception {
        Long itemNumber = 10001l;
        java.util.Optional<Inventory> inv = java.util.Optional.of(new Inventory());
        given(inventoryService.get(10001l)).willReturn(inv);

        this.mvc.perform(get("/inventory/10001"))
                .andExpect(status().isOk());
    }

    @Test
    public void getRequestShouldReturnAllInventory() throws Exception {
        String uri = "/inventory";
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = mvc.perform(get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
    }

    @Test
    public void createInventoryShouldReturn200() throws Exception {
        String uri = "/inventory";
        Inventory inventory = new Inventory();
        inventory.setItemNumber(10005l);
        inventory.setProductCode("ABCDEFG");
        inventory.setProductDescription("desc greater than 10 chars");
        inventory.setStartingCount(50);
        inventory.setReorderPoint(11);

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String inputJson = mapToJson(inventory);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "10005");
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
