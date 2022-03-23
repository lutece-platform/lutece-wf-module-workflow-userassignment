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