package app.warehouse.system;

import app.warehouse.system.dto.InventoryDto;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WarehouseApplication.class)
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
public class InventoryTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        System.out.println("Inventory - Test Start Up");
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void cleanData() {
        System.out.println("Test.tearDown Inventory");
    }

    @Test
    @Tag("01_Get_All_Inventory")
    public void test_02_01() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory/")
                        .param("pageNo", "0")
                        .param("pageSize", "5")
                        .param("sortBy", "name")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    @Tag("02_Get_Inventory_By_Id")
    public void test_02_02() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/inventory/id")
                        .param("inventoryId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    @Tag("03_Update_Inventory")
    public void test_02_03() throws Exception {
        InventoryDto dto = new InventoryDto();
        dto.setInventoryId(1L);
        dto.setPrice(10.0);
        dto.setName("Test");
        dto.setQuantity(1);
        dto.setActive(true);

        Gson gson = new Gson();
        String json = gson.toJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/")
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().is(200));
    }

    @Test
    @Tag("04_Disable_Inventory")
    public void test_02_04() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/inventory/disable")
                        .param("inventoryId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }
}
