package tech.qiantong.qdata.common.exception.file;

import tech.qiantong.qdata.common.exception.base.BaseException;

/**
 * 文件信息异常类
 *
 * @author qdata
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
