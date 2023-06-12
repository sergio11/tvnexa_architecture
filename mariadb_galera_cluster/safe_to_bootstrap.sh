#!/bin/sh
#Define cleanup procedure
cleanup() {
    echo "Container stopped, performing safe to bootstrap"
    sed -i 's/safe_to_bootstrap: 0/safe_to_bootstrap: 1/' /var/lib/mysql/grastate.dat
}

#Trap SIGTERM
trap 'cleanup' SIGTERM

#Execute a command
"${@}" &

#Wait
wait $!

#Cleanup
cleanup

