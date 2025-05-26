package tech.qiantong.qdata.server;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnSaveReqVO;
import tech.qiantong.qdata.module.dp.service.model.impl.DpModelColumnServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = QDataApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IDpModelColumnServiceTest {

    @Autowired
    private DpModelColumnServiceImpl iDpModelColumnService;



    /**
     * 批量新增测试
     */
    @Test
    public void testCreateMaterializedTableDM8() {

        // 创建测试用例列表
        List<DpModelColumnSaveReqVO> dpModelColumnList = generateTestCases();






        iDpModelColumnService.createDpModelColumnList(dpModelColumnList);
    }


    private static List<DpModelColumnSaveReqVO> generateTestCases() {
        List<DpModelColumnSaveReqVO> dpModelColumnList = new ArrayList<>();

        // 用例 1：普通字段测试
        DpModelColumnSaveReqVO case1 = new DpModelColumnSaveReqVO();
        case1.setModelId(1001L);
        case1.setEngName("column_name_1");
        case1.setCnName("字段名称1");
        case1.setColumnType("VARCHAR");
        case1.setColumnLength(255L);
        case1.setColumnScale(null);
        case1.setDefaultValue("0");
        case1.setPkFlag("0");
        case1.setNullableFlag("0");
        case1.setSortOrder(1L);
        case1.setAuthorityDept("IT Department");
        case1.setDataElemId(5001L);
        case1.setCreatorId(101L);
        case1.setCreateBy("admin");
        case1.setCreateTime(new Date());
        case1.setUpdatorId(102L);
        case1.setUpdateBy("editor");
        case1.setUpdateTime(new Date());
        case1.setRemark("测试用例 1");
        dpModelColumnList.add(case1);

        // 用例 2：整型和默认值测试
        DpModelColumnSaveReqVO case2 = new DpModelColumnSaveReqVO();
        case2.setModelId(1002L);
        case2.setEngName("column_name_2");
        case2.setCnName("字段名称2");
        case2.setColumnType("INTEGER");
        case2.setColumnLength(10L);
        case2.setColumnScale(null);
        case2.setDefaultValue("0");
        case2.setPkFlag("0");
        case2.setNullableFlag("0");
        case2.setSortOrder(2L);
        case2.setAuthorityDept("Finance Department");
        case2.setDataElemId(5002L);
        case2.setCreatorId(103L);
        case2.setCreateBy("finance_user");
        case2.setCreateTime(new Date());
        case2.setUpdatorId(null);
        case2.setUpdateBy(null);
        case2.setUpdateTime(null);
        case2.setRemark("测试用例 2");
        dpModelColumnList.add(case2);

        // 用例 3：小数类型测试
        DpModelColumnSaveReqVO case3 = new DpModelColumnSaveReqVO();
        case3.setModelId(1003L);
        case3.setEngName("column_name_3");
        case3.setCnName("字段名称3");
        case3.setColumnType("DECIMAL");
        case3.setColumnLength(18L);
        case3.setColumnScale(2L);
        case3.setDefaultValue("0");
        case3.setPkFlag("0");
        case3.setNullableFlag("0");
        case3.setSortOrder(3L);
        case3.setAuthorityDept("HR Department");
        case3.setDataElemId(5003L);
        case3.setCreatorId(104L);
        case3.setCreateBy("hr_admin");
        case3.setCreateTime(new Date());
        case3.setUpdatorId(104L);
        case3.setUpdateBy("hr_admin");
        case3.setUpdateTime(new Date());
        case3.setRemark("测试用例 3");
        dpModelColumnList.add(case3);
        return dpModelColumnList;
    }

}
