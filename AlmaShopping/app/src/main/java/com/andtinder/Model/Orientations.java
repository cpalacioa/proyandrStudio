package com.andtinder.Model;

/**
 * Created by ASUS K550 on 11/02/2015.
 */
public class Orientations {
    public enum Orientation {
        Ordered, Disordered;

        public static Orientation fromIndex(int index) {
            Orientation[] values = Orientation.values();
            if(index < 0 || index >= values.length) {
                throw new IndexOutOfBoundsException();
            }
            return values[index];
        }
    }

}
