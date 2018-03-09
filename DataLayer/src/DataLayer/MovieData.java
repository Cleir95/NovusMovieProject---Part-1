package DataLayer;

import ClassLayer.*;
import ApplicationVariables.AppVariables;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;


public class MovieData {
    
    String message = "";
    
    //read data from CSV file - path provided as param
    public Films getFilmData(String csvPath){
        
        Films films = new Films();// creates a new isntance of Film
       
        String[] line; // reads string line by line
        
        // creates a new csv reader which reades the input file 
        try(CSVReader csv = new CSVReader(new FileReader(csvPath));){
            String[] headers = csv.readNext(); //read first line for header strings
           
            while((line = csv.readNext()) != null){  // reads line by line
                
                films = storeLine(line, films);  // store each line data in films
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
        
        return films;
    }
    
    
    
    private Films storeLine(String[] line, Films films){
        Films tmpFilms = films;
        
        
        if(tmpFilms.stream().anyMatch(item -> item.filmID.equals(line[AppVariables.filmID]))){ // checks if the filmid equals
            //  if yes then, get the  instance of the film and save it to tmpFilmd
            Film tmpFilm = tmpFilms.stream().filter(item -> item.filmID.equals(line[AppVariables.filmID])).findFirst().get();//.collect(Collectors.toList()).get(0);
// checks if the  passed directorid equals to any of in tmpfile
            if(tmpFilm.directors.stream().anyMatch(item -> item.getID().equals(line[AppVariables.directorID]))){

            }else{//if not then, add a director object with ID to the tmpfile
                Director director = this.getDirectorFromData(line);
                tmpFilm.directors.add(director);
            }
            // checks if the  passed actorId equals to any of in tmpfile
            if(tmpFilm.actors.stream().anyMatch(item -> item.getID().equals(line[AppVariables.actorID]))){

            }else{//if not then, add a actor object with ID to the tmpfile
                Actor actor = this.getActorFromData(line);
                tmpFilm.actors.add(actor);
            }
        }else{ // if there is no existing films with the same ID, then  add film based on the data we are passing to tmpFilms
            Film film = this.getFilmFromData(line);
            tmpFilms.add(film);
        }
        
        return tmpFilms;
    }
    
    private Director getDirectorFromData(String[] line){
        //Creates a new Director with the director id and name  got from the line array
        Director director = new Director(line[AppVariables.directorID].trim(), 
                                         line[AppVariables.directorName].trim());
        return director;
    }
    
    private Actor getActorFromData(String[] line){ 
           //Creates a new Actor with actor id and names values got from the line array
        Actor actor = new Actor(line[AppVariables.actorID].trim(), 
                                line[AppVariables.actorName].trim());
        return actor;
    }
    
    private Film getFilmFromData(String[] line){ // create a Film object using the data from the string array
        
        Director director = this.getDirectorFromData(line); //Director object created using data from line array
        Actor actor = this.getActorFromData(line); // actor object created uding the data from the line array
        // Film object created with id, name, rating, year.
        Film film = new Film(line[AppVariables.filmID].trim(), 
                             line[AppVariables.filmName].trim(),
                             line[AppVariables.imdbRating].trim(),
                             line[AppVariables.filmYear].trim());
        film.directors.add(director);
        film.actors.add(actor);
        
        return film;
    }
    
   

    
    public String getResultMessage(){
        return message;
    }
}
