package ca.szc.keratin.core.event.message.interfaces;

public interface Replyable
{
    /**
     * Reply to the event with some text.
     * 
     * @param reply Text to use in reply
     */
    public void reply( String reply );
}
