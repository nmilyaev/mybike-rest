create table if not exists bike (
   bike_id uuid not null,
   make varchar(100),
   model varchar(100),
   value numeric(8, 2),
   owner_id uuid not null,
   primary key (bike_id)
)