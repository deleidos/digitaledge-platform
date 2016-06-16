/*************************************************************************
 * Copyright (c) 2008 Facundo Olano.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************/
package src.facundo.olano.getterAndSetterTester.sample;

/*
 * This class has a hard-to-instantiate field and it won't accept
 * a null value for that field. The tester won't be able
 * to instantiate it and will try to set it as null, so an exception
 * will be thrown. Therefore the field should be excluded or a default instance
 * should be provided to the tester.
 */
public class ComplexBean {

    //a hard-to-instantiate field
    private SampleBean1 bean1;

    private String sillyField;

    public String getSillyField() {
        return sillyField;
    }

    public void setSillyField(String sillyField) {
        this.sillyField = sillyField;
    }

    public SampleBean1 getBean1() {
        return bean1;
    }

    //null is not accepted for this field
    public void setBean1(SampleBean1 bean1) {
        if (bean1 == null){
            throw new RuntimeException();
        }
        this.bean1 = bean1;
    }

}
