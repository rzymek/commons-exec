/*
 * Copyright 2000-2001,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.jexl.util;

import org.apache.commons.logging.Log;
/**
 *  Handles discovery and valuation of a
 *  boolean object property, of the
 *  form public boolean is<property> when executed.
 *
 *  We do this separately as to preserve the current
 *  quasi-broken semantics of get<as is property>
 *  get< flip 1st char> get("property") and now followed
 *  by is<Property>
 *
 *  @author <a href="geirm@apache.org">Geir Magnusson Jr.</a>
 *  @version $Id: BooleanPropertyExecutor.java,v 1.4 2004/02/28 13:45:21 yoavs Exp $
 */
public class BooleanPropertyExecutor extends PropertyExecutor
{
    public BooleanPropertyExecutor(Log rlog, org.apache.commons.jexl.util.introspection.Introspector is, Class clazz, String property)
    {
        super(rlog, is, clazz, property);
    }

    protected void discover(Class clazz, String property)
    {
        try
        {
            char c;
            StringBuffer sb;

            Object[] params = {  };

            /*
             *  now look for a boolean isFoo
             */

            sb = new StringBuffer("is");
            sb.append(property);

            c = sb.charAt(2);

            if (Character.isLowerCase(c))
            {
                sb.setCharAt(2, Character.toUpperCase(c));
            }

            methodUsed = sb.toString();
            method = introspector.getMethod(clazz, methodUsed, params);

            if (method != null)
            {
                /*
                 *  now, this has to return a boolean
                 */

                if (method.getReturnType() == Boolean.TYPE)
                    return;

                method = null;
            }
        }
        catch(Exception e)
        {
            rlog.error("PROGRAMMER ERROR : BooleanPropertyExector() : " + e);
        }
    }
}