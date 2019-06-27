package com.icceey.jweb.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
public class Todo implements Serializable {

    private Integer id, ownerId, ownerType;
    private String title , content;
    private Date date;

}
