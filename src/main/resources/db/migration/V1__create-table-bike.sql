create table if not exists bike (
   bike_id uuid not null,
   make varchar(100),
   model varchar(100),
   worth numeric(19, 2),
   primary key (bike_id)
)