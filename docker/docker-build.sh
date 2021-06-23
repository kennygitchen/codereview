DOCKER_REG=351762539201.dkr.ecr.us-east-1.amazonaws.com
DOCKER_REPO=$DOCKER_REG/codereview
GIT_SHA=$(git rev-parse --short HEAD)
docker login -u AWS -p $(aws ecr get-login-password --region us-east-1) $DOCKER_REG
docker build . -f docker/Dockerfile -t codereview-jar:$GIT_SHA -t codereview-jar:latest
docker tag codereview-jar:$GIT_SHA $DOCKER_REPO:$GIT_SHA
docker tag codereview-jar:latest $DOCKER_REPO:latest
docker image push $DOCKER_REPO:$GIT_SHA
docker image push $DOCKER_REPO:latest