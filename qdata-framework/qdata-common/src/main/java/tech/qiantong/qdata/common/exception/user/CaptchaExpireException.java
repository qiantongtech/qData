package tech.qiantong.qdata.common.exception.user;

/**
 * 验证码失效异常类
 *
 * @author qdata
 */
public class CaptchaExpireException extends UserException
{
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException()
    {
        super("user.jcaptcha.expire", null);
    }
}
