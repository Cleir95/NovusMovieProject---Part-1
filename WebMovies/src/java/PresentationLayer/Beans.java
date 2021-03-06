package PresentationLayer;

import ApplicationVariables.AppVariables;
import BusinessLayer.MovieBusinessLayer;
import ClassLayer.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;


@ViewScoped
@Named("bean")


public class Beans extends BaseBean implements Serializable{
    
    private MovieBusinessLayer mbl = new MovieBusinessLayer(); // isntance of the class MovieBusinessLayer
    private String selectedFilm, selectedDirector, selectedActor, selectedYear, selectedRating;
    List<Director> directors;
    List<Actor> actors;
    List<SimplisticFilm> sFilms;
    List<String> filmYears, filmRatings;
    private boolean isSubmitted = false, isAllSelected = false;
    //
    // annotation 
    @PostConstruct
    protected void load(){
        if (this.isPostback()){ // postback orginates if the user clickes(dropdown, button, etc)
            String filmID = (selectedFilm == null ? null : selectedFilm); // set the filmID to selectedFilm
            String directorID = (selectedDirector == null ? null : selectedDirector);
            String actorID = (selectedActor == null ? null : selectedActor);
            String filmYear = (selectedYear == null ? null : selectedYear);
            String filmRating = (selectedRating == null ? null : selectedRating);

            populateDropDownsWithFilteredData(filmID, directorID, actorID, filmYear, filmRating);// put  the selected values in table
        }else{ // if no postback then,
            populateDropDownsWithOriginalData();
        }
    }
    
    //populate dropdown lists with ALL data retrieved
    private void populateDropDownsWithOriginalData(){
        try{
            Films films = mbl.getFilms();
            
            directors = mbl.getDistinctDirectorsFromFilms(films);
            actors = mbl.getDistinctActorsFromFilms(films);
            sFilms = mbl.getDistinctSimplisticFilmsFromFilms(films);
            filmYears = mbl.getDistinctYearsFromFilms(films);
            filmRatings = mbl.getDistinctRatingsFromFilms(films);
        }catch(Exception e){
           e.printStackTrace();
        }
    }
    
    //populate dropdown based on filters currently in place
    private void populateDropDownsWithFilteredData(String filmID, String directorID, String actorID, String filmYear, String filmRating){
        Films films = mbl.getFilms();  // get all movies 
            
        Films tmp = mbl.getFilmsSubset(filmID, directorID, actorID, filmYear, filmRating, films);

        actors = (actorID == null) ? mbl.getDistinctActorsFromFilms(tmp) : mbl.getDistinctActor(tmp, actorID);
        directors = (directorID == null) ? mbl.getDistinctDirectorsFromFilms(tmp) : mbl.getDistinctDirector(tmp, directorID);
        sFilms = (filmID == null) ? mbl.getDistinctSimplisticFilmsFromFilms(tmp) : tmp.getDistinctSimplisticFilm(filmID);
        filmYears = (filmYear == null) ? mbl.getDistinctYearsFromFilms(tmp) : mbl.getDistinctYear(tmp, filmYear);
        filmRatings = (filmRating == null) ? mbl.getDistinctRatingsFromFilms(tmp) : tmp.getDistinctFilmRating(filmID); 
        
        //display table of data once all values are selected
        if(sFilms.size() == 1 && directors.size() == 1 && actors.size() == 1 && filmYears.size() == 1){
            isAllSelected = true;
            
            
            List<String> filmsIDs = sFilms.stream().map(x -> x.filmID).collect(Collectors.toList());
            
            this.populateFields(sFilms.get(0).filmID, directors.get(0).personID, actors.get(0).personID);
           
           
        }
    }
    
    //populate and return a list of data based on what the films list currently holds
    public List getFilms(){ return populateDropDownList(this.sFilms);}
    public List getDirectors(){ return populateDropDownList(this.directors); }
    public List getActors(){ return populateDropDownList(this.actors); }
    public List getFilmYears(){ return populateDropDownList(this.filmYears); }
    public List getFilmRatings(){ return populateDropDownList(this.filmRatings); }
    
    
    //http://ruleoftech.com/2012/jsf-1-2-and-getting-selected-value-from-dropdown
    public void filmValueChanged(ValueChangeEvent e){
        if(isPostback()){
            selectedFilm = e.getNewValue().toString();
            this.load();
        }
    }
    
    public void directorValueChanged(ValueChangeEvent e){
        if(isPostback()){
            selectedDirector = e.getNewValue().toString();
            this.load();
        }
    }
    
    public void actorValueChanged(ValueChangeEvent e){
        if(isPostback()){
            selectedActor = e.getNewValue().toString();
            this.load();
        }
    }
    
    public void yearValueChanged(ValueChangeEvent e){
        if(isPostback()){
            selectedYear = e.getNewValue().toString();
            this.load();
        }
    }
    
    public void ratingValueChanged(ValueChangeEvent e){
        if(isPostback()){
            selectedRating = e.getNewValue().toString();
            this.load();
        }
    }
            
    //refresh the page to initial state
    public void reset() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
    
    
    
    //check if status of page is postback
    public static boolean isPostback() {
       return FacesContext.getCurrentInstance().isPostback();
    }
    
    //+getters, +setters
    public String getSelectedFilm(){return this.selectedFilm;}
    public void setSelectedFilm(String si){this.selectedFilm = si;}
    public String getSelectedDirector(){return this.selectedDirector;}
    public void setSelectedDirector(String si){this.selectedDirector = si;}
    public String getSelectedActor(){return this.selectedActor;}
    public void setSelectedActor(String si){this.selectedActor = si;}
    public String getSelectedYear(){return this.selectedYear;}
    public void setSelectedYear(String si){this.selectedYear = si;}
    public String getSelectedRating(){return this.selectedRating;}
    public void setSelectedRating(String si){this.selectedRating = si;}
    public boolean getIsSubmitted(){return this.isSubmitted;}
    public void setIsSubmitted(Boolean isSubmitted){this.isSubmitted = isSubmitted;}
    public boolean getIsAllSelected(){return this.isAllSelected;}
    public void setIsAllSelected(Boolean isAllSelected){this.isAllSelected = isAllSelected;}
    
    
    //-------------------------------------------------
    //   Populating strings with selected film data
    //-------------------------------------------------
    private Film film;
    private Director director;
    private Actor actor;
    private String FilmImdbLink;
private String DirectorImdbLink;
 private String ActorImdbLink;
    
    public void populateFields(String filmID, String directorID, String actorID){
        this.film = mbl.getFilmFromSimplisticFilm(filmID);
        this.director = mbl.getDirectorFromSimplisticFilm(film, directorID);
        this.actor = mbl.getActorFromSimplisticFilm(film, actorID);
         this.FilmImdbLink = String.format(AppVariables.WebProperties.imdbFilmURL, film.filmID);
      this.DirectorImdbLink = String.format(AppVariables.WebProperties.imdbProfileURL,  director.personID);
      this.ActorImdbLink = String.format(AppVariables.WebProperties.imdbProfileURL, actor.personID);
    }
    
    //JSF read access to fields
    public Film getFilm(){return film;}
    public Director getDirector(){return director;}
    public Actor getActor(){return actor;}
   // public String getFilmImdbLink() {return String.format(AppVariables.WebProperties.imdbFilmURL, film.filmID);}
   // public String getDirectorImdbLink() {return String.format(AppVariables.WebProperties.imdbProfileURL, director.personID);}
    //public String getActorImdbLink() {return String.format(AppVariables.WebProperties.imdbProfileURL, actor.personID);}
    public String getFilmImdbLink() {return FilmImdbLink;}
   public String getDirectorImdbLink() {return DirectorImdbLink;}
   public String getActorImdbLink() {return ActorImdbLink;}
   
   
   
   private Films tableFilms;
   public Films getTableFilms() { return tableFilms; }   
   public void populateTable(String filmID, String directorID, String actorID, String filmYear) {
       Films allFilms = mbl.getFilms();
      tableFilms = mbl.getFilmsSubset(filmID, directorID, actorID, filmYear, null, allFilms);        
   }
}