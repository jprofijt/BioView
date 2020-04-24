package nl.bioinf.bioview.metadataapi;

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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class to test Image attribute resource output that gets used in the main project.
 *
 * @author Kim Chau Duong
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MetadataApiApplication.class)
@AutoConfigureMockMvc
public class ImageAttributeTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    /**
     * Checks an average output of a given file path that exists
     * Should give image attributes that are expected as response
     * @throws Exception
     */
    @Test
    public void testMyResourceFilePathRoot() throws Exception{
        String responseJson = "[{\"id\":0,\"imageName\":\"image.tiff\",\"path\":\"root\",\"imageSize\":222,\"dateCreated\":\"2019-05-12 23:47:22.0\",\"fileType\":\"TIFF\"}]";

        this.mockMvc.perform(get("/api/metadata/filepath?path=root/image.tiff"))
                .andExpect(status().isOk())
                .andExpect(content().json((responseJson)))
                .andDo(document("filepath",
                        requestParameters(
                                parameterWithName("path").description("File path of selected directory")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("ID of image"),
                                fieldWithPath("[].imageName").description("Image name"),
                                fieldWithPath("[].path").description("Location of image"),
                                fieldWithPath("[].imageSize").description("Size of image in bytes"),
                                fieldWithPath("[].dateCreated").description("Creation date of image"),
                                fieldWithPath("[].fileType").description("File type of image")
                        )));

    }

    /**
     * Checks the average output image attributes retrieval, but with the directory path
     * Gives back multiple image attributes
     *
     * @throws Exception
     */
    @Test
    public void testMyResourcePathForwardSlash() throws Exception{
        String responseJson = "[{\"id\":0,\"imageName\":\"test.jpeg\",\"path\":\"root/folder1\",\"imageSize\":90,\"dateCreated\":\"2019-05-13 23:47:29.0\",\"fileType\":\"JPG\"}," +
                "{\"id\":0,\"imageName\":\"dummy.png\",\"path\":\"root/folder1\",\"imageSize\":66,\"dateCreated\":\"2019-05-13 23:47:55.0\",\"fileType\":\"PNG\"}]";
        this.mockMvc.perform(get("/api/metadata/path?path={path}", "root/folder1"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }

    /**
     * Checks the image attribute retrieval output when a path is given that contains a backslash
     * Should return a BAD_REQUEST response
     * @throws Exception
     */
    @Test
    public void testMyResourcePathBackSlash() throws Exception{
        this.mockMvc.perform(get("/api/metadata/path?path={path}", "root\\folder1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    /**
     * Checks what happens when the given parameter path doesn't exist
     * Doesn't return an error, but a list that's empty
     * @throws Exception
     */
    @Test
    public void testMyResourceEmptyList() throws Exception{
        String responseJson = "[]";
        this.mockMvc.perform(get("/api/metadata/path?path={path}", "root/unexistingfolder"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }
}
