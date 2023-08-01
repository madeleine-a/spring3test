create table local_date_time_entity
(
    created    timestamp(6) not null,
    date_value timestamp(6) not null,
    updated    timestamp(6) not null,
    version    bigint not null,
    id bigint GENERATED ALWAYS AS IDENTITY primary key 
);

create table instant_entity
(
    created    timestamp(6) not null,
    date_value timestamp(6) not null,
    updated    timestamp(6) not null,
    version    bigint not null,
    id bigint GENERATED ALWAYS AS IDENTITY primary key
);



create table offset_date_time_entity
(
    created    timestamp(6) not null,
    date_value timestamp(6) not null,
    updated    timestamp(6) not null,
    version    bigint not null,
    id bigint GENERATED ALWAYS AS IDENTITY primary key
);



create table zoned_date_time_entity
(
    created    timestamp(6) not null,
    date_value timestamp(6) not null,
    updated    timestamp(6) not null,
    version    bigint not null,
    id bigint GENERATED ALWAYS AS IDENTITY primary key
);


create table instant_with_zone_entity
(
    created    timestamp(6) with time zone not null,
    date_value timestamp(6) with time zone not null,
    updated    timestamp(6) with time zone not null,
    version    bigint not null,
    id bigint GENERATED ALWAYS AS IDENTITY primary key
);



create table offset_date_time_with_zone_entity
(
    created    timestamp(6) with time zone not null,
    date_value timestamp(6) with time zone not null,
    updated    timestamp(6) with time zone not null,
    version    bigint not null,
    id bigint GENERATED ALWAYS AS IDENTITY primary key
);



create table zoned_date_time_with_zone_entity
(
    created    timestamp(6) with time zone not null,
    date_value timestamp(6) with time zone not null,
    updated    timestamp(6) with time zone not null,
    version    bigint not null,
    id bigint GENERATED ALWAYS AS IDENTITY primary key
);


