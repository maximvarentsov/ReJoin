package ru.gtncraft.rejoin;

import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class ReJoin extends Plugin implements Listener{

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, this);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onServerKick(final ServerKickEvent event) {
        ServerInfo kickedFrom;

        if (event.getPlayer().getServer() != null) {
            kickedFrom = event.getPlayer().getServer().getInfo();
        } else if (ProxyServer.getInstance().getReconnectHandler() != null) {
            kickedFrom = ProxyServer.getInstance().getReconnectHandler().getServer(event.getPlayer());
        } else {
            kickedFrom = AbstractReconnectHandler.getForcedHost(event.getPlayer().getPendingConnection());
            if (kickedFrom == null)
            {
                kickedFrom = ProxyServer.getInstance().getServerInfo(event.getPlayer().getPendingConnection().getListener().getDefaultServer());
            }
        }

        ServerInfo kickTo = ProxyServer.getInstance().getServerInfo(event.getPlayer().getPendingConnection().getListener().getDefaultServer());

        // Avoid the loop
        if (kickedFrom != null && kickedFrom.equals(kickTo)) {
            return;
        }

        event.setCancelled(true);
        event.setCancelServer(kickTo);
    }
}
