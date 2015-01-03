import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class e02_ExecuterService 
{
	public static void main(String[] args) 
	{
		ExecutorService thread_Pool = Executors.newCachedThreadPool();
		
		thread_Pool.execute(new myThread("T_1"));
		thread_Pool.execute(new myThread("T_2"));
		thread_Pool.execute(new myThread("T_3"));
		
		// NEED TO SHUT DOWN SERVICE WHEN USED, ELSE WILL REMAIN ACTIVE
		// comment below line to verify
		thread_Pool.shutdown();
	}

}


@SuppressWarnings("unused")
class myThread implements Runnable
{
	private String tName;

	@Override
	public void run() 
	{
		try {
			    //Thread.currentThread().setName(tName);
				System.out.println(Thread.currentThread().getName()+" - going to sleep");
				
				Thread.sleep(3000);
			} 
		catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		System.out.println("\n"+Thread.currentThread().getName()+" - completed");
	}
	
	myThread(String tName)
	{
		this.tName=tName;
	}
}
