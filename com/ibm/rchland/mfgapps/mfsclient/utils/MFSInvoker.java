/* © Copyright IBM Corporation 2010. All rights reserved.
 * Dates should follow the ISO 8601 standard YYYY-MM-DD
 * The @ symbol has a predefined meaning in Java.
 * Thus, Flags should not start with the @ symbol.  Please use ~ instead.
 *
 * Date       Flag IPSR/PTR Name             Details
 * ---------- ---- -------- ---------------- ----------------------------------
 * 2010-03-12       42558JL Santiago SC      -Initial version, Java 5.0
 ******************************************************************************/

package com.ibm.rchland.mfgapps.mfsclient.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.ibm.rchland.mfgapps.mfscommon.exception.MFSException;

/**
 * <code>MFSInvoker</code> wrapper for <code>MFSFieldPanelBuilder</code>
 * @author The MFS Client Development Team
 */
public class MFSInvoker 
{
	/**
	 * Invoke the getter method in the given object source. A getter method
	 * doesn't have parameters.
	 * @param source where the method will be invoked.
	 * @param methodName the <code>String</code> name of the method to invoke.
	 * @return the <code>Object</code> result of the method invocation.
	 * @throws MFSException
	 */
	public static Object invokeGetter(Object source, String methodName)
		throws MFSException
	{
		StringBuffer erms = new StringBuffer("");
		Object sourceData = null;

		try 
		{		
			// Get the source clazz
			Class<?> clazzType = source.getClass();
			Class<?>[] parameters = null;		
			// Get the clazz method
			Method method = clazzType.getMethod(methodName, parameters);
			Object[] arguments = null;
			// invoke the method of the source
			sourceData = method.invoke(source, arguments);	
		}
		catch (SecurityException e) 
		{
			erms = new StringBuffer("");
			erms.append("Error: Method security violation.");
		}
		catch (NoSuchMethodException e) 
		{
			erms.append("Error: No such method exists.");
		} 
		catch (IllegalArgumentException e) 
		{
			erms.append("Error: Illegal method arguments.");
		} 
		catch (IllegalAccessException e) 
		{
			erms.append("Error: Illegal method access.");
		} 
		catch (InvocationTargetException e)
		{
			erms.append("Error: Called method threw exception - \n");
			erms.append(e.getMessage());
		}
		catch (NullPointerException e)
		{
			erms.append("Error: Null Pointer Exception.\n The source object ");
			erms.append("from which data is retrieved is null.");
			
			throw new MFSException(erms.toString(), false);
		}
		
		if(!erms.toString().equals(""))
		{
			erms.append("\nSource = ");
			erms.append(source.getClass().getSimpleName());
			erms.append(" Method = ");
			erms.append(methodName);
			
			throw new MFSException(erms.toString(), false);
		}
		
		return sourceData;
	}
	
	/**
	 * Check if the source object is an instance of the clazzName
	 * @param source the <code>Object</code>
	 * @param clazzName the <code>Class</code> 
	 * @return true if is instance, else false
	 */
	public static boolean isInstance(Object source, String clazzName)
	{
		boolean isInstance = false;

		try
		{
			if(!clazzName.startsWith(MFSConstants.MFS_CLIENT_PACKAGE))
			{
				clazzName = MFSConstants.MFS_CLIENT_PACKAGE + "." + clazzName;
			}
			
			Class<?> clazzType = Class.forName(clazzName);			
			isInstance = clazzType.isInstance(source);
		}
		catch (ClassNotFoundException e)
		{
			// do nothing
		}		

		return isInstance;
	}
}
