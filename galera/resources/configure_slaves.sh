#!/bin/sh

while ! mysqladmin ping -h mariadb_master_1 --silent; do
  sleep 1
done
mysql -u replicant -preplicant00 -h mariadb_master_1 -e "FLUSH TABLES WITH READ LOCK;"
mysqldump -u replicant -preplicant00 -h mariadb_master_1 --all-databases --master-data --single-transaction --flush-logs --events > /tmp/master_dump.sql
log_file=`mysql -u replicant -preplicant00 -h mariadb_master_1 -e "SHOW MASTER STATUS\G" | grep File: | awk '{print $2}'`
pos=`mysql -u replicant -preplicant00 -h mariadb_master_1 -e "SHOW MASTER STATUS\G" | grep Position: | awk '{print $2}'`
slaves=("mariadb_slave_1" "mariadb_slave_2" "mariadb_slave_3")
for slave in "${slaves[@]}"
do
   echo "Configure $slave"
   while ! mysqladmin ping -h slave --silent; do
     sleep 1
   done
  mysql -u iptv -piptv00 -h slave -e "STOP SLAVE;";
  mysql -u iptv -piptv00 -h slave < /tmp/master_dump.sql
  mysql -u iptv -piptv00 -h slave -e "RESET SLAVE";
  mysql -u iptv -piptv00 -h slave -e "CHANGE MASTER TO MASTER_HOST='mariadb_master_1', MASTER_USER='replicant', MASTER_PASSWORD='replicant00', MASTER_LOG_FILE='${log_file}', MASTER_LOG_POS=${pos};"
  mysql -u iptv -piptv00 -h slave -e "start slave"
done

mysql -u replicant -preplicant00 -h mariadb_master_1 -e "UNLOCK TABLES;"