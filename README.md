# NWDAF_SUB_CLIENT
To build the image:
    - Windows:
        ./mvnw -DskipTests clean install
        docker build . --tag thanlinardos/nwdaf_sub_client:0.0.2-SNAPSHOT
    - Linux:
        mvn -DskipTests clean install && sudo docker build . --tag thanlinardos/nwdaf_sub_client:0.0.2-SNAPSHOT
Use the compose file from nwdaf_sub project to spin up the client instances