#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/crm2-frankfurt.pem \
    target/crm2-0.0.3-SNAPSHOT.jar \
    ec2-user@ec2-3-122-233-160.eu-central-1.compute.amazonaws.com:/home/ec2-user/

echo 'Restart server...'

ssh -i ~/.ssh/crm2-frankfurt.pem ec2-user@ec2-3-122-233-160.eu-central-1.compute.amazonaws.com << EOF
pgrep java | xargs kill -9
nohup java -jar crm2-0.0.3-SNAPSHOT.jar > log.txt &
EOF

echo 'Bye'


