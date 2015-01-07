import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class q03_Unsynchronized_Buffering 
{
	public static void main(String[] args) throws InterruptedException
	{
		
		// create Buffer(of size 1) to store Ints
	    Buffer<Integer> sharedBuffer = new unSynchronizedBuffer();
		
		// create threadPool for 2 threads
		ExecutorService threadPool= Executors.newCachedThreadPool();
		
		// request Trigger of 2 fresh Threads
		threadPool.execute(new ProducerThread<Buffer<Integer> > (sharedBuffer));
		threadPool.execute(new ConsumerThread<Buffer<Integer> > (sharedBuffer));
		
		// no new tasks accepted
		threadPool.shutdown();
		
		//wait limit is 1 minute, else will kill
		if( threadPool.awaitTermination(5, TimeUnit.SECONDS) )
			System.out.println("\n\nALL CHILD THREADS COMPLETED. SHUTTING MAIN NOW");
		else
		{
			System.out.println("\n\nWARNING: CHILD THREADS DID NOT COMEPLETE IN 5 SEC.");
			
			//System.out.println("KILLING FOR FUN NOW");  // Comment BELOW Line to continue
			//threadPool.shutdownNow();
		}
		
	}

}


//-------------------------------------------------------------


class unSynchronizedBuffer implements Buffer<Integer> 
{
	
	private Integer bufferValue ; // default initial value 'Null'
	
	@Override
	public void blockingPush(Integer value) throws InterruptedException 
	{
		bufferValue = value;
	}

	@Override
	public Integer blockingPull() throws InterruptedException 
	{
		return bufferValue;
	}
	
	public String toString()
	{
		if(bufferValue!=null)
		return Integer.toString(bufferValue);
		else return "EMPTY";
		
	}
}
