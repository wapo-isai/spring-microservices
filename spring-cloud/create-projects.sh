#!/usr/bin/env bash

spring init \
--boot-version=3.3.2 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=eureka-server \
--package-name=se.isai.springcloud.eurekaserver \
--groupId=se.isai.springcloud.eurekaserver \
--version=1.0.0-SNAPSHOT \
eureka-server

