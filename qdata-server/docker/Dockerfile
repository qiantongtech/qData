FROM openjdk:8-jdk

# 接收构建时代理参数
ARG http_proxy
ARG https_proxy

# 显式设置为环境变量，curl/apt 都能用
ENV http_proxy=$http_proxy
ENV https_proxy=$https_proxy

# 设置时区和应用目录
ENV TZ=Asia/Shanghai \
    APP_HOME=/usr/app/jar

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
 && echo $TZ > /etc/timezone \
 && mkdir -p $APP_HOME

WORKDIR $APP_HOME

# 拷贝产物
COPY ./*.jar ./qdata.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xms512m", "-Xmx2g", "-Dspring.profiles.active=prod","-jar","qdata.jar"]
CMD ["--server.port=8080"]
