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

-- changeset workflow-userassignment:update_db_workflow_userassignment-3.0.0-3.0.1.sql-decode-entities
-- preconditions onFail:MARK_RAN onError:WARN
-- comment decodes, once, the HTML entities the removed input filter of lutece-core 8 (sanitizeFilterMode, LUT-34109) wrote into the plain text values: the subject, the sender and the recipients of the assignment notification. Its message, typed with the rich text editor, is left as it is, as is workflow_task_assign_user_information (history).
-- This changeset rewrites the data of the site. Back up the database before the upgrade. It must run ONCE only: run
-- again, it would decode a second time a text that really holds an entity (a typed &lt; stored as &amp;lt; becomes &lt;
-- once, then < the second time). Only the values matching LIKE '%&%;%' are concerned; to list them before the upgrade,
-- run the WHERE clauses below in a SELECT.
UPDATE workflow_task_userassignment_assignment_notification_cf SET subject = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(subject, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE subject LIKE '%&%;%';
UPDATE workflow_task_userassignment_assignment_notification_cf SET sender_name = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(sender_name, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE sender_name LIKE '%&%;%';
UPDATE workflow_task_userassignment_assignment_notification_cf SET sender_email = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(sender_email, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE sender_email LIKE '%&%;%';
UPDATE workflow_task_userassignment_assignment_notification_cf SET recipients_cc = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(recipients_cc, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE recipients_cc LIKE '%&%;%';
UPDATE workflow_task_userassignment_assignment_notification_cf SET recipients_bcc = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(recipients_bcc, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE recipients_bcc LIKE '%&%;%';

-- changeset workflow-userassignment:update_db_workflow_userassignment-3.0.0-3.0.1.sql-decode-entities-history
-- preconditions onFail:MARK_RAN onError:WARN
-- comment decodes, once, the HTML entities the removed input filter of lutece-core 8 (sanitizeFilterMode, LUT-34109) wrote into workflow_task_assign_user_information, which the back office shows: the e-mail of the notified user (key INFORMATION, copied from core_admin_user.email, which the core decodes). The user ids (key USER_ID) and the keys (identifiers) are left as they are.
-- This changeset rewrites the data of the site. Back up the database before the upgrade. It must run ONCE only: run
-- again, it would decode a second time a text that really holds an entity (a typed &lt; stored as &amp;lt; becomes &lt;
-- once, then < the second time). Only the values matching LIKE '%&%;%' are concerned; to list them before the upgrade,
-- run the WHERE clauses below in a SELECT.
UPDATE workflow_task_assign_user_information SET information_value = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(information_value, '&#39;', ''''), '&#x27;', ''''), '&#34;', '"'), '&quot;', '"'), '&#96;', '`'), '&#61;', '='), '&#64;', '@'), '&#43;', '+'), '&lt;', '<'), '&gt;', '>'), '&amp;', '&') WHERE information_value LIKE '%&%;%' AND information_key = 'INFORMATION';
