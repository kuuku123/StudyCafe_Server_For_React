# StudyCafe_Server_For_React


it is a website that is used for creating study , and let other people join study that you created

## Features
- **Login and Logout with Email Verification**
    - Emails are sent asynchronously
    - once you verify email , then you can create your own study
    - using browser session storage to manage login status
- **Create Study**
    - you can own multiple study and join multiple study
    - once you create your study you will be a manager of study and have authority to publish one
    - schedule members and you for studying
    - study image, tag and zone can be configured


## Installation

currently no deployment pipe line
- **manual way**
    - build react app with webpack
    - put static output to src/main/resources/static
  ```bash
  maven clean package -DSkipTests
  
  java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=\*:5005 
  -Djasypt.encryptor.password=my_jasypt_key 
  -Dspring.profiles.active=dev 
  -jar StudyCafe_Server_For_React-1.0-SNAPSHOT.jar
  
  ```




## Usage




## Contributing

Guidelines on how others can contribute to your project.

## License

This project is licensed under the MIT License.
