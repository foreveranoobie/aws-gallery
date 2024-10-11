#!/bin/bash
 
while 'true'
do
    if [ "$(curl -s http://localhost:8080/actuator/health)" = '{"status":"UP"}' ]
    then
        echo "Application is running!"
        sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/home/ec2-user/apps/gallery/amazon-cloudwatch-agent.json
        sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a start
        exit 0
    else
        echo "Checking application status..."
        sleep 3s
    fi
done