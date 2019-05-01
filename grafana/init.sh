#!/bin/bash

set -e

./bin/grafana-server &

sleep 5

function init() {
  result=`./curl-check-grafana-health | python3 -c "import sys, json; print(json.load(sys.stdin)['database'])"`
  echo -e "Running: $result\n"

  if [ $result != "ok" ]; then
    sleep 5
    init
  else
    addDatasource
  fi
}

function addDatasource() {
  echo -e "Adding the Prometheus datasource\n"
  result=`./curl-add-datasource | python3 -c "import sys, json; print(json.load(sys.stdin)['message'])"`

  if [ $result != "Datasource added" ]; then
    echo -e "ERROR -> $result\n"
    exit 1
  else
    addDashboard
  fi
}

function addDashboard() {
  echo -e "Adding the Grafana dashboard\n"
  result=`./curl-add-dashboard | python3 -c "import sys, json; print(json.load(sys.stdin)['status'])"`
  if [ $result != "success" ]; then
    echo -e "ERROR -> $result\n"
    exit 1
  else
    echo -e "All set, enjoy it!\n"
  fi
}

init
