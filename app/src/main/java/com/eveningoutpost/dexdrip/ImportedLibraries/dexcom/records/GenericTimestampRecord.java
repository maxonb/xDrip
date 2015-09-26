package com.eveningoutpost.dexdrip.ImportedLibraries.dexcom.records;

import com.eveningoutpost.dexdrip.ImportedLibraries.dexcom.Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

// This code and this particular library are from the NightScout android uploader
// Check them out here: https://github.com/nightscout/android-uploader
// Some of this code may have been modified for use in this project

public class GenericTimestampRecord {

    protected final int OFFSET_SYS_TIME = 0;
    protected final int OFFSET_DISPLAY_TIME = 4;
    protected Date systemTime;
    protected long systemTimeSeconds;
    protected Date displayTime;
    protected long displayTimeSeconds;

    public GenericTimestampRecord(byte[] packet) {
        systemTimeSeconds = ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN).getInt(OFFSET_SYS_TIME);
        systemTime = Utils.receiverTimeToDate(systemTimeSeconds);
        displayTimeSeconds = ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN).getInt(OFFSET_DISPLAY_TIME);
        displayTime = Utils.receiverTimeToDate(displayTimeSeconds);
    }

    public GenericTimestampRecord(Date displayTime, Date systemTime){
        this.displayTime=displayTime;
        this.systemTime=systemTime;
    }

    public Date getSystemTime() {
        return systemTime;
    }
    public Date getDisplayTime() {
        return displayTime;
    }
    public long getSystemTimeSeconds() {
        return systemTimeSeconds;
    }
    public long getDisplayTimeSeconds() {
        return displayTimeSeconds;
    }

}
