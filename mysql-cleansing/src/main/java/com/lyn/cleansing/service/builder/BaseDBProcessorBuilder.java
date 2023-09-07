package com.lyn.cleansing.service.builder;

import java.util.List;
import java.util.Map;
import org.springframework.util.CollectionUtils;
import com.lyn.cleansing.exception.CleansingException;
import com.lyn.cleansing.javaBean.dto.CleansingConfigDto;
import com.lyn.cleansing.javaBean.dto.DbCleansingProcessDto;
import com.lyn.cleansing.javaBean.dto.DbProcessorBuildDto;
import com.lyn.cleansing.javaBean.queryBean.InformationSchemaDto;
import com.lyn.cleansing.service.processor.BaseCleansingProcessor;
import com.lyn.cleansing.service.processor.DBCleansingProcessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseDBProcessorBuilder {

    /**
     *  构建数据库清洗处理器
     *
     * @param buildDto 构建执行器参数
     */
    public abstract void buildDbProcessor(DbProcessorBuildDto buildDto) throws CleansingException;


    /**
     * 创建数据库执行器
     */
    protected void buildProcessorByEntity(DbProcessorBuildDto buildDto, List<CleansingConfigDto> tableConfigDtoList)
            throws CleansingException {
        Map<String, List<InformationSchemaDto>> tableMap = buildDto.getTableMap();
        List<BaseCleansingProcessor> processorList = buildDto.getProcessorList();

        for (CleansingConfigDto configDto : tableConfigDtoList) {
            //拼接入参
            DbCleansingProcessDto processDto = new DbCleansingProcessDto();
            List<InformationSchemaDto> informationSchemaDtos = tableMap.get(configDto.getTbName());
            if (CollectionUtils.isEmpty(informationSchemaDtos)) {
                throw new CleansingException("未找到数据库信息");
            }
            processDto.setInformationSchemaDtos(informationSchemaDtos);

            //转化成各个执行器
            DBCleansingProcessor dbCleansingProcessor = new DBCleansingProcessor();
            dbCleansingProcessor.setConfigDto(configDto);
            dbCleansingProcessor.setParam(processDto);
            processorList.add(dbCleansingProcessor);
        }

        log.info("当前执行器数量：{}",processorList.size());
    }

}
