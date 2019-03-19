FROM fedora:29
RUN yum install -y wget net-tools java-1.8.0-openjdk-devel unzip
RUN java -version
ENTRYPOINT ["/bin/bash"]
