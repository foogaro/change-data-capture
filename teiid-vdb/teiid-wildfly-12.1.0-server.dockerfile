FROM foogaro/fedora29-openjdk1.8.0.201.b098
RUN mkdir -p /opt/rh
WORKDIR /opt/rh
RUN curl -O https://oss.sonatype.org/service/local/repositories/releases/content/org/teiid/wildfly/teiid-wildfly/12.1.0/teiid-wildfly-12.1.0-server.zip
RUN unzip teiid-wildfly-12.1.0-server.zip
RUN rm -rf teiid-wildfly-12.1.0-server.zip
WORKDIR /opt/rh/teiid-12.1.0
RUN ./bin/add-user.sh -s -e -cw -u admin -p admin.2019
RUN ./bin/add-user.sh -s -e -cw -a -u user -p user.2019 -g rest,rest-all,odata

COPY VDBUser.proto .
COPY model-0.0.1.jar .
COPY hotrod.cli .
COPY user-vdb.xml standalone/deployments/
COPY user-vdb.xml.dodeploy standalone/deployments/

RUN ./bin/jboss-cli.sh --command="module add --name=com.foogaro.cdc.infinispan.model --resources=model-0.0.1.jar"
RUN ./bin/jboss-cli.sh --file=hotrod.cli

RUN rm -rf standalone/configuration/standalone_xml_history

EXPOSE 8080 9990 8443 31000 35432

ENTRYPOINT ["./bin/standalone.sh", "-c", "standalone-teiid.xml"]
CMD ["-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
