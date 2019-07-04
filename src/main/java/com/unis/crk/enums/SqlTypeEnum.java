package com.unis.crk.enums;

import lombok.Getter;

/**
 * @author pengxl
 * @version 1.0
 * 创建时间: 2019/06/10 14:12
 */
@Getter
public enum SqlTypeEnum {

    SELECT(1),
    INSERT(2),
    UPDATE(3),
    DELETE(4);

    private Integer type;

    SqlTypeEnum(Integer type) {
        this.type = type;
    }

    /**
     * 匹配sql类型  类型名称匹配 并返回类型值
     * @param sqlType
     * @return
     */
    public static Integer getValue(String sqlType) {
        for (SqlTypeEnum item : SqlTypeEnum.values()) {
            if (item.name().equalsIgnoreCase(sqlType)) {
                return item.getType();
            }
        }
        return null;
    }

    /**
     *匹配sql类型  类型值匹配 并返回类型名称转小写
     * @param sqlType
     * @return
     */
    public static String getName(Integer sqlType) {
        for (SqlTypeEnum item : SqlTypeEnum.values()) {
            if (item.getType().equals(sqlType)) {
                return item.name().toLowerCase();
            }
        }
        return null;
    }
}
