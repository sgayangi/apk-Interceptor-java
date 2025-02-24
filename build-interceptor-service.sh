set -ex
IMAGE_NAME="wso2am/apk-sample-xml-interceptor-java"
VERSION="v1.0.0"
SRC="interceptors-java-jar"

cd $SRC
docker build -t $IMAGE_NAME:$VERSION --build-arg CERT_DIR=certs .

