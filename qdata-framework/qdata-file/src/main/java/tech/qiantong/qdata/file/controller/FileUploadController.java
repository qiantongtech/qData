package tech.qiantong.qdata.file.controller;

import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.config.ServerConfig;
import tech.qiantong.qdata.file.util.FileUploadUtil;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * 文件上传控制器
 * 提供一系列文件上传的 API 接口
 * 该控制器将调用静态工具类 FileUploadUtil 来实现文件上传功能
 * 使用 @RestController 注解以支持 RESTful API 形式
 *
 * @author qdata
 */
@RestController
public class FileUploadController {

    /**
     * 文件存储服务
     * 使用 Spring 的 @Autowired 注解自动注入 FileStorageService 实例
     */
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ServerConfig serverConfig;
    @Value("${dromara.x-file-storage.local-plus[0].storage-path}")
    private String storagePath;

    /**
     * 初始化方法
     * 使用 @PostConstruct 注解表示在依赖注入完成后会自动调用该方法
     * 将注入的 FileStorageService 实例传递给静态工具类 FileUploadUtil
     */
    @PostConstruct
    public void init() {
        FileUploadUtil.init(fileStorageService, serverConfig, storagePath);
    }

    /**
     * 上传文件接口 -可用
     * 处理文件上传请求，将文件上传到默认存储平台
     *
     * @param file 要上传的文件，使用 MultipartFile 接收上传的文件
     * @return 上传成功后返回文件信息（FileInfo 对象）
     */
    @PostMapping("/upload")
    public FileInfo upload(MultipartFile file, String platForm) {
        return FileUploadUtil.uploadByParam(file, null,platForm);
    }

    /**
     * 上传图片接口 -暂不可用
     * 处理图片文件上传请求，同时生成缩略图
     *
     * @param file 要上传的图片文件，使用 MultipartFile 接收上传的图片文件
     * @return 上传成功后返回图片文件的信息（FileInfo 对象）
     */
    @PostMapping("/upload-image")
    public FileInfo uploadImage(MultipartFile file) {
        return FileUploadUtil.uploadImage(file);
    }

    /**
     * 上传文件到指定存储平台的接口 -暂不可用
     * 处理文件上传请求，将文件上传到指定的存储平台
     *
     * @param file 要上传的文件，使用 MultipartFile 接收上传的文件
     * @return 上传成功后返回文件信息（FileInfo 对象）
     */
    @PostMapping("/upload-platform")
    public FileInfo uploadPlatform(MultipartFile file) {
        return FileUploadUtil.uploadPlatform(file);
    }

    /**
     * 通过 HttpServletRequest 直接上传文件的接口 -暂不可用
     * 处理文件上传请求，直接从 HttpServletRequest 中读取文件进行上传
     *
     * @param request HttpServletRequest 对象，包含上传的文件数据
     * @return 上传成功后返回文件信息（FileInfo 对象）
     */
    @PostMapping("/upload-request")
    public FileInfo uploadPlatform(HttpServletRequest request) {
        return FileUploadUtil.uploadRequest(request);
    }
}
