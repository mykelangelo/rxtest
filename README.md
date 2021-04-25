# How to run tests
1. open root directory in terminal
2. run `sudo docker-compose up` to run Redis
3. open new terminal window in the root directory
4. run `./gradlew test` to run the tests
5. run `sudo docker-compose down` to shut down Redis
6. you may close both terminal windows now

# How to run the app
1. open root directory in terminal
2. run `sudo docker-compose up` to run Redis
3. open new terminal window in the root dir
4. run `./gradlew bootRun` to run the app
5. use the app at localhost:8080
6. press Ctrl+Z in the latter terminal window to stop the app
7. run `sudo docker-compose down` to shut down Redis
8. you may close both terminal windows now