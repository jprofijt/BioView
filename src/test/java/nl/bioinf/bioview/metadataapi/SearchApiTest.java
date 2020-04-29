package nl.bioinf.bioview.metadataapi;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Search tests
 * @author Jouke Profijt
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MetadataApiApplication.class)
@AutoConfigureMockMvc
public class SearchApiTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets/searchApi");

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext context;
    private RestDocumentationResultHandler document;

    @Before
    public void setUp(){
        this.document = document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(
                        this.restDocumentation))
                .alwaysDo(this.document)
                .build();
    }

    @Test
    public void testSimpleSearch() throws Exception{
        String responseJson = "[{'ph':6.2,'temp':2.0,'o2':50,'co2':50,'tags':['LowPh','testTag1','Bloodcell','testTag2'],'image':3}]";
        this.mockMvc.perform(get("/api/roi/search/?phMin=0.0&phMax=13&temperatureMin=1&temperatureMax=4&O2Min=0&O2Max=80&CO2Min=0&CO2Max=50&tags=testTag2"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(this.document.document(
                        requestParameters(
                                parameterWithName("phMin").description("Starting range ph. Must be between 0 and 14"),
                                parameterWithName("phMax").description("End range ph. Must be between 0 and 14"),
                                parameterWithName("temperatureMin").description("Starting range temperature. Can't be smaller than -273.15"),
                                parameterWithName("temperatureMax").description("End range temperature. Can't be smaller than -273.15"),
                                parameterWithName("O2Min").description("Starting range Oxygen. Percentage, Must be between 0% and 100%"),
                                parameterWithName("O2Max").description("End range Oxygen. Percentage, Must be between 0% and 100%"),
                                parameterWithName("CO2Min").description("Starting range Carbon Dioxide. Percentage, Must be between 0% and 100%"),
                                parameterWithName("CO2Max").description("End range Carbon Dioxide. Percentage, Must be between 0% and 100%"),
                                parameterWithName("tags").description("Search tags. Searches for tags within region of interest")

                        ),
                        responseFields(
                                fieldWithPath("[].ph").description("Search result Ph"),
                                fieldWithPath("[].temp").description("Search result Temperature"),
                                fieldWithPath("[].o2").description("Search result Oxygen Percentage"),
                                fieldWithPath("[].co2").description("Search result Carbon Dioxide Percentage"),
                                fieldWithPath("[].tags").description("Search result containing tags"),
                                fieldWithPath("[].image").description("Image id where roi is located")
                        )
                ));
    }

    @Test
    public void testBadPh() throws Exception{
        String responseJson = "{'message':'Ph must be between 0 and 14','status':'BAD_REQUEST','minimum':-1.0,'maximum':13.0}";

        this.mockMvc.perform(get("/api/roi/search/?phMin={phMin}&phMax={phMax}", "-1.0", "13"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseJson))
                .andDo(this.document.document(
                        requestParameters(
                                parameterWithName("phMin").description("Starting range ph. Must be between 0 and 14"),
                                parameterWithName("phMax").description("End range ph. Must be between 0 and 14")

                        ),
                        responseFields(
                                fieldWithPath("message").description("Error message generated"),
                                fieldWithPath("status").description("Status code generated"),
                                fieldWithPath("minimum").description("Minimum that was used for request"),
                                fieldWithPath("maximum").description("Maximum that was used for request")
                        )
                ));
    }

    @Test
    public void testBadPhHigh() throws Exception{
        String responseJson = "{'message':'Ph must be between 0 and 14','status':'BAD_REQUEST','minimum':0.0,'maximum':15.0}";

        this.mockMvc.perform(get("/api/roi/search/?phMin={phMin}&phMax={phMax}", "0.0", "15"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseJson))
                .andDo(this.document.document(
                        requestParameters(
                                parameterWithName("phMin").description("Starting range ph. Must be between 0 and 14"),
                                parameterWithName("phMax").description("End range ph. Must be between 0 and 14")

                        ),
                        responseFields(
                                fieldWithPath("message").description("Error message generated"),
                                fieldWithPath("status").description("Status code generated"),
                                fieldWithPath("minimum").description("Minimum that was used for request"),
                                fieldWithPath("maximum").description("Maximum that was used for request")
                        )
                ));
    }

    @Test
    public void testPhMismatch() throws Exception{
        String responseJson = "{'message':'Ph minimum larger than maximum','status':'BAD_REQUEST','minimum':4.0,'maximum':2.0}";

        this.mockMvc.perform(get("/api/roi/search/?phMin={phMin}&phMax={phMax}", "4", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseJson))
                .andDo(this.document.document(
                        requestParameters(
                                parameterWithName("phMin").description("Starting range ph. Must be between 0 and 14"),
                                parameterWithName("phMax").description("End range ph. Must be between 0 and 14")

                        ),
                        responseFields(
                                fieldWithPath("message").description("Error message generated"),
                                fieldWithPath("status").description("Status code generated"),
                                fieldWithPath("minimum").description("Minimum that was used for request"),
                                fieldWithPath("maximum").description("Maximum that was used for request")
                        )
                ));
    }

    @Test
    public void testBadTemp() throws Exception{
        String responseJson = "{'message':'Temperature cant be smaller than the absolute zero temperature','status':'BAD_REQUEST','minimum':-274.0,'maximum':4.0}";

        this.mockMvc.perform(get("/api/roi/search/?temperatureMin={tempMin}&temperatureMax={tempMax}", "-274", "4"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseJson))
                .andDo(this.document.document(
                        requestParameters(
                                parameterWithName("temperatureMin").description("Starting range temperature. Can't be smaller than -273.15"),
                                parameterWithName("temperatureMax").description("End range temperature. Can't be smaller than -273.15")

                        ),
                        responseFields(
                                fieldWithPath("message").description("Error message generated"),
                                fieldWithPath("status").description("Status code generated"),
                                fieldWithPath("minimum").description("Minimum that was used for request"),
                                fieldWithPath("maximum").description("Maximum that was used for request")
                        )
                ));
    }

    @Test
    public void testTempMismatch() throws Exception{
        String responseJson = "{'message':'temperature minimum larger than maximum','status':'BAD_REQUEST','minimum':44.0,'maximum':4.0}";

        this.mockMvc.perform(get("/api/roi/search/?temperatureMin={tempMin}&temperatureMax={tempMax}", "44", "4"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseJson))
                .andDo(this.document.document(
                        requestParameters(
                                parameterWithName("temperatureMin").description("Starting range temperature. Can't be smaller than -273.15"),
                                parameterWithName("temperatureMax").description("End range temperature. Can't be smaller than -273.15")

                        ),
                        responseFields(
                                fieldWithPath("message").description("Error message generated"),
                                fieldWithPath("status").description("Status code generated"),
                                fieldWithPath("minimum").description("Minimum that was used for request"),
                                fieldWithPath("maximum").description("Maximum that was used for request")
                        )
                ));
    }

    @Test
    public void testBadO2() throws Exception{
        String responseJson = "{'message':'O2 is stored as a percentage cant be smaller than 0 or larger than 100','status':'BAD_REQUEST','minimum':-1.0,'maximum':80.0}";

        this.mockMvc.perform(get("/api/roi/search/?O2Min={O2Min}&O2Max={O2Max}", "-1", "80"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseJson))
                .andDo(this.document.document(
                        requestParameters(
                                parameterWithName("O2Min").description("Starting range Oxygen. Percentage, Must be between 0% and 100%"),
                                parameterWithName("O2Max").description("End range Oxygen. Percentage, Must be between 0% and 100%")
                        ),
                        responseFields(
                                fieldWithPath("message").description("Error message generated"),
                                fieldWithPath("status").description("Status code generated"),
                                fieldWithPath("minimum").description("Minimum that was used for request"),
                                fieldWithPath("maximum").description("Maximum that was used for request")
                        )
                ));
    }

    @Test
    public void testO2Mismatch() throws Exception{
        String responseJson = "{'message':'O2 minimum larger than maximum','status':'BAD_REQUEST','minimum':80.0,'maximum':2.0}";

        this.mockMvc.perform(get("/api/roi/search/?O2Min={O2Min}&O2Max={O2Max}", "80", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseJson))
                .andDo(this.document.document(
                        requestParameters(
                                parameterWithName("O2Min").description("Starting range Oxygen. Percentage, Must be between 0% and 100%"),
                                parameterWithName("O2Max").description("End range Oxygen. Percentage, Must be between 0% and 100%")

                        ),
                        responseFields(
                                fieldWithPath("message").description("Error message generated"),
                                fieldWithPath("status").description("Status code generated"),
                                fieldWithPath("minimum").description("Minimum that was used for request"),
                                fieldWithPath("maximum").description("Maximum that was used for request")
                        )
                ));
    }

    @Test
    public void testBadCO2() throws Exception{
        String responseJson = "{'message':'CO2 is stored as a percentage cant be smaller than 0 or larger than 100','status':'BAD_REQUEST','minimum':-1.0,'maximum':50.0}";

        this.mockMvc.perform(get("/api/roi/search/?CO2Min={CO2Min}&CO2Max={CO2Max}", "-1", "50"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseJson))
                .andDo(this.document.document(
                        requestParameters(
                                parameterWithName("CO2Min").description("Starting range Carbon Dioxide. Percentage, Must be between 0% and 100%"),
                                parameterWithName("CO2Max").description("End range Carbon Dioxide. Percentage, Must be between 0% and 100%")

                        ),
                        responseFields(
                                fieldWithPath("message").description("Error message generated"),
                                fieldWithPath("status").description("Status code generated"),
                                fieldWithPath("minimum").description("Minimum that was used for request"),
                                fieldWithPath("maximum").description("Maximum that was used for request")
                        )
                ));
    }

    @Test
    public void testCO2Mismatch() throws Exception{
        String responseJson = "{'message':'CO2 minimum larger than maximum','status':'BAD_REQUEST','minimum':80.0,'maximum':50.0}";

        this.mockMvc.perform(get("/api/roi/search/?CO2Min={CO2Min}&CO2Max={CO2Max}", "80", "50"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseJson))
                .andDo(this.document.document(
                        requestParameters(
                                parameterWithName("CO2Min").description("Starting range Carbon Dioxide. Percentage, Must be between 0% and 100%"),
                                parameterWithName("CO2Max").description("End range Carbon Dioxide. Percentage, Must be between 0% and 100%")
                        ),
                        responseFields(
                                fieldWithPath("message").description("Error message generated"),
                                fieldWithPath("status").description("Status code generated"),
                                fieldWithPath("minimum").description("Minimum that was used for request"),
                                fieldWithPath("maximum").description("Maximum that was used for request")
                        )
                ));
    }

    @Test
    public void testNoResult() throws Exception {
        String responseJson = "{'message':'No results found','status':'NO_CONTENT'}";

        this.mockMvc.perform(get("/api/roi/search/?tags=hello"))
                .andExpect(status().isNoContent())
                .andExpect(content().json(responseJson))
                .andDo(this.document.document(
                        requestParameters(
                                parameterWithName("tags").description("Search tags. Searches for tags within region of interest")

                        ),
                        responseFields(
                                fieldWithPath("message").description("Error message generated"),
                                fieldWithPath("status").description("Status code generated")
                        )
                ));
    }




}
