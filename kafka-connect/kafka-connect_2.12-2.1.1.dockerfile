FROM foogaro/cdc-kafka:2.12-2.1.1
RUN mkdir -p /opt/rh/plugins/infinispan-kafka-connector
RUN curl -O https://repo1.maven.org/maven2/io/debezium/debezium-connector-sqlserver/0.9.2.Final/debezium-connector-sqlserver-0.9.2.Final-plugin.tar.gz
RUN tar xvzf debezium-connector-sqlserver-0.9.2.Final-plugin.tar.gz -C /opt/rh/plugins
RUN rm -rf debezium-connector-sqlserver-0.9.2.Final-plugin.tar.gz
RUN sed -i 's/localhost:9092/cdc-kafka-server:9092/g' config/connect-standalone.properties
RUN sed -i 's/\#plugin.path=/plugin.path=\/opt\/rh\/plugins/g' config/connect-standalone.properties
COPY infinispan-kafka-connector /opt/rh/plugins/infinispan-kafka-connector
COPY InfinispanSinkConnector.properties /opt/rh/plugins
COPY mssql-connector.properties /opt/rh/plugins
EXPOSE 8083
ENTRYPOINT ["./bin/connect-standalone.sh"]
CMD ["config/connect-standalone.properties", "/opt/rh/plugins/InfinispanSinkConnector.properties", "/opt/rh/plugins/mssql-connector.properties"]
