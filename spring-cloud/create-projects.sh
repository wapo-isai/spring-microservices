#!/usr/bin/env bash

spring init \
--boot-version=3.3.2 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=gateway \
--package-name=se.isai.springcloud.gateway \
--groupId=se.isai.springcloud.gateway \
--version=1.0.0-SNAPSHOT \
gateway
