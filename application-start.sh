#!/bin/bash

if [ "$DEPLOYMENT_GROUP_NAME" == "Gallery-QA" ]
then
    echo 'launching app with QA profile'
    nohup java -jar -Dspring.profiles.active=qa /home/ec2-user/apps/gallery/gallery-0.0.1-SNAPSHOT.jar > /home/ec2-user/apps/gallery/out1 2> /home/ec2-user/apps/gallery/out2 < /dev/null &
else
    echo 'launching app with dev profile'
    nohup java -jar -Dspring.profiles.active=dev /home/ec2-user/apps/gallery/gallery-0.0.1-SNAPSHOT.jar > /home/ec2-user/apps/gallery/out1 2> /home/ec2-user/apps/gallery/out2 < /dev/null &
fi
exit 0