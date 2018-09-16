//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           P9 Boarding Scheduler
// Files:           Passenger, BoardingScheduler, BoardingHeap
// Course:          CS300, Spring 2018
//
// Author:          Meghan Whitehead
// Email:           mwhitehead2@wisc.edu
// Lecturer's Name: Gary Dahl
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:    N/A
// Partner Email:   N/A
// Lecturer's Name: N/A
// 
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//   _x_ Write-up states that pair programming is allowed for this assignment.
//   _x_ We have both read and understand the course Pair Programming Policy.
//   _x_ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully 
// acknowledge and credit those sources of help here.  Instructors and TAs do 
// not need to be credited here, but tutors, friends, relatives, room mates 
// strangers, etc do.  If you received no outside help from either type of 
// source, then please explicitly indicate NONE.
//
// Persons:         No persons, other than instructors.
// Online Sources:  I used piazza posts to help complete this program.
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

/**
 * The BoardingHeap class is the construction and destruction of the priority queue. This
 * works by adding and removing Passenger objects. The highest priority of the queue contains the 
 * passenger that will board first. When that passenger boards, the are dequeued and the rest of 
 * the queue must be reorganized. To do this, the lowest priority passenger is moved to the top of
 * the queue, and then swapped with its children until it is at the right spot. However, objects
 * cannot be dequeued if the queue is empty, so we have a method checking the size of the queue.
 * @author MeghanWhitehead
 *
 */
public class BoardingHeap {
	//You may store additional private fields as needed
	private Passenger[] heap; //array of passengers currently in the heap
	private int size;		  //number of passengers in the heap

	/**
	 * This constructor will create a single priority queue that begins with a size of 0 that 
	 * can hold up to ten passengers. Once there are more than ten passengers, a new array will
	 * be created.
	 */
	public BoardingHeap() { 
		this.size = 0;
		this.heap = new Passenger[10];
	}
	
	/**
	 * Enqueue adds a passenger to the priority queue (a.k.a. the boarding heap)
	 * This operation is executed such that the hierarchy of the queue remains the same. This 
	 * means that each passenger must have a greater priority than the two passengers below it. 
	 * @param p - passenger to be added to the boarding queue
	 */
	public void enqueue(Passenger p){ 
		size++;				// the size of the queue increases when a passenger is added
		int child = size; 	// index to insert the passenger
		int parent = child/2; // index of the parent node (relative to child)
		if(child == heap.length){
			// resize the queue if necessary
			Passenger[] newHeap = new Passenger[heap.length*2];
			for(int i=0;i<heap.length;i++) newHeap[i] = heap[i];
			heap = newHeap;
		}
		heap[child] = p;
		while(parent > 0 && heap[parent].compareTo(heap[child])<0){
			// swap the passengers at (parent) and (child) indices
			heap[child] = heap[parent];
			heap[parent] = p;
			child = parent;
			parent = child/2;
		}
	}
	
	/**
	 * The dequequed class removes the passenger at the highest priority. This passenger is the 
	 * next passenger to board the plane. Once this passenger is removed, the queue will need to be 
	 * rearranged to look like a tree again. This is done by taking the lowest priority passenger,
	 * and moving them to the top of the queue. This passenger is then swapped with its children
	 * until all of the passengers below it have a lower priority and all of the passengers above 
	 * it have a higher priority.
	 * @return - passenger that was dequeued
	 */
	public Passenger dequeue() {
		// an exception is thrown if we attempt to dequeue and empty queue
		if(this.isEmpty()) {
			throw new IllegalStateException("no passengers in queue");
		}
		
		Passenger ret = heap[1];	// highest priority passenger that is returned
		int parent = 1;				// position of the parent
		heap[parent] = heap[size];	// takes lowest priority and puts it at the root
		heap[size] = null;			// lowest priority was moved, so it's old spot is null
		boolean tryswap = true;		// boolean that decides if the while loop keeps going
		
		// swaps parent with child until the parent is in the correct position
		while(tryswap) {
			int c1 = parent*2;	// first child position
			int c2 = c1+1;		// second child position
			int child = 0;
			if (c1 < size && c2 < size) {	
				child = (heap[c1].compareTo(heap[c2]) > 0) ? c1 : c2;	
				// if both children are less than its parent, 
				// the second child will be swapped with the parent
			} else if (c1 < size) {
				child = c1;	// if the first child is less than the parent (but the second is not) 
							// the first child is swapped with the parent
			} 
			if (child == 0) {
				tryswap = false; 
				// neither child is less than the parent, no more swapping needs to be done
			} else {
				if(heap[child].compareTo(heap[parent]) >= 0) {
					// swapping of parent and child if the child is less than the parent
					Passenger temp = heap[parent];	
					heap[parent] = heap[child];
					heap[child] = temp;
					parent = child;
				} else {
					tryswap = false; // if nothing needs to be swapped, the loop ends
				}
			}
		}
		size--;	// number of passengers is decreased by one
		return ret;	// the original root is returned
	}

	/**
	 * The isEmpty class will check if the queue has any passengers in it. It does this by checking
	 * the size of the queue. If the size is less than or equal to zero, the queue is empty and 
	 * returns true. Otherwise the queue is not empty and returns false.
	 * @return - true/false
	 */
	public boolean isEmpty() { 
		if (size <= 0) {
			return true;
		} else {
			return false;
		}
	}
}
