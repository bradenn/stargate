#!/bin/sh

if ls server &>/dev/null; then
  if screen -list | grep -q "stargateServerThread"; then
    rm ./server/plugins/original-stargates-1.0-SHADE.jar
    echo "[INFO] Found server, attempting to reload plugin.";
    screen -S stargateServerThread -p 0 -X stuff "plugman reload Stargate$(echo '\015')"
    screen -S stargateServerThread -p 0 -X stuff "say §7Stargate Reloaded §7@ §7$(date)$(echo '\015')";
    echo "[INFO] Stargate plugin reloaded.";
  else
    echo "[INFO] Server is not running, attempting to start the server.";
    exec ./scripts/start.sh
  fi
else
  echo "[INFO] Server directory could not be found, ignoring.";
  exit 0
fi

