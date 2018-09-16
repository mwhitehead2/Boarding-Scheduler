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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * The BoardingScheduler class will schedule the passengers for boarding. The passengers are all
 * stored with in an ArrayList that is sent to the boardFlight method. This method will iterate
 * through that list, add them to the priority queue, estimate the time they will be done boarding,
 * and dequeue them from the priority heap. When the passengers are dequeued, their information
 * will be printed.
 * @author MeghanWhitehead
 *
 */
public class BoardingScheduler {
	
	private Passenger lastPassenger; // last passengers boarding time used to determine when all 
									 // passengers have finished boarding
	private Passenger prevP; // previous passenger info used when boarding time is calculated
	private int startTime;   // time when boarding can begin
	private int time;        // keeps track of the time while looping through passengers
	private ArrayList<Passenger> boardedPass = new ArrayList<Passenger>(); // stores dequeued 
	   																	   // passengers
	
	/**
	 * The boardFlight method will iterate through all of the passengers that want to board the 
	 * plane. Each timeStep, all passengers that have a boarding time equal to that timeStep will
	 * be added to the queue and one passenger will be dequeue(because only one passenger can 
	 * board at a time). When a passenger is dequeued, their information will be printed and 
	 * added to a different ArrayList that will be used to calculate the doneTimeEstimate for 
	 * later passengers.
	 * @param passengers - iterator that goes through all of the passengers
	 * @param startTime - this is the time that passengers will be allowed to begin boarding
	 */
	public static void boardFlight(Iterator<Passenger> passengers, int startTime) {		
		BoardingScheduler list = new BoardingScheduler(); // allows private variables to be used
		BoardingHeap heap = new BoardingHeap();	// creates the queue
				
		System.out.println(startTime + " Boarding begins\n"); // beginning statement
		Passenger current = passengers.next();	// first passenger in the iterator
		list.time = startTime; 	// time used to determine when a passenger boards
		boolean next = true;	// boolean used to run the second while loop
		list.startTime = startTime; // time passengers are allowed to board
		
		/*
		 * The first while loop will end when all passengers have left the iterator, the heap
		 * is empty(meaning all passengers have began boarding), and all passengers have finished
		 * boarding(one way to track this is to remove passengers from ArrayList once they have 
		 * finished boarding)
		 */
		while(passengers.hasNext() || !heap.isEmpty() || !list.boardedPass.isEmpty()) {
			/*
			 * The second while loop will check if there are any passengers that have a boarding
			 * time that is less than or equal to the current time step. If they do, their done
			 * time will be estimated, and they are added to the queue.
			 */
			while(current.getTime() <= list.time && next) {
				list.calculateDoneTimeEstimate(current); // calculate the passenger's doneTime
				heap.enqueue(current); // enqueue the passenger
				// checks to see if there is another passenger in the iterator
				if(passengers.hasNext()) {
					current = passengers.next(); // if there is, that passenger becomes current
				} else {
					next = false; // if there is not, next becomes false to stop the while loop
				}
			}
			// if the heap is not empty, a passenger is dequeued so they can board the plane
			if (!heap.isEmpty()) {
				Passenger beginsBoarding = heap.dequeue();
				// as the passenger boards, their information is printed out
				list.calculateActualDoneTime(beginsBoarding);
				System.out.println(list.time + " " + beginsBoarding.getName() + " " 
						+ beginsBoarding.getSeat() + " (done " 
						+ beginsBoarding.getBoardTime() + ")");
				list.boardedPass.add(beginsBoarding); // they are added to the ArrayList
				list.lastPassenger = beginsBoarding;
			} 
			
			// for loop determines if any passengers have completed boarding, and if they have they
			// are removed from the ArrayList
			for(int i = 0; i < list.boardedPass.size(); i++) {
				if(list.boardedPass.get(i).getBoardTime() <= list.time) {
					list.boardedPass.remove(list.boardedPass.get(i));
				}
			}
			list.time++; // timeStep that is incremented each pass through the while loop
		}
		
		System.out.print(list.lastPassenger.getBoardTime() + 1);	// final time is printed when all passengers have boarded
		System.out.println(" All passengers have boarded!");
	}
	
	/**
	 * This method will calculate the actual time a passenger finishes boarding. This is done
	 * when they are dequeued, and compared with the passenger that was dequeued before them.
	 * If they need to wait, their done time will be five minutes after the previous passenger.
	 * If they do not need to wait, their done time will only be one minute after the previous
	 * passenger.
	 * @param p
	 */
	private void calculateActualDoneTime(Passenger p) {
		int doneTime = 0;
		boolean waiting = waiting(p);
		
		// the first passenger will finish five minutes after they are dequeued
		if(boardedPass.isEmpty()) {
			doneTime = startTime + p.getTime() + 5;
		} else {
			if(waiting) {
				doneTime = prevP.getBoardTime() + 5;	// waiting for another passenger
			} else {
				doneTime = prevP.getBoardTime() + 1;	// not waiting for another passenger
			}
		}
		p.setBoardTime(doneTime);	
		prevP = p;	// setting this passenger's info to previous for the next passenger to use
	}
	
	/**
	 * This helper method determines whether the passenger must wait for another passenger to
	 * finish boarding before they can begin boarding. This is used when calculating the actual
	 * done boarding time of each passenger.
	 * @param p - passenger that is passed in
	 * @return	true/false
	 */
	private boolean waiting(Passenger p) {
		// if the passenger is the first to board, they will not have to wait for other passengers
		if(!boardedPass.isEmpty()) {
			if(prevP.getSeatNum() <= p.getSeatNum()) {
				p.setWaiting(true);
				return true; // there is a passenger blocking their way
			} else {
				p.setWaiting(false);
				return false; // there is not a passenger blocking their way
			}
		} else {
			return false;	// first passenger to board
		}
	}
	
	/**
	 * The calculateDoneTimeEstimate method will estimate the time that each passenger will be 
	 * done boarding the plane. It does so by first checking to see if the ArrayList containing
	 * the dequeued passengers is empty, and if it is, sets the doneTimeEstimate to the start
	 * time plus the passengers board time plus five. This is because this passenger is the first
	 * one to board. Otherwise, the passenger's seat number will be compared with other passengers
	 * seat numbers (who have already began boarding) to see if they need to wait for any other
	 * passengers. If they need to wait, their doneTimeEstimate is five minutes after the 
	 * passenger's that they are waiting for. If they do not need to wait, their doneTimeEstimate
	 * is only one minute after  the passenger boarding ahead of them.
	 * @param p - the passenger that needs their done time estimated
	 * @return - the done time estimate
	 */
	public int calculateDoneTimeEstimate(Passenger p) {
		int timeEstimate = 0;	// return variable
		boolean mustWait = true; // checks if a passenger is waiting
		
		if(boardedPass.isEmpty()) {	// this means the passenger is the first passenger to board
			timeEstimate = p.getTime() + 5 + startTime;
			p.setDoneTimeEstimate(timeEstimate);
		} else {
			Passenger waitingFor = p;
			// checks to see if there are passengers boarding that might be in their way
			for(int i = 0; i < boardedPass.size(); i++) {
				// if the passenger's seat number is not blocked, they do not need to wait 
				if(p.getSeatNum() < boardedPass.get(i).getSeatNum()){
					mustWait = false;
					p.setWaiting(false);
					waitingFor = boardedPass.get(i); // passenger they are waiting for
				} else {
					mustWait = true;
					p.setWaiting(true);
					waitingFor = boardedPass.get(i); // passenger they are waiting for				
				}
			}
			if(mustWait) {
				// doneTimeEstimate added to another passenger
				timeEstimate = waitingFor.getDoneTimeEstimate() + 5;
				p.setDoneTimeEstimate(timeEstimate);
			} else {
				// doneTimeEstimate added to another passenger
				timeEstimate = waitingFor.getDoneTimeEstimate() + 1;
				p.setDoneTimeEstimate(timeEstimate);
			}
		}
		return timeEstimate; 
	}

	/**
	 * Reads in a file containing a list of flight passengers in the order they
	 * check in and runs the boardFlight() method with those passengers.
	 * @author Tina, Alexi
	 * @param flight is the name of the input file in the project directory
	 */
	public static void checkIn(String flight) {
		File f = new File(flight);
		try {
			Scanner s = new Scanner(f);
			List<Passenger> passengers = new ArrayList<Passenger>();
			while(s.hasNextLine()) {
				//Data are separated by commas and possibly also whitespace.
				String[] line = s.nextLine().split("\\s*,\\s*");
				if (line.length == 3) //Default preferredBoarding 0 constructor
					passengers.add(new Passenger(line[0],
							Integer.parseInt(line[1]),
							line[2]));
				else //Use the preferredBoarding number if given
					passengers.add(new Passenger(line[0],
							Integer.parseInt(line[1]),
							line[2],
							Integer.parseInt(line[3])));
			}
			s.close();
			boardFlight(passengers.iterator(), 1);
		} catch (IOException e) {
			System.out.println("Error: Unable to load file " + flight);
		}
	}
	
	/**
	 * This main method is used to test the code of this program by sending the name of a text
	 * file to the check-in method.
	 * @param args
	 */
	public static void main(String[] args) {
		checkIn("sample1.txt");
	}
}
