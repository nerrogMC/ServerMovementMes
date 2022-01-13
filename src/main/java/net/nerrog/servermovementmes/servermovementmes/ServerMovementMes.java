package net.nerrog.servermovementmes.servermovementmes;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.slf4j.Logger;

@Plugin(
        id = "servermovementmes",
        name = "ServerMovementMes",
        version = "1.1-SNAPSHOT",
        authors = {"nerrog"}
)
public class ServerMovementMes {

    private final Logger logger;
    private final ProxyServer proxyServer;

    @Inject
    public ServerMovementMes(ProxyServer proxyServer, Logger logger){
        this.proxyServer = proxyServer;
        this.logger = logger;
    }


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        proxyServer.getEventManager().register(this, new MoveListener());
    }

    private void broadcast(String message) {
        TextComponent textComponent = Component.text(message);
        proxyServer.sendMessage(textComponent);
    }


    public class MoveListener {

        private String MovemesBuilder(Player p, ServerInfo s){
            return "[Proxy]"+p.getUsername()+"が"+s.getName()+"に移動しました。";
        }

        @Subscribe(order = PostOrder.EARLY)
        public void onServerConnected(ServerConnectedEvent e){
            broadcast(MovemesBuilder(e.getPlayer(), e.getServer().getServerInfo()));
        }
    }
}
