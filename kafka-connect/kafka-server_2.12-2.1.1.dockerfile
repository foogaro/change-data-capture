FROM foogaro/cdc-kafka:2.12-2.1.1
RUN sed -i 's/localhost:2181/cdc-zookeeper:2181/g' config/server.properties
EXPOSE 9092
ENTRYPOINT ["./bin/kafka-server-start.sh"]
CMD ["config/server.properties"]
