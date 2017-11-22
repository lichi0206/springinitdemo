package com.lc.springioinit.springinitdemo.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @auther lichi
 * @create 2017-11-15 21:57
 */
public class FlagValidatorClass implements ConstraintValidator<FlagValidator, Object> {

    // 临时变量保存Flag值
    private String values;

    // 初始化value的值
    @Override
    public void initialize(FlagValidator flagValidator) {
        // 将注解内配置的值赋值给临时变量
        this.values = flagValidator.values();
    }

    /**
     * 实现验证
     * @param o
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        String[] value_array = values.split(",");
        boolean isFlag = false;

        // 遍历比对有效值
        for (String value : value_array) {
            // 存在一致，跳出循环
            if (value.equals(o)) {
                isFlag = true;
                break;
            }
        }

        return isFlag;
    }
}
