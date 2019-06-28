package com.icceey.jweb.beans;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Todo implements Serializable {

    private Integer id;
    private Integer ownerId;
    private Integer ownerType;


    @NotBlank(message = "标题不能为空")
    @Length(max = 20, message = "标题过长")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    private Date datetime;

}
