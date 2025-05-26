package tech.qiantong.qdata.spark.etl.utils.db.exception;


import java.io.PrintWriter;
import java.io.StringWriter;

public class DBException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    public DBException(String errorMessage) {
        super(errorMessage);
    }


    public static DBException asDataXException(String message) {
        return new DBException(message);
    }


    private static String getMessage(Object obj) {
        if (obj == null) {
            return "";
        }

        if (obj instanceof Throwable) {
            StringWriter str = new StringWriter();
            PrintWriter pw = new PrintWriter(str);
            ((Throwable) obj).printStackTrace(pw);
            return str.toString();
            // return ((Throwable) obj).getMessage();
        } else {
            return obj.toString();
        }
    }
}
