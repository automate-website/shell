# Automate Website Shell (AWS)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/website.automate/shell/badge.svg)](https://maven-badges.herokuapp.com/maven-central/website.automate/shell) [![Build Status](https://travis-ci.org/automate-website/shell.svg?branch=master)](https://travis-ci.org/automate-website/shell) [![codecov.io](https://codecov.io/github/automate-website/shell/coverage.svg?branch=master)](https://codecov.io/github/automate-website/shell?branch=master) [![Docker Hub](https://img.shields.io/docker/pulls/automatewebsite/shell.svg)](https://hub.docker.com/r/automatewebsite/shell) 

Provides a shell to interact with the automate.website API.

## Getting Started

### Docker Image

Run interactive AWS:

    docker run -it automatewebsite/shell

Run AWS script:

     docker run -it automatewebsite/shell -v /script.aws:/script.aws @/script.aws

### Java Artifact (>= JRE 8)

#### Cross-platform:

    java -jar shell-1.0.0.jar

#### CentOS/Ubuntu/Debian

    ./shell-1.0.0.jar

## Commands

Comands may be bundled into a script file and executed:

    

Certain order assumed, when parameters specified without names:

    login admin secret

Explicit parameter naming allows reordering

    login --password secret --user admin

| Name  | Parameters | Description | Example |
| ------------- | ------------- | ------------- | ------------- |
| login | username, password  | Authenticate against automate.website API. | login admin secret |
| list-projects | -  | List all projects for the authenticated user. | list-projects |
| list-scenarios | project  | List project scenarios for the authenticated user. | list-scenarios example-project |
| run-scenarios | project, scenarios  | Run selected project scenarios for the authenticated user. | run-scenarios example-project example-scenario |

## References
Refer to the [changelog] for recent notable changes and modifications.

## Continuous Integration and Delivery

See [.travis.yml](.travis.yml).

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/automate-website/shell/tags).


## Maintainers

- [build-failure](https://github.com/build-failure)
- [dimw](https://github.com/dimw)

## License

See the [LICENSE](LICENSE) file for details

[changelog]: CHANGELOG.md
