package org.vinsert.api.event;

/**
 * An event published when a message is received by the client.
 */
public final class MessageEvent {
    private final String message;
    private final String sender;
    private final String clanName;

    public MessageEvent(String raw) {
        String[] data = raw.split(":");
        if (data[0].length() == 0) {
            sender = "SERVER";
        } else {
            sender = data[0];
        }
        message = data[1];
        clanName = data[2];
    }

    public String getSender() {
        return sender;
    }

    public String getClanName() {
        return clanName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return sender + ":" + message + ":" + clanName;
    }
}
