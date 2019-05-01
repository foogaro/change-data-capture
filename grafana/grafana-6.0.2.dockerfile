FROM foogaro/fedora29-openjdk1.8.0.201.b098
RUN mkdir -p /opt/rh
WORKDIR /opt/rh
RUN curl -O https://dl.grafana.com/oss/release/grafana-6.0.2.linux-amd64.tar.gz
RUN tar -zxvf grafana-6.0.2.linux-amd64.tar.gz
RUN rm -rf grafana-6.0.2.linux-amd64.tar.gz

WORKDIR /opt/rh/grafana-6.0.2

COPY init.sh .
#COPY curl-* /opt/rh/grafana-6.0.2/
COPY curl-* ./
COPY datasource.json .
COPY dashboard.json .

RUN ls -la

RUN ./init.sh

EXPOSE 3000

ENTRYPOINT ["./bin/grafana-server"]
CMD [""]
