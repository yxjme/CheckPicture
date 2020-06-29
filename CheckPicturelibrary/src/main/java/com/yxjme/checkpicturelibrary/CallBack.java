package com.yxjme.checkpicturelibrary;

public interface CallBack<T> {
    void result(T t);
    void error(String s);
}
