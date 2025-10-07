DATA_FILE_PATH="./replicaset"
DOCKER_FILE_PATH="./docker-compose.dev.yml"
MONGO_PRIMARY_NAME="rs01p"
REPLICA_INIT_FILE_PATH="./scripts/rs-init.sh"
MONGO_CREATE_USER_FILE_PATH="./scripts/mongo-create-user.sh"

UP_CONTAINER_DELAY=10
REPLICA_CONFIG_DELAY=25

echo "****************** Reset docker container Shell Script ******************"
echo "Data File Path: ${DATA_FILE_PATH}"
echo "Docker File Path: ${DOCKER_FILE_PATH}"
echo "MongoDB Primary name: ${MONGO_PRIMARY_NAME}"
echo "Replica set init Script File Path: ${REPLICA_INIT_FILE_PATH}"
echo "Mongo create user file path: ${MONGO_CREATE_USER_FILE_PATH}"

sleep 1;

echo "****************** Stop docker container ******************"
docker-compose -f ${DOCKER_FILE_PATH} stop
echo "****************** Completed Stop docker container ******************"

sleep 1;

echo "****************** Down docker container ******************"
docker-compose -f ${DOCKER_FILE_PATH} down
echo "****************** Completed Down docker container ******************"

sleep 1;

echo "****************** Remove Data ******************"
rm -rf ${DATA_FILE_PATH}
echo "****************** Completed Remove Data ******************"

sleep 1;

echo "****************** Up docker container ******************"
docker-compose -f ${DOCKER_FILE_PATH} up -d 
echo "****************** Completed Up docker container ******************"

echo "****** Waiting for ${UP_CONTAINER_DELAY} seconds ******"
sleep $UP_CONTAINER_DELAY;

echo "****************** Run Replica Set Shell Script ******************"
docker exec -i ${MONGO_PRIMARY_NAME} bash < ${REPLICA_INIT_FILE_PATH}

echo "****** Waiting for ${REPLICA_CONFIG_DELAY} seconds for replicaset configuration to be applied ******"
sleep $REPLICA_CONFIG_DELAY

echo "****************** Run Create DB User Shell Script ******************"
docker exec -i ${MONGO_PRIMARY_NAME} bash < "${MONGO_CREATE_USER_FILE_PATH}"

echo "****************** Completed Replica Shell Script ******************"
