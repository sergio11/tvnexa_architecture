CREATE USER 'monitor'@'%' IDENTIFIED BY 'monitor';
GRANT ALL PRIVILEGES on *.* TO 'monitor'@'%';
FLUSH PRIVILEGES;