#!/bin/bash

if [ "$APP_PROFILE" == "qa" ]
then
    nohup java -jar -Dspring-boot.run.profiles=qa /home/ec2-user/apps/gallery/gallery-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &
else
    nohup java -jar -Dspring-boot.run.profiles=dev /home/ec2-user/apps/gallery/gallery-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &
fi
exit 0