package org.example.mybatis.datasource.pooled;

import lombok.SneakyThrows;
import org.example.mybatis.datasource.unpooled.UnpooledDataSource;

import java.util.logging.Logger;

import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

public class PooledDataSource implements DataSource {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(PooledDataSource.class);

    //池状态
    private PoolState state = new PoolState(this);

    private final UnpooledDataSource dataSource;
    //活跃连接数
    protected int poolMaximumActiveConnections = 10;
    //空闲连接数
    protected int poolMaximumIdleConnections = 5;
    //在被强制返回之前,池中连接被检出时间
    protected int poolMaximumCheckoutTime = 20000;
    // 这是给连接池一个打印日志状态机会的低层次设置,还有重新尝试获得连接, 这些情况下往往需要很长时间 为了避免连接池没有配置时静默失败)。
    protected int poolTimeToWait = 20000;
    // 发送到数据的侦测查询,用来验证连接是否正常工作,并且准备 接受请求。默认是“NO PING QUERY SET” ,这会引起许多数据库驱动连接由一 个错误信息而导致失败
    protected String poolPingQuery = "NO PING QUERY SET";
    // 开启或禁用侦测查询
    protected boolean poolPingEnabled = false;
    // 用来配置 poolPingQuery 多次时间被用一次
    protected int poolPingConnectionsNotUsedFor = 0;
    private int expectedConnectionTypeCode;

    public PooledDataSource() {
        dataSource = new UnpooledDataSource();
    }

    @SneakyThrows
    @Override
    public Connection getConnection() {
        return popConnection(dataSource.getUsername(), dataSource.getPassword()).getProxyConnection();
    }

    @SneakyThrows
    @Override
    public Connection getConnection(String username, String password) {
        return popConnection(username, password).getProxyConnection();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public void setDriver(String driver) {
        dataSource.setDriver(driver);
        forceCloseAll();
    }

    public void setUrl(String url) {
        dataSource.setUrl(url);
        forceCloseAll();
    }

    public void setUsername(String username) {
        dataSource.setUsername(username);
        forceCloseAll();
    }

    public void setPassword(String password) {
        dataSource.setPassword(password);
        forceCloseAll();
    }

    public int assembleConnectionTypeCode(String url, String username, String password) {
        return ("" + url + username + password).hashCode();
    }

    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        dataSource.setAutoCommit(defaultAutoCommit);
        forceCloseAll();
    }

    private void forceCloseAll() {
    }

    private PooledConnection popConnection(String username, String password) throws SQLException, InterruptedException {
        boolean countWait = false;
        long startTime = System.currentTimeMillis();
        int localBadConnectionCount = 0;
        PooledConnection conn = null;
        while (conn == null) {
            synchronized (state) {
                if (!state.idleConnections.isEmpty()) {
                    conn = state.idleConnections.remove(0);
                    logger.info("Checked out connection " + conn.getRealHashCode() + " from pool.");
                } else {
                    if (state.activeConnections.size() < poolMaximumActiveConnections) {
                        conn = new PooledConnection(dataSource.getConnection(), this);
                        logger.info("Created connection " + conn.getRealHashCode() + ".");
                    } else {
                        PooledConnection oldestConnection = state.activeConnections.get(0);
                        long oldestConnectionCheckoutTimestamp = oldestConnection.getCheckoutTimestamp();
                        if (oldestConnectionCheckoutTimestamp > poolMaximumCheckoutTime) {
                            state.claimedOverdueConnectionCount++;
                            state.accumulatedCheckoutTimeOfOverdueConnections += oldestConnectionCheckoutTimestamp;
                            state.accumulatedCheckoutTime += oldestConnectionCheckoutTimestamp;
                            state.activeConnections.remove(oldestConnection);
                            if (!oldestConnection.getRealConnection().getAutoCommit()) {
                                oldestConnection.getRealConnection().rollback();
                            }
                            conn = new PooledConnection(dataSource.getConnection(), this);
                            oldestConnection.invalid();
                            logger.info("Claimed overdue connection " + conn.getRealHashCode() + ".");
                        } else {
                            try {
                                if (!countWait) {
                                    state.hadToWaitCount++;
                                    countWait = true;
                                }
                                logger.info("Waiting as long as " + poolTimeToWait + " milliseconds for connection.");
                                long wt = System.currentTimeMillis();
                                state.wait(poolTimeToWait);
                                state.accumulatedWaitTime += System.currentTimeMillis() - wt;
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                    }
                }
                if (conn != null) {
                    if (conn.isValid()) {
                        if (!conn.getRealConnection().getAutoCommit()) {
                            conn.getRealConnection().commit();
                        }
                        conn.setConnectionTypeCode(assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword()));
                        // 记录checkout时间
                        conn.setCheckoutTimestamp(System.currentTimeMillis());
                        conn.setLastUsedTimestamp(System.currentTimeMillis());
                        state.activeConnections.add(conn);
                        state.requestCount++;
                        state.accumulatedRequestTime += System.currentTimeMillis() - startTime;
                    } else {
                        logger.info("A bad connection (" + conn.getRealHashCode() + ") was returned from the pool, getting another connection");
                        // 如果没拿到，统计信息：失败链接 +1
                        state.badConnectionCount++;
                        localBadConnectionCount++;
                        conn = null;
                        // 失败次数较多，抛异常
                        if (localBadConnectionCount > (poolMaximumIdleConnections + 3)) {
                            logger.debug("PooledDataSource: Could not get a good connection to the database.");
                            throw new SQLException("PooledDataSource: Could not get a good connection to the database.");
                        }
                    }
                }
            }
        }
        return conn;
    }


    public void pushConnection(PooledConnection pooledConnection) throws SQLException {
        synchronized (state) {
            state.activeConnections.remove(pooledConnection);
            if (pooledConnection.isValid()) {
                if (state.idleConnections.size() < poolMaximumIdleConnections && pooledConnection.getConnectionTypeCode() == expectedConnectionTypeCode) {
                    state.accumulatedCheckoutTime += pooledConnection.getCheckoutTimestamp();
                    if (!pooledConnection.getRealConnection().getAutoCommit()) {
                        pooledConnection.getRealConnection().rollback();
                    }
                    PooledConnection newConnections = new PooledConnection(pooledConnection.getRealConnection(), this);
                    state.idleConnections.add(newConnections);
                    newConnections.setCreatedTimestamp(pooledConnection.getCreatedTimestamp());
                    newConnections.setLastUsedTimestamp(pooledConnection.getLastUsedTimestamp());
                    pooledConnection.invalid();
                    logger.info("Returned connection " + newConnections.getRealHashCode() + " to pool.");
                    state.notifyAll();
                } else {
                    state.accumulatedCheckoutTime += pooledConnection.getCheckoutTimestamp();
                    if (!pooledConnection.getRealConnection().getAutoCommit()) {
                        pooledConnection.getRealConnection().rollback();
                    }
                    pooledConnection.getRealConnection().close();
                    logger.info("Closed connection " + pooledConnection.getRealHashCode() + ".");
                    pooledConnection.invalid();
                }
            } else {
                logger.info("A bad connection (" + pooledConnection.getRealHashCode() + ") attempted to return to the pool, discarding connection.");
                state.badConnectionCount++;
            }

        }
    }
}
