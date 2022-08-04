package pers.sample.sb;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
//@SpringBootTest
@WebMvcTest(RandomNumberController.class)
//@ExtendWith(SpringExtension.class)
public class RandomNumberControllerTest {
    private static final Logger _lgoger = LoggerFactory.getLogger(RandomNumberControllerTest.class);

    @Resource
    private MockMvc _mockMvc;

    @MockBean
    private RandomNumberService _service;

    @Test
    public void test_01() throws Exception {
        String value = "{\"randomNumber\":3017,\"hitCount\":10}";
        ObjectMapper mapper = new ObjectMapper();
        RandomNumber rn = mapper.readValue(value, RandomNumber.class);

        when(_service.acquireRandomNumber()).thenReturn(rn);
        MvcResult mvcResult = _mockMvc.perform(
            MockMvcRequestBuilders.request(HttpMethod.GET, "/randomNumber")
            .contentType("application/json").content(value)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.randomNumber").value(3017))
        .andExpect(MockMvcResultMatchers.jsonPath("$.hitCount").value(10))
        .andDo(print()).andReturn();

        mvcResult.getResponse().setCharacterEncoding("UTF-8");
        _lgoger.info(mvcResult.getResponse().getContentAsString());
    }
}
