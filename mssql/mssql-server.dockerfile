# building from ubuntu
#FROM ubuntu:16.04
FROM microsoft/mssql-server-linux:2017-latest
 
# install curl & sudo & apt-transport-https
#RUN apt-get update && apt-get install -y curl sudo && apt-get install -y apt-transport-https
 
# Import the public repository GPG keys
#RUN curl https://packages.microsoft.com/keys/microsoft.asc | apt-key add -
 
# Register the Microsoft SQL Server Ubuntu repository
#RUN curl https://packages.microsoft.com/config/ubuntu/16.04/mssql-server-2017.list | tee /etc/apt/sources.list.d/mssql-server.list
 
# update package list 
#RUN apt-get update -y
 
# install sql server
#RUN apt-get install -y mssql-server
 
# enable the agent
RUN /opt/mssql/bin/mssql-conf set sqlagent.enabled true 
 
# start sql server 
#CMD /opt/mssql/bin/sqlservr
