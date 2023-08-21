package com.joyowo.cleansing.controller;


import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.joyowo.cleansing.other.DbHandlerThreadLocal;
import com.joyowo.cleansing.service.CleansingService;
import com.joyowo.cleansing.javaBean.req.CleansingDataReqDto;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lyn
 * @date 2023/8/1
 */
@Slf4j
@RestController
@RequestMapping(value = "/cleansing")
public class CleansingController {

    @Autowired
    private CleansingService cleansingService;

    /**
     * 清洗数据
     *
     * 建议该方法用于测试
     * 建议依赖该项目后，自行调用com.joyowo.cleansing.handler.NormalHandler#cleansingMysqlData(java.util.List)方法
     *
     * @Author: lyn
     * @Date: 2023/8/1
     */
    @PostMapping({"/cleansingData"})
    public String cleansingData(@RequestBody CleansingDataReqDto reqDto) {
        List<Map<String, String>> list = reqDto.getList();
        if (CollectionUtils.isEmpty(list)) {
            return "failed";
        }
        try {
            cleansingService.cleansingMysqlData(list);

            //清除数据库操作handler
            DbHandlerThreadLocal.removeDbHandler();

            log.info("数据处理结束...");
        } catch (Exception e) {
            log.error("处理数据失败", e);

            return "failed";
        }
        return "success";
    }

}
