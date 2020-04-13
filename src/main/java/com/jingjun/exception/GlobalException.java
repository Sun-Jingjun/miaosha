package com.jingjun.exception;

import com.jingjun.result.CodeMsg;

/**
 *
 */
public class GlobalException extends RuntimeException{

    private CodeMsg msg;

    public GlobalException(CodeMsg codeMsg) {
        super();
        this.msg = codeMsg;
    }

    public CodeMsg getMsg() {
        return msg;
    }

    public void setMsg(CodeMsg msg) {
        this.msg = msg;
    }


}
