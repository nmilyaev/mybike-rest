create table if not exists mybike_user (
    id uuid not null,
    firstname varchar(50) not null,
    surname varchar(50) not null,
    address varchar(255),
    postcode varchar(10) not null,
    city varchar(50),
    email varchar(100) not null,
    phone varchar(20),
    password varchar(50) not null,
    primary key (id)
)