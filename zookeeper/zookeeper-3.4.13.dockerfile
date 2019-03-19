FROM foogaro/fedora29-openjdk1.8.0.201.b098
RUN mkdir -p /opt/rh
WORKDIR /opt/rh
RUN curl -O https://www-eu.apache.org/dist/zookeeper/stable/zookeeper-3.4.13.tar.gz
RUN tar xvzf zookeeper-3.4.13.tar.gz
RUN rm -rf zookeeper-3.4.13.tar.gz
WORKDIR /opt/rh/zookeeper-3.4.13
RUN cp -a conf/zoo_sample.cfg conf/zoo.cfg
EXPOSE 2181
ENTRYPOINT ["./bin/zkServer.sh"]
CMD ["start-foreground"]
