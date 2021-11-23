package com.chas.telenortest.integrationTests;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@TestPropertySource(locations = "/application-integrationtest.properties")
@AutoConfigureMockMvc
public class ProductControllerTest {

    //Developer's note for the rest:
    // Placing the expected json results in giant strings like this is not great, as it make it harder to read the json.
    // It would have been smarter to place the expected result in separate text files, with properly formatted json data.
    // I decided not to, just to save time for the test.
    private static final String EXPECTED_RESULT_NO_FILTER = "{\"data\":[{\"locationOfSale\":\"Blake granden, Karlskrona\",\"price\":277.0,\"property\":\"color:gron\",\"addressOfSale\":\"Blake granden\",\"id\":1,\"type\":\"phone\",\"cityOfSale\":\"Karlskrona\"},{\"locationOfSale\":\"Hazel allen, Malmo\",\"price\":191.0,\"property\":\"gb_limit:50\",\"addressOfSale\":\"Hazel allen\",\"id\":2,\"type\":\"subscription\",\"cityOfSale\":\"Malmo\"},{\"locationOfSale\":\"Karlsson gardet, Stockholm\",\"price\":474.0,\"property\":\"gb_limit:10\",\"addressOfSale\":\"Karlsson gardet\",\"id\":3,\"type\":\"subscription\",\"cityOfSale\":\"Stockholm\"},{\"locationOfSale\":\"Olsson gatan, Karlskrona\",\"price\":800.0,\"property\":\"gb_limit:10\",\"addressOfSale\":\"Olsson gatan\",\"id\":4,\"type\":\"subscription\",\"cityOfSale\":\"Karlskrona\"},{\"locationOfSale\":\"Jaydon allen, Karlskrona\",\"price\":528.0,\"property\":\"color:silver\",\"addressOfSale\":\"Jaydon allen\",\"id\":5,\"type\":\"phone\",\"cityOfSale\":\"Karlskrona\"}]}";
    private static final String EXPECTED_RESULT_TYPE_FILTER = "{\"data\":[{\"locationOfSale\":\"Blake granden, Karlskrona\",\"price\":277.0,\"property\":\"color:gron\",\"addressOfSale\":\"Blake granden\",\"id\":1,\"type\":\"phone\",\"cityOfSale\":\"Karlskrona\"},{\"locationOfSale\":\"Jaydon allen, Karlskrona\",\"price\":528.0,\"property\":\"color:silver\",\"addressOfSale\":\"Jaydon allen\",\"id\":5,\"type\":\"phone\",\"cityOfSale\":\"Karlskrona\"}]}";
    private static final String EXPECTED_RESULT_PRICE_FILTER = "{\"data\":[{\"locationOfSale\":\"Olsson gatan, Karlskrona\",\"price\":800.0,\"property\":\"gb_limit:10\",\"addressOfSale\":\"Olsson gatan\",\"id\":4,\"type\":\"subscription\",\"cityOfSale\":\"Karlskrona\"},{\"locationOfSale\":\"Jaydon allen, Karlskrona\",\"price\":528.0,\"property\":\"color:silver\",\"addressOfSale\":\"Jaydon allen\",\"id\":5,\"type\":\"phone\",\"cityOfSale\":\"Karlskrona\"}]}";
    private static final String EXPECTED_RESULT_CITY_FILTER = "{\"data\":[{\"locationOfSale\":\"Karlsson gardet, Stockholm\",\"price\":474.0,\"property\":\"gb_limit:10\",\"addressOfSale\":\"Karlsson gardet\",\"id\":3,\"type\":\"subscription\",\"cityOfSale\":\"Stockholm\"}]}";
    private static final String EXPECTED_RESULT_PROPERTY_COLOR_FILTER = "{\"data\":[{\"locationOfSale\":\"Hazel allen, Malmo\",\"price\":191.0,\"property\":\"gb_limit:50\",\"addressOfSale\":\"Hazel allen\",\"id\":2,\"type\":\"subscription\",\"cityOfSale\":\"Malmo\"},{\"locationOfSale\":\"Karlsson gardet, Stockholm\",\"price\":474.0,\"property\":\"gb_limit:10\",\"addressOfSale\":\"Karlsson gardet\",\"id\":3,\"type\":\"subscription\",\"cityOfSale\":\"Stockholm\"},{\"locationOfSale\":\"Olsson gatan, Karlskrona\",\"price\":800.0,\"property\":\"gb_limit:10\",\"addressOfSale\":\"Olsson gatan\",\"id\":4,\"type\":\"subscription\",\"cityOfSale\":\"Karlskrona\"},{\"locationOfSale\":\"Jaydon allen, Karlskrona\",\"price\":528.0,\"property\":\"color:silver\",\"addressOfSale\":\"Jaydon allen\",\"id\":5,\"type\":\"phone\",\"cityOfSale\":\"Karlskrona\"}]}";
    private static final String EXPECTED_RESULT_PROPERTY_GB_LIMIT_FILTER = "{\"data\":[{\"locationOfSale\":\"Blake granden, Karlskrona\",\"price\":277.0,\"property\":\"color:gron\",\"addressOfSale\":\"Blake granden\",\"id\":1,\"type\":\"phone\",\"cityOfSale\":\"Karlskrona\"},{\"locationOfSale\":\"Hazel allen, Malmo\",\"price\":191.0,\"property\":\"gb_limit:50\",\"addressOfSale\":\"Hazel allen\",\"id\":2,\"type\":\"subscription\",\"cityOfSale\":\"Malmo\"},{\"locationOfSale\":\"Jaydon allen, Karlskrona\",\"price\":528.0,\"property\":\"color:silver\",\"addressOfSale\":\"Jaydon allen\",\"id\":5,\"type\":\"phone\",\"cityOfSale\":\"Karlskrona\"}]}";

    @Autowired
    private MockMvc mvc;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testGetAllProducts() throws Exception {
        //Given
        //Context load

        //When
        String result = mvc.perform(get("/product"))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse()
                .getContentAsString();


        //Then
        Assertions.assertEquals(EXPECTED_RESULT_NO_FILTER, result);
    }

    @Test
    public void testGetTypeFilteredProducts() throws Exception {
        //Given
        //Context load

        //When
        String result = mvc.perform(get("/product?type=phone"))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Then
        Assertions.assertEquals(EXPECTED_RESULT_TYPE_FILTER, result);
    }

    @Test
    public void testGetPriceFilteredProducts() throws Exception {
        //Given
        //Context load

        //When
        String result = mvc.perform(get("/product?min_price=500&max_price=800"))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Then
        Assertions.assertEquals(EXPECTED_RESULT_PRICE_FILTER, result);
    }

    @Test
    public void testGetCityFilteredProducts() throws Exception {
        //Given
        //Context load

        //When
        String result = mvc.perform(get("/product?city=stockholm"))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Then
        Assertions.assertEquals(EXPECTED_RESULT_CITY_FILTER, result);
    }

    @Test
    public void testGetPropertyColorFilteredProducts() throws Exception {
        //Given
        //Context load

        //When
        String result = mvc.perform(get("/product?property=color&property:color=silver"))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Then
        Assertions.assertEquals(EXPECTED_RESULT_PROPERTY_COLOR_FILTER, result);
    }

    @Test
    public void testGetPropertyGbLimitFilteredProducts() throws Exception {
        //Given
        //Context load

        //When
        String result = mvc.perform(get("/product?property=gb_limit&property:gb_limit_min=20&property:gb_limit_max=50"))
                .andExpect(status().isFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Then
        Assertions.assertEquals(EXPECTED_RESULT_PROPERTY_GB_LIMIT_FILTER, result);
    }
}
