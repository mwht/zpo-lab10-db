import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student implements Serializable {
    private String name;
    private String surname;
    private Integer indexNo;
    private List<Integer> grades;

    public Student(String name, String surname, Integer indexNo) {
        this.name = name;
        this.surname = surname;
        this.indexNo = indexNo;
        this.grades = new ArrayList<Integer>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getIndexNo() {
        return indexNo;
    }

    public void setIndexNo(Integer indexNo) {
        this.indexNo = indexNo;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    public void dodajOcene(Integer ocena) {
        grades.add(ocena);
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", indexNo=" + indexNo +
                ", grades=" + grades +
                '}';
    }
}
