FROM openjdk:8u102-jre
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
RUN mkdir /usr/app
RUN mkdir /usr/app/jar

COPY ./*.jar  /usr/app/jar/qdata.jar

CMD ["--server.port=8080"]

EXPOSE 8080

CMD sleep 1;cd /usr/app/jar;java -Dspring.profiles.active=prod -Dqdata.profile=/home/qdata/uploadPath/ -jar qdata.jar
