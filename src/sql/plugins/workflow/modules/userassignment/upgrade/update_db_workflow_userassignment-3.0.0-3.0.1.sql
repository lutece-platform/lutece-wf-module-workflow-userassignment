--liquibase formatted sql
--changeset workflow-userassignment:update_db_workflow_userassignment-3.0.0-3.0.1.sql
--preconditions onFail:MARK_RAN onError:WARN
-- LUT-33153 (FreeMarker HTML auto-escaping): the message of workflow_task_userassignment_assignment_notification_cf is a
-- FreeMarker template (markers agent_name, resourceId, resourceType) rendered with HTML auto-escaping. Hand escaping
-- (?html, ?xhtml, ?xml, ?rtf, ?web_safe) no longer parses: the notification would fail at run time. It is removed;
-- auto-escaping escapes the marker values. The subject is not a template: unchanged.
-- This script rewrites site data (table workflow_task_userassignment_assignment_notification_cf, column message).
-- Idempotent: a message without these built-ins is not updated.
-- Not handled, to review by hand: <#escape>/<#noescape> directives, and a built-in inside a <script> of a message
-- (it needs ?js_string there). To list them before the upgrade:
--   SELECT id_task FROM workflow_task_userassignment_assignment_notification_cf WHERE message LIKE '%<#escape%' OR message LIKE '%<script%';
UPDATE workflow_task_userassignment_assignment_notification_cf SET message = REPLACE( REPLACE( REPLACE( REPLACE( REPLACE( message, '?html', '' ), '?xhtml', '' ), '?xml', '' ), '?rtf', '' ), '?web_safe', '' )
  WHERE message LIKE '%?html%' OR message LIKE '%?xhtml%' OR message LIKE '%?xml%' OR message LIKE '%?rtf%' OR message LIKE '%?web_safe%';
