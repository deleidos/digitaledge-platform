/*************************************************************************
 * Copyright (c) 2008 Facundo Olano.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************/
package src.facundo.olano.getterAndSetterTester.sample;

import java.util.List;

public class SampleBean2 {
    private char aChar;
    private String aString;
    private List<Integer> theList;

    public char getAChar() {
        return aChar;
    }
    public void setAChar(char char1) {
        aChar = char1;
    }
    public String getAString() {
        return aString;
    }
    public void setAString(String string) {
        aString = string;
    }
    public List<Integer> getTheList() {
        return theList;
    }
    public void setTheList(List<Integer> theList) {
        this.theList = theList;
    }
}
