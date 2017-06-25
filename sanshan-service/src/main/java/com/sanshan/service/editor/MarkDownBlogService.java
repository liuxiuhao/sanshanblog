package com.sanshan.service.editor;

import com.github.pagehelper.PageInfo;
import com.sanshan.pojo.dto.MarkDownBlogDTO;
import com.sanshan.pojo.entity.MarkDownBlogDO;
import com.sanshan.service.convent.MarkDownEditorConvert;
import com.sanshan.service.editor.CacheService.MarkDownBlogCacheService;
import com.sanshan.service.vo.JwtUser;
import com.sanshan.util.BlogIdGenerate;
import com.sanshan.util.info.EditorTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MarkDownBlogService {
    @Autowired
    private   MarkDownBlogCacheService cacheService;

    @Autowired
    private  BlogIdGenerate blogIdGenerate;

    /**
     * DTO查询
     *
     * @return
     */
    public List<MarkDownBlogDTO> queryDtoAll() {
        return MarkDownEditorConvert.doToDtoList(cacheService.queryAll());
    }


    /**
     * DTO查询
     * @param example 查询条件
     * @return
     */
    public List<MarkDownBlogDTO> queryDtoListByWhere(MarkDownBlogDO example) {
        return MarkDownEditorConvert.doToDtoList(cacheService.queryListByWhere(example));
    }


    /** DTO查询
     *
     */
    public MarkDownBlogDTO queryDtoById(Long id){
        return MarkDownEditorConvert.doToDto(cacheService.queryById(id));
    }

    /**
     * DTO查询
     *
     * @param example 条件
     * @param page    页数
     * @param rows    行数
     * @return
     */
    public PageInfo<MarkDownBlogDTO> queryDtoPageListByWhere(MarkDownBlogDO example, Integer page, Integer rows) {
        PageInfo<MarkDownBlogDO> markDownBlogDOPageInfo = cacheService.queryPageListByWhere(example, page, rows);
        return MarkDownEditorConvert.doToDtoPage(markDownBlogDOPageInfo);
    }

    public Integer saveDO(String content, String title,String tag) {
        MarkDownBlogDO markDownBlog = new MarkDownBlogDO();
        markDownBlog.setId(blogIdGenerate.getId());
        markDownBlog.setContent(content);
        markDownBlog.setTag(tag);
        markDownBlog.setTitle(title);
        markDownBlog.setCreated(new Date());
        markDownBlog.setUpdated(new Date());
        Date date = new Date();
        markDownBlog.setTime(date);
        //获得当前用户
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        markDownBlog.setUser(user.getUsername());

        //加入到索引中
        blogIdGenerate.putTag(tag,blogIdGenerate.getId());
        blogIdGenerate.putTitle(title,blogIdGenerate.getId());
        blogIdGenerate.putDate(date,blogIdGenerate.getId());

        //加入IdMap对应
        blogIdGenerate.addIdMap(blogIdGenerate.getId(), EditorTypeEnum.MarkDown_EDITOR);

        return cacheService.save(markDownBlog);
    }


    public MarkDownBlogDTO updateDO(MarkDownBlogDO markDownBlogDO){
        return MarkDownEditorConvert.doToDto(cacheService.update(markDownBlogDO));
    }


    public Boolean  updateSelectiveDO(Long id,String content,String title,String tag){
        MarkDownBlogDO markDownBlogDO = new MarkDownBlogDO();
        markDownBlogDO.setId(id);
        markDownBlogDO.setContent(content);
        markDownBlogDO.setUpdated(new Date());
        markDownBlogDO.setTag(tag);
        markDownBlogDO.setTitle(title);
        //加入到索引中
        if (tag!=null)
            blogIdGenerate.putTag(tag,id);
        if (title!=null)
            blogIdGenerate.putTitle(title,id);
        MarkDownEditorConvert.doToDto(cacheService.updateSelective(markDownBlogDO));
        return true;
    }


    public Integer deleteDOById(Long id) {
        return cacheService.deleteById(id);
    }

}
