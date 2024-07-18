package com.li.entity;

import lombok.Data;

import java.io.Serializable;

/*全局异常处理*/
@Data
public class Result implements Serializable { // 序列化
  private int code; // 200是正常 400表示异常
  private String msg;
  private Object data; // 返回数据

  // 成功
  public static Result success(Object data) {

    return success(200, "操作成功", data);
  }

  public static Result success() {

    return success(200, "操作成功", "");
  }

  public static Result ok(Object data) {

    return success(200, "操作成功", data);
  }

  public static Result ok() {

    return success(200, "操作成功", "");
  }

  // 成功
  public static Result success(int code, String msg, Object data) {
    Result r = new Result();
    r.setCode(code);
    r.setMsg(msg);
    r.setData(data);
    return r;
  }

  public static Result ok(int code, String msg, Object data) {
    Result r = new Result();
    r.setCode(code);
    r.setMsg(msg);
    r.setData(data);
    return r;
  }

  // 失败
  public static Result fail(String msg) {

    return fail(400, msg, null);
  }

  // 失败
  public static Result fail(String msg, Object data) {

    return fail(400, msg, data);
  }

  // 失败
  public static Result fail(int code, String msg, Object data) {
    Result r = new Result();
    r.setCode(code);
    r.setMsg(msg);
    r.setData(data);
    return r;
  }
}
