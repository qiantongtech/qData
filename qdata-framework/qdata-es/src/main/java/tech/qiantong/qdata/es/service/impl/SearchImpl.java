package tech.qiantong.qdata.es.service.impl;

import com.github.pagehelper.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.dromara.easyes.core.conditions.update.LambdaEsUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.es.esmapper.example.EsTextDocumentMapper;
import tech.qiantong.qdata.es.model.Search;
import tech.qiantong.qdata.es.model.example.EsTextDocument;
import tech.qiantong.qdata.es.service.ISearchService;

import javax.annotation.PostConstruct;

/**
 * 实体模板Service业务层处理
 *
 * @author qdata
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SearchImpl implements ISearchService {
    private final EsTextDocumentMapper esTextDocumentMapper;

 /*
     作为实例展示，没有实际功能
     @Autowired
     private SysNoticeMapper noticeMapper;
 */

    /**
     * 项目启动时，初始化索引及数据
     */
    @PostConstruct
    public void init() {
        if (!esTextDocumentMapper.existsIndex("text_document"))
        {
            esTextDocumentMapper.createIndex();
        }
    }

    /**
     * 重置全文检索数据
     */
    @Override
    public void resetTextCache() {
        esTextDocumentMapper.deleteIndex("text_document");
        esTextDocumentMapper.createIndex();
    /*
        作为实例展示，没有实际功能
        List<EsTextDocument> esTextDocumentList = new ArrayList<EsTextDocument>();
        List<SysNotice> noticeList = noticeMapper.selectNoticeList(new SysNotice());
        for (SysNotice sysNotice : noticeList)
        {
            EsTextDocument esTextDocument = new EsTextDocument();
            BeanUtils.copyBeanProp(esTextDocument, sysNotice);
            esTextDocumentList.add(esTextDocument);
        }
        esTextDocumentMapper.insertBatch(esTextDocumentList);
    */
    }

    /**
     * 查询全文检索数据
     */
    @Override
    public EsPageInfo<EsTextDocument> selectTextList(Search search) {
        Integer pageNum = search.getPageNum();
        Integer pageSize = search.getPageSize();
        String searchValue = search.getSearchValue();

        LambdaEsQueryWrapper<EsTextDocument> wrapper = new LambdaEsQueryWrapper<>();
        if (StringUtil.isNotEmpty(searchValue))
        {
            wrapper.and(a -> a.match(EsTextDocument::getNoticeTitle, searchValue, 2.0f).or()
                    .match(EsTextDocument::getNoticeContent, searchValue, 1.0f));
        }
        return esTextDocumentMapper.pageQuery(wrapper, pageNum, pageSize);
    }

    /**
     * 新增全文检索数据
     */
    @Override
    public Integer addEsTextDocument(EsTextDocument esTextDocument) {
        // 如果业务需要同步到数据库
        // noticeMapper.insertNotice(new SysNotice());
        return esTextDocumentMapper.insert(esTextDocument);
    }

    /**
     * 修改全文检索数据
     */
    @Override
    public Integer updateEsTextDocument(EsTextDocument esTextDocument) {
        // 如果业务需要同步到数据库
        // noticeMapper.updatetNotice(new SysNotice());
        LambdaEsUpdateWrapper<EsTextDocument> wrapper = new LambdaEsUpdateWrapper<>();
        wrapper.eq(EsTextDocument::getNoticeId, esTextDocument.getNoticeId());
        return esTextDocumentMapper.update(esTextDocument, wrapper);
    }

    /**
     * 根据ID查询全文检索数据
     */
    @Override
    public EsTextDocument getEsTextDocument(Long noticeId) {
        LambdaEsQueryWrapper<EsTextDocument> wrapper = new LambdaEsQueryWrapper<EsTextDocument>();
        wrapper.eq(EsTextDocument::getNoticeId, noticeId);
        return esTextDocumentMapper.selectOne(wrapper);
    }

    /**
     * 根据ID删除全文检索数据
     */
    @Override
    public void deleteEsTextDocument(Long[] noticeIds) {
        for (Long noticeId : noticeIds)
        {
            // 如果业务需要同步到数据库
            // noticeMapper.deleteNotice(new SysNotice());

            LambdaEsQueryWrapper<EsTextDocument> wrapper = new LambdaEsQueryWrapper<EsTextDocument>();
            wrapper.eq(EsTextDocument::getNoticeId, noticeId);
            esTextDocumentMapper.delete(wrapper);
        }
    }
}
