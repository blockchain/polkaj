package io.emeraldpay.polkaj.apiws;

import com.fasterxml.jackson.databind.JavaType;
import io.emeraldpay.polkaj.api.RpcCall;
import io.emeraldpay.polkaj.api.Subscription;

import java.util.function.Consumer;

public class DefaultSubscription<T> implements Subscription<T>, Consumer<Subscription.Event<? extends T>> {

    private String id;
    private final JavaType type;
    private final String unsubscribeMethod;
    private final PolkadotWsApi client;
    private Consumer<? extends Event<? extends T>> handlers;

    public DefaultSubscription(JavaType type, String unsubscribeMethod, PolkadotWsApi client) {
        this.type = type;
        this.unsubscribeMethod = unsubscribeMethod;
        this.client = client;
    }

    public String getId() {
        return id;
    }

    public JavaType getType() {
        return type;
    }

    public void setId(String id) {
        if (this.id != null) {
            throw new IllegalStateException("Subscription id is already set to " + this.id);
        }
        this.id = id;
    }

    @Override
    public void handler(Consumer<? extends Event<? extends T>> handler) {
        this.handlers = handler;
    }

    @SuppressWarnings("unchecked")
    public void accept(Subscription.Event<? extends T> event) {
        Consumer<Subscription.Event<? extends T>> handler = (Consumer<Subscription.Event<? extends T>>) this.handlers;
        if (handler == null) {
            return;
        }
        handler.accept(event);
    }

    @Override
    public void close() throws Exception {
        if (id == null) {
            return;
        }
        client.execute(RpcCall.create(Boolean.class, unsubscribeMethod, id));
        client.removeSubscription(id);
    }

}
