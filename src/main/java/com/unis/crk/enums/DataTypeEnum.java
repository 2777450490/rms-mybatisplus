package com.unis.crk.enums;

import lombok.Getter;

/**
 * @author pengxl
 * @version 1.0
 * 创建时间: 2019/06/10 14:12
 */
@Getter
public enum DataTypeEnum {

    INT(1),
    VARCHAR(2),
    CHAR(2),
    DATETIME(9),
    DATE(5);

    private Integer type;

    DataTypeEnum(Integer type) {
        this.type = type;
    }

    /**
     * 匹配数据类型  类型名称匹配 并返回类型值
     * @param dataType
     * @return
     */
    public static Integer getDataTypeValue(String dataType) {
        for (DataTypeEnum item : DataTypeEnum.values()) {
            if (item.name().equalsIgnoreCase(dataType)) {
                return item.getType();
            }
        }
        return DataTypeEnum.VARCHAR.getType();
    }

    /**
     *匹配数据类型  类型值匹配 并返回类型名称转小写
     * @param dataType
     * @return
     */
    public static String getDataTypeName(Integer dataType) {
        for (DataTypeEnum item : DataTypeEnum.values()) {
            if (item.getType().equals(dataType)) {
                return item.name().toLowerCase();
            }
        }
        return DataTypeEnum.VARCHAR.name().toLowerCase();
    }
}
