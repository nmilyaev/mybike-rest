create table if not exists mybike.bike (
   bike_id uuid not null,
   make varchar(100),
   model varchar(100),
   worth numeric(8, 2),
   deposit numeric(8, 2),
   daily_rate numeric(8, 2),
   owner_id uuid not null,
   constraint bike_owner_id
       foreign key (owner_id)
       references mybike_user,
   primary key (bike_id)
)