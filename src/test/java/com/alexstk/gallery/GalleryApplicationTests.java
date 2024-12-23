package com.alexstk.gallery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;

@SpringBootTest
@AutoConfigureMockMvc
@EnableConfigurationProperties
@ActiveProfiles("test")
public class GalleryApplicationTests {
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @Autowired
    private MockMvc mockMvc;

	protected <T> T performGetRequest(String path, Class<T> responseType, ResultMatcher expectedStatus) throws
			Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(path)).andExpect(expectedStatus).andReturn();
		return convertStringToClass(mvcResult.getResponse().getContentAsString(), responseType);
	}

    protected <T> T performPostRequest(String path, Object object, Class<T> responseType,
                                       ResultMatcher expectedStatus) throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(path)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(object))).andExpect(expectedStatus).andReturn();
        if(responseType != null) {
            return convertStringToClass(mvcResult.getResponse().getContentAsString(), responseType);
        } else {
            return null;
        }
    }

    private <T> T convertStringToClass(String jsonString, Class<T> responseType) throws JsonProcessingException {
        return mapper.readValue(jsonString, responseType);
    }
}
