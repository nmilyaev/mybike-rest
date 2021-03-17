create table if not exists bike (
   id uuid not null,
    make varchar(255),
    model varchar(255),
    worth numeric(19, 2),
    primary key (id)
)