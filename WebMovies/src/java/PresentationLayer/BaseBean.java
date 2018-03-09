package PresentationLayer;

import ApplicationVariables.AppVariables;
import ClassLayer.Person;
import ClassLayer.SimplisticFilm;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.model.SelectItem;


public abstract class BaseBean {
    
   
    protected <T>List populateDropDownList(List<T> dataSource){
        List<SelectItem> siList = new ArrayList();  // create new list of selectItem 
        
         
        //if the dataSource file size is > 1 then,  a SelectItem with value null and label of dropDownDefault
        if(dataSource.size() > 1){
            //No selection option = <--SELECT--> 
            SelectItem noSelect = new SelectItem(null, AppVariables.WebProperties.dropDownDefault);
            noSelect.setNoSelectionOption(true);
            siList.add(noSelect);
        }
        
        //checks if dataSource not null and if it is an istance of SimplisticFilm 
        if(dataSource != null && dataSource.get(0) instanceof SimplisticFilm){
            List<SimplisticFilm> tmpList = (List<SimplisticFilm>)dataSource;
           
            //tmplist into stream, mapped it with lambda function and collected the elements to a list.
            siList.addAll(tmpList.stream()
                    // creates new stream corresponding each element to the this stream
                 .map(f -> new SelectItem(f.getFilmID(), f.getFilmName()))
                 .collect(Collectors.toList()));// put the selected item into a list
            
        }else if(dataSource != null && dataSource.get(0) instanceof Person){
            List<Person> tmpList = (List<Person>)dataSource;
            siList.addAll(tmpList.stream()
                 .map(p -> new SelectItem(p.getID(), p.getName()))
                 .collect(Collectors.toList()));
            
        }else if(dataSource != null && dataSource.get(0) instanceof String){
            List<String> tmpList = (List<String>)dataSource;
            tmpList.stream()
                .map(a -> siList.add(new SelectItem(a)))
                .collect(Collectors.toList());
        }
        
        return siList; // return the selecteditem List
    }
    
}
