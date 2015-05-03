package funnytime.ru.whitemonkteam.funny_time.funnytime.models;

import java.io.Serializable;

public class Serial extends Item implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -1514891393483447111L;

    public int Season ;
    public int Series ;
    public String Creator;
    public String Actors ;
    public int SeasonsCount ;
    public int Count ;
}
