create schema hmp_schema;
create table hmp_schema.warned_workers
(
    id        serial  not null
        constraint warned_workers_pk
            primary key,
    workerId  integer not null,
    timestamp bigint  not null,
    interval  integer not null
);
create table hmp_schema.setting
(
    id                                     serial        not null
        constraint menu_conf_pk primary key,
    contact_tracing_min_distance_mt        decimal(2, 1) not null,
    contact_tracing_min_time_contact_sec   int           not null,
    contact_tracing_time_frame_contact_sec int           not null,
    contact_tracing_data_retention_day     int           not null,
    contact_tracing_lookup_previous_day    int           not null,
    contact_tracing_deep_level             int           not null,
    contact_tracing_enabled                boolean       not null,
    social_distancing_min_time_contact_sec int           not null,
    social_distancing_min_distance_mt      decimal(2, 1) not null,
    social_distancing_data_retention_day   int           not null,
    secret_mdm                             varchar       not null
);
create table hmp_schema.buildings
(
    id         serial         not null
        constraint buildings_pk
            primary key,
    name       varchar(255)   not null,
    latitude   decimal(10, 7) not null,
    longitude  decimal(10, 7) not null,
    radius     decimal(10, 2) not null,
    maxworkers integer default NULL
);
create table hmp_schema.floors
(
    id         serial       not null
        constraint floors_pk
            primary key,
    name       varchar(255) not null,
    buildingid integer      not null,
    maxworkers integer default NULL
);
create table hmp_schema.location
(
    id         serial       not null
        constraint location_pk
            primary key,
    name       varchar(255) not null,
    floorid    integer      not null,
    maxworkers integer default NULL
);
create table hmp_schema.locationsbeaconsmatrix
(
    locationid integer not null,
    beaconid   integer not null
);
create table hmp_schema.movements
(
    id        serial                              not null
        constraint movements_pk
            primary key,
    workerid  integer                             not null,
    beaconid  integer                             not null,
    timestamp timestamp default CURRENT_TIMESTAMP not null
);
create table hmp_schema.devices
(
    id                  serial                                  not null
        constraint devices_pk primary key,
    workerid            integer                                 not null,
    uuid                varchar(255)  default NULL :: character varying,
    id_device_model     integer,
    os_version          varchar(255)  default NULL :: character varying,
    os_name             varchar(255)  default NULL :: character varying,
    device_manufacturer varchar(255)  default NULL :: character varying,
    device_model        varchar(255)  default NULL :: character varying,
    distance_reference  numeric(6, 2) default NULL :: numeric,
    measured_rssi       numeric(6, 2) default NULL :: numeric,
    tokennotification   varchar(255)  default NULL :: character varying,
    mark                varchar(255)  default NULL :: character varying,
    secret              varchar(255),
    active              boolean       default true,
    static              boolean       default false,
    language            varchar(10)   default 'en',
    createdat           timestamp     default CURRENT_TIMESTAMP not null,
    updatedat           timestamp     default CURRENT_TIMESTAMP not null
);
create table hmp_schema.device_model
(
    id                 serial                                          not null
        constraint device_model_pk primary key,
    manufacturer       varchar(255)    default '' :: character varying not null,
    model              varchar(255)    default '' :: character varying not null,
    reference_distance numeric(3, 2)   default NULL :: numeric,
    tx_power           integer,
    path_loss          numeric(4, 2)   default NULL :: numeric,
    standard_deviation smallint,
    received_power     numeric(19, 15) default NULL :: numeric,
    type               integer         default NULL,
    unique (manufacturer, model)
);
comment on column hmp_schema.device_model.type is '0:''smartphone'',
1:''wearable''';
alter table hmp_schema.devices
    add constraint devices_device_model_id_fk foreign key (id_device_model) references hmp_schema.device_model on update restrict on delete set null;
create table hmp_schema.interactions_clean
(
    id            serial                              not null
        constraint interactions_clean_pkey primary key,
    device_id_1   integer                             not null,
    device_id_2   integer                             not null,
    rssi          integer                             not null,
    distance      numeric(4, 2)                       not null,
    timestamp     timestamp                           not null,
    tx_power      double precision,
    time_interval integer   default 1                 not null,
    platform      integer                             not null,
    createdat     timestamp default CURRENT_TIMESTAMP not null,
    updatedat     timestamp
);
comment on column hmp_schema.interactions_clean.platform is '1: ios, 2: android';
create index interactions_clean_timestamp_index on hmp_schema.interactions_clean (timestamp);
create table hmp_schema.interactions_raw
(
    id            serial                              not null
        constraint interactions_raw_pk primary key,
    device_id_1   integer                             not null,
    device_id_2   integer                             not null,
    rssi          integer                             not null,
    distance      numeric(4, 2)                       not null,
    timestamp     timestamp                           not null,
    tx_power      double precision,
    time_interval integer   default 1                 not null,
    platform      integer                             not null,
    num_signals   integer   default 1                 not null,
    createdat     timestamp default CURRENT_TIMESTAMP not null,
    updatedat     timestamp
);
comment on column hmp_schema.interactions_raw.platform is '1: ios, 2: android';
create index interactions_raw_timestamp_index on hmp_schema.interactions_raw (timestamp);
create table hmp_schema.users
(
    id            serial                              not null
        constraint users_pk primary key,
    email         varchar(50)                         not null
        constraint users_email_key unique,
    username      varchar(50)                         not null
        constraint users_username_key unique,
    password      varchar(255)                        not null,
    name          varchar(30)                         not null,
    surname       varchar(30)                         not null,
    createdat     timestamp default CURRENT_TIMESTAMP not null,
    legalEntityId int       default 1                 not null,
    role          smallint  default 0                 not null
);
comment on column hmp_schema.users.role is '0:''admin'',
1:''manager'',
2:''frontDesk'',
3:''digitalSignage''';
create table hmp_schema.cells
(
    id   serial      not null
        constraint cells_pk primary key,
    name varchar(30) not null
);
create table hmp_schema.workers
(
    id                      serial                              not null
        constraint workers_pk primary key,
    email                   varchar(50)                              not null,
    name                    varchar(30)                              not null,
    surname                 varchar(30)                              not null,
    cellId                  int          default 1                   not null,
    createdat               timestamp    default CURRENT_TIMESTAMP   not null,
    enabled                 boolean      default true                not null,
    contact_tracing_enabled boolean      default true                not null,
    geofencing_enabled      boolean      default true                not null,
    lastupdate              timestamp    default CURRENT_TIMESTAMP   not null,
    type                    int          default 0                   not null,
    expirein                timestamp    default null,
    legalEntityId           int          default 1                   not null,
    phone_num               varchar      default NULL,
    matricula               varchar      default null,
    city                    varchar      default null,
    buildingId              int          default null, 
    orgUnitCode             varchar          default null, 
    guestSubject            varchar      default null
);

create table hmp_schema.secure_contacts
(
    id               serial                  not null
        constraint secure_contacts_pk primary key,
    first_worker_id  int                     not null,
    second_worker_id int                     not null,
    created_at       timestamp default now() not null,
    reason           int                     not null
);
comment on column hmp_schema.secure_contacts.reason is '0:''secure condition'',
1:''secure contact'',
2:''anomaly''';

create table hmp_schema.legal_entities
(
    id           serial  not null
        constraint legal_entities_pk primary key,
    legal_entity varchar not null
        constraint legal_entity_key unique,
    country      varchar not null
);

create table hmp_schema.contact_tracing_logs
(
    id        serial                  not null
        constraint contact_tracing_logs_pk primary key,
    timestamp timestamp default now() not null,
    count     int                     not null
);

comment on column hmp_schema.workers.type is '0:''employer'',
1:''internal'',
2:''external''';