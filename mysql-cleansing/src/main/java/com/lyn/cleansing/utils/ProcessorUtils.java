package com.lyn.cleansing.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;
import com.lyn.cleansing.constant.LogicConstant;
import com.lyn.cleansing.enums.ProcessorTypeEnum;
import com.lyn.cleansing.exception.CleansingException;
import com.lyn.cleansing.javaBean.node.DBCleansingProcessorNode;
import com.lyn.cleansing.service.processor.BaseCleansingProcessor;
import com.lyn.cleansing.service.processor.DBCleansingProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * 排序工具类
 *
 * @author lyn
 * @date 2023/8/7
 */
@Slf4j
public class ProcessorUtils {


    /**
     * 执行器排序
     * 排序逻辑：
     * 1.序号orderNo优先，降序处理
     * 2.在1优先的情况下，数据库配置优先
     */
    public static void processorSort(List<BaseCleansingProcessor> processorList) {
        Comparator<BaseCleansingProcessor> customComparator = (processor1, processor2) -> {
            // First, compare by orderNo in descending order
            Integer orderNo2 =
                    processor2.getOrderNo() == null ? LogicConstant.DEFAULT_ORDER_NO : processor2.getOrderNo();
            Integer orderNo1 =
                    processor1.getOrderNo() == null ? LogicConstant.DEFAULT_ORDER_NO : processor1.getOrderNo();
            int orderNoComparison = orderNo2.compareTo(orderNo1);
            if (orderNoComparison != 0) {
                return orderNoComparison;
            }

            // If orderNo is the same, compare by ProcessorTypeEnum
            ProcessorTypeEnum type1 = processor1.getType();
            ProcessorTypeEnum type2 = processor2.getType();
            int typeComparison = type1.compareTo(type2);
            return typeComparison;

        };
        Collections.sort(processorList, customComparator);
    }

    /**
     * 校验字段注解格式
     */
    public static boolean validColumnComment(String columnComment, String regex) {
        //匹配注释格式
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(columnComment);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 条件循环判定
     *
     * 如果多个数据库处理器中的条件和修改字段互相影响，则需要做排序修改
     * 如果有循环情况或者和指定排序不一致的情况，则报错说明
     *
     * @param processorList 排序好的执行器集合
     */
    public static void conditionCycleValid(LinkedList<BaseCleansingProcessor> processorList) {
        //过滤出所有的数据库执行器。逻辑执行器不参与循环判断
        List<DBCleansingProcessor> dbCleansingProcessors = processorList.stream()
                .filter(o -> o instanceof DBCleansingProcessor).map(o -> (DBCleansingProcessor) o)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(dbCleansingProcessors)) {

            //根据不同表分组
            Map<String, List<DBCleansingProcessor>> dbMap = dbCleansingProcessors.stream()
                    .collect(Collectors.groupingBy(o -> o.getConfigDto().getTbName()));

            //遍历每个表的执行器
            for (Entry<String, List<DBCleansingProcessor>> entry : dbMap.entrySet()) {
                //同表的数据，转化成图节点
                List<DBCleansingProcessorNode> nodeList = buildDbProcessorNode(entry);

                //遍历校验每个执行器是否有循环情况出现
                try {
                    //todo:可优化，现在会把所有节点都校验一次，但是其中同路径上的节点是重复的操作，不需要
                    for (DBCleansingProcessorNode node : nodeList) {
                        //把所有节点访问标识清除
                        nodeList.stream().forEach(o -> o.setVisitFlag(false));
                        cycleValid(node, nodeList);
                    }
                } catch (CleansingException e) {
                    log.error("存在节点循环问题，表：{}", entry.getKey());
                    throw new CleansingException("存在节点循环问题");
                }

                //根据修改字段名和条件名，修改顺序
                sortProcessorByDependency(processorList, nodeList, entry.getKey());
            }
        }
    }

    /**
     * 根据依赖先后关系，重新排序执行器
     *
     * 只排序当前排序一致的执行器，如果有冲突情况，则报错
     *
     * @param nodeList 执行器图节点集合
     * @param processorList 执行器集合
     * @param tableName 当前表名
     * @throws CleansingException 节点循环异常或者节点排序冲突
     * @Author: lyn
     * @Date: 2023/9/6
     */
    private static void sortProcessorByDependency(LinkedList<BaseCleansingProcessor> processorList,
            List<DBCleansingProcessorNode> nodeList, String tableName) {
        for (DBCleansingProcessorNode node : nodeList) {
            Integer orderNo = node.getProcessor().getOrderNo();
            if (!CollectionUtils.isEmpty(node.getChildrenNode())) {
                //查出手动指定执行顺序并且执行顺序低的子节点，报错。这里子节点需要优先执行
                List<DBCleansingProcessorNode> gtList = node.getChildrenNode().stream()
                        .filter(o -> o.getProcessor().getOrderNo().compareTo(orderNo) < 0).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(gtList)) {
                    log.error("存在节点排序问题，表：{}，涉及冲突清洗字段{}", tableName,
                            node.getProcessor().getConfigDto().getCleansingColumn());
                    throw new CleansingException("存在节点排序问题");
                }

                //查出执行顺序一样的节点，重新排序，子节点优先执行
                List<DBCleansingProcessorNode> eqList = node.getChildrenNode().stream()
                        .filter(o -> o.getProcessor().getOrderNo().equals(orderNo)).collect(Collectors.toList());

                //重新排序
                for (DBCleansingProcessorNode eqNode : eqList) {
                    processorList.remove(eqNode.getProcessor());

                    int index = processorList.indexOf(node.getProcessor());
                    processorList.add(index, eqNode.getProcessor());
                }

            }
        }
    }

    /**
     * 执行器转化为执行器节点
     *
     * 用于后续递归判断等使用
     *
     * @param tableProcessorGroup 表执行器分组集合
     * @return 转化后的执行器节点
     */
    private static List<DBCleansingProcessorNode> buildDbProcessorNode(
            Entry<String, List<DBCleansingProcessor>> tableProcessorGroup) {

        List<DBCleansingProcessorNode> nodeList = new ArrayList<>();
        List<DBCleansingProcessor> processors = tableProcessorGroup.getValue();
        //处理该表下的所有执行器，转化为node图节点
        for (DBCleansingProcessor processor : processors) {
            DBCleansingProcessorNode node = new DBCleansingProcessorNode();
            node.setProcessor(processor);

            //寻找所有清洗字段是当前执行器条件字段们 的执行器，作为子节点，子节点后续会先执行
            List<DBCleansingProcessor> childrenProcessor = processors.stream().filter((o) -> {
                if ((o.getConfigDto().getCleansingColumn().equals(processor.getConfigDto().getConditionColumn1()) || o
                        .getConfigDto().getCleansingColumn().equals(processor.getConfigDto().getConditionColumn2()) || o
                        .getConfigDto().getCleansingColumn().equals(processor.getConfigDto().getConditionColumn3()))
                        && !processor.equals(o)) {
                    return true;
                } else {
                    return false;
                }
            }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(childrenProcessor)) {
                node.setChildrenProcessors(childrenProcessor);
            }

            nodeList.add(node);
        }
        return nodeList;
    }

    /**
     * 循环校验
     *
     * @param node 当前校验节点
     * @param nodeList 全部节点集合
     * @throws CleansingException 节点循环异常
     */
    private static void cycleValid(DBCleansingProcessorNode node, List<DBCleansingProcessorNode> nodeList) {
        if (node.isVisitFlag()) {
            log.error("存在节点循环问题，当前节点清洗字段：{}", node.getProcessor().getConfigDto().getCleansingColumn());
            throw new CleansingException("存在节点循环问题");
        }
        node.setVisitFlag(true);

        //如果有子节点了，则继续递归
        if (!CollectionUtils.isEmpty(node.getChildrenProcessors())) {
            List<DBCleansingProcessorNode> childrenNodes = nodeList.stream()
                    .filter(o -> node.getChildrenProcessors().contains(o.getProcessor())).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(childrenNodes)) {
                node.setChildrenNode(childrenNodes);
                for (DBCleansingProcessorNode childrenNode : childrenNodes) {
                    cycleValid(childrenNode, nodeList);
                }
            }
        }
    }

}
