#!/bin/bash

sudo nohup java -jar /home/ec2-user/apps/gallery/gallery-0.0.1-SNAPSHOT.jar > /home/ec2-user/apps/gallery/out1 2> /home/ec2-user/apps/gallery/out2 < /dev/null &
exit 0