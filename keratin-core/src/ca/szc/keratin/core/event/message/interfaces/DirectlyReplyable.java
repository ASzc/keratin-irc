package ca.szc.keratin.core.event.message.interfaces;

public interface DirectlyReplyable
{
    /**
     * Reply to the event with some text by appending the sender to the reply message.
     * 
     * @param reply Text to use in reply
     */
    public void replyDirectly( String reply );
}
