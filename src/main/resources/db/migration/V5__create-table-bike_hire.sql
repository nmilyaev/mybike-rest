create table if not exists bike_hire (
   hire_id serial not null,
   deposit numeric(8, 2),
   daily_rate numeric(8, 2),
   bike_id uuid not null,
   borrower_id uuid not null,
   constraint bike_hire_bike_id
       foreign key (bike_id)
       references bike,
    constraint bike_hire_borrower_id
       foreign key (borrower_id)
       references mybike_user,
    primary key(hire_id)
)