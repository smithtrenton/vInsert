package org.vinsert.api.event;

/**
 * @author : const_
 */
public final class PlayerMentionEvent {

    private MessageEvent message;

    public PlayerMentionEvent(MessageEvent message) {
        this.message = message;
    }

    public MessageEvent getMessage() {
        return message;
    }
}
