create table if not exists bike_offer (
   offer_id serial not null,
   deposit numeric(8, 2),
   daily_rate numeric(8, 2),
   bike_id uuid not null,
   constraint bike_offer_bike_id
       foreign key (bike_id)
       references bike,
    primary key(offer_id)

)