import java.security.SecureRandom;


public class ProducerThread<T extends Buffer<Integer> >   implements Runnable
{
	T refToBuffer;
	private static final SecureRandom randomGenerator = new SecureRandom();
	
	@Override
	public void run() 
	{
		Integer sum=0;
		for(Integer i=1;i<=10;i++)
		{
			try {
					Thread.sleep(randomGenerator.nextInt(1500)); // sleep 0 - 1.5 sec
					refToBuffer.blockingPush(i);
					sum += i;
					
					//System.out.print("\nProducer pushed "+i+". Total Push sums="+sum+".   Buffer = "+refToBuffer+"\n");
			    } 
			catch (InterruptedException e) 
			    {
					Thread.currentThread().interrupt();
			    }
			
		}
	}

	public ProducerThread(T refToBuffer) 
	{
		super();
		this.refToBuffer = refToBuffer;
	}

}
