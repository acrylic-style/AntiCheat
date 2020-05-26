package xyz.acrylicstyle.anticheat;

import xyz.acrylicstyle.anticheat.api.AntiCheatConfiguration;

public class AntiCheatConfigurationImpl extends AntiCheatConfiguration {
    public AntiCheatConfigurationImpl(String path) {
        super(path);
    }

    @Override
    public boolean kickPlayer() {
        return this.getBoolean("kickPlayer", false);
    }

    @Override
    public void setKickPlayer(boolean flag) {
        this.set("kickPlayer", flag);
    }

    @Override
    public boolean detectBlink() {
        return this.getBoolean("detectBlink", true);
    }

    @Override
    public boolean detectFly() {
        return this.getBoolean("detectFly", false);
    }

    @Override
    public boolean detectClickBot() {
        return this.getBoolean("detectClickBot", true);
    }

    @Override
    public boolean detectSpeed() {
        return this.getBoolean("detectSpeed", false);
    }

    @Override
    public void setDetectBlink(boolean b) {
        this.set("detectBlink", b);
    }

    @Override
    public void setDetectFly(boolean b) {
        this.set("detectFly", b);
    }

    @Override
    public void setDetectClickBot(boolean b) {
        this.set("detectClickBot", b);
    }

    @Override
    public void setDetectSpeed(boolean b) {
        this.set("detectSpeed", b);
    }

    @Override
    public int getBlinkPacketsThreshold() {
        return this.getInt("blinkPacketsThreshold", 40);
    }

    @Override
    public int getFlyVerticalThreshold() {
        return this.getInt("flyVerticalThreshold", 12);
    }

    @Override
    public int getClicksThreshold() {
        return this.getInt("clicksThreshold", 35);
    }

    @Override
    public int getSpeedThreshold() {
        return this.getInt("speedThreshold", 30);
    }

    @Override
    public void setBlinkPacketsThreshold(int i) {
        this.set("blinkPacketsThreshold", i);
    }

    @Override
    public void setFlyVerticalThreshold(int i) {
        this.set("flyVerticalThreshold", i);
    }

    @Override
    public void setClicksThreshold(int i) {
        this.set("clicksThreshold", i);
    }

    @Override
    public void setSpeedThreshold(int i) {
        this.set("speedThreshold", i);
    }
}
