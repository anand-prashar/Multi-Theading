import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class q05_SyncBuffer_UsingWaitNotify 
{
	public static void main(String[] args) throws InterruptedException
	{
		
		Buffer<Integer> sharedBuffer2 = new SyncedBuffer();
		
		// create threadPool for 2 threads
		ExecutorService threadPool= Executors.newCachedThreadPool();
		
		// request Trigger of 2 fresh Threads
		threadPool.execute(new ProducerThread <Buffer<Integer>>  (sharedBuffer2));
		threadPool.execute(new ConsumerThread <Buffer<Integer>>  (sharedBuffer2));
		
		
		// no new tasks accepted
		threadPool.shutdown();				
		
	}

}

//----------------------------------------------------------------

class SyncedBuffer implements Buffer<Integer>
{
	Integer bufferData=0;
	Boolean getBufferLock=true, alreadyChanged=false;

	@Override
	public synchronized  void blockingPush(Integer value) throws InterruptedException 
	{
		
		System.out.println("...GOT IN PUSH = "+value);
		while(true)
		{
		synchronized (getBufferLock) 
		{
			
			if(alreadyChanged==false)
			{
				this.bufferData = value; // push data
				System.out.println("...WROT :  "+value+" .. buffer="+bufferData);
				
				alreadyChanged = true;
				
				getBufferLock.notifyAll(); //
			}
			else
				getBufferLock.wait();  // wait to get lock on 'getBufferLock'	
			
			break; // break While
		}}
		System.out.println("...EXITING PUSH = "+value);
		
				
	}

	@Override
	public  Integer blockingPull() throws InterruptedException 
	{
		Integer data=null;
		
		while(true)
		synchronized (getBufferLock) 
		{
			
			if(alreadyChanged==false)
				getBufferLock.wait();
			
			data = bufferData;
			
			bufferData= 0;  // means buffer is empty now
			//System.out.println(" - - READ :  "+data+" .. buffer="+bufferData);
			
			alreadyChanged = false;
			getBufferLock.notifyAll();
			
			break;
			
		}
		return data;

	}
	
	public String toString()
	{
		if (bufferData== null)
		return "NULL";
		else
			return Integer.toString(bufferData);
	}
}
