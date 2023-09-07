package com.lyn.cleansing.javaBean.node;

import java.util.List;
import com.lyn.cleansing.service.processor.DBCleansingProcessor;
import lombok.Data;

/**
 * @author lyn
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
