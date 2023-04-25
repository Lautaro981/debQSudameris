# --- !Ups

create table attribute (
  id                        bigint identity(1,1) not null,
  attribute                 varchar(255),
  attribute_value           varchar(max),
  service_id                bigint,
  constraint pk_attribute   primary key (id))
;

create table service (
  id                        bigint identity(1,1) not null,
  name                      varchar(255),
  constraint pk_service     primary key (id))
;

alter table attribute add constraint fk_attribute_service_1 foreign key (service_id) references service (id);
create index ix_attribute_service_1 on attribute (service_id);

# --- !Downs

alter table attribute drop constraint fk_attribute_service_1 ;
drop index ix_attribute_service_1 on attribute ;

drop table service;

drop table attribute;
