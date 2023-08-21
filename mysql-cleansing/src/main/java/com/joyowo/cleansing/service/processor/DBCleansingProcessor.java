package com.joyowo.cleansing.service.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;
import com.joyowo.cleansing.constant.DBConstant;
import com.joyowo.cleansing.constant.LogicConstant;
import com.joyowo.cleansing.constant.SqlConstant;
import com.joyowo.cleansing.enums.ProcessorTypeEnum;
import com.joyowo.cleansing.exception.CleansingException;
import com.joyowo.cleansing.javaBean.dto.CleansingConfigDto;
import com.joyowo.cleansing.javaBean.dto.DbCleansingProcessDto;
import com.joyowo.cleansing.javaBean.dto.MybatisDatabaseHandlerDto;
import com.joyowo.cleansing.javaBean.queryBean.InformationSchemaDto;
import com.joyowo.cleansing.other.DbHandlerThreadLocal;
import com.joyowo.cleansing.utils.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据库类型执行器
 * 直接数据库sql进行操作，无关逻辑
 *
 * @author lyn
 * @date 2023/7/28
 */
@Data
@Slf4j
public class DBCleansingProcessor implements BaseCleansingProcessor {

    /**
     * 需要处理的数据
     */
    private CleansingConfigDto configDto;

    private DbCleansingProcessDto param;

    private List<String> bakTableNames;

    @Override
    public void process(List<Map<String, String>> list) throws CleansingException {

        CleansingConfigDto configDto = this.getConfigDto();
        if (StringUtils.isEmpty(configDto.getTbName())
                || StringUtils.isEmpty(configDto.getCleansingColumn())
                || StringUtils.isEmpty(configDto.getDataCleansingName())
                || StringUtils.isEmpty(configDto.getConditionColumn1())
                || StringUtils.isEmpty(configDto.getDataConditionName1())) {
            log.error("sql执行参数异常，跳过该执行器执行操作，{}", JsonUtils.toJson(configDto));
            return;
        }

        log.info("执行配置::{}", JsonUtils.toJson(configDto));
        List<String> sqlList = buildExecuteSqlList(list, configDto);
        log.info("执行内容：{}", JsonUtils.toJson(sqlList));

        //操作清洗数据
        MybatisDatabaseHandlerDto handlerDto = new MybatisDatabaseHandlerDto();
        handlerDto.setSqlList(sqlList);
        DbHandlerThreadLocal.getDbHandler().doCleansingSql(handlerDto);
    }

    /**
     * 构建执行sql
     */
    private List<String> buildExecuteSqlList(List<Map<String, String>> list, CleansingConfigDto configDto) {
        List<String> sqlList = new ArrayList<>();
        for (Map<String, String> dataMapMap : list) {
            //修改后数据
            String cleansingData = getDataString(dataMapMap, configDto.getDataCleansingName(),
                    configDto.getCleansingColumn());

            //清洗数据
            List<String> cleansingList = new ArrayList<>();
            String paramStr = String.format(SqlConstant.PARAMS_SQL, configDto.getCleansingColumn(), cleansingData);
            cleansingList.add(paramStr);

            //条件数据
            List<String> conditionList = new ArrayList<>();

            String conditionData1 = getDataString(dataMapMap, configDto.getDataConditionName1(),
                    configDto.getConditionColumn1());
            String condition1 = String.format(SqlConstant.PARAMS_SQL, configDto.getConditionColumn1(), conditionData1);
            conditionList.add(condition1);

            //如果有第二个条件数据
            if (!StringUtils.isEmpty(configDto.getDataConditionName2()) && !StringUtils
                    .isEmpty(configDto.getConditionColumn2())) {
                String conditionData2 = getDataString(dataMapMap, configDto.getDataConditionName2(),
                        configDto.getConditionColumn2());
                String condition2 = String
                        .format(SqlConstant.PARAMS_SQL, configDto.getConditionColumn2(), conditionData2);
                conditionList.add(condition2);
            }
            //如果有第三个条件数据
            if (!StringUtils.isEmpty(configDto.getDataConditionName3()) && !StringUtils
                    .isEmpty(configDto.getConditionColumn3())) {
                String conditionData3 = getDataString(dataMapMap, configDto.getDataConditionName3(),
                        configDto.getConditionColumn3());
                String condition3 = String
                        .format(SqlConstant.PARAMS_SQL, configDto.getConditionColumn3(), conditionData3);
                conditionList.add(condition3);
            }
            String cleansingParam = cleansingList.stream().collect(Collectors.joining(","));
            String conditionParam = conditionList.stream().collect(Collectors.joining(" and "));
            //拼接sql
            String result = String.format(SqlConstant.UPDATE_SQL, configDto.getTbName(), cleansingParam, conditionParam);
            sqlList.add(result);
        }
        return sqlList;
    }

    /**
     * 数据根据字段类型，增加单引号
     *
     * @param dataMap 目标洗入数据
     * @param dataColumnName 洗入数据字段名
     * @param dbColumnName 数据库字段名
     */
    private String getDataString(Map<String, String> dataMap, String dataColumnName, String dbColumnName)
            throws CleansingException {
        String dataStr;
        List<InformationSchemaDto> informationSchemaDtos = this.param.getInformationSchemaDtos();
        InformationSchemaDto informationSchemaDto = informationSchemaDtos.stream()
                .filter(x -> x.getColumnName().equals(dbColumnName)).findFirst().orElse(null);
        if (informationSchemaDto == null) {
            log.error("没有匹配的字段{}\n{}", JsonUtils.toJson(informationSchemaDtos), dbColumnName);
            throw new CleansingException("处理数据失败");
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
        CleansingConfigDto configDto = this.getConfigDto();
        if(configDto.getOrderNo()==null){
            return LogicConstant.DEFAULT_ORDER_NO;
        }else{
            return configDto.getOrderNo();
        }
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
        return bakTableNames;
    }

}
