#!/bin/bash

isLaunched=`pgrep java`
if [[ -n $isLaunched ]]; then
    sudo kill $isLaunched
fi