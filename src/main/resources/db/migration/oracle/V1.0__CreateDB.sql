create table json_holder (
    id NUMBER GENERATED ALWAYS AS IDENTITY primary key,
    aggregate blob,
    CHECK (aggregate is json),
    check (json_value(aggregate, '$.attribute1') is not null)
);

create table json_entity
(
    trapp varchar2(1000) not null,
    vanlig_string varchar2(1000) not null,
    trapp_lista clob,
    date_value timestamp,
    id NUMBER GENERATED ALWAYS AS IDENTITY primary key
);

create table local_date_time_entity
(
    created    timestamp not null,
    date_value timestamp not null,
    updated    timestamp not null,
    version    number not null,
    id NUMBER GENERATED ALWAYS AS IDENTITY primary key
);

create table instant_entity
(
    created    timestamp not null,
    date_value timestamp not null,
    updated    timestamp not null,
    version    number not null,
    id NUMBER GENERATED ALWAYS AS IDENTITY primary key
);

create table instant_with_six_entity
(
    created    timestamp not null,
    date_value timestamp(6) not null,
    updated    timestamp not null,
    version    number not null,
    id NUMBER GENERATED ALWAYS AS IDENTITY primary key
);

create table offset_date_time_entity
(
    created    timestamp not null,
    date_value timestamp not null,
    updated    timestamp not null,
    version    number not null,
    id NUMBER GENERATED ALWAYS AS IDENTITY primary key
);



create table zoned_date_time_entity
(
    created    timestamp not null,
    date_value timestamp not null,
    updated    timestamp not null,
    version    number not null,
    id NUMBER GENERATED ALWAYS AS IDENTITY primary key
);


create table instant_with_zone_entity
(
    created    timestamp with time zone not null,
    date_value timestamp with time zone not null,
    updated    timestamp with time zone not null,
    version    number not null,
    id NUMBER GENERATED ALWAYS AS IDENTITY primary key
);



create table offset_date_time_with_zone_entity
(
    created    timestamp with time zone not null,
    date_value timestamp with time zone not null,
    updated    timestamp with time zone not null,
    version    number not null,
    id NUMBER GENERATED ALWAYS AS IDENTITY primary key
);



create table zoned_date_time_with_zone_entity
(
    created    timestamp with time zone not null,
    date_value timestamp with time zone not null,
    updated    timestamp with time zone not null,
    version    number not null,
    id NUMBER GENERATED ALWAYS AS IDENTITY primary key
);


