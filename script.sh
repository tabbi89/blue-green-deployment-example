#!/bin/bash

while true; do curl -s -o /dev/null -w "%{http_code}\r\n" 139.59.205.145; sleep 0.001; done