#
# This file is part of the auto1-oss/test-data-storage-service.
#
# (c) AUTO1 Group SE https://www.auto1-group.com
#
# For the full copyright and license information, please view the LICENSE
# file that was distributed with this source code.
#

FROM adoptopenjdk:11-jre-hotspot
ADD target/*.jar application.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /application.jar" ]
