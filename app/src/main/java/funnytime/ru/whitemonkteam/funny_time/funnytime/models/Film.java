package funnytime.ru.whitemonkteam.funny_time.funnytime.models;

import java.io.Serializable;

public class Film extends Item implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8866609093945347502L;
	
	 public String Creator;
     public int Duration;
     public long Due ;
     public long Budget ;
     public int Year ;

     public String Actors;
     public int Count;

}
