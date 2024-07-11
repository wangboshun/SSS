namespace Common.Utils;

public class ResponseUtils
{
    /// <summary>
    /// 只输出响应码和状态
    /// </summary>
    /// <returns></returns>
    public static object Ok()
    {
        return new
        {
            code = 200,
            status = true
        };
    }

    /// <summary>
    /// 只输出响应码和状态
    /// </summary>
    /// <returns></returns>
    public static object Fail()
    {
        return new
        {
            code = 200,
            status = false
        };
    }

    /// <summary>
    /// 只输出响应码和状态
    /// </summary>
    /// <returns></returns>
    public static object Exception()
    {
        return new
        {
            code = 500,
            status = false
        };
    }

    /// <summary>
    /// 输出响应码、状态、消息
    /// </summary>
    /// <param name="msg"></param>
    /// <returns></returns>
    public static object Ok(string msg)
    {
        return new
        {
            code = 200,
            msg = msg,
            status = true
        };
    }

    /// <summary>
    /// 根据布尔值输出操作提示，输出响应码、状态、消息
    /// </summary>
    /// <param name="b"></param>
    /// <returns></returns>
    public static object Ops(bool b)
    {
        return b ? Ok("处理成功") : Fail("处理失败");
    }

    /// <summary>
    /// 输出响应码、状态、数据
    /// </summary>
    /// <param name="data"></param>
    /// <returns></returns>
    public static object Ok(dynamic data)
    {
        return new
        {
            code = 200,
            data = data,
            status = true
        };
    }

    /// <summary>
    /// 输出响应码、状态、消息
    /// </summary>
    /// <param name="msg"></param>
    /// <returns></returns>
    public static object Fail(string msg)
    {
        return new
        {
            code = 200,
            msg = msg,
            status = false
        };
    }

    /// <summary>
    /// 输出响应码、状态、消息
    /// </summary>
    /// <param name="msg"></param>
    /// <returns></returns>
    public static object Exception(string msg)
    {
        return new
        {
            code = 500,
            msg = msg,
            status = false
        };
    }

    /// <summary>
    /// 输出响应码、状态、消息
    /// </summary>
    /// <param name="e"></param>
    /// <returns></returns>
    public static object Exception(Exception e)
    {
        return new
        {
            code = 500,
            msg = e.Message,
            status = false
        };
    }

    /// <summary>
    /// 输出响应码、状态、消息、数据
    /// </summary>
    /// <param name="msg"></param>
    /// <param name="data"></param>
    /// <returns></returns>
    public static object Ok(string msg, object data)
    {
        return new
        {
            code = 200,
            msg = msg,
            data = data,
            status = true
        };
    }

    /// <summary>
    /// 输出响应码、状态、错误码
    /// </summary>
    /// <param name="msg"></param>
    /// <param name="errorCode"></param>
    /// <returns></returns>
    public static object Fail(string msg, string errorCode)
    {
        return new
        {
            code = 200,
            msg = msg,
            error_code = errorCode,
            status = false
        };
    }

    /// <summary>
    /// 输出响应码、状态码
    /// </summary>
    /// <param name="msg"></param>
    /// <param name="code"></param>
    /// <returns></returns>
    public static object Fail(string msg, int code)
    {
        return new
        {
            code = code,
            msg = msg,
            status = false
        };
    }
}