cd /opt/keycloak/bin
./kcadm.sh config credentials --server http://localhost:9090 --realm master --user ${KEYCLOAK_ADMIN}
./kcadm.sh update realms/master -s sslRequired=NONE