package nl.bioinf.bioview.metadataapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MetadataApiApplication.class)
@AutoConfigureMockMvc
public class MetadataApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void roiTest() throws Exception {
        getRoiTest();
    }

    private void getRoiTest() throws Exception {
        String responseJson = "[{\"x\":456,\"y\":590},{\"x\":20,\"y\":5900},{\"x\":760,\"y\":900},{\"x\":907,\"y\":20},{\"x\":2,\"y\":16}]";
        this.mockMvc.perform(get("/api/image/roi/{roi-id}", "1"))
                .andExpect(status().isOk())
                .andExpect((content().json(responseJson)));

    }

}
