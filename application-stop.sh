#!/bin/bash

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a stop
isLaunched=`pgrep java`
if [[ -n $isLaunched ]]; then
    sudo kill $isLaunched
fi