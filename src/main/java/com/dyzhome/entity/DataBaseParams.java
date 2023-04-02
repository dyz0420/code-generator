package com.dyzhome.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Dyz
 */
@Data
public class DataBaseParams {
    @NotBlank(message = "不可为空")
    private String ip;
    @NotBlank(message = "不可为空")
    private String port;
    @NotBlank(message = "不可为空")
    private String dbName;
    @NotBlank(message = "不可为空")
    private String username;
    @NotBlank(message = "不可为空")
    private String password;

    private String author;

    private String pack;

    private String[] tablePrefix;
}
