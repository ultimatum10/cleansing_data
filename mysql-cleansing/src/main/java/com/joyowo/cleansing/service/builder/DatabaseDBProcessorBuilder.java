package com.joyowo.cleansing.service.builder;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import com.joyowo.cleansing.exception.CleansingException;
import com.joyowo.cleansing.javaBean.dto.CleansingConfigDto;
import com.joyowo.cleansing.javaBean.dto.DbProcessorBuildDto;
import com.joyowo.cleansing.javaBean.entity.CleansingTableConfigEntity;
import com.joyowo.cleansing.other.DbHandlerThreadLocal;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lyn
 * @date 2023/8/7
 */
@Component
@Slf4j
public class DatabaseDBProcessorBuilder extends BaseDBProcessorBuilder {

    /**
     * 构建数据库清洗处理器
     *
     * @param buildDto 构建执行器参数
     */
    @Override
    public void buildDbProcessor(DbProcessorBuildDto buildDto) throws CleansingException {
        log.info("使用数据库配置，创建执行器");
        //查询配置表
        List<CleansingTableConfigEntity> tableConfigDtoList = DbHandlerThreadLocal.getDbHandler()
                .getTableCleansingConfigAll();

        List<CleansingConfigDto> configDtoList = copyConfigDtoList(tableConfigDtoList);

        //数据库配置转化成db执行器
        buildProcessorByEntity(buildDto, configDtoList);
    }

    /**
     * 拷贝数据
     */
    private List<CleansingConfigDto> copyConfigDtoList(List<CleansingTableConfigEntity> tableConfigDtoList) {
        List<CleansingConfigDto> configDtoList = new ArrayList<>();
        for (CleansingTableConfigEntity entity : tableConfigDtoList) {
            CleansingConfigDto configDto = new CleansingConfigDto();
            configDto.setTbName(entity.getTbName());
            configDto.setCleansingColumn(entity.getCleansingColumn());
            configDto.setDataCleansingName(entity.getDataCleansingName());
            configDto.setConditionColumn1(entity.getConditionColumn1());
            configDto.setDataConditionName1(entity.getDataConditionName1());
            configDto.setConditionColumn2(entity.getConditionColumn2());
            configDto.setDataConditionName2(entity.getDataConditionName2());
            configDto.setConditionColumn3(entity.getConditionColumn3());
            configDto.setDataConditionName3(entity.getDataConditionName3());
            configDto.setOrderNo(entity.getOrderNo());
            configDtoList.add(configDto);
        }
        return configDtoList;
    }

}
