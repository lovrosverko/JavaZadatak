// Java program to implement solution of producer 
// consumer problem. 
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner; 

public class Threadexample 
{ 
	public static void main(String[] args) 
						throws InterruptedException 
	{ 
		// Object of a class that has both produce() 
		// and consume() methods 
		final PC pc = new PC(); 

		// Create producer thread 
		Thread t1 = new Thread(new Runnable() 
		{ 
			@Override
			public void run() 
			{ 
				try
				{ 
					pc.produce(); 
				} 
				catch(InterruptedException e) 
				{ 
					e.printStackTrace(); 
				} 
			} 
		}); 

		// Create consumer thread 
		Thread t2 = new Thread(new Runnable() 
		{ 
			@Override
			public void run() 
			{ 
				try
				{ 
					pc.consume(); 
				} 
				catch(InterruptedException e) 
				{ 
					e.printStackTrace(); 
				} 
			} 
		}); 

		// Start both threads 
		t1.start(); 
		t2.start(); 

		// t1 finishes before t2 
		t1.join(); 
		t2.join(); 
	} 

	// This class has a list, producer (adds items to list 
	// and consumber (removes items). 
	public static class PC 
	{ 
		// Create a list shared by producer and consumer 
		// Size of list is 2. 
		LinkedList<String[]> transakcija = new LinkedList<>(); 
		int capacity = 2; 
		String datoteka = "//home//lovro//eclipse-workspace//testMAI//docs//HOCIDC.001";
		String kontrola = "F";
		int brojac2 = 0;
		
		// Function called by producer thread 
		public void produce() throws InterruptedException 
		{ 
			String[] buffer = new String[12];
			int brojac = 0;
			try (Scanner scanner = new Scanner(new File(datoteka));) 
			{
			scanner.useDelimiter("\n");
			
			while (scanner.hasNext()) {
				String token = scanner.nextLine();
				buffer = token.split(":");
				brojac++;
				transakcija.add(buffer);
				if (buffer[6].equals(kontrola))
					break;
				
			}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			while (true) 
			{ 
				synchronized (this) 
				{ 
					// producer thread waits while list 
					// is full 
					while (transakcija.size()==capacity) 
						wait(); 

					System.out.println("Producer produced-"
												+ buffer[6]); 

					// to insert the jobs in the list 
					transakcija.add(buffer); 

					// notifies the consumer thread that 
					// now it can start consuming 
					notify(); 

					// makes the working of program easier 
					// to understand 
					Thread.sleep(1000); 
				} 
			} 
		} 

		// Function called by consumer thread 
		public void consume() throws InterruptedException 
		{ 
			while (true) 
			{ 
				synchronized (this) 
				{ 
					// consumer thread waits while list 
					// is empty 
					while (transakcija.size()==0) 
						wait(); 

					//to retrive the ifrst job in the list 
					String[] val = transakcija.removeFirst(); 

					for (int i = 0; i<val.length; i++) {
					System.out.println("Consumer consumed-"
													+ val[i]);
					}

					// Wake up producer thread 
					notify(); 

					// and sleep 
					Thread.sleep(1000); 
				} 
			} 
		} 
	} 
} 
