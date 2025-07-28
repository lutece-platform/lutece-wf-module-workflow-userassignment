--liquibase formatted sql
--changeset workflow-userassignment:create_db_userassignment.sql
--preconditions onFail:MARK_RAN onError:WARN
DROP TABLE IF EXISTS workflow_task_assign_user_config;
CREATE TABLE workflow_task_assign_user_config (
	 id_task INT DEFAULT 0 NOT NULL ,
	 provider_name varchar(100) default NULL,
	 PRIMARY KEY (id_task)
);

DROP TABLE IF EXISTS workflow_task_assign_user_information;
CREATE TABLE workflow_task_assign_user_information (
  id_history INT NOT NULL,
  id_task INT NOT NULL,
  information_key VARCHAR(255) NOT NULL,
  information_value VARCHAR(255) NULL
);

/*==================================================================*/
/* Table structure for table workflow_task_userassignment_assignment_notification_cf */
/*==================================================================*/
DROP TABLE IF EXISTS workflow_task_userassignment_assignment_notification_cf;
CREATE TABLE workflow_task_userassignment_assignment_notification_cf (
  id_task INT NOT NULL,
  message LONG VARCHAR NOT NULL,
  subject LONG VARCHAR NOT NULL,
  sender_name VARCHAR(255) DEFAULT NULL,
  sender_email VARCHAR(255) DEFAULT NULL,
  recipients_cc LONG VARCHAR DEFAULT NULL,
  recipients_bcc LONG VARCHAR DEFAULT NULL,
  PRIMARY KEY (id_task)
 ) ;