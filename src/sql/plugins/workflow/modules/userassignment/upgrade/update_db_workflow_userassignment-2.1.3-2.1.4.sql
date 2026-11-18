--liquibase formatted sql
--changeset workflow-userassignment:update_db_workflow_userassignment-2.1.3-2.1.4.sql
--preconditions onFail:MARK_RAN onError:WARN
-- Removes what tasks left behind once removed. Only rows whose task or history no longer exists are deleted.
DELETE FROM workflow_task_assign_user_information WHERE NOT EXISTS ( SELECT 1 FROM workflow_task t WHERE t.id_task = workflow_task_assign_user_information.id_task );
DELETE FROM workflow_task_assign_user_config WHERE NOT EXISTS ( SELECT 1 FROM workflow_task t WHERE t.id_task = workflow_task_assign_user_config.id_task );
DELETE FROM workflow_task_userassignment_assignment_notification_cf WHERE NOT EXISTS ( SELECT 1 FROM workflow_task t WHERE t.id_task = workflow_task_userassignment_assignment_notification_cf.id_task );
DELETE FROM workflow_task_assign_user_information WHERE NOT EXISTS ( SELECT 1 FROM workflow_resource_history h WHERE h.id_history = workflow_task_assign_user_information.id_history );
