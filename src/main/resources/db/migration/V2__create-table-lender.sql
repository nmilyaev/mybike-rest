create table if not exists lender (
    id uuid not null,
    firstname varchar(50) not null,
    surname varchar(50) not null,
    address varchar(255),
    postcode varchar(10),
    city varchar(50),
    email varchar(100) not null,
    phone varchar(20),
    primary key (id)
)