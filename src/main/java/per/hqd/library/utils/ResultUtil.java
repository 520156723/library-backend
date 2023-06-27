package per.hqd.library.utils;

public class ResultUtil {
    public static Result success(Object data) {
        Result<Object> res = new Result<>();
        res.setCode(0);
        res.setMessage("成功!");
        res.setData(data);
        return res;
    }

    public static Result success() {
        return success(null);
    }

    public static Result error(Integer code, String message) {
        Result<String> res = new Result<>();
        res.setCode(code);
        res.setMessage(message);
        return res;
    }
}