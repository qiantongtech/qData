package tech.qiantong.qdata.server;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.qiantong.qdata.module.da.service.datasource.IDaDatasourceService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = QDataApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IDaDatasourceTest {

    @Autowired
    private IDaDatasourceService iDaDatasourceService;
}
