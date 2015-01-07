import java.security.SecureRandom;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class q04_Synchronized_Buffering 
{
	public static void main(String[] args) throws InterruptedException
	{
		
		// create Threadsafe Buffer(of size 1) to store Ints
		ArrayBlockingQueue<Integer> sharedMemory = new ArrayBlockingQueue<>(1); // Q is of size 1
		Buffer<Integer> sharedBuffer2 = new SynchronizedBuffer(sharedMemory);
		
		// create threadPool for 2 threads
		ExecutorService threadPool= Executors.newCachedThreadPool();
		
		// request Trigger of 2 fresh Threads
		threadPool.execute(new ProducerThread <Buffer<Integer>>  (sharedBuffer2));
		threadPool.execute(new ConsumerThread <Buffer<Integer>>  (sharedBuffer2));
		
		/*threadPool.execute(new ProducerThread2(sharedMemory));
		threadPool.execute(new ConsumerThread2(sharedMemory));*/
		
		
		
		// no new tasks accepted
		threadPool.shutdown();
		
		
		
	}
}


//----------------------------- FOR BUFFER VARIABLE OBJECT--------------------------------


class SynchronizedBuffer implements Buffer<Integer> 
{
	
	ArrayBlockingQueue<Integer> bufferValue;   //ABQ HAS TAKE() & PUT() - threadsafe methods
	
	public SynchronizedBuffer(ArrayBlockingQueue<Integer> bufferValue) 
	{
		this.bufferValue = bufferValue;
	}
	
	@Override
	public void blockingPush(Integer value) throws InterruptedException 
	{
		bufferValue.put(value);   // CAUSES TO WAIT if buffer is full
	}

	@Override
	public Integer blockingPull() throws InterruptedException 
	{
		return bufferValue.take();  // CAUSES TO WAIT if Buffer is empty
		
	}
	
	public String toString()
	{
		if(bufferValue.peek()!=null)
		return Integer.toString(bufferValue.peek());
		else return "EMPTY";
		
	}
}




/*  NOT NEEDED - Using Original Prod+ Consumer classes now
 * 
 */
//----------------------------- PRODUCER ---------------------------------

class ProducerThread2 implements Runnable
{
	ArrayBlockingQueue<Integer> refToBuffer; // Need ABQ here
	private static final SecureRandom randomGenerator = new SecureRandom();
	
	@Override
	public void run() 
	{
		Integer sum=0;
		for(Integer i=1;i<=10;i++)
		{
			try {
					Thread.sleep(randomGenerator.nextInt(750)); // sleep 0 - 1.5 sec
					refToBuffer.put(i);
					sum += i;
					
					System.out.print("\nProducer pushed "+i+". Total Push sums="+sum+".   Buffer = "+refToBuffer+"\n");
			    } 
			catch (InterruptedException e) 
			    {
					Thread.currentThread().interrupt();
			    }
			
		}
	}

	public ProducerThread2(ArrayBlockingQueue<Integer> refToBuffer) 
	{
		super();
		this.refToBuffer = refToBuffer;
	}	
}

//---------------------------- CONSUMER---------------------------------

class ConsumerThread2   implements Runnable
{
	ArrayBlockingQueue<Integer> refToBuffer;
	private static final SecureRandom randomGenerator = new SecureRandom();
	
	@Override
	public void run() 
	{
		Integer sum=0, pulledVal;
		for(Integer i=1;i<=10;i++)
		{
			try {
					Thread.sleep(randomGenerator.nextInt(750)); // sleep 0 - 1.5 sec
					pulledVal = refToBuffer.take();  // <----- FORCES MULTITHREADNIG
					if(pulledVal != null)
					sum += pulledVal;
					
					System.out.print("Consumer_"+i+" read "+pulledVal+". Total pull sums="+sum+".   Buffer = "+refToBuffer+"\n");
			    } 
			catch (InterruptedException e) 
			    {
					Thread.currentThread().interrupt();
			    }
			
		}
	}

	public ConsumerThread2(ArrayBlockingQueue<Integer> refToBuffer) 
	{
		super();
		this.refToBuffer = refToBuffer;
	}	
}