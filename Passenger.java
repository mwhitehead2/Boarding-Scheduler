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
 * The Passenger class holds all of the information about each passenger 
 * @author MeghanWhitehead
 *
 */
public class Passenger implements Comparable<Passenger> {
    
	// You may create additional private fields
	
	private String name;	// The name of the passenger
	private int time;		// The time a passenger checked in, as a number in minutes after the
							// airport opened for business that day
	private String seat;	// This string indicates the seat number of the passenger. 
							// (Examples: “2A”, “15D”) 
	private int preferredBoarding; // This number indicates extra priority that a passenger may 
								   // have bought at the check-in desk. 
								   // Higher numbers indicate higher priority. 
								   // A passenger without a preferredBoarding number is assigned 
								   // a default 0
	private int doneTimeEstimate;  // estimate for when the passenger will be done boarding
	private int seatNum;	// the passengers seat without the letter at the end
	private int boardTime;	// time the passenger is dequeued
	private boolean waiting; // states whether passenger must wait for passengers ahead of them
	
	/**
	 * This constructor method will initialize all of the private fields for a specific passenger.
	 * The preferredBoarding number was not specified, so we assume that the passenger did not 
	 * purchase any, and it is defaulted to 0.
	 * @param name
	 * @param time
	 * @param seat
	 */
	public Passenger(String name, int time, String seat) {
    	this.name = name;				// passenger's name
    	this.time = time;				// time the passenger arrived
    	this.seat = seat;				// the passenger's seat
    	this.preferredBoarding = 0; 	// default 
	}
    
	/**
	 * This constructor method will initialize all of the private fields for a specific passenger.
	 * The preferredBoarding number was specified, so we set that to the number given as well.
	 * @param name
	 * @param time
	 * @param seat
	 * @param preferredBoarding
	 */
    public Passenger(String name, int time, String seat, int preferredBoarding) {
    	this.name = name;				// passenger's name
    	this.time = time;				// time the passenger arrived
    	this.seat = seat;				// passenger's seat
    	this.preferredBoarding = preferredBoarding;		// non-default preferred boarding
	}
    
    /**
     * This getter method allows us to access the passenger's name outside of this class. We
     * will need to do this when printing out the passenger's information.
     * @return - the passenger's name
     */
	public String getName() {
		return this.name;
	}
	
	/**
	 * This getter method allows us to access the passenger's arrival time (check-in time)
	 * outside of this class. We will need to do this when adding the passenger to the priority 
	 * queue.
	 * @return - check-in time
	 */
	public int getTime() {
		return this.time;
	}
	
	/**
	 * This getter method allows us to access the passenger's seat number outside of this class.
	 * We will need to do this when calculating the doneTimeEstimate because the time a passenger
	 * can begin boarding depends on their seat number relative to another passenger's seat 
	 * number.
	 * @return - seat 
	 */
	public String getSeat() {
		return this.seat;
	}
	
	/**
	 * This getter method allows us to access the passenger's preferred boarding number outside of
	 * this class. We will need to do this when adding a passenger to the priority queue in the 
	 * case that two passengers arrived at the same time and have the same seat numbers.
	 * @return - preferred boarding number
	 */
	public int getPreferredBoarding() {
		return this.preferredBoarding;
	}
	
    /**
     * This setter method will allow the passenger's doneTimeEstimate to be set after the 
     * passenger is added to the iterator. This field is calculated based on the time the 
     * passenger arrives, and the boarding times of passengers that arrived earlier.
     * @param estimate - passenger's doneTimeEstimate
     */
    public void setDoneTimeEstimate(int estimate) { 
    	this.doneTimeEstimate = estimate;
    }
	
	/**
	 * This getter method allows us to access the passenger's done time estimate outside of this
	 * class. We will need to do this when printing the passenger's information after they have
	 * been dequeued.
	 * @return - done time estimate
	 */
	public int getDoneTimeEstimate() {
		return this.doneTimeEstimate;
	}
	
	/**
	 * This getter method allows us to access the passenger's seat number outside of this class.
	 * We will need to do this when calculating the doneTimeEstimate because the time a passenger
	 * can begin boarding depends on their seat number relative to another passenger's seat 
	 * number.
	 * @return - seat 
	 */
	public int getSeatNum() {
		String[] pRow = seat.split("[^A-Z0-9]+|(?<=[A-Z])(?=[0-9])|(?<=[0-9])(?=[A-Z])");
		int row = Integer.parseInt(pRow[0]);
		this.seatNum = row;
		return this.seatNum;
	}
	
	/**
	 * This setter method will be used to set the board time of this passenger. This is needed
	 * for determining the boarding time of passengers that follow this passenger.  
	 * @param boardTime
	 */
	public void setBoardTime(int boardTime) {
		this.boardTime = boardTime;
	}
	
	/**
	 * This getter method will allow the passengers boarding time to be accessed outside of this
	 * class. It will be used when determining the boarding time of all passengers.
	 * @return
	 */
	public int getBoardTime() {
		return this.boardTime;
	}
    
    /**
     * This setter method will set the boolean waiting depending on the passenger that boarded
     * before this passenger. This is determined by the passengers' seat numbers.
     * @param waiting
     */
    public void setWaiting(boolean waiting) {
    	this.waiting = waiting;
    }
    
    /**
     * This getter method will allow the waiting boolean to be accessed from outside of this class
     * This will be used when determining how long a passenger must wait for other passengers
     * before boarding the plane.
     * @return
     */
    public boolean getWaiting() {
    	return this.waiting;
    }
 
	/**
	 * The compareTo method is used when adding passengers to the priority queue. The preferred
	 * boarding number takes priority over all other numbers, so if one passenger has a higher
	 * preferred boarding number, they have a higher priority. If the two passenger have the 
	 * same preferred boarding number, their done time estimates are looked at next. Lower
	 * done time estimates have a higher priority number, since they will finish boarding first.
	 */
    public int compareTo(Passenger other) {
		// preferred boarding is most important for determining priority
    	if (this.preferredBoarding > other.getPreferredBoarding()) {
			return 1;
		} else if (this.preferredBoarding < other.getPreferredBoarding()) {
			return -1;
		} else if (this.doneTimeEstimate < other.getDoneTimeEstimate()) {
			return 1;
		} else if (this.doneTimeEstimate == other.getDoneTimeEstimate()){
			return 0;
		} // done time estimate is next for determining priority
		return -1;
    } 
}