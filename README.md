# MOT Public API

MOT Public API

This is the source code for an API which provides access to public MOT data.

It is written as a set of AWS Lambdas, intended to be connected to AWS API Gateway endpoints.

There is some unit test coverage, focussed on the most important code paths for frequently used endpoints.

This repository uses git-secrets to prevent secrets from being committed.
Please ensure you have git-secrets installed on your machine: https://github.com/awslabs/git-secrets#installing-git-secrets
