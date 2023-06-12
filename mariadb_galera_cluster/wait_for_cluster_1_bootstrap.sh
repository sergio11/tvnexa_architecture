#!/bin/sh

echo 'waiting for 300 seconds for MariaDB Cluster Galera Bootstrap to be accessable before starting replicant' && sh /wait-for-it.sh -t 300 mariadb_master_1:3306