package com.hp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hp.enumerate.TagEnum;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class Suggestion {

    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    private String id;

    @NotBlank
    private String title;

    @Lob
    @Column(columnDefinition="TEXT")
    @NotBlank
    private String content;

    @NotBlank
    private String tag;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;


    public String displayTag(){
        if(StringUtils.isNotBlank(this.tag)){
            List<String> tags  = new ArrayList<>();
            final Map<String,TagEnum> tagEnumMap =TagEnum.toMap();
            Arrays.stream(this.tag.split(","))
                  .filter(tagEnumMap::containsKey)
                  .forEach(str -> tags.add(tagEnumMap.get(str).getDescription()));
            if(tags.size() > 0){
                String [] strings = tags.toArray(new String[0]);
                return String.join(",",strings);
            }
        }
        return "";
    }
}
