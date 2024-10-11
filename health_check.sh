#!/bin/bash
 
while 'true'
do
    if [ "$(curl -s http://localhost:8080/actuator/health)" = '{"status":"UP"}' ]
    then
        echo "Application is running!"
        exit 0
    else
        echo "Checking application status..."
        sleep 3s
    fi
done