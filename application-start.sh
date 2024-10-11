#!/bin/bash

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -s -c file:/tmp/amazon-cloudwatch-agent.json
sudo nohup java -jar /home/ec2-user/apps/gallery/gallery-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &
exit 0