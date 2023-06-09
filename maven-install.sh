#!/usr/bin/env bash

./mvnw install:install-file \
-Dfile=../hypersistence-optimizer-2.6.3-trial/lib/hypersistence-optimizer-2.6.3-trial.jar \
-Dsources=../hypersistence-optimizer-2.6.3-trial/lib/hypersistence-optimizer-2.6.3-trial-sources.jar \
-Djavadoc=../hypersistence-optimizer-2.6.3-trial/lib/hypersistence-optimizer-2.6.3-trial-javadoc.jar \
-DgroupId=io.hypersistence \
-DartifactId=hypersistence-optimizer \
-Dversion=2.6.3-trial \
-Dpackaging=jar \
-DgeneratePom=true

./mvnw install:install-file \
-Dfile=../hypersistence-optimizer-2.6.3-trial/lib/hypersistence-optimizer-2.6.3-trial-jre6.jar \
-Dsources=../hypersistence-optimizer-2.6.3-trial/lib/hypersistence-optimizer-2.6.3-trial-jre6-sources.jar \
-Djavadoc=../hypersistence-optimizer-2.6.3-trial/lib/hypersistence-optimizer-2.6.3-trial-jre6-javadoc.jar \
-DgroupId=io.hypersistence \
-DartifactId=hypersistence-optimizer \
-Dversion=2.6.3-trial-jre6 \
-Dpackaging=jar \
-DgeneratePom=true

./mvnw install:install-file \
-Dfile=../hypersistence-optimizer-2.6.3-trial/lib/hypersistence-optimizer-2.6.3-trial-jakarta.jar \
-Dsources=../hypersistence-optimizer-2.6.3-trial/lib/hypersistence-optimizer-2.6.3-trial-jakarta-sources.jar \
-Djavadoc=../hypersistence-optimizer-2.6.3-trial/lib/hypersistence-optimizer-2.6.3-trial-jakarta-javadoc.jar \
-DgroupId=io.hypersistence \
-DartifactId=hypersistence-optimizer \
-Dversion=2.6.3-trial-jakarta \
-Dpackaging=jar \
-DgeneratePom=true
