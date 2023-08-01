#!/bin/bash

healthCheck() {
  echo "Checking if Solace is up and running." >&2
  http_code=$(curl --write-out "%{http_code}" --silent --output /dev/null -u admin:admin "${SOLACE_HOST}/SEMP/v2/config/about/api")
  if [ "$http_code" -eq 200 ]; then
    echo "Solace seems to be up and running!" >&2
  else
    echo "It doesn't seem like Solace is up and running yet. Checking again in 10 seconds..." >&2
    sleep 10
    http_code=$(healthCheck)
  fi
}

deleteQueue() {
  echo "Trying to delete queue $1." >&2
  http_code=$(curl --write-out "%{http_code}" --silent --output /dev/null -X DELETE -u admin:admin "${SOLACE_HOST}/SEMP/v2/config/msgVpns/default/queues/$1")

  if [ "$http_code" -eq 200 ]; then
    echo "Successfully deleted queue $1." >&2
  elif [ "$http_code" -eq 400 ]; then
    echo "Failed to delete queue $1. Received http code 400. Assuming that queue $1 doesn't exist." >&2
  else
    echo "Failed to delete queue $1. Received http code $http_code." >&2
  fi
}

createQueue() {
  queue_name=$1
  max_ttl=$2
  dead_msg_queue=$3
  respect_ttl=$4
  respect_priority=$5
  echo "Trying to create queue $queue_name." >&2

  http_code=$(curl --write-out "%{http_code}" --silent --output /dev/null -X POST -u admin:admin "${SOLACE_HOST}/SEMP/v2/config/msgVpns/default/queues" -H "content-type: application/json" \
    -d "{\"accessType\":\"non-exclusive\",\"maxRedeliveryCount\":1,\"maxTtl\":$max_ttl,\"queueName\":\"$queue_name\",\"deadMsgQueue\":\"$dead_msg_queue\",\"ingressEnabled\":true,\"egressEnabled\":true,\"maxMsgSpoolUsage\":1,\"respectMsgPriorityEnabled\":$respect_priority,\"respectTtlEnabled\":$respect_ttl,\"owner\":\"default\"}")

  if [ "$http_code" -eq 200 ]; then
    echo "Successfully created queue $queue_name." >&2
  else
    echo "Failed to create queue $queue_name. Received http code $http_code." >&2
  fi
}

subscribeToTopic() {
  queue_name=$1
  topic_name=$2
  echo "Trying to subscribe to topic $topic_name." >&2

  http_code=$(curl --write-out "%{http_code}" --silent --output /dev/null -X POST -u admin:admin "${SOLACE_HOST}/SEMP/v2/config/msgVpns/default/queues/$queue_name/subscriptions" -H "content-type: application/json" \
    -d "{\"subscriptionTopic\":\"$topic_name\"}")

  if [ "$http_code" -eq 200 ]; then
    echo "Successfully subscribed to topic $topic_name." >&2
  else
    echo "Failed to subscribe to topic $topic_name. Received http code $http_code." >&2
  fi
}

SOLACE_HOST=$1

healthCheck

SOLACE_HOST=$1

healthCheck

# Clean up
deleteQueue spring3-test.queue


# Setup queues
createQueue spring3-test.queue.dmq 0 "#DEAD_MSG_QUEUE" false false
createQueue spring3-test.queue 0 "spring3-test.queue.dmq" false false
createQueue spring3-test.queue.retry.delayed 1 "spring3-test.queue" true false
