ProducerConsumer.java
Rashi Ghosh
UAID: 010699523


Program Overview

In this file, the Producer-Consumer problem, also known as the bounded buffer problem, is attempted to be implemented via the Java programming language. It can be ran and tested by entering the following commands into the command line:
javac ProducerConsumer.java
java ProducerConsumer [sleep time] [# producer threads] [# consumer threads]
This will have the program create the desired amount of producer and consumer threads, and then sleep for the specified amount of time to allow the threads to process.
The threads process by going through the following cycle 100 times:
1. Sleep a random amount of time between 0-0.5 seconds
2. Add random number (producer)/remove item (consumer) from buffer
3. Print the number it added (producer) or consumed (consumer)


Problem Overview

The Producer-Consumer problem is also known as the bounded buffer problem, as its primary concern is synchronizing the access & manipulation of a shared bounded buffer between two entities. The producer thread writes items to the buffer, whereas the consumer uses up items from the buffer. This process must be carefully implemented so that the producer does not write to a full buffer, the consumer does not take from an empty buffer, and so that two threads (of either type) are not attempting to manipulate the buffer at the same time.



Semaphores & Mutex Locks

In order to synchronize this process properly, semaphores and mutex locks must be utilized. These are declared as static variables inside the ProducerConsumer class so that both producer and consumer thread types can access them.

The Critical Section
In each thread, there is a critical section, in which the manipulation of the buffer takes place. For the producer threads, it is adding an item to a location in the buffer. For the consumer, it is taking one out. This section must be protected, in a sense, so that only thread can be accessing its critical section at a time. This is achieved via a mutex lock (also known as a mutex semaphore). Additionally, before even checking if it can enter the critical section, the thread must also check the buffer resource itself (too see if it is full, for the producer, or to see if there is anything in it, for the consumer). This is achieved via a **counting semaphore** (will also be referred to as the buffer semaphore).

The Buffer Semaphore
There are two implementations of this semaphore, since the producer and the consumer threads are checking for different circumstances:
- The Full Semaphore
	> The full semaphore checks to see if the shared resource (the buffer), is full or not. Because the buffer is initially empty, we initialize this semaphore to 0.  The producer, when attempting to enter the critical section, will do a check on the full semaphore to make sure there are spots, and if there are, it will increment it at the same time and move on. The consumer, when exiting its critical section, will release the full semaphore -- in other words, it will decrement the number of slots since it just consumed one.
- The Empty Semaphore
	> The empty semaphore checks to see if the shared resource (the buffer), has anything in it to be consumed. In this particular implementation, the buffer is bound at 5 slots. Hence, this semaphore is initialized with 5 in order to indicate there are 5 empty slots to begin with. The consumer, when attempting to enter its critical section, will do a check on this number and increment it, indicating a slot will be consumed and freed. The producer, on the other hand, releases this semaphore when exiting its critical section, and decrements the number of empty slots since it just wrote something to the buffer.

The Mutex Lock
Now, in order to actually enter the critical section and make changes to the buffer, each thread type must check the mutex lock -- there is only one. This is initialized with 1 in order to indicate that there is one lock. When attempting to enter the critical section, if there is a lock available, the thread successfully enters, and decrements the value to 0 to indicate the lock is currently taken. When exiting the critical section, the thread releases the lock, incrementing its value back to 1 and making it available again.

Implementation Notes

The Bounded Buffer:
The bounded buffer was implemented as a linked list queue due to the FIFO (first in first out) nature of a queue data structure. This made it (a) easier to add producer items to the end by the simple add() function, and (b) easier to take items from the beginning by the simple remove() function.

The Threads:
The Producer and Consumer classes were implemented from the Runnable interface. They each have their own run() function. An instance of these classes is created in the main program, and are passed into the Thread class in main, and started, calling the run() function implicitly.

Random Number & Sleep Time:
Random numbers were generated via the Random() function for the random integers being added. For the random sleep time between 0-0.5s, the Math.random() function was used. This number had to be multiplied 1000 to make it milliseconds.

The Main Program:
Inside main, two arrays of threads were created -- one for producer threads and one for consumer threads. Then two for loops are used to iterate through and create each individual thread and start them. The program then sleeps for the desired amount of time to allow them to process in the given time frame.
