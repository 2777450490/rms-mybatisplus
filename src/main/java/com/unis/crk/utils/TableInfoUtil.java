package com.unis.crk.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengxl
 * @version 1.0
 * 创建时间: 2019/05/28 17:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableInfoUtil {

    /**
     * 表字段名
     */
    private String columnName;

    /**
     * 表字段类型
     */
    private Integer columnType;

    /**
     * 表字段备注
     */
    private String remarks;

    /**
     * 表字段长度
     */
    private Integer columnLength;

    /**
     * 表字段小数位长度
     */
    private Integer columnDecLength;

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (this == obj){
            return true;
        }
        if (obj instanceof TableInfoUtil) {
            TableInfoUtil infoUtil = (TableInfoUtil) obj;
            return this.columnName != null ? this.columnName.equals(infoUtil.columnName) : infoUtil.columnName == null;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.columnName != null ? this.columnName.hashCode() : 0;
    }

//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;
//
//        TableInfoUtil infoUtil = (TableInfoUtil) o;
//
//        return columnName != null ? columnName.equals(infoUtil.columnName) : infoUtil.columnName == null;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = super.hashCode();
//        result = 31 * result + (columnName != null ? columnName.hashCode() : 0);
//        return result;
//    }
}
