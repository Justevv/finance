For enable replication use:
CREATE ROLE replica WITH LOGIN REPLICATION PASSWORD 'sa'; on master

update pg_hba.conf in docker 
execute docker network inspect, and get subnet, then put this subnet to
host    replication     all             172.21.0.0/1            trust

update postgresql.conf in docker
wal_level = replica
max_wal_senders = 4
max_replication_slots = 4

enter docker container
docker exec -it database_slave_1 bash
clean data
rm -r /var/lib/postgresql/data/*
create backup
su - postgres -c "pg_basebackup --host=database --username=replica --pgdata=/var/lib/postgresql/data --wal-method=stream --write-recovery-conf"

to check use
select * from pg_stat_replication;