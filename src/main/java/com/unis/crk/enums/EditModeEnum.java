package com.unis.crk.enums;

/**
 * 报表工作模式
 * @author Administrator
 *
 */
public enum EditModeEnum {
    Design((short)0),				//设计模式
    Input((short)1);				//录入模式
    
 // 定义私有变量
    private Short value;
    // 构造函数，枚举类型只能为私有
    private EditModeEnum(Short value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
    
    /**
     * 枚举项的数字值
     * @return 枚举项的数字值
     */
    public Short value(){
    	return this.value;
    }
    
    /**
     * 获取枚举值
     * @param value：枚举项的数字值
     * @return 枚举值
     */
    public static EditModeEnum valueOf(Short value)
    {
    	switch(value){
        	case 0:
        		return Design;
        	case 1:
        		return Input;
        	default:
    			return null;
        }
    }
}