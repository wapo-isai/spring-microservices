#!/usr/bin/env bash

mkdir microservices
cd microservices

spring init \
--boot-version=3.3.2 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=product-service \
--package-name=se.isai.microservices.core.product \
--groupId=se.isai.microservices.core.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-service

spring init \
--boot-version=3.3.2 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=order-service \
--package-name=se.isai.microservices.core.order \
--groupId=se.isai.microservices.core.order \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
order-service

spring init \
--boot-version=3.3.2 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=user-service \
--package-name=se.isai.microservices.core.user \
--groupId=se.isai.microservices.core.user \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
user-service

spring init \
--boot-version=3.3.2 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=product-composite-service \
--package-name=se.isai.microservices.composite.product \
--groupId=se.isai.microservices.composite.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-composite-service

cd ..
