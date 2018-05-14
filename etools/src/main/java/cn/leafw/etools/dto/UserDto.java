package cn.leafw.etools.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author CareyWYR
 * @description
 * @date 2018/5/8 11:31
 */
@Data
public class UserDto {

    /**用户编号**/
    private double userId;
    /**用户名**/
    private String userName;
    /**年龄**/
    private int age;
    /**出生日期**/
    private Date birthDate;
    /**月薪**/
    private BigDecimal salary;
}
