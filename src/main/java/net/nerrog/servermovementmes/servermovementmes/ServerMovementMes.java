package net.nerrog.servermovementmes.servermovementmes;

import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
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
        version = "1.2-SNAPSHOT",
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

        //サーバー間移動
        @Subscribe(order = PostOrder.EARLY)
        public void onServerConnected(ServerConnectedEvent e){
            //移動前のサーバーの情報があれば移動、なければ参加
            if (e.getPreviousServer().isPresent()){
                broadcast(
                        "[Proxy]"+e.getPlayer().getUsername()+"が"+e.getPreviousServer().get().getServerInfo().getName()+"から"+e.getServer().getServerInfo().getName()+"に移動しました。");
            }else {
                broadcast(
                        "[Proxy]"+e.getPlayer().getUsername()+"がサーバーに参加しました");
            }

        }

        //プロキシから切断されるときに呼び出される
        @Subscribe(order = PostOrder.EARLY)
        public void OnPlayerDisconnect(DisconnectEvent e){
            broadcast("[Proxy]"+e.getPlayer().getUsername()+"がサーバーから切断しました");
        }


    }
}
