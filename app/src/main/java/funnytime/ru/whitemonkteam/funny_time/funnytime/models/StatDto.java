package funnytime.ru.whitemonkteam.funny_time.funnytime.models;

import java.util.ArrayList;
import java.util.HashMap;

public class StatDto 
{
   public ArrayList<Book> list;
   public HashMap<String, ArrayList<Book>> map;
   
   public ArrayList<Film> listFilm;
   public HashMap<String, ArrayList<Film>> mapFilm;
   
   public ArrayList<Serial> listSerial;
   public HashMap<String, ArrayList<Serial>> mapSerial;
}
