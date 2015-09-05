#!/bin/sh
if [ "$#" == "0" ]; then
	./gradlew run
else
	./gradlew run -Pargs="$*"
fi
