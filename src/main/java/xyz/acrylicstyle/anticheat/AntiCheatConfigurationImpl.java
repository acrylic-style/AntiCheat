package xyz.acrylicstyle.anticheat;

import util.CollectionList;
import util.ICollectionList;
import xyz.acrylicstyle.anticheat.api.AntiCheatConfiguration;

import java.util.List;
import java.util.UUID;

public class AntiCheatConfigurationImpl extends AntiCheatConfiguration {
    public AntiCheatConfigurationImpl(String path) {
        super(path);
    }

    @Override
    public CollectionList<UUID> getBypassList() {
        return (CollectionList<UUID>) ICollectionList.asList(this.getStringList("bypassList")).map(UUID::fromString);
    }

    @Override
    public void setBypassList(List<UUID> list) {
        this.set("bypassList", ICollectionList.asList(list).map(UUID::toString));
    }

    @Override
    public void addBypassList(UUID uuid) {
        setBypassList(getBypassList().clone().addChain(uuid));
    }

    @Override
    public void removeBypassList(UUID uuid) {
        setBypassList(getBypassList().clone().removeThenReturnCollection(uuid));
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
        return this.getInt("blinkPacketsThreshold", 60);
    }

    @Override
    public int getFlyVerticalThreshold() {
        return this.getInt("flyVerticalThreshold", 17);
    }

    @Override
    public int getClicksThreshold() {
        return this.getInt("clicksThreshold", 30);
    }

    @Override
    public int getSpeedThreshold() {
        return this.getInt("speedThreshold", 14);
    }

    @Override
    public int getBlockBreaksThreshold() {
        return this.getInt("blockBreaks", 50);
    }

    @Override
    public void setBlockBreaksThreshold(int i) {
        this.set("blockBreaks", i);
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

    @Override
    public void setDisableMovementCheck(boolean b) {
        this.set("disableMovementCheck", b);
    }

    @Override
    public boolean isDisableMovementCheck() {
        return this.getBoolean("disableMovementCheck", false);
    }
}
