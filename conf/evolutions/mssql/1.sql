# --- !Ups

create table s_server (
  id                        numeric(19) identity(1,1) not null,
  server_name               varchar(255),
  last_activity             datetime2(0),
  online                    bit,
  url                       VARCHAR(100),
  constraint pk_s_server primary key (id)
);

create table s_server_task (
  task_type                 varchar(255) not null,
  id                        numeric(19) identity(1,1) not null,
  status                    varchar(9),
  last_run                  datetime2(0),
  interval_in_minutes       integer,
  for_all                   bit,
  next_run                  datetime2(0),
  init_exclusion_time       time(0),
  end_exclusion_time        time(0),
  last_error                varchar(max),
  failure_count             integer,
  extra_data                varchar(max),
  one_shot                  bit,
  visible                   bit,
  company_id                numeric(19),
  branch_id                 numeric(19),
  interval_in_seconds       bigint,
  name                      varchar(255)  NOT NULL,
  system                    varchar(255),
  node_name                 varchar(255),
  constraint ck_s_server_task_status check (status in ('EXECUTING','IDLE','STOPPED')),
  constraint pk_s_server_task primary key (id)
);

create table s_server_task_server (
s_server_task_id               numeric(19) not null,
s_server_id                    numeric(19) not null,
constraint pk_s_server_tasks_s_server primary key (s_server_task_id, s_server_id)
);

create TABLE server_log (
    id						    numeric(19) identity(1,1) not null,
    servers					  varchar(255),
    task_id					  numeric(19),
    created_at				datetime2(0),
    ready             bit,
    CONSTRAINT pk_server_logs primary key (id)
);


alter table s_server_task add constraint df_s_server_task_for_all default 0 for for_all;
alter table s_server_task add constraint df_s_server_task_one_shot default 0 for one_shot;
alter table s_server_task add constraint df_s_server_task_visible default 0 for visible;


alter table s_server add constraint df_s_server_online default 0 for online;
alter table s_server_task_server add constraint fk_s_server_task_server_task_id foreign key (s_server_task_id) references s_server_task (id);
alter table s_server_task_server add constraint fk_s_server_task_server_server_id foreign key (s_server_id) references s_server (id);


insert into s_server_task (task_type, status, interval_in_minutes,interval_in_seconds, system, next_run, for_all, one_shot, visible, failure_count, name) values
('UpdateForAll', 'IDLE', 1,60, 'CONECTOBASE', getdate(), 0, 0, 0, 0, 'ACTUALIZACION DE SERVIDORES ONLINE');

# --- !Downs

drop table s_server_task_server ;
drop table s_server_task ;
drop table s_server;
drop table server_log;
