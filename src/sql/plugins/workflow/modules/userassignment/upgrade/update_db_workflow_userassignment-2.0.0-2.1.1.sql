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
 
 
 
 -- CONCATENATION LASTNAME FIRSTNAME
INSERT INTO workflow_task_assign_user_information
SELECT id_history, id_task, 'information_value_contact',GROUP_CONCAT(information_value SEPARATOR ' ') as name 
FROM workflow_task_assign_user_information
GROUP BY id_history, id_task;

-- INSERT WITH USER ID FOR FIRSTNAME LASTNAME
INSERT INTO workflow_task_assign_user_information
SELECT w.id_history, w.id_task, 'USER_ID', c.id_user
FROM workflow_task_assign_user_information w, core_admin_user c
WHERE concat( c.first_name,' ', c.last_name) = w.information_value
AND w.information_key = 'information_value_contact'
GROUP BY id_history, id_task ;

-- INSERT WITH USER ID FOR LASTNAME FIRSTNAME
INSERT INTO workflow_task_assign_user_information
SELECT w.id_history, w.id_task, 'USER_ID', c.id_user
FROM workflow_task_assign_user_information w, core_admin_user c
WHERE concat( c.last_name,' ', c.first_name) = w.information_value
AND w.information_key = 'information_value_contact' AND c.access_code != 'admin'
GROUP BY id_history, id_task;

-- DELETE
DELETE FROM workflow_task_assign_user_information WHERE information_key != 'USER_ID';