FROM foogaro/fedora29-openjdk1.8.0.201.b098
RUN mkdir -p /opt/rh
WORKDIR /opt/rh
RUN wget https://github.com/prometheus/prometheus/releases/download/v2.8.1/prometheus-2.8.1.linux-amd64.tar.gz
RUN tar xvzf prometheus-2.8.1.linux-amd64.tar.gz
RUN rm -rf prometheus-2.8.1.linux-amd64.tar.gz
WORKDIR /opt/rh/prometheus-2.8.1.linux-amd64
RUN echo -e "\n  - job_name: 'infinispan'\n    metrics_path: /metrics\n    static_configs:\n      - targets: ['cdc-infinispan:8088']\n" >> prometheus.yml

EXPOSE 9090

ENTRYPOINT ["./prometheus"]
CMD []
