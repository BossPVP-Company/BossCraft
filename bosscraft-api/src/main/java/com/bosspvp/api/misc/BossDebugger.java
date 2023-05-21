package com.bosspvp.api.misc;

import com.bosspvp.api.BossPlugin;

public class BossDebugger {
    private final String taskId;
    private final String startInfo;
    private final BossPlugin plugin;
    /**
     * Task Debugger, which can help u to
     * check the time required to execute the task
     *
     * @param plugin the plugin
     * @param taskId id of a task
     * @param startInfo info that is written on start debugging
     */
    public BossDebugger(BossPlugin plugin, String taskId, String startInfo){
        this.plugin = plugin;
        this.taskId = taskId;
        this.startInfo = startInfo;
    }
    /**
     * Start the task
     *
     */
    public void start(Runnable taskToDebug){
        if(!plugin.getConfigSettings().isDebug()){
            taskToDebug.run();
            return;
        }
        plugin.getLogger().info("&eDEBUG-> task with id "+taskId+" has been started\n"+startInfo);
        long debugTimer = System.currentTimeMillis();
        try{
            taskToDebug.run();
        }catch (Exception exception){
            plugin.getLogger().info("&eDEBUG-> task with id "+taskId+"&c FAILED WITH AN ERROR!");
            throw exception;
        }
        debugTimer = System.currentTimeMillis() - debugTimer;
        plugin.getLogger().info("&eDEBUG-> task with id "+taskId+"&a SUCCESS\n " +
                "&eIt took &6"+debugTimer+"&e milliseconds to execute");

    }
}
