package org.example.mybatis.session;

/**
 * 结果上下文
 */
public interface ResultContext {
    /**
     * 获取结果
     */
    Object getResultObject();

    /**
     * 获取结果数量
     * @return
     */
    int getResultCount();
}
