/*************************************************************************
 * Copyright (c) 2008 Facundo Olano.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *************************************************************************/
package src.facundo.olano.getterAndSetterTester.sample;

import java.util.HashMap;
import java.util.Map;

import src.facundo.olano.getterAndSetterTester.GetterAndSetterTester;
import junit.framework.TestCase;

public class SampleTest extends TestCase {

    private SampleBean1 bean1;
    private ComplexBean complexBean;
    private GetterAndSetterTester tester;

    public void setUp(){
        bean1 = new SampleBean1(5);
        tester = new GetterAndSetterTester();
        complexBean = new ComplexBean();
    }

    /**
     * Test the getters and setters of a the given class.
     * Instantiation is left top the tester.
     *
     */
    public void testClass(){
        tester.testClass(SampleBean2.class);
    }

    /**
     * Test getters and setters of a given instance.
     * Usefull when you need to test a class which instantiation
     * is too complex for the tester to handle (in most cases this
     * is not needed).
     */
    public void testInstance(){
        tester.testInstance(bean1);
    }


    /**
     * Test getters and setters exluding those for the specified
     * field names.
     * This is usefull when the methods are complex, for example
     * in the case that the setters modify the objects before setting
     * the field value.
     *
     */
    public void testWithExclussions(){
        //the field is specified to be ignored by the tests
        tester.setIgnoredFields("bean1");
        //the rest of the class will be tested normally
        tester.testInstance(complexBean);
    }

    /**
     * As the tester needs to make instances of the classes or primitive
     * types for the fields which accessors are being tested, it will
     * make the best guess to instantiate those objects.
     * When instantiation cannot be handled by the tester (and you find exceptions
     * due to this), you might want to provide default objects for the tester to use
     * to be able to still test the getters and setters of the problematic fields.
     *
     */
    public void testWithDefaultInstances(){
        //a map with a default instance associated to the field class
        //is passed to the tester to use
        Map<Class,Object> map = new HashMap<Class, Object>();
        map.put(SampleBean1.class, bean1);
        tester.addDefaultInstances(map);

        //all the fields will be tested now, using the given object
        //for the complex field
        tester.testInstance(complexBean);

        }



}
