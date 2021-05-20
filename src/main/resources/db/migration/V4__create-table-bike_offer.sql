create table if not exists bike_offer (
   user_id uuid not null,
   bike_id uuid not null,
   constraint bike_offer_bike_id
       foreign key (bike_id)
       references bike,
    constraint bike_offer_user_id
       foreign key (user_id)
       references mybike_user,
    constraint bike_offer_pk primary key(user_id, bike_id)
)