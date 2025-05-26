package tech.qiantong.qdata.server;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpMaterializedMethodReqVO;
import tech.qiantong.qdata.module.dp.service.model.impl.DpModelMaterializedServiceImpl;

import java.util.Arrays;
import java.util.List;

/**
 * 演示测试 DpModelMaterializedServiceImpl 的 createMaterializedTable 方法
 *
 * 注意：
 * 1. @SpringBootTest 默认会启动整个 Spring Boot 应用，
 *    并扫描所有 @Service、@Repository 等 Bean 进行注入。
 * 2. 确保你已经在相应的 application.yml 或测试配置中
 *    配置好数据库信息，以便可以连接并执行数据库操作。
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = QDataApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DpModelMaterializedServiceImplTest {

    @Autowired
    private DpModelMaterializedServiceImpl dpModelMaterializedService;

    @Autowired
    private DataSourceFactory dataSourceFactory;

    /**
     * DM测试
     */
    @Test
    public void testCreateMaterializedTableDM8() {
        // 准备测试用请求对象
        DpMaterializedMethodReqVO reqVO = new DpMaterializedMethodReqVO();

        // 假设你要测试对多个模型ID进行物化建表
        // 这里随意放一些模拟的 modelId (需要数据库实际有对应记录，或逻辑里自己处理)
        reqVO.setModelId(Arrays.asList(1L));

        // 2. 数据源ID
        reqVO.setDatasourceId(1L);
        // 3. 数据源名称
        reqVO.setDatasourceName("Q_DATA");
        // 5. 数据源类型
        //    例如: "dm8", "oracle", "kingbase8"、或直接写 "MySQL"、"PostgreSQL" 等
        reqVO.setDatasourceType("DM8");
        // 6. 数据源配置 (JSON 字符串)
        //    例如其中包含账号和密码
        String configJson = "{\"username\":\"Q_DATA\", \"password\":\"2LKqLVMQ!xVDT$Qx\", \"dbName\":\"Q_DATA\"}";
        reqVO.setDatasourceConfig(configJson);
        // 7. IP地址
        reqVO.setIP("110.42.38.62");
        // 8. 端口号 (Long)
        //    若想使用 Integer，可在实体中改为 Integer
        reqVO.setPort(40013L);

        // 调用要测试的方法
        Long result = dpModelMaterializedService.createMaterializedTable(reqVO);

    }
    /**
     * Oracle测试
     */
    @Test
    public void testCreateMaterializedTableOracle() {
        // 准备测试用请求对象
        DpMaterializedMethodReqVO reqVO = new DpMaterializedMethodReqVO();

        // 假设你要测试对多个模型ID进行物化建表
        // 这里随意放一些模拟的 modelId (需要数据库实际有对应记录，或逻辑里自己处理)
        reqVO.setModelId(Arrays.asList(1L));

        // 2. 数据源ID
        reqVO.setDatasourceId(2L);
        // 3. 数据源名称
        reqVO.setDatasourceName("Q_DATA");
        // 5. 数据源类型
        //    例如: "dm8", "oracle", "kingbase8"、或直接写 "MySQL"、"PostgreSQL" 等
        reqVO.setDatasourceType("Oracle");
        // 6. 数据源配置 (JSON 字符串)
        //    例如其中包含账号和密码
        String configJson = "{\"username\":\"TEST\", \"password\":\"2LKqLVMQ!xVDT$Qx\", \"sid\":\"helowin\", \"dbName\":\"TEST\"}";
        reqVO.setDatasourceConfig(configJson);
        // 7. IP地址
        reqVO.setIP("110.42.38.62");
        // 8. 端口号 (Long)
        //    若想使用 Integer，可在实体中改为 Integer
        reqVO.setPort(40002L);

        // 调用要测试的方法
        Long result = dpModelMaterializedService.createMaterializedTable(reqVO);

    }

    /**
     * 人大金仓测试
     */
    @Test
    public void testCreateMaterializedTableKingbase8() {
        // 准备测试用请求对象
        DpMaterializedMethodReqVO reqVO = new DpMaterializedMethodReqVO();

        // 假设你要测试对多个模型ID进行物化建表
        // 这里随意放一些模拟的 modelId (需要数据库实际有对应记录，或逻辑里自己处理)
        reqVO.setModelId(Arrays.asList(1L));

        // 2. 数据源ID
        reqVO.setDatasourceId(2L);
        // 3. 数据源名称
        reqVO.setDatasourceName("人大金仓");
        // 5. 数据源类型
        //    例如: "dm8", "oracle", "kingbase8"、或直接写 "MySQL"、"PostgreSQL" 等
        reqVO.setDatasourceType("Kingbase8");
        // 6. 数据源配置 (JSON 字符串)
        //    例如其中包含账号和密码
        String configJson = "{\"username\":\"qdata_dev\", \"password\":\"2LKqLVMQ!xVDT$Qx\", \"sid\":\"public\", \"dbName\":\"qdata_dev\"}";
        reqVO.setDatasourceConfig(configJson);
        // 7. IP地址
        reqVO.setIP("110.42.38.62");
        // 8. 端口号 (Long)
        //    若想使用 Integer，可在实体中改为 Integer
        reqVO.setPort(40020L);

        // 调用要测试的方法
        Long result = dpModelMaterializedService.createMaterializedTable(reqVO);

    }

    /**
     * dm
     */
    @Test
    public void testSqlConnectionDM8() {
        DbQueryProperty dbQueryProperty = new DbQueryProperty("DM8", "110.42.38.62",
                "Q_DATA", "2LKqLVMQ!xVDT$Qx", 40013, "Q_DATA", "");
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        List<DbTable> tables = dbQuery.getTables(dbQueryProperty);
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println(tables.toString());
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        List<DbColumn> columns = dbQuery.getTableColumns(dbQueryProperty, tables.get(0).getTableName());
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println(columns.toString());
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");

    }

    /**
     * oracle
     */
    @Test
    public void testSqlConnectionOracle() {
        DbQueryProperty dbQueryProperty = new DbQueryProperty("Oracle", "110.42.38.62",
                "TEST", "2LKqLVMQ!xVDT$Qx", 40002, "TEST", "helowin");
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        List<DbTable> tables = dbQuery.getTables("TEST");
        System.out.println(tables.toString());
    }

    /**
     * 人大金仓测试
     */
    @Test
    public void testSqlConnectionKingbase8() {
        DbQueryProperty dbQueryProperty = new DbQueryProperty("Kingbase8", "110.42.38.62",
                "qdata_dev", "2LKqLVMQ!xVDT$Qx", 40020, "qdata_dev", "public");
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        List<DbTable> tables = dbQuery.getTables(dbQueryProperty);
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println(tables.toString());
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        List<DbColumn> columns = dbQuery.getTableColumns(dbQueryProperty, tables.get(0).getTableName());
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println(columns.toString());
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
        System.out.println("````````````````````````````````````````````````````````````````````````````");
    }
}
