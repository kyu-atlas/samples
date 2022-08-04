create table "number_world" (
    id integer not null,
    hit_count integer not null default 0,
    primary key (id)
);

insert into "number_world" (id)
select x.id from generate_series(1, 10000) as x(id);
