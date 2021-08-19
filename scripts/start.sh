#!/usr/bin/env bash

if ls server &>/dev/null; then
  if screen -S stargateServerThread -dm sh server/start.sh; then
    echo "[INFO] Server is now starting."
  else
    echo "[WARN] Failed to start server.";
  fi
else
  exit 0
fi
