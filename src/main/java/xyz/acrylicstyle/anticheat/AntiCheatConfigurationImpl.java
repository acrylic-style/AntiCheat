package xyz.acrylicstyle.anticheat;

import org.bukkit.configuration.InvalidConfigurationException;
import xyz.acrylicstyle.anticheat.api.AntiCheatConfiguration;

import java.io.IOException;

public class AntiCheatConfigurationImpl extends AntiCheatConfiguration {
    public AntiCheatConfigurationImpl(String path) throws IOException, InvalidConfigurationException {
        super(path);
    }

    @Override
    public boolean detectBlink() {
        return this.getBoolean("detectBlink", true);
    }

    @Override
    public boolean detectFly() {
        return this.getBoolean("detectFly", true);
    }

    @Override
    public boolean detectClickBot() {
        return this.getBoolean("detectClickBot", true);
    }

    @Override
    public boolean detectSpeed() {
        return this.getBoolean("detectSpeed", true);
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
        return this.getInt("flyVerticalThreshold", 10);
    }

    @Override
    public int getClicksThreshold() {
        return this.getInt("clicksThreshold", 38);
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
