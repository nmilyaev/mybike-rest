alter table bike
   add constraint bike_owner_id
       foreign key (user_id)
       references mybike_user;
