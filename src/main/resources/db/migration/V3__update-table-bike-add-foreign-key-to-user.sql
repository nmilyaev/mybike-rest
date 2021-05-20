alter table bike
   add constraint bike_owner_id
       foreign key (owner_id)
       references mybike_user;
