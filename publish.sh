#!/usr/bin/env bash

if [ -z "$1" ]; then
    echo -e "Usage:\n\t$0 <schrnorrkel-lib-path>";
    exit 1;
fi

unzip $1 -o -d polkaj-schnorrkel/build/rust/release/

./gradlew publish 
