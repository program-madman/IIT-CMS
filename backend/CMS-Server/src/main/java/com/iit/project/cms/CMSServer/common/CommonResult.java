package com.iit.project.cms.CMSServer.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Daniel
 * @date 2023年07月31日 1:35
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult {

    //是否成功
    private boolean success;
    //请求结果
    private Object result;
    //报错信息
    private String errMsg;




}
