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