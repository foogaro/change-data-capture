FROM foogaro/fedora-openjdk
RUN mkdir -p /opt/rh
WORKDIR /opt/rh
RUN curl -O https://www-eu.apache.org/dist/zookeeper/stable/apache-zookeeper-3.5.5-bin.tar.gz
RUN tar xvzf apache-zookeeper-3.5.5-bin.tar.gz
RUN rm -rf apache-zookeeper-3.5.5-bin.tar.gz
WORKDIR /opt/rh/apache-zookeeper-3.5.5-bin
RUN cp -a conf/zoo_sample.cfg conf/zoo.cfg
EXPOSE 2181
ENTRYPOINT ["./bin/zkServer.sh"]
CMD ["start-foreground"]
