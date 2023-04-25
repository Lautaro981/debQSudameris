# --- !Ups
create table token (
  id                        bigint identity(1,1) not null,
  value                     varchar(255),
  last_update               date,
  constraint pk_token       primary key (id))
;

INSERT INTO token (value, last_update) VALUES
(NULL, NULL);

create table q_customer_type (
id                        bigint identity(1,1) not null,
label                     varchar(255) not null,
id_q                      bigint not null unique,
constraint                pk_q_customer_type primary key (id))
;

INSERT INTO q_customer_type (label, id_q) VALUES
('No cliente', 2);

create table map_rule (
id                  bigint identity(1,1) not null,
segment             VARCHAR(255) not null unique,
q_customer_type_id  bigint not null,
constraint pk_rule primary key (id))
;

alter table map_rule add constraint fk_rule_q_customer_type foreign key (q_customer_type_id) references q_customer_type  (id);

create index ix_rule_q_customer_type on map_rule (q_customer_type_id);


# --- !Downs
drop index ix_rule_q_customer_type on map_rule;

alter table map_rule drop constraint fk_rule_q_customer_type ;

drop table map_rule;

drop table q_customer_type;

drop table token;
