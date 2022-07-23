#!/bin/bash

set -ex

export ARTIFACT_VERSION="0.2-SNAPSHOT"
export DEPLOY_DIR="${HOME}/repos/maelstrom-bin"

mvn package
cp "target/JavaEcho-${ARTIFACT_VERSION}-jar-with-dependencies.jar" "${DEPLOY_DIR}/JavaEcho.jar"
cp jecho.sh "${DEPLOY_DIR}"
