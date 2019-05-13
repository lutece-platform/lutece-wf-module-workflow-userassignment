DROP TABLE IF EXISTS workflow_resource_user;

CREATE TABLE workflow_resource_user (
	id int AUTO_INCREMENT,
	id_resource INT DEFAULT 0 NOT NULL,
	resource_type VARCHAR(255) DEFAULT NULL,
	id_user INT DEFAULT NULL,
	assignment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	is_active SMALLINT NOT NULL DEFAULT '0',
	PRIMARY KEY (id)
);

CREATE INDEX workflow_resource_user_id_resource_fk ON workflow_resource_user(id_resource);
CREATE INDEX workflow_resource_user_resource_type_fk ON workflow_resource_user(resource_type);
CREATE INDEX workflow_resource_user_id_admin_fk ON workflow_resource_user(id_user);