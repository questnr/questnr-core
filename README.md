Open for contribution!

# All the credentials have been revoked!

# Project Setup
- Make directory path
> sudo mkdir /var/quest_nr/indexes
- Give permission to access and write permission
> sudo chmod a+rwx /var/quest_nr/indexes

# Run postgres locally
1. Install Postgres
2. To start postgres service, pg_ctl -D /usr/local/var/postgres start
3. To use postgres service, psql postgres 
4. CREATE DATABASE betadb;
5. CREATE USER betadb WITH ENCRYPTED PASSWORD 'vUHouEbUwGKK5rPXe6sx';
6. GRANT ALL PRIVILEGES ON DATABASE betadb TO betadb;
