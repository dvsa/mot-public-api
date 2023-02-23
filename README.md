# MOT Public API

MOT Public API

This is the source code for an API which provides access to public MOT data.

It is written as a set of AWS Lambdas, intended to be connected to AWS API Gateway endpoints.

There is some unit test coverage, focussed on the most important code paths for frequently used endpoints.

This repository uses git-secrets to prevent secrets from being committed.
Please ensure you have git-secrets installed on your machine: https://github.com/awslabs/git-secrets#installing-git-secrets

### Building and running TAPI Locally

#### Prerequisite

- Java 8/JDK8
- AWS Sam CLI - https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install-mac.html
- MTS database up and running
- Run ``` cp template.yaml.example template.yaml  ``` in the root directory - this will create a template.yaml file using the example file.

- In the ```template.yaml```, change the following variables:-
- ``` SEARCH_API_URL: "http://localhost:8082"``` to use search api locally  
- ```SEARCH_API_URL``` and the corresponding ```SEARCH_API_KEY``` to use the SeachApi directly (for example 'https://api.vehicle.search.dev.dvsacloud.uk/1.0')
- 
#### Build
 
To build the application make sure you are in the root directory and then:

``` sam build ```

#### Run

To run the application locally make sure have a successful build whilst in the root directory and then:

``` sam local start-api ```

This will start the application at ```http:://127.0.0.1:3000```.

All paths and environments are listed in the ```template.yaml``` file.

You can add/edit environment variables and paths as shown below:

```yaml
 Environment:
        Variables:
          REGION: eu-west-1
```

Or use parameters and references as shown below:

```yaml
Parameters:
  TEST_PARAM:
    Type: String
    Default: 'test'
    ...
    
 Environment:
        Variables:
          TEST_PARAM: !Ref TEST_PARAM
```

You can edit/add endpoints:
```yaml
Events:
  VrmGet:
    Type: Api
    Properties:
      Path: /trade/vehicles/mot-tests
      Method: GET
  VrmPost:
    Type: Api
    Properties:
      Path: /trade/vehicles/annual-tests
      Method: GET
```

Each time you edit the code or ```template.yaml``` you will need to build: ```sam build``` and run the application: ``` sam local start-api ``` to see the changes.

