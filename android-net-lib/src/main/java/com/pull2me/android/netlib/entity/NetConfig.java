package com.pull2me.android.netlib.entity;

import java.io.Serializable;

/**
 * Created by drz on 2017/3/2.
 */

public class NetConfig implements Serializable {
    private int connectionOutTime = 60;
    private int readOutTime = 60 * 10;

    public NetConfig() {

    }

    public int getConnectionOutTime() {
        return connectionOutTime;
    }

    public NetConfig(int connectionOutTime, int readOutTime) {
        this.connectionOutTime = connectionOutTime;
        this.readOutTime = readOutTime;
    }

    public void setConnectionOutTime(int connectionOutTime) {
        this.connectionOutTime = connectionOutTime;
    }

    public int getReadOutTime() {
        return readOutTime;
    }

    public void setReadOutTime(int readOutTime) {
        this.readOutTime = readOutTime;
    }
}
