package xiaojin.gachaaddiction.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface ToClientPayload {
    void handle(IPayloadContext context);
}
