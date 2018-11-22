/*

Juan Guillen Scholten.
Leiden University.

An example of monitor use in Java.

There are N drinkers drinking beer in a pub. After a while some of them want to 
go to the toilet, unfortunately there is just one available. So only 
one drinker can enjoy the toilet at the same time and the rest must wait.

*/

// The monitor class.
class Toilet {
	private boolean occupied = false;

	// Try to enter the toilet. If occupied then drinker has to wait, else drinker
	// can enter.
	synchronized void try_to_enter() {
		while (occupied) {
			try {
				wait();
			} catch (InterruptedException e) {
				// In Java you always have to catch an exception when doing a wait.
				System.out.println("InterruptedException caught");
			}
		}
		occupied = true;
	}

	// Leave the toilet. Do a notify for if somebody else is waiting.
	synchronized void leave_toilet() {
		occupied = false;
		notify(); // same as signal
	}
}

class Drinker extends Thread {
	int id;
	private Toilet toilet;

	// constructor
	Drinker(int number, Toilet a) {
		super("Drinker" + number);
		this.id = number;
		toilet = a;
	}

	public void run() {
		int times;
		int index = 0;
		int sleeptime;

		// number of times going to the toilet
		times = (int) (Math.random() * 20);

		while (index < times) {

			// Drinking beer.
			sleeptime = (int) (Math.random() * 2000); // 2000 = 2 sec.
			try {
				sleep(sleeptime);
			} catch (Exception e) {
			}

			// Trying to enter the toilet.
			System.out.println("Drinker " + id + " wants to go to the toilet.");
			toilet.try_to_enter();

			// Using the toilet.
			System.out.println("Drinker " + id + " enters the toilet.");
			sleeptime = (int) (Math.random() * 2000);
			try {
				sleep(sleeptime);
			} catch (Exception e) {
			}

			// Leaving the toilet.
			System.out.println("Drinker " + id + " leaves the toilet and says: NEXT!");
			System.out.println(" ");
			toilet.leave_toilet();
			index++;

		}
		System.out.println("Drinker " + id + " goes home.");
		System.out.println(" ");
	}
}

public class BeerDrinkers {

	// The entry point of the program
	public static void main(String args[]) {
		Toilet toilet;

		// Number of drinkers
		int N = 4;
		int i;
		System.out.println("Amount of drinkers : " + N + ".");
		System.out.println(" ");
		toilet = new Toilet();
		Drinker[] drinker = new Drinker[N];

		// Creating the drinkers.
		for (i = 0; i < N; i++) {
			drinker[i] = new Drinker(i, toilet);
		}

		// Starting the drinkers.
		for (i = 0; i < N; i++) {
			drinker[i].start();
		}

	}
}
