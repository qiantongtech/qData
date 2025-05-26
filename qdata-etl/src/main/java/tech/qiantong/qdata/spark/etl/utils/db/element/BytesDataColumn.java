package tech.qiantong.qdata.spark.etl.utils.db.element;

import org.apache.commons.lang3.ArrayUtils;
import tech.qiantong.qdata.spark.etl.utils.db.exception.DBException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by jingxing on 14-8-24.
 */
public class BytesDataColumn extends DataColumn {

    public BytesDataColumn() {
        this(null);
    }

    public BytesDataColumn(byte[] bytes) {
        super(ArrayUtils.clone(bytes), Type.BYTES, null == bytes ? 0
                : bytes.length);
    }

    @Override
    public byte[] asBytes() {
        if (null == this.getRawData()) {
            return null;
        }

        return (byte[]) this.getRawData();
    }

    @Override
    public String asString() {
        if (null == this.getRawData()) {
            return null;
        }

        try {
            return ColumnCast.bytes2String(this);
        } catch (Exception e) {
            throw DBException.asDataXException(String.format("Bytes[%s]不能转为String .", this.toString()));
        }
    }

    @Override
    public Long asLong() {
        throw DBException.asDataXException("Bytes类型不能转为Long .");
    }

    @Override
    public BigDecimal asBigDecimal() {
        throw DBException.asDataXException("Bytes类型不能转为BigDecimal .");
    }

    @Override
    public BigInteger asBigInteger() {
        throw DBException.asDataXException("Bytes类型不能转为BigInteger .");
    }

    @Override
    public Double asDouble() {
        throw DBException.asDataXException("Bytes类型不能转为Long .");
    }

    @Override
    public Date asDate() {
        throw DBException.asDataXException("Bytes类型不能转为Date .");
    }

    @Override
    public Date asDate(String dateFormat) {
        throw DBException.asDataXException("Bytes类型不能转为Date .");
    }

    @Override
    public Boolean asBoolean() {
        throw DBException.asDataXException("Bytes类型不能转为Boolean .");
    }
}
