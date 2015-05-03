package funnytime.ru.whitemonkteam.funny_time.funnytime.models;

import java.io.Serializable;

public class Item extends BaseEntity implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 3744419562647669654L;
	public String Name;
    public int Action;
    public Boolean WasSeen;
    public float Mark ;
    public long DateTime;
    public long DateChange;
    public long DateRemember;
    public String Comment;
    public String ImageURL;
    public String ImagePath ;
    public boolean IsPrivate;
    public boolean IsSawToday;
    public long UserId;
    public long NoteId;
}
