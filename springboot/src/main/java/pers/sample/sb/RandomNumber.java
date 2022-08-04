package pers.sample.sb;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("number_world")
public class RandomNumber {

    public RandomNumber() {}
    public RandomNumber(int id, int hit) {
        this.randomNumber = id;
        this.hitCount = hit;
    }

    @Id
    @Column("id")
    public int randomNumber;
    public int hitCount;
}
