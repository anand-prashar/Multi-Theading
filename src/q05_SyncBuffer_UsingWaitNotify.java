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
	Integer bufferData;
	Boolean lockedForWrite=false;

	@Override
	public void blockingPush(Integer value) throws InterruptedException 
	{
		synchronized (lockedForWrite) 
		{
			if(lockedForWrite==false && bufferData==null)
			{
				lockedForWrite= true;
				this.bufferData = value; // push data
				lockedForWrite = false;
				notifyAll(); // 
			}
			else
				this.wait();  // wait current thread	
			
		}
		
		
		
	}

	@Override
	public Integer blockingPull() throws InterruptedException 
	{
		while(lockedForWrite==false)
		{
			wait();
		}
		return bufferData;
	}
	
}
