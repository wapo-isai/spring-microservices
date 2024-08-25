#!/usr/bin/env bash

spring init \
--boot-version=3.3.2 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=config-server \
--package-name=se.isai.springcloud.configserver \
--groupId=se.isai.springcloud.configserver \
--version=1.0.0-SNAPSHOT \
config-server
