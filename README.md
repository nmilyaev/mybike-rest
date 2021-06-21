# mybike-rest

A new wave in sharing society.

The site users can offer their unused bikes to other users in a safe way and make some cash. The renter can require the 
borrower pay daily amount of money for their bike's hire and can require a deposit to be paid before the start of hire.
 
It is assumed if the bike is not returned or returned damaged, the renter can withold all or part of the deposit as a 
compensation.  

Current application is a rest-based backend engine for the site.

# Main entities
## User 

Represents site user. Users can own multiple bikes that are offered for hire and users can hire other users' bikes.
 
## Bike

Represents a bike offered for hire. A bike has value, asked daily rate and a deposit. 

## Bike Hire
Represents an agreement between two users to hire a bike. 

A bike hire has a value, agreed daily rate and a deposit, that could differ from the asked ones.

Hire rules (per bike):
1. Minimum hire is one day, when startDate = returnDate. 
2. A new hire can only start the next day after an existing hire ends, 
or it can end one day before the pre-existing hire starts. No hire overlaps are allowed. 
3. Start date can't be after end date.
4. New hire can't be created in the past.