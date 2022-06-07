package ua.university.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Patient implements IModel {
    private int id;
    private String name;

    @Override
    public String modelURLPattern() {
        return "/patients";
    }

    @Override
    public String toString() {
        return "Name: " +
                this.name;
    }

}
