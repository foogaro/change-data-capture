FROM foogaro/fedora29-openjdk1.8.0.201.b098
RUN mkdir -p /opt/rh
WORKDIR /opt/rh
RUN curl -O http://downloads.jboss.org/infinispan/9.4.9.Final/infinispan-server-9.4.9.Final.zip
RUN unzip infinispan-server-9.4.9.Final.zip
RUN rm -rf infinispan-server-9.4.9.Final.zip
WORKDIR /opt/rh/infinispan-server-9.4.9.Final
RUN ./bin/add-user.sh -s -e -cw -u admin -p admin.2019
RUN ./bin/add-user.sh -s -e -cw -a -u user -p user.2019

RUN ls -la standalone/configuration


COPY infinispan-listener-*.jar .
COPY deploy-cache-store.cli .
COPY local-caches-configuration.cli .
COPY local-caches.cli .
RUN ls -la
RUN cat standalone/configuration/standalone.xml
RUN ./bin/ispn-cli.sh --file=deploy-cache-store.cli
RUN rm infinispan-listener-*.jar
RUN ./bin/ispn-cli.sh --file=local-caches-configuration.cli
RUN ./bin/ispn-cli.sh --file=local-caches.cli
RUN ls -la standalone/configuration
RUN cat standalone/configuration/standalone.xml
RUN rm -rf standalone/configuration/standalone_xml_history
RUN ls -la standalone/configuration

#Deploy the listener and hook it to the Infinispan module
#COPY infinispan-listener-*.jar* standalone/deployments/

COPY model-0.0.1.jar .
RUN ./bin/ispn-cli.sh --command="module add --name=com.foogaro.cdc.infinispan.model --resources=model-0.0.1.jar"

#RUN sed -i 's/<dependencies>/<dependencies><module name=\"com.foogaro.cdc.infinispan.model\"\/>/g' modules/system/add-ons/ispn/org/infinispan/ispn-9.4/module.xml
RUN rm model-*.jar
#RUN ls -la 
#RUN cat modules/system/add-ons/ispn/org/infinispan/ispn-9.4/module.xml
#RUN ls -la modules/system/add-ons/ispn/org/infinispan/ispn-9.4/

#RUN ./bin/ispn-cli.sh --command="module add --name=com.foogaro.cdc.infinispan.listener --resources=listener-*.jar --dependencies=org.infinispan"
#RUN ./bin/ispn-cli.sh --command="module add --name=com.foogaro.cdc.infinispan.listener --resources=listener-0.0.1.jar"
#RUN sed -i 's/<dependencies>/<dependencies><module name=\"com.foogaro.cdc.infinispan.listener\"\/>/g' modules/system/add-ons/ispn/org/infinispan/ispn-9.4/module.xml
#RUN rm listener-*.jar
#RUN ls -la 
#RUN cat modules/system/add-ons/ispn/org/infinispan/ispn-9.4/module.xml
#RUN ls -la modules/system/add-ons/ispn/org/infinispan/ispn-9.4/

RUN curl -O http://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.11.0/jmx_prometheus_javaagent-0.11.0.jar
COPY jmx_prometheus.yml .
#COPY prometheus.conf .
ENV JBOSS_HOME=/opt/rh/infinispan-server-9.4.9.Final
ENV JBOSS_MODULES_SYSTEM_PKGS="org.jboss.byteman,org.jboss.logmanager"
ENV PROMETHEUS_JAR=jmx_prometheus_javaagent-0.11.0.jar
ENV PROMETHEUS_YML=jmx_prometheus.yml
ENV JBOSS_LOG_MANAGER_LIB=$JBOSS_HOME/modules/system/layers/base/org/jboss/logmanager/main/jboss-logmanager-2.1.4.Final.jar
ENV WILDFLY_COMMON_LIB=$JBOSS_HOME/modules/system/layers/base/org/wildfly/common/main/wildfly-common-1.4.0.Final.jar

ENV JAVA_OPTS="$JAVA_OPTS -Xbootclasspath/p:$JBOSS_LOG_MANAGER_LIB"
ENV JAVA_OPTS="$JAVA_OPTS -Xbootclasspath/p:$WILDFLY_COMMON_LIB"
ENV JAVA_OPTS="$JAVA_OPTS -Djboss.modules.system.pkgs=org.jboss.byteman,org.jboss.logmanager"
ENV JAVA_OPTS="$JAVA_OPTS -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_OPTS="$JAVA_OPTS -javaagent:$PROMETHEUS_JAR=8088:$PROMETHEUS_YML"

#ENV JAVA_OPTS="$JAVA_OPTS -Dsun.util.logging.disableCallerCheck=true"
#ENV JAVA_OPTS="$JAVA_OPTS -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
#java.util.logging.manager=org.jboss.logmanager.LogManager"
#ENV JAVA_OPTS="$JAVA_OPTS -Xbootclasspath/p:$WILDFLY_COMMON_LIB"
#ENV JAVA_OPTS="$JAVA_OPTS -Xbootclasspath/p:$JBOSS_LOG_MANAGER_LIB"
#ENV JAVA_OPTS="$JAVA_OPTS -javaagent:$PROMETHEUS_JAR=8088:$PROMETHEUS_YML"

EXPOSE 8080 8088 9990 11222
ENTRYPOINT ["./bin/standalone.sh"]
CMD ["-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
