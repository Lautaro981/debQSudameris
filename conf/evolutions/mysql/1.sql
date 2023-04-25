--concat(date(NOW() + INTERVAL 1 DAY), ' 00:00:00')
# --- !Ups

create table s_server (
  id                        bigint auto_increment not null,
  server_name               varchar(255),
  last_activity             datetime,
  online                    bit default 0,
  url                       varchar(100),
  constraint pk_s_server primary key (id))
;

create table s_server_task (
  task_type                 varchar(255) not null,
  id                        bigint auto_increment not null,
  status                    varchar(9),
  last_run                  datetime,
  interval_in_minutes       integer,
  for_all                   tinyint(1) default 0,
  next_run                  datetime,
  init_exclusion_time       time,
  end_exclusion_time        time,
  last_error                text,
  failure_count             integer,
  extra_data                text,
  one_shot                  tinyint(1) default 0,
  visible                   tinyint(1) default 0,
  company_id                bigint,
  branch_id                 bigint,
  interval_in_seconds       bigint,
  name                      varchar(255),
  system                    varchar(255),
  node_name                 varchar(255),
  constraint ck_s_server_task_status check (status in ('EXECUTING','IDLE','STOPPED')),
  constraint pk_s_server_task primary key (id));

create table s_server_task_server (
  s_server_task_id              bigint not null,
  s_server_id                   bigint not null,
  constraint pk_s_server_task_s_server primary key (s_server_task_id, s_server_id));

create TABLE server_log (
id						BIGINT auto_increment not null,
servers					varchar(255),
task_id				    bigint,
created_at				datetime,
ready                   bit(1),
CONSTRAINT pk_server_logs primary key (id)
);

alter table s_server_task_server add constraint fk_s_server_task_server_task_id foreign key (s_server_task_id) references s_server_task (id) on delete restrict on update restrict;
alter table s_server_task_server add constraint fk_s_server_task_server_server_id foreign key (s_server_id) references s_server (id) on delete restrict on update restrict;

INSERT INTO `s_server_task` (`task_type`, `status`, `interval_in_minutes`, `interval_in_seconds`, `system`, `next_run`, `for_all`, `one_shot`, `visible`, `failure_count`, `name`) VALUES
('UpdateForAll', 'IDLE', 1,60, 'CONECTORBINGOAGG', NOW(), 0, 0, 0, 0, 'ACTUALIZACION DE SERVIDORES ONLINE');

# --- !Downs

drop table s_server_task_server ;
drop table s_server_task ;
drop table s_server;
drop table server_log;
