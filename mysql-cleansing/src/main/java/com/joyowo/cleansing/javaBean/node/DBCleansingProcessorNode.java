package com.joyowo.cleansing.javaBean.node;

import java.util.List;
import com.joyowo.cleansing.service.processor.DBCleansingProcessor;
import lombok.Data;

/**
 * @author linkaidi
 * @date 2023/9/5
 */
@Data
public class DBCleansingProcessorNode {

    /**
     * 当前节点对象
     */
    private DBCleansingProcessor processor;

    /**
     * 当前节点对象关联子处理器
     */
    private List<DBCleansingProcessor> childrenProcessors;

    /**
     * 所有子节点
     */
    private List<DBCleansingProcessorNode> childrenNode;

    /**
     * 当前节点是否访问过
     */
    private boolean visitFlag = false;

}
