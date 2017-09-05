DROP TABLE IF EXISTS tb_audit_flow;

CREATE TABLE `tb_audit_flow` (
  `id`          BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键Id',
  `flow_id`     VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '流程Id',
  `template_id` VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '项目模板Id',
  `project_id`  VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '审核项目Id',
  `node_id`     VARCHAR(64)         NOT NULL DEFAULT ''
  COMMENT '流程当前节点Id',
  `creator`     VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '审核流程发起人',
  `version`     INT(11)             NOT NULL DEFAULT '0'
  COMMENT '版本号',
  `node_status` TINYINT(11)         NOT NULL DEFAULT '0'
  COMMENT '当前节点状态，1=待审核，2=审核通过， 3=审核不通过',
  `flow_status` TINYINT(11)         NOT NULL DEFAULT '0'
  COMMENT '当前流程审核状态，1=待审核，2=审核通过， 3=审核不通过',
  `create_time` TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `update_time` TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_flow_id` (`flow_id`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_creator` (`creator`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '审核流程表';


DROP TABLE IF EXISTS tb_audit_member;

DROP TABLE IF EXISTS tb_audit_node;
CREATE TABLE tb_audit_node (
  `id`            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键Id',
  `node_id`       VARCHAR(64)         NOT NULL DEFAULT ''
  COMMENT '节点Id',
  `node_name`     VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '节点名称',
  `node_desc`     VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '节点描述信息',
  `previous_node` VARCHAR(64)         NOT NULL DEFAULT ''
  COMMENT '前置节点',
  `next_node`     VARCHAR(64)         NOT NULL DEFAULT ''
  COMMENT '后置节点',
  `audit_role_id` VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '当前节点审核角色',
  PRIMARY KEY (`id`),
  UNIQUE KEY uniq_node_id(node_id),
  KEY idx_previous_node(previous_node),
  KEY idx_next_node(next_node)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '审核节点表';

DROP TABLE IF EXISTS tb_audit_template;
CREATE TABLE tb_audit_template (
  `id`                 BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键Id',
  `template_id`        VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '项目Id',
  `template_name`      VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '项目名称',
  `template_desc`      VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '项目简单描述',
  `start_node`         VARCHAR(64)         NOT NULL DEFAULT ''
  COMMENT '起始节点',
  `end_node`           VARCHAR(64)         NOT NULL DEFAULT ''
  COMMENT '终止节点',
  `audit_flow_diagram` VARCHAR(128)        NOT NULL DEFAULT ''
  COMMENT '项目审核流程图',
  `create_time`        TIMESTAMP           NOT NULL DEFAULT current_timestamp
  COMMENT '创建时间',
  `update_time`        TIMESTAMP           NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp
  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY uniq_template_id(template_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '审核模板表';

DROP TABLE IF EXISTS tb_audit_log;
CREATE TABLE tb_audit_log (
  `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键Id',
  `record_id`    VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '审核记录Id',
  `flow_id`      VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '审核流程Id',
  `node_id`      VARCHAR(64)         NOT NULL DEFAULT ''
  COMMENT '审核节点Id',
  `auditor`      VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '审核人Id',
  `action`       TINYINT(11)         NOT NULL DEFAULT -1
  COMMENT '审核动作，0=初始化审核流程 1=审核通过动作，2=审核否决动作 3=撤销动作，4=回退动作,',
  `audit_result` VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '审核结果',
  `create_time`  TIMESTAMP           NOT NULL DEFAULT current_timestamp
  COMMENT '创建时间',
  `update_time`  TIMESTAMP           NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp
  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY uniq_record_id(record_id),
  KEY idx_flow_id(flow_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '项目审核日志记录表';


DROP TABLE IF EXISTS tb_class_audit_record;
CREATE TABLE `tb_class_audit_record` (
  `id`            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键Id',
  `record_id`     VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '记录Id',
  `flow_id`     VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '审核流程Id',
  `class_id`      VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '班级Id',
  `class_name`    VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '班级名称',
  `agency_id`     VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '机构Id',
  `agency_name`   VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '机构名称',
  `audit_role_id` VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '审核角色Id',
  `auditor`       VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '实际审核人',
  `audit_result`  VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '审核结果',
  `audit_status`  TINYINT(11)         NOT NULL DEFAULT '1'
  COMMENT '审核状态，1=等待审核， 2=审核通过，3=审核不通过',
  `apply_time`   TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '申请时间',
  `create_time`   TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `update_time`   TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_audit_role_id_flow_id` (`audit_role_id`, `flow_id`),
  KEY idx_audit_role_id_agency_id(`audit_role_id`, `agency_id`, `id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 30
  DEFAULT CHARSET = utf8mb4
  COMMENT = '课程审核记录表';


  DROP TABLE IF EXISTS tb_ledger_audit_record;
CREATE TABLE `tb_ledger_audit_record` (
  `id`            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键Id',
  `record_id`     VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '记录Id',
  `flow_id`       VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '审核流程Id',
  `ledger_id`     VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '台账Id',
  `agency_id`     VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '机构Id',
  `course_id`     VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '课程Id',
  `class_id`      VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '班级Id',
  `key_word`      VARCHAR(128)        NOT NULL DEFAULT ''
  COMMENT '搜索关键词，格式定义：培训机构名,培训课程,培训班级',
  `audit_role_id` VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '审核角色Id',
  `auditor`       VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '实际审核人',
  `audit_result`  VARCHAR(32)         NOT NULL DEFAULT ''
  COMMENT '审核结果',
  `audit_status`  TINYINT(11)         NOT NULL DEFAULT  1
  COMMENT '审核状态，1=等待审核， 2=审核通过，3=审核不通过',
  `create_time`   TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  `update_time`   TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_audit_role_id_flow_id` (`audit_role_id`, `flow_id`),
  KEY `idx_audit_role_id_agency_id` (`audit_role_id`, `agency_id`, `id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 31
  DEFAULT CHARSET = utf8mb4
  COMMENT = '台账审核记录表';


INSERT INTO mohress_training.tb_audit_template (template_id, template_name, template_desc, start_node, end_node, audit_flow_diagram, create_time, update_time) VALUES ('Class_audit_template', '培训课程审核模板', '', 'Node_class_audit_people_society', '', '', '2017-08-23 17:13:18', '2017-08-23 17:13:46');
INSERT INTO mohress_training.tb_audit_template (template_id, template_name, template_desc, start_node, end_node, audit_flow_diagram, create_time, update_time) VALUES ('Ledger_audit_template', '台账审核模板', '', 'Node_ledger_audit_people_society', 'Node_ledger_audit_finance', '', '2017-08-25 22:31:15', '2017-09-05 20:33:53');

INSERT INTO mohress_training.tb_audit_node (node_id, node_name, node_desc, previous_node, next_node, audit_role_id) VALUES ('Node_class_audit_people_society', '培训班级-人社局审核节点', '', '', '', '17081610225621055996');
INSERT INTO mohress_training.tb_audit_node (node_id, node_name, node_desc, previous_node, next_node, audit_role_id) VALUES ('Node_ledger_audit_people_society', '台账-人社科审核节点', '', '', 'Node_ledger_audit_people_society_boss', '17081610225621055996');
INSERT INTO mohress_training.tb_audit_node (node_id, node_name, node_desc, previous_node, next_node, audit_role_id) VALUES ('Node_ledger_audit_finance', '台账-财政局审核节点', '', 'Node_ledger_audit_people_society', '', '17081610225621055998');
INSERT INTO mohress_training.tb_audit_node (node_id, node_name, node_desc, previous_node, next_node, audit_role_id) VALUES ('Node_ledger_audit_people_society_boss', '台账-人社局审核节点', '', 'Node_ledger_audit_people_society', 'Node_ledger_audit_finance', '17081610225621055999');