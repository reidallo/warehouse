package app.warehouse.system;

import app.warehouse.system.dto.CustomerDto;
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
public class CustomerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        System.out.println("Customer - Test Start Up");
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void cleanData() {
        System.out.println("Test.tearDown Customer");
    }

    @Test
    @Tag("01 Get All Customers")
    public void test_01_01() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer/")
                        .param("pageNo", "1")
                        .param("pageSize", "5")
                        .param("sortBy", "firstName")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    @Tag("02 Get Customer By Id")
    public void test_01_02() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customer/id")
                        .param("customerId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    @Tag("03 Update")
    public void test_01_03() throws Exception {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(1L);
        dto.setUserId(1L);
        dto.setFirstName("Test");
        dto.setLastName("Test");
        dto.setState("Test");
        dto.setPhone("Test");
        dto.setCity("Test");

        Gson gson = new Gson();
        String json = gson.toJson(dto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/customer/")
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().is(200));
    }

    @Test
    @Tag("04 Get Customer By Id")
    public void test_01_04() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/customer/disable")
                        .param("customerId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }
}
