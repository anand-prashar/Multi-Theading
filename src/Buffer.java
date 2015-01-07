
public interface Buffer <E>
{
	// to add something to the Buffer
	public void blockingPush(E value) throws InterruptedException;
	
	// to read something from the buffer
	public E blockingPull() throws InterruptedException;
	
}
