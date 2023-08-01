package com.lyn.cleansing.handler.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.lyn.cleansing.constant.DBConstant;
import com.lyn.cleansing.constant.LogicConstant;
import com.lyn.cleansing.constant.SqlConstant;
import com.lyn.cleansing.enums.ProcessorTypeEnum;
import com.lyn.cleansing.javaBean.dto.DbCleansingProcessDto;
import com.lyn.cleansing.javaBean.entity.CleansingTableConfigEntity;
import com.lyn.cleansing.javaBean.queryBean.InformationSchemaDto;
import com.lyn.cleansing.mapper.CleansingTableMapper;
import com.lyn.cleansing.utils.ApplicationUtils;
import com.lyn.cleansing.utils.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库类型执行器
 * 直接数据库sql进行操作，无关逻辑
 *
 * @author ultimatum10
 * @date 2023/7/28
 */
@Data
@Slf4j
public class DBCleansingProcessor implements BaseCleansingProcessor{

    /**
     * 需要处理的数据
     */
    private CleansingTableConfigEntity configDto;

    private DbCleansingProcessDto param;

    @Override
    public void process(List<Map<String, String>> list) throws Exception {
        CleansingTableMapper cleansingTableMapper = ApplicationUtils.getApplicationContext()
                .getBean(CleansingTableMapper.class);
        CleansingTableConfigEntity configDto = this.getConfigDto();
        log.info("执行配置::{}", JsonUtils.toJson(configDto));
        List<String> sqlList = new ArrayList<>();
        for (Map<String, String> dataMapMap : list) {
            //修改后数据
            String cleansingData = getDataString(dataMapMap, configDto.getDataCleansingName(),
                    configDto.getCleansingColumn());
            //条件数据
            String conditionData = getDataString(dataMapMap, configDto.getDataConditionName(),
                    configDto.getConditionColumn());

            //拼接sql
            String result = String.format(SqlConstant.UPDATE_SQL, configDto.getTbName(), configDto.getCleansingColumn(),
                    cleansingData, configDto.getConditionColumn(), conditionData);
            sqlList.add(result);
        }
        Long time = new Date().getTime();
        log.info("执行sql::{}\n{}", time, JsonUtils.toJson(sqlList));
        cleansingTableMapper.cleansingTable(sqlList);
        log.info("执行结束，使用时间:{}ms", new Date().getTime() - time);

    }

    /**
     * 数据根据字段类型，增加单引号
     *
     * @param dataMap 目标洗入数据
     * @param dataColumnName 洗入数据字段名
     * @param dbColumnName 数据库字段名
     */
    private String getDataString(Map<String, String> dataMap, String dataColumnName, String dbColumnName)
            throws Exception {
        String dataStr;
        List<InformationSchemaDto> informationSchemaDtos = this.param.getInformationSchemaDtos();
        InformationSchemaDto informationSchemaDto = informationSchemaDtos.stream()
                .filter(x -> x.getColumnName().equals(dbColumnName)).findFirst().orElse(null);
        if (informationSchemaDto == null) {
            log.error("没有匹配的字段{}\n{}", JsonUtils.toJson(informationSchemaDtos), dbColumnName);
            throw new Exception("处理数据失败");
        }
        if (!informationSchemaDto.getDataType().contains(DBConstant.DB_TYPE_INT)) {
            dataStr = "'" + dataMap.get(dataColumnName) + "'";
        } else {
            dataStr = dataMap.get(dataColumnName);
        }
        return dataStr;
    }

    /**
     * 操作顺序，默认100。降序处理。
     */
    @Override
    public Integer getOrderNo() {
        return LogicConstant.DEFAULT_ORDER_NO;
    }

    /**
     * 类型
     * 默认逻辑处理
     */
    @Override
    public ProcessorTypeEnum getType() {
        return ProcessorTypeEnum.DB;
    }

    /**
     * 备份表表名集合
     */
    @Override
    public List<String> getBakTableNames() {
        return new ArrayList<>();
    }

}
