/**
 * Copyright (c) 2019 Kim Chau Duong
 * All rights reserved
 */
package nl.bioinf.jp_kcd_wr.image_library.hateos_resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * DTO object that extends ResourceSupport
 * It creates pagination objects for unique folder and image tags
 *
 * @author Kim Chau Duong
 */
public class UniqueTags extends ResourceSupport{
    private final List<String> content;

    @JsonCreator
    public UniqueTags(@JsonProperty("content") List<String> content) {
        this.content = content;
    }

    public List<String> getContent(){
        return content;
    }
}
