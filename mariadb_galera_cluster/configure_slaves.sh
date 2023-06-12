#!/bin/sh
echo "Waiting for cluster setup to create Master/Slave configuration"
sleep 120
while ! mysqladmin ping -u replicant -preplicant00 -h mariadb_master_1 --silent; do
  sleep 1
done
mysql -u replicant -preplicant00 -h mariadb_master_1 -e "FLUSH TABLES WITH READ LOCK;"
mysqldump -u replicant -preplicant00 -h mariadb_master_1 --all-databases --master-data --single-transaction --flush-logs --events > /tmp/master_dump.sql
log_file=`mysql -u replicant -preplicant00 -h mariadb_master_1 -e "SHOW MASTER STATUS\G" | grep File: | awk '{print $2}'`
pos=`mysql -u replicant -preplicant00 -h mariadb_master_1 -e "SHOW MASTER STATUS\G" | grep Position: | awk '{print $2}'`
echo "Configure mariadb_slave_1"
   while ! mysqladmin ping -u iptv -piptv00 -h mariadb_slave_1 --silent; do
     sleep 1
   done
mysql -u iptv -piptv00 -h mariadb_slave_1 -e "STOP SLAVE;";
mysql -u iptv -piptv00 -h mariadb_slave_1 < /tmp/master_dump.sql
mysql -u iptv -piptv00 -h mariadb_slave_1 -e "RESET SLAVE;";
mysql -u iptv -piptv00 -h mariadb_slave_1 -e "CHANGE MASTER TO MASTER_HOST='mariadb_master_1', MASTER_USER='replicant', MASTER_PASSWORD='replicant00', MASTER_LOG_FILE='${log_file}', MASTER_LOG_POS=${pos};"
mysql -u iptv -piptv00 -hmariadb_slave_1 -e "START SLAVE;"

mysql -u replicant -preplicant00 -h mariadb_master_1 -e "UNLOCK TABLES;"
echo "Configuration finished"
exit 0