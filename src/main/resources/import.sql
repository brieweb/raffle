---- You can use this file to load seed data into the database using SQL statements
----insert into Contestant (id, firstName, lastName) values (0, 'Brian', 'Lavender')
-- insert into Contestant (firstName, lastName, version, id) values ('Brian', 'Lavender', 0, 0) 
-- insert into Contestant (firstName, lastName, version, id) values ('Timmy', 'Tester', 0, 1)
-- insert into Contestant (firstName, lastName, version, id) values ('Joe', 'Glow', 0, 2)
-- insert into Contestant (firstName, lastName, version, id) values ('Mikey', 'Mike', 0, 3)
-- insert into Contestant (firstName, lastName, version, id) values ('Granny', 'Smith', 0, 4)

 insert into Contestant (firstName, lastName, version, wonPrize) values ('Brian', 'Lavender', 0, false) 
 insert into Contestant (firstName, lastName, version, wonPrize) values ('Timmy', 'Tester', 0, false)
 insert into Contestant (firstName, lastName, version, wonPrize) values ('Willow', 'Schlanger', 0, false)
 insert into Contestant (firstName, lastName, version, wonPrize) values ('Ima', 'Shiver', 0, false)
 insert into Contestant (firstName, lastName, version, wonPrize) values ('Seymour', 'Butts', 0, false)
 
  insert into Prize (description, won, version) values ('Beer', false, 0)
  insert into Prize (description, won, version) values ('Wine', false, 0)
  insert into Prize (description, won, version) values ('Caviar', false, 0)
  insert into Prize (description, won, version) values ('Money', false, 0)