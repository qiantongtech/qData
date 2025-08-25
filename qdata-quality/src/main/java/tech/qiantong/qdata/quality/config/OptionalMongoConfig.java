package tech.qiantong.qdata.quality.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@ConditionalOnProperty(name = "custom.mongo.enabled", havingValue = "true")
public class OptionalMongoConfig {
    private static final Logger log = LoggerFactory.getLogger(OptionalMongoConfig.class);

//    @Bean
//    public MongoTemplate mongoTemplate() {
//        try {
//            log.info("✅ 正在初始化自定义 MongoTemplate（OptionalMongoConfig）...");
//            System.out.println("✅ 初始化 OptionalMongoConfig 中的 MongoTemplate...");
//            MongoClientSettings settings = MongoClientSettings.builder()
//                .applyConnectionString(new ConnectionString("mongodb://sjzt:Desl9Y4eIQP1BHh7@110.42.38.62:40004/data?authSource=admin"))
//                .build();
//            MongoClient client = MongoClients.create(settings);
//            return new MongoTemplate(client, "qdata");
//        } catch (Exception e) {
//            System.err.println("⚠️ MongoDB 初始化失败：" + e.getMessage());
//            return null;
//        }
//    }

    @Bean
    public CommandLineRunner checkMongoTemplate(@Autowired(required = false) MongoTemplate mongoTemplate) {
        return args -> {
            System.out.println("✅ MongoTemplate 是否注入成功：" + (mongoTemplate != null));
        };
    }
}
