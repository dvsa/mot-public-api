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
- MOTH running locally - https://github.com/dvsa/mot-history
- Run ``` cp .env.example .env  ``` in the root directory - this will create a .env file using the example file.

- In the ```.env```, change the following variables:-
- ``` MOTH_API_URL: "http://docker.for.mac.localhost:3010"``` to use MOTH api locally  
- ```MOTH_API_URL``` and the corresponding ```MOTH_API_KEY``` to use the MOTH api directly.

#### Build
 
To build the application make sure you are in the root directory and then:

``` sam build ```

#### Run

To run the application locally make sure have a successful build whilst in the root directory and then:

``` sam local start-api --env-vars .env```

This will start the application at ```http:://127.0.0.1:3000```.

> **_NOTE:_**
If you would like to change the port number to for example 3001, use ```-p <port_number>```.
> > ```sam local start-api --env-vars .env -p 3001```


All paths and environments are listed in the ```template.yaml``` file.
The values of the environment variables are listed in the ```.env``` file.

You can add/edit environment variables and paths as shown below:

```template.yaml:```
```yaml
 Environment:
        Variables:
          MOTH_API_URL:
          MOTH_API_KEY:
```
```.env:```
```json
{
  "Parameters": {
    "MOTH_API_URL": "https://api.search.com",
    "MOTH_API_KEY": "randomKey"
  }
}
```
You can edit/add endpoints:

```template.yaml:```
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

Each time you edit the code or ```template.yaml```/```.env``` you will need to build: ```sam build``` and run the application: ``` sam local start-api --env-vars .env``` to see the changes.

