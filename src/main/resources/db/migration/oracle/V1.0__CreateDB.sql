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


