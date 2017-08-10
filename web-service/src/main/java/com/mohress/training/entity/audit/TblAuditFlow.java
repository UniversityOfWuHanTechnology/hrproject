package com.mohress.training.entity.audit;

import lombok.Data;

import java.util.Date;

/**
 * 审批流程
 *
 * Created by youtao.wan on 2017/8/10.
 */
@Data
public class TblAuditFlow {

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 流程Id(唯一键)
     */
    private String flowId;

    /**
     * 流程当前节点Id
     */
    private String nodeId;

    /**
     * 流程当前节点状态
     */
    private Integer nodeStatus;

    /**
     * 审核流程发起人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
