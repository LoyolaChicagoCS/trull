package edu.luc.cs.trull.demo.wristwatch;

/**
 * The passive data model of the wristwatch.
 */
public class DateModel implements DateConstants {
	
	private int time;
	private int day;
	private int month;
	
	public DateModel() {
		this.month = 1;
		this.day = 1;
		this.time = 0;
	}
	
	public void setTime(int newtime){
		this.time = newtime;
	}
	public void setDay(int newday) {
		this.day = newday;
	}
	public void setMonth(int newmonth) {
		this.month = newmonth;
	}
	public int getMonth(){
		return this.month;
	}
	public int getDay(){
		return this.day;
	}
	public int getTime(){
		return this.time;
	}
	public void incrementDay(){
		day ++;
	}
	public void incrementSecond(){
		time = time + SEC_PER_TICK;
		if (time >= SEC_PER_DAY) {
			time = 0;
			day += 1;
			if (day >= this.daysPerMonth() + 1) {
				day = 1;
				month += 1;
				if (month >= MONTHS_PER_YEAR + 1) {
					month = 1;
				}
			}
		}	
	}
	//
	final int daysPerMonth() {
		if (month ==2){
			return 29;
		} else if (month <= 7 && month % 2 != 0 || month >= 8 && month % 2 == 0) {
			return 31;
		} else {
			return 30;
		}
	}
}
