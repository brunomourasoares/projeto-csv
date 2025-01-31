create table users
(
	id	integer	generated always as identity	primary key,
	nome	varchar(100)	not null,
	email	varchar(100)	not null	unique,
	telefone	varchar(20)	not null
);