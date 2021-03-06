package com.hippo.jmx.filter;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author saitxuc
 * 2015-3-20
 */
public class MBeansAttributeQueryFilter extends AbstractQueryFilter {
    public static final String KEY_OBJECT_NAME_ATTRIBUTE = "Attribute:ObjectName:";

    private MBeanServerConnection jmxConnection;
    private Set attribView;

    /**
     * Create an mbean attributes query filter that is able to select specific
     * mbean attributes based on the object name to get.
     * 
     * @param jmxConnection - JMX connection to use.
     * @param attribView - the attributes to extract
     * @param next - the next query filter
     */
    public MBeansAttributeQueryFilter(MBeanServerConnection jmxConnection, Set attribView, MBeansObjectNameQueryFilter next) {
        super(next);
        this.jmxConnection = jmxConnection;
        this.attribView = attribView;
    }

    /**
     * Filter the query by retrieving the attributes specified, this will modify
     * the collection to a list of AttributeList
     * 
     * @param queries - query list
     * @return List of AttributeList, which includes the ObjectName, which has a
     *         key of MBeansAttributeQueryFilter.KEY_OBJECT_NAME_ATTRIBUTE
     * @throws Exception
     */
    public List query(List queries) throws Exception {
        return getMBeanAttributesCollection(next.query(queries));
    }

    /**
     * Retrieve the specified attributes of the mbean
     * 
     * @param result - collection of ObjectInstances and/or ObjectNames
     * @return List of AttributeList
     * @throws IOException
     * @throws ReflectionException
     * @throws InstanceNotFoundException
     * @throws NoSuchMethodException
     */
    protected List getMBeanAttributesCollection(Collection result) throws IOException, ReflectionException, InstanceNotFoundException, NoSuchMethodException, IntrospectionException {
        List mbeansCollection = new ArrayList();

        for (Iterator i = result.iterator(); i.hasNext();) {
            Object mbean = i.next();
            if (mbean instanceof ObjectInstance) {
                try {
                    mbeansCollection.add(getMBeanAttributes(((ObjectInstance)mbean).getObjectName(), attribView));
                } catch (InstanceNotFoundException ignore) {
                    // mbean could have been deleted in the meantime
                }
            } else if (mbean instanceof ObjectName) {
                try {
                    mbeansCollection.add(getMBeanAttributes((ObjectName)mbean, attribView));
                } catch (InstanceNotFoundException ignore) {
                    // mbean could have been deleted in the meantime
                }
            } else {
                throw new NoSuchMethodException("Cannot get the mbean attributes for class: " + mbean.getClass().getName());
            }
        }

        return mbeansCollection;
    }

    /**
     * Retrieve the specified attributes of the mbean
     * 
     * @param obj - mbean ObjectInstance
     * @param attrView - list of attributes to retrieve
     * @return AttributeList for the mbean
     * @throws ReflectionException
     * @throws InstanceNotFoundException
     * @throws IOException
     */
    protected AttributeList getMBeanAttributes(ObjectInstance obj, Set attrView) throws ReflectionException, InstanceNotFoundException, IOException, IntrospectionException {
        return getMBeanAttributes(obj.getObjectName(), attrView);
    }

    /**
     * Retrieve the specified attributes of the mbean
     * 
     * @param objName - mbean ObjectName
     * @param attrView - list of attributes to retrieve
     * @return AttributeList for the mbean
     * @throws IOException
     * @throws ReflectionException
     * @throws InstanceNotFoundException
     */
    protected AttributeList getMBeanAttributes(ObjectName objName, Set attrView) throws IOException, ReflectionException, InstanceNotFoundException, IntrospectionException {
        // If no attribute view specified, get all attributes
        String[] attribs;
        if (attrView == null || attrView.isEmpty()) {
            MBeanAttributeInfo[] infos = jmxConnection.getMBeanInfo(objName).getAttributes();
            attribs = new String[infos.length];

            for (int i = 0; i < infos.length; i++) {
                if (infos[i].isReadable()) {
                    attribs[i] = infos[i].getName();
                }
            }

            // Get selected attributes
        } else {

            attribs = new String[attrView.size()];
            int count = 0;
            for (Iterator i = attrView.iterator(); i.hasNext();) {
                attribs[count++] = (String)i.next();
            }
        }

        AttributeList attribList = jmxConnection.getAttributes(objName, attribs);

        attribList.add(0, new Attribute(KEY_OBJECT_NAME_ATTRIBUTE, objName));

        return attribList;
    }
}
