#!/bin/sh
/usr/local/bin/aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws

docker build -t grizzly-api-runtime-function-public:latest .
    
docker tag grizzly-api-runtime-function-public:latest public.ecr.aws/f2o9u3j9/grizzly-runtime-function-public:latest

docker push public.ecr.aws/f2o9u3j9/grizzly-runtime-function-public

