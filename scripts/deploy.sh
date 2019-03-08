#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/crm2-frankfurt.pem \
    target/crm2-0.0.1-SNAPSHOT.jar \
    ec2-user@ec2-18-184-77-117.eu-central-1.compute.amazonaws.com:/home/ec2-user/

echo 'Restart server...'

ssh -i ~/.ssh/crm2-frankfurt.pem ec2-user@ec2-18-184-77-117.eu-central-1.compute.amazonaws.com << EOF
pgrep java | xargs kill -9
nohup java -jar crm2-0.0.1-SNAPSHOT.jar > log.txt &
EOF

echo 'Bye'


