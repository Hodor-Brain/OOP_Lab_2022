package ua.university.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Doctor implements IModel {
    private int id;
    private int type;
    private String name;

    @Override
    public String modelURLPattern() {
        return "/doctors";
    }

    @Override
    public String toString() {
        String result =  "Name: " + this.name;
        if (this.type == 0)
            return result + ", Type: Nurse";
        return result + ", Type: Doctor";
    }

}
