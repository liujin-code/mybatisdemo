package org.example.mybatis.datasource.pooled;

import java.util.ArrayList;
import java.util.List;

public class PoolState {
    protected PooledDataSource dataSource;

    //空闲连接
    protected final List<PooledConnection> idleConnections = new ArrayList<>();
    //活跃连接
    protected final List<PooledConnection> activeConnections = new ArrayList<>();
    //请求次数
    protected final long requestCount = 0;
    //总请求时间
    protected final long accumulatedRequestTime = 0;
    //总检查时间
    protected final long accumulatedCheckoutTime = 0;
    //已声明的逾期连接计数
    protected final long claimedOverdueConnectionCount = 0;
    //累计结账超时连接时间数
    protected final long accumulatedCheckoutTimeOfOverdueConnections = 0;
    //总等待时间
    protected final long accumulatedWaitTime = 0;
    //要等待的次数
    protected final long hadToWaitCount = 0;
    //失败的链接次数
    protected final long badConnectionCount = 0;

    public PoolState(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public synchronized long getRequestCount() {
        return requestCount;
    }

    public synchronized long getAverageRequestTime() {
        return requestCount > 0 ? accumulatedRequestTime / requestCount : 0;
    }

    public synchronized long getAverageWaitTime() {
        return hadToWaitCount > 0 ? accumulatedWaitTime / hadToWaitCount : 0;
    }

    public synchronized long getHadToWaitCount() {
        return hadToWaitCount;
    }

    public synchronized long getBadConnectionCount() {
        return badConnectionCount;
    }

    public synchronized long getClaimedOverdueConnectionCount() {
        return claimedOverdueConnectionCount;
    }

    public synchronized long getAverageOverdueCheckoutTime() {
        return claimedOverdueConnectionCount > 0 ? accumulatedCheckoutTimeOfOverdueConnections / claimedOverdueConnectionCount : 0;
    }

    public synchronized long getAverageCheckoutTime() {
        return requestCount > 0 ? accumulatedCheckoutTime / requestCount : 0;
    }

    public synchronized int getIdleConnectionCount() {
        return idleConnections.size();
    }

    public synchronized int getActiveConnectionCount() {
        return activeConnections.size();
    }
}
