package com.lyn.cleansing.javaBean.req;

import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @author lyn
 * @date 2023/8/1
 */
@Data
public class CleansingDataReqDto {

    /**
     *  清洗数据
     *
     * 数据格式List<Map<列名,数值>>
     */
    private List<Map<String, String>> list;
}
