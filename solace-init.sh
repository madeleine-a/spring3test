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
deleteQueue responsible-communication.konto
deleteQueue responsible-communication.konto.dmq
deleteQueue responsible-communication.konto.retry.delayed
deleteQueue responsible-communication.selfexclusion
deleteQueue responsible-communication.selfexclusion.dmq
deleteQueue responsible-communication.selfexclusion.retry.delayed
deleteQueue responsible-communication.selfexclusion.stats
deleteQueue responsible-communication.selfexclusion.stats.dmq
deleteQueue responsible-communication.optins.test

# Setup queues
createQueue responsible-communication.konto.dmq 0 "#DEAD_MSG_QUEUE" false false
createQueue responsible-communication.konto 0 "responsible-communication.konto.dmq" false false
createQueue responsible-communication.konto.retry.delayed 1 "responsible-communication.konto" true false

createQueue responsible-communication.selfexclusion.dmq 0 "#DEAD_MSG_QUEUE" false false
createQueue responsible-communication.selfexclusion 0 "responsible-communication.selfexclusion.dmq" false false
createQueue responsible-communication.selfexclusion.retry.delayed 600 "responsible-communication.selfexclusion" true false

createQueue responsible-communication.selfexclusion.stats.dmq 0 "#DEAD_MSG_QUEUE" false false
createQueue responsible-communication.selfexclusion.stats 0 "responsible-communication.selfexclusion.stats.dmq" false false

createQueue responsible-communication.optins.test.dmq 0 "#DEAD_MSG_QUEUE" false false
createQueue responsible-communication.optins.test 0 "responsible-communication.optins.test.dmq" false false
createQueue responsible-communication.optins.test.retry.delayed 30 "responsible-communication.optins.test" false false


# Setup Queue subscriptions
subscribeToTopic responsible-communication.konto "atg-konto/consent/optin"
subscribeToTopic responsible-communication.konto "atg-konto/account/statuschange"
subscribeToTopic responsible-communication.konto "atg-konto/account/deleted"
subscribeToTopic responsible-communication.konto "atg-konto/account/created"

subscribeToTopic responsible-communication.selfexclusion "atg-service-responsible-gambling/event/restriction/selfexclusion/>"

subscribeToTopic responsible-communication.selfexclusion.stats "atg-service-responsible-gambling/statistics/selfexclusion"

subscribeToTopic responsible-communication.optins.test "responsible-communication/optin"
subscribeToTopic responsible-communication.optins.test "responsible-communication/optin/delete"