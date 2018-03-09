package ClassLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Films extends ArrayList<Film>{
    
    public Films() { }

    public Films(List<Film> films){
        this.addAll(films);
    }
    
    //returns the movies which pass through the filter
    public Films getFilmsFilteredSubset(String filmID, String directorID, String actorID, String filmYear, String filmRating){
        Films tmpFilms = new Films();
        tmpFilms.addAll(this.stream().filter(f -> f.filmID.equals((filmID == null) ? f.filmID : filmID)) //checks if there is an filmId filter value, if yes then movies with that id is passed
                                     .filter(f -> f.filmYear.equals((filmYear == null) ? f.filmYear : filmYear))
                                     .filter(f -> f.imdbRating.equals((filmRating == null) ? f.imdbRating : filmRating))
                                     .filter(f -> f.directors.stream().anyMatch(p -> p.getID().equals((directorID == null) ? p.getID() : directorID)))//checks if any director ID matches then pass the filter
                                     .filter(f -> f.actors.stream().anyMatch(p -> p.getID().equals((actorID == null) ? p.getID() : actorID)))//checks if any actors ID matches then pass the filter
                                     .sorted(Comparator.comparing(f -> f.getFilmName())) //sort the movies by their names
                                     .collect(Collectors.toList())); // convert to list
        return tmpFilms;  //return the list.
    }
  
    
    
    // sorts (this) by film name and convets it to a list 
    public List<SimplisticFilm> toListSimplisticFilm(){
        return this.stream().sorted(Comparator.comparing(fi -> fi.getFilmName()))
                            .collect(Collectors.toList());
    }
    
    
    // filters the film with same filmID in (this) and returns lsit of SimplicFilms
    public List<SimplisticFilm> getDistinctSimplisticFilm(String filmID){
        return this.stream().filter(f -> f.filmID.equals(filmID))
                            .collect(Collectors.toList());
    }
    
    
    public List<Director> toListDistinctDirector(){
        List <Director> tmpList = new ArrayList();
        //combine the streams, directors into streams. 
        this.stream().flatMap(film -> film.directors.stream()
                    .filter(dir -> tmpList.stream()
                            // checking if there is any dulipcate directors before adding them 
             
                            .noneMatch(di -> di.getID().equals(dir.getID())))
                //Add those distinct directors to tmpList
                    .map(nDir -> tmpList.add(nDir)))
                    .collect(Collectors.toList());

        tmpList.sort(Comparator.comparing(c -> c.getName()));

        return tmpList;   
    }
    
    public List<Director> getDistinctDirector(String directorID){
        List <Director> tmpList = new ArrayList();

        this.stream().flatMap(film -> film.directors.stream()
                    .filter(dir -> tmpList.stream()
                    
                            .noneMatch(di -> di.getID().equals(dir.getID())) && dir.getID().equals(directorID))   //checks if there is any duplicate directors
                    .map(nDir -> tmpList.add(nDir))) 
                    .collect(Collectors.toList());

        return tmpList;  
    }
    
    
    public List<Actor> toListDistinctActor(){
        List <Actor> tmpList = new ArrayList();
            
        this.stream().flatMap(film -> film.actors.stream()
                    .filter(act -> tmpList.stream()
                            .noneMatch(ac -> ac.getID().equals(act.getID())))
                    .map(nAct -> tmpList.add(nAct)))
                
                    .collect(Collectors.toList());

        tmpList.sort(Comparator.comparing(c -> c.getName())); // sort by Actor name

        return tmpList;   
    }
    
    public List<Actor> getDistinctActor(String actorID){
        List <Actor> tmpList = new ArrayList();

        this.stream().flatMap(film -> film.actors.stream()
                    .filter(act -> tmpList.stream()
                            .noneMatch(ac -> ac.getID().equals(act.getID())) && act.getID().equals(actorID))
                    .map(nAct -> tmpList.add(nAct)))
                    .collect(Collectors.toList());

        return tmpList;  
    }
    
    
    public List<String> toListDistinctYear(){
        List <String> tmpList = new ArrayList();
        
        this.stream()
                .filter(f -> tmpList.stream().noneMatch(y -> y.equals(f.filmYear)))
                .map(f -> tmpList.add(f.filmYear))
                .collect(Collectors.toList());
        
        Collections.sort(tmpList);
                
        return tmpList;  
    }
    
    public List<String> getDistinctYear(String year){
        List <String> tmpList = new ArrayList();
        
        this.stream()
                .filter(f -> tmpList.stream().noneMatch(y -> y.equals(f.filmYear) && f.filmYear.equals(year)))
                .map(f -> tmpList.add(f.filmYear))
                .collect(Collectors.toList());
        
        Collections.sort(tmpList);
                
        return tmpList;   
    }
    
    
    public List<String> toListDistinctFilmRatings(){
        List <String> tmpList = new ArrayList();

         this.stream()
                 .filter(f -> tmpList.stream().noneMatch(y -> y.equals(f.imdbRating)))
                 .map(f -> tmpList.add(f.imdbRating))
                 .collect(Collectors.toList());

         Collections.sort(tmpList);

         return tmpList;
    }
    
    public List<String> getDistinctFilmRating(String imdbRating){
        List <String> tmpList = new ArrayList();
        
        this.stream()
                .filter(f -> tmpList.stream().noneMatch(y -> y.equals(f.imdbRating) && f.imdbRating.equals(imdbRating)))
                .map(f -> tmpList.add(f.imdbRating))
                .collect(Collectors.toList());
        
        Collections.sort(tmpList);
                
        return tmpList;
    }
}
