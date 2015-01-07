import java.security.SecureRandom;


public class q01_InterruptMethod {

	public static void main(String[] args) 
	{
		// Generates a Cryptographically secure Random number
		SecureRandom sc = new SecureRandom();
		for(int i=0;i<5;i++)
		System.out.println( sc.nextInt(10) );
		
		Thread.currentThread().interrupt();  // what does it do  ??
	}

}
