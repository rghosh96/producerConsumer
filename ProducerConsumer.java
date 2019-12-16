//Name: Rashi Ghosh
//UAID: 010699523


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer {
	
	//locks & semaphores - notice buffer size is maxed out at 5
	private static Semaphore empty = new Semaphore(5);
    private static Semaphore full = new Semaphore(0);
    private static Semaphore mutex = new Semaphore(1);
    

    //implement buffer as a linked list queue
    static Queue<Integer> buffer = new LinkedList<Integer>();
    
	
    
	//Thread activities
    static class ProducerThread implements Runnable {
		//generates random integer to be loaded into buffer
		private Random rand = new Random();
		private int randNum;
		
		//random amount sleep time between 0-5s
		double sleepyTime = (Math.random() * .5) * 1000;


		public void run() {
			for (int i = 0; i < 100; i++) {
	            //acquire semaphore (check if space in buffer, and decrement # of empty spots)
	            try {
					empty.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	            
	            //acquire mutex lock (check if can enter critical section, & take lock; 0)
	            try {
					mutex.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	            
	            //*X*X*X*X*X*X*X*X*X*X*!!!NOW ENTERING CRITICAL SECTION!!!*X*X*X*X*X*X*X*X*X*X*
	            
	            //sleep random amount of time between 0s - 0.5s to simulate processing
	            try {
					Thread.sleep((long) sleepyTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	            
	            //generate random number to add to buffer & print that number
	            randNum = rand.nextInt(1000000000);
	            System.out.println("Producer produced " + randNum);
	            
	            //add random number to buffer & increment spot in buffer
	            buffer.add(randNum);
	            
	            //*X*X*X*X*X*X*X*X*X*X*!!!NOW EXITING CRITICAL SECTION!!!*X*X*X*X*X*X*X*X*X*X*
	            
	            //release lock (increment back to 1)
	            mutex.release();
	            //increment # of full spots in buffer
	            full.release();
	    	}
		}
	}
	
    static class ConsumerThread implements Runnable {
    	//random amount sleep time between 0-5s
		double sleepyTime = (Math.random() * .5) * 1000;

		
		public void run() {
			for (int i = 0; i < 100; i++) {
				// acquire semaphore (check if anything in buffer, & decrement # of full spots)
	            try {
					full.acquire();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
	            
	            //acquire mutex lock (check if can enter critical section, & take lock; 0)
	            try {
					mutex.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	            
	            //*X*X*X*X*X*X*X*X*X*X*!!!NOW ENTERING CRITICAL SECTION!!!*X*X*X*X*X*X*X*X*X*X*
	            
	            //sleep random amount of time between 0s - 0.5s to simulate processing
	            try {
					Thread.sleep((long) sleepyTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	            
	            //print & consume first item from buffer, yummy!
	            System.out.println("       Consumer consumed " + buffer.peek());
	            buffer.remove();

	            //*X*X*X*X*X*X*X*X*X*X*!!!NOW EXITING CRITICAL SECTION!!!*X*X*X*X*X*X*X*X*X*X*
	            
	            //release lock, increment lock semaphore back to 1
	            mutex.release();
	            //increment # of empty spots in buffer
	            empty.release();
	    	}
		}
	}
	
	
	
	//main program
	public static void main(String[] args) {

		//load command line arguments into program
		long SLEEPIEtimie = Long.parseLong(args[0]);
		int numProd = Integer.parseInt(args[1]);
		int numCons = Integer.parseInt(args[2]);
		
		System.out.println();
		System.out.println("*************************************************************");
		System.out.println("Hello!!! Here's what you entered from the command line: ");
		System.out.println();
		System.out.println("Sleepy Time: " + SLEEPIEtimie + " seconds");
		System.out.println("Number of Producer Threads : " + numProd);
		System.out.println("Number of Consumer Threads: " + numCons);
		System.out.println();
		System.out.println("Now let's begin!!!");
		System.out.println("*************************************************************");
		System.out.println();
		
		//u gotta make sure to convert s to ms!!!
		SLEEPIEtimie = SLEEPIEtimie * 1000;
		
		//create arrays that will hold multiple threads; one for producer threads, other for consumer	
		Thread[] p = new Thread[numProd];
		Thread[] c = new Thread[numCons];
		
		//create producer threads & let them ggoooooo
		for (int i = 0; i < numProd; i++) {
			p[i] = new Thread(new ProducerThread(), "Producer Thread");
			p[i].start();
		}
		
		//create consumer threads & let em run yeet
		for (int i = 0; i < numCons; i++) {
			c[i] = new Thread(new ConsumerThread(), "Consumer Thread");
			c[i].start();
		}
		
        //let application take a lil napper :)
		try {
			Thread.sleep(SLEEPIEtimie);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		System.exit(0); 
		//System.out.print("boop");
	}

}