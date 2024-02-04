docker compose -f ../NWDAF_SUB/src/main/resources/compose_files/docker-compose.yml down nwdafSubClient &&
cd ../nwdaf_library &&
mvn -DskipTests install &&
cd ../NWDAF_SUB_CLIENT &&
mvn -DskipTests install &&
docker build . --tag thanlinardos/nwdaf_sub_client:0.0.2-SNAPSHOT &&
docker compose -f ../NWDAF_SUB/src/main/resources/compose_files/docker-compose.yml up nwdafSubClient -d &&
docker logs nwdafSubClient --follow