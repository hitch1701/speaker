sbt clean
sbt dist
scp target/universal/speaker-*.zip root@207.154.241.88:/root/speaker.zip
ssh root@207.154.241.88 unzip -o speaker.zip
# ps -ef | grep -v grep | grep speaker | awk '{print "kill " $2}'
ssh root@207.154.241.88 nohup ./speaker-0.1.0/bin/speaker -Dmary.base=./mary -J-Xmx1024m &
