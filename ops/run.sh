#bin/bash
rm -rf test-server/plugins/ChickenDefender.jar
cp target/ChickenDefender.jar test-server/plugins/ChickenDefender.jar
cd test-server
java -DIReallyKnowWhatIAmDoingISwear=true -jar spigot-1.16.5.jar nogui