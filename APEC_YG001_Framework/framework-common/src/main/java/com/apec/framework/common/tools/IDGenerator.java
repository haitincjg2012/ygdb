package com.apec.framework.common.tools;

import com.apec.framework.common.exception.ApecRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 类 编 号：
 * 类 名 称：IDGenerator
 * 内容摘要：ID生成器
 * 完成日期：
 * 编码作者：
 */

public abstract class IDGenerator
{

    protected static final Log log = LogFactory.getLog( IDGenerator.class );

    private static long workerId = 1;

    public static void setWorkerId(long workerId)
    {
        IDGenerator.workerId = workerId;
    }

    /**
     * 获取本机mac
     * @return 机器地址
     */
    private static String getLocalMac()
    {
        StringBuilder sb = new StringBuilder( "" );
        try
        {
            InetAddress ia = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress( ia ).getHardwareAddress();
            for(int i = 0; i < mac.length; i++)
            {
                if(i != 0)
                {
                    sb.append( "-" );
                }
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString( temp );
                if(str.length() == 1)
                {
                    sb.append( "0" ).append( str );
                }
                else
                {
                    sb.append( str );
                }
            }
        }
        catch (SocketException | UnknownHostException e)
        {
            log.error( "LocalMac.getLocalMac:case" + e.getMessage() );
            throw new ApecRuntimeException( "IDGenerator.getLocalMac:case", e.getMessage() );
        }

        return sb.toString();
    }

    /**
     * 获取pid
     * @return 进程ID
     */
    private static String getPid()
    {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.split( "@" )[0];
    }

    /**
     * 获取机器ID
     * @return UID
     */
    public synchronized static long getWorkId()
    {
        log.debug( "IDGenerator.getWorkId workerId:" + workerId );
        String pid = getPid();
        final IdWorker idWorker = new IdWorker( workerId, pid.hashCode() % 30 );
        return idWorker.nextId();
    }

    public static void main(String[] args)
    {
        final IdWorker idWorker = new IdWorker( 1, 30 );
        System.out.println( idWorker.nextId() );
    }
}
