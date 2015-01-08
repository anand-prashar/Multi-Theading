import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class q06_CircularBuffer_onArray 
{
	public static void main(String[] args) 
	{
		Buffer<Integer> circularBuffer = new CircularBuffer();
		
		// create threadPool for 2 threads
		ExecutorService threadPool= Executors.newCachedThreadPool();
		
		// request Trigger of 2 fresh Threads
		threadPool.execute(new ProducerThread <Buffer<Integer>>  (circularBuffer));
		threadPool.execute(new ConsumerThread <Buffer<Integer>>  (circularBuffer));
		
		
		// no new tasks accepted
		threadPool.shutdown();	

	}

}

//---------- Circular Buffer-------------------------

class CircularBuffer implements Buffer<Integer>
{
	private int[] buffArray={-1,-1,-1}; // -1 =empty
	private int writeIndex = 0,
				cellsUsed  = 0,
				readIndex  = 0;
	
	// place value in buffer
	public synchronized void blockingPush(Integer value) throws InterruptedException 
	{
		while(cellsUsed >= buffArray.length)  // wait till buffer is full
		{
			System.out.println("Buffer FULL. Write- going to wait()");
			this.wait();  // Lock on Object of this class- is released
		}
		
		buffArray[writeIndex] = value;
		cellsUsed++;
		writeIndex = (writeIndex+1) % buffArray.length;  // need to rotate on limit-reach
		
		System.out.println("\nPushed = "+value+". OK.");
		showBuffer();
		
		this.notifyAll();  // notify all threads waiting to read from buffer
	}

	// read value from buffer
	public synchronized Integer blockingPull() throws InterruptedException 
	{
		if(cellsUsed==0)  // wait till buffer is empty
		{
			System.out.println("Buffer NULL. Read- going to wait()");
			this.wait();  // Lock on Object of this class- is released
		}
		
		Integer readValue = buffArray[readIndex];
		buffArray[readIndex] = -1;
		
		cellsUsed--;
		readIndex = (readIndex+1) % buffArray.length;
		
		System.out.println("\nPulled = "+readValue+". OK.");
		showBuffer();
		
		this.notifyAll();   // notify all threads waiting to read from buffer
		return readValue;
	}
	
	private synchronized void showBuffer()
	{
		System.out.print("[   ");
		for(int i=0;i<buffArray.length;i++)
		{	
			if(buffArray[i] != -1)
				System.out.print(buffArray[i]+"   ");
			else
				System.out.print("-   ");
		}
		System.out.println("]");
		
		System.out.print("R="+readIndex+" , W="+writeIndex);
		
	}
	public String toString()
	{
		return "";
	}
}

