package com.sellermatch.process.common.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonDTO {
    private String result = "SUCCESS";
    private int status = 200;
    private Object content;
}
