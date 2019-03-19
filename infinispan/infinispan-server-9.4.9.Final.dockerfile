FROM foogaro/fedora29-openjdk1.8.0.201.b098
RUN mkdir -p /opt/rh
WORKDIR /opt/rh
RUN curl -O http://downloads.jboss.org/infinispan/9.4.9.Final/infinispan-server-9.4.9.Final.zip
RUN unzip infinispan-server-9.4.9.Final.zip
RUN rm -rf infinispan-server-9.4.9.Final.zip
WORKDIR /opt/rh/infinispan-server-9.4.9.Final
RUN ./bin/add-user.sh -s -e -cw -u admin -p admin.2019
EXPOSE 8080 9990 11222
ENTRYPOINT ["./bin/standalone.sh"]
CMD ["-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
