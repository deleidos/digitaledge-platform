/*************************************************************************
 * Copyright (c) 2008 Facundo Olano.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************/
package src.facundo.olano.getterAndSetterTester.sample;

public class SampleBean1 {

    private Integer weirdInteger;

    private DayOfWeek someDay;

    //Constructor with a hard-to-guess field setting behaviour
    //Providing an instance to test is more appropiate in this case.
    public SampleBean1 (int anInt){
        if (anInt != 5){
            throw new RuntimeException ();
        }
        weirdInteger = anInt;
    }

    //has getter but not setter. This method will still be tested.
    public Integer getWeirdInteger() {
        return weirdInteger;
    }

    public enum DayOfWeek {
        MONDAY("monday"), TUESDAY("tuesday"), ANDTHATSIT("got bored");

        private String name;

        DayOfWeek(String theName){
            name = theName;
        }

        public String toString() {
            return name;
        }

    }

    public DayOfWeek getSomeDay() {
        return someDay;
    }

    public void setSomeDay(DayOfWeek someDay) {
        this.someDay = someDay;
    }
}
