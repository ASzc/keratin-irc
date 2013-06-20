package ca.szc.keratin.core.event.message.interfaces;

public interface PrivatelyReplyable
{
    /**
     * Reply to the event's sender privately with some text.
     * 
     * @param reply Text to use in reply
     */
    public void replyPrivately( String reply );
}
