import java.security.SecureRandom;


public class ConsumerThread<E extends Buffer<Integer>>   implements Runnable
{
	E refToBuffer;
	private static final SecureRandom randomGenerator = new SecureRandom();
	
	@Override
	public void run() 
	{
		Integer sum=0, pulledVal;
		for(Integer i=1;i<=10;i++)
		{
			try {
					Thread.sleep(randomGenerator.nextInt(1500)); // sleep 0 - 1.5 sec
					pulledVal = refToBuffer.blockingPull();
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

	public ConsumerThread(E refToBuffer) 
	{
		super();
		this.refToBuffer = refToBuffer;
	}

	
}
