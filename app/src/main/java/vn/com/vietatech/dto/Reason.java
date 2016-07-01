package vn.com.vietatech.dto;

import java.util.ArrayList;
import java.util.List;

public class Reason {
    private int id;
    private String name;
    private List<Solution> solutions;

    public Reason() {
        id = 0;
        name = "";
        solutions = new ArrayList<Solution>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }
}
