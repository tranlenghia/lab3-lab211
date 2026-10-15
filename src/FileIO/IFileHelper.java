package fileio;

import java.util.ArrayList;

public interface IFileHelper<T> {

    // Load data from a text file
    ArrayList<T> load(String fileName);

    // Save data to a text file
    boolean save(String fileName, ArrayList<T> data);
}