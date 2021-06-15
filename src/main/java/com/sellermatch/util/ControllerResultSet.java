package com.sellermatch.util;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import org.springframework.stereotype.Component;

@Component
public class ControllerResultSet {

    public static CommonDTO errorCode (CommonDTO result, Integer Status, Object emptyContent) {
            result.setResult(CommonConstant.ERROR);
            result.setStatus(Status);
            result.setContent(emptyContent);
        return result;
    }

    public static CommonDTO errorCode (CommonDTO result, Integer Status) {
        result.setResult(CommonConstant.ERROR);
        result.setStatus(Status);
        return result;
    }
}
