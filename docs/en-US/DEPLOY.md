# 🚀 System Deployment Guide

<p align="center">
  <a href="../zh-CN/DEPLOY.md">📖简体中文</a> | 📖English | <a href="../ja-JP/DEPLOY.md">📖日本語</a>
</p>

## 🛠️ 1. Prerequisites
### System Requirements

| Component | Version Requirement |
|------|---------------|
| JDK | 1.8 |
| Node.js | 18+ |
| yarn | v1.22.22+ |
| DM8 | Case-insensitive, GB18030 encoding |
| Redis | 5.0+ |
| RabbitMQ | No version requirement |
| Maven | 3.6+ |
| Docker | 1.13.1+ |
| Docker Compose | 1.28.0+ |

## 📁 2. Directory Structure
### 2.1 Project Structure&#xA;
```
├─qdata-framework           # Common configuration module
├─qdata-server              # Startup project
├─qdata-module-system       # System management module
├─qdata-module-att          # Basic management module
├─qdata-module-dp           # Data standard management module
├─qdata-module-da           # Data asset module
├─qdata-module-dpp          # Data aggregation module
├─qdata-module-ds           # Data service module
├─qdata-api-ds              # DS scheduler interface module
├─qdata-service-ai          # Independently deployed AI capability service
├─qdata-service-quality     # Independently deployed data quality capability service
├─qdata-executor-etl        # Executor JAR required by the Spark/Flink task engine
├─qdata-ui                  # Frontend module
├─sql                       # SQL scripts
├─README.md                 # Project introduction
├─DEPLOY.md                 # Quick start guide
```
### 2.2 Backend Structure&#xA;
```
├─qdata-framework           # Common configuration module
├─   ├─qdata-websocket      # WebSocket module
├─   ├─qdata-security       # Security module
├─   ├─qdata-redis          # Redis module
├─   ├─qdata-quartz         # Scheduled task module
├─   ├─qdata-mybatis        # MyBatis configuration
├─   ├─qdata-generator      # Code generator
├─   ├─qdata-file           # File management module
├─   ├─qdata-es             # ES module
├─   ├─qdata-config         # Configuration module
├─   ├─qdata-common         # Common module
├─   ├─qdata-auth           # OAuth2 module
├─qdata-server              # Startup project
├─qdata-module-system       # System management module
├─qdata-module-att          # Basic management module
├─qdata-module-dp           # Data standard management module
├─qdata-module-da           # Data asset module
├─qdata-module-dpp          # Data aggregation module
├─qdata-module-ds           # Data service module
├─qdata-api-ds              # DS scheduler interface module
├─qdata-service-ai          # Independently deployed AI capability service
├─qdata-service-quality     # Independently deployed data quality capability service
├─qdata-executor-etl        # Executor JAR required by the Spark/Flink task engine
```

### 2.3 Frontend Structure&#xA;

```
├─qdata-ui                  # Frontend module
├─   ├─public                   # Static resources directory
├─   ├─vite.config.js           # Vite configuration file
├─   ├─src
├─   |  ├─views                     # Page views
├─   |  |   ├─system                # System management module
├─   |  |   ├─att                   # Basic management module
├─   |  |   ├─dp                    # Data standard management module
├─   |  |   ├─da                    # Data asset module
├─   |  |   ├─dpp                   # Data aggregation module
├─   |  |   ├─ds                    # Data service module
├─   |  ├─utils                 # Utilities
├─   |  ├─store                 # State management
├─   |  ├─router                # Router
├─   |  ├─plugins               # Plugins
├─   |  ├─layout                # Layout
├─   |  ├─components            # Common components
├─   |  ├─assets                # Images, styles, and other resources
├─   |  ├─api                   # API interfaces
├─   ├─.env.development         # Development environment configuration
├─   ├─.env.production          # Production environment configuration
```

## 🚀 3. Quick Start

### 3.1 Spark Deployment (Linux Environment)

#### 1. Download Spark
🔗 [Download Spark 3.5.5](https://downloads.apache.org/spark/spark-3.5.5/spark-3.5.5-bin-hadoop3.tgz)

#### 2. Verify Java Environment
```
java -version

#  Expected output

java version "1.8.0\_441"
Java(TM) SE Runtime Environment (build 1.8.0\_441-b07)
Java HotSpot(TM) 64-Bit Server VM (build 25.441-b07, mixed mode)
```

#### 3. Extract Files
```
tar -xzf spark-3.5.5-bin-hadoop3.tgz
```

#### 4. Start Master Node
```
cd spark/sbin

./start-master.sh
```

✅ Verification: Access `http://<server-ip>:8080`. If the Spark admin page is displayed, the master node started successfully. 📋 Record the Master URL (e.g., `spark://127.0.0.1:7077`) for starting the Worker node.


#### 5. Start Worker Node
```
cd spark/sbin

./start-slave.sh <Master URL>  # Replace with the URL recorded in the previous step
```

✅ Verification: Refresh the Spark admin page and check whether the "Workers" list shows the new node.

![Spark Worker Verification](../../images/deploy/scheduler/verify-spark.png)

### 3.2 DS Scheduler Startup

**1. Get the Code**
- 🔗 [Baidu Netdisk](https://pan.baidu.com/s/5A7-TUZ_EujpsWO93RektIg)

**2. Startup Guide**
🔗 [DolphinScheduler Development Environment Setup](https://dolphinscheduler.apache.org/en-us/docs/3.2.2/contribute/development-environment-setup)

### 3.3 Backend Configuration File Changes ⚙️&#xA;

##### 1. Switch to Development Environment

```
#  application.properties
spring:
 profiles:
   active: dev  # Set to development environment
```

##### 2. Configure Key Parameters (application-dev.yml)
```
# Main datasource selection
datasource:
  type: mysql # Currently supports mysql and dm8

# MySQL configuration
mysql:
  driver-class-name: com.mysql.cj.jdbc.Driver
  url: jdbc:mysql://127.0.0.1:3306/qdata?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true&serverTimezone=Asia/Shanghai
  username: <database-username>  # Replace with actual username
  password: <database-password>  # Replace with actual password

#  DM8 database configuration
dm8:
 driver-class-name: dm.jdbc.driver.DmDriver
 url: jdbc:dm://127.0.0.1:5236/QDATA?STU\&zeroDateTimeBehavior=convertToNull\&useUnicode=true\&characterEncoding=utf-8\&schema=QDATA\&serverTimezone=Asia/Shanghai
 username: <database-username>  # Replace with actual username
 password: <database-password>  # Replace with actual password

#  RabbitMQ configuration
rabbitmq:
 host: 127.0.0.1
 port: 40003
 username: <username>  # Replace with actual username
 password: <password>  # Replace with actual password

#  DS scheduler configuration
ds:
 base_url: http://127.0.0.1:12345/dolphinscheduler
 token: <scheduler-token>  # Create in scheduler - Security Center - Token Management
 spark:
   master_url: spark://127.0.0.1:7077  # Must match Spark Master address
   main_jar: file:/dolphinscheduler/default/resources/spark-jar/qdata-executor-etl-3.8.8.jar  # Path after uploading ETL jar
   main_class: tech.qiantong.qdata.spark.etl.EtlApplication
```

### 3.4. Initialize Database
1. **Create Database Schema**
    - Default schema name: `QDATA` (MySQL default schema is `qdata`)
    - To modify: edit the schema name in `sql/dm/dm.sql` or `sql/mysql/mysql.sql`

2. **Execute Initialization Scripts**
   ```bash
   # Execute using DM8 command-line tool
   disql SYSDBA/SYSDBA@127.0.0.1:5236 -f sql/dm/dm.sql

   # Execute using Navicat tool
   sql/mysql/mysql.sql
   ```

### 3.5. Start Backend Service
```
#  Execute the main method of QDataApplication
#  Success message
(♥◠‿◠)ﾉﾞ  qData Data Middle Platform started successfully!  ლ(´ڡ\`ლ)ﾞ
```

### 3.6 Frontend Configuration and Startup

#### 1. Configure Proxy (vite.config.js)
```
// Proxy configuration
server: {
 port: 81,
 host: true,
 open: true,
 proxy: {
   "/dev-api": {
     target: "http://<backend-ip>:<port>",  // Replace with actual backend address, e.g., http://localhost:8080
     changeOrigin: true,
     rewrite: (p) => p.replace(/^\\/dev-api/, ""),
   }
 }
}
```

#### 2. Install Dependencies
```
cd qdata-ui

yarn install  # or npm install
```

#### 3. Start Frontend Service

```
yarn run dev  # or npm run dev
```

#### 4. Browser Access 🚀 Open `http://localhost:81` to enter the system
