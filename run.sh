sbt clean
sbt dist
rm -rf speaker-0.1.0
unzip -o target/universal/speaker-0.1.0.zip
cd speaker-0.1.0/bin/
./speaker -Dmary.base=../../mary
cd ../..
