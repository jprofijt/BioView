package nl.bioinf.bioview.metadataapi.control;

import nl.bioinf.bioview.metadataapi.MetadataApiApplication;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MetadataApiApplication.class)
@AutoConfigureMockMvc
public class ImageDataControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .apply(documentationConfiguration(
                        this.restDocumentation))

                .build();
    }


    @Test
    public void testRoiTags() throws Exception{
        String responseJson = "{\"id\":1,\"tags\":[\"LowPh\",\"Bloodcell\"]}";
        this.mockMvc.perform(get("/api/roi/tags/?roi={roi}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(document("roi",
                        requestParameters(
                                parameterWithName("roi").description("Region of interest id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Image id returned"),
                                fieldWithPath("tags").description("UniqueTags that are assigned to image")
                        )));
    }

}