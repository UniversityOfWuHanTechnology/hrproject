package com.mohress.training.dao;

import com.mohress.training.entity.audit.TblAuditRecord;

import java.util.List;

/**
 * 审核记录
 *
 * Created by youtao.wan on 2017/8/11.
 */
public interface TblAuditRecordDao {

    int insert(TblAuditRecord record);

    List<TblAuditRecord> selectByFlowIdAndAuditor(String flowId, String auditor);
}
