/*
 * Copyright (c) 2013-2024 QuartzDesk.com.
 * Licensed under the MIT license (https://opensource.org/licenses/MIT).
 */

package com.quartzdesk.test_webapps.common;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Various commonly used methods used throughout the agent API.
 */
public final class CommonUtils
{
  // CSOFF: IllegalType

  /**
   * Empty array.
   */
  private static final Object[] EMPTY_ARRAY = new Object[] {};
  // CSON: IllegalType


  /**
   * Private constructor of a utility class.
   */
  private CommonUtils()
  {
  }


  /**
   * Adds all elements present in the <code>from</code> collection but missing
   * in the <code>to</code> collection to the <code>to</code> collection.
   *
   * @param <T>  the type of collection elements.
   * @param to   the target collection.
   * @param from the source collection.
   * @return the updated target collection.
   */
  public static <T> Collection<T> addIfMissing( Collection<T> to, Collection<T> from )
  {
    for ( T o : from )
    {
      if ( !to.contains( o ) )
        to.add( o );
    }
    return to;
  }


  /**
   * Adds all elements present in the <code>from</code> enumeration but missing
   * in the <code>to</code> collection to the <code>to</code> collection.
   *
   * @param <T>  the type of collection elements.
   * @param to   the target collection.
   * @param from the source enumeration.
   * @return the updated target collection.
   */
  public static <T> Collection<T> addIfMissing( Collection<T> to, Enumeration<T> from )
  {
    while ( from.hasMoreElements() )
    {
      T o = from.nextElement();

      if ( !to.contains( o ) )
        to.add( o );
    }
    return to;
  }


  /**
   * Adds all elements present in the <code>from</code> iterator but missing
   * in the <code>to</code> collection to the <code>to</code> collection.
   *
   * @param <T>  the type of collection elements.
   * @param to   the target collection.
   * @param from the source iterator.
   * @return the updated target collection.
   */
  public static <T> Collection<T> addIfMissing( Collection<T> to, Iterator<T> from )
  {
    while ( from.hasNext() )
    {
      T o = from.next();

      if ( !to.contains( o ) )
        to.add( o );
    }
    return to;
  }


  /**
   * Adds the specified element to the collection if the collection
   * does not already contain it.
   *
   * @param <T>     the type of collection elements.
   * @param to      the target collection.
   * @param element an element to add.
   * @return the updated target collection.
   */
  public static <T> Collection<T> addIfMissing( Collection<T> to, T element )
  {
    if ( !to.contains( element ) )
      to.add( element );

    return to;
  }


  /**
   * Returns an empty object array.
   *
   * @return an empty object array.
   */
  public static Object[] emptyArray()
  {
    return EMPTY_ARRAY;
  }


  /**
   * Simply prepends the package name of the specified class to the
   * specified name and replaces all '.' with '/'.
   *
   * @param name  a resource name.
   * @param clazz a class to be used to resolve the name if it
   *              is a relative name.
   * @return the absolute resource name.
   */
  public static String getAbsoluteResourceName( String name, Class<?> clazz )
  {
    StringBuilder absName = new StringBuilder( clazz.getPackage().getName().replace( '.', '/' ) );
    absName.append( '/' );
    absName.append( name );
    return absName.toString();
  }


  /**
   * Returns the cause of the specified exception.
   *
   * @param t an exception.
   * @return the cause.
   */
  public static Throwable getCause( Throwable t )
  {
    // SQLException does not use "standard" cause chaining...grrr
    if ( t instanceof SQLException )
    {
      return ( (SQLException) t ).getNextException();
    }
    else
    {
      return t.getCause();
    }
  }


  /**
   * Returns the hostname of the local host. If the host name
   * cannot be determined, this method returns an empty string.
   *
   * @return the hostname of the local host.
   */
  public static String getLocalHost()
  {
    try
    {
      InetAddress localhost = InetAddress.getLocalHost();
      return localhost.getHostName();
    }
    catch ( UnknownHostException e )
    {
      return CommonConst.EMPTY_STRING;
    }
  }


  /**
   * Returns the reader for the resource with the specified absolute name.
   * The resource is loaded through the current thread's context class loader.
   *
   * @param absoluteName a resource name.
   * @param enc          the resource encoding.
   * @return the resource reader.
   * @throws java.io.IOException if the resource was not found.
   */
  public static Reader getResourceAsReader( String absoluteName, String enc )
      throws IOException
  {
    InputStream ins = getResourceAsStream( absoluteName );
    return new InputStreamReader( ins, enc );
  }


  /**
   * Returns the reader for the resource with the specified relative name.
   * The name is treated as relative, because the package name of the specified
   * class is prepended to that name to make the name absolute. The resource
   * is loaded through the current thread's context class loader.
   *
   * @param relativeName a resource name (relative, or absolute).
   * @param clazz        a class used to make the resource name absolute and load the resource.
   * @param enc          the resource encoding.
   * @return the resource reader.
   * @throws java.io.IOException if the resource was not found.
   */
  public static Reader getResourceAsReader( String relativeName, Class<?> clazz, String enc )
      throws IOException
  {
    String newName = getAbsoluteResourceName( relativeName, clazz );
    return getResourceAsReader( newName, enc );
  }


  /**
   * Returns the input stream for the resource with the specified absolute name.
   * The resource is loaded through the current thread's context class loader.
   *
   * @param absoluteName a resource name.
   * @return the resource input stream.
   * @throws java.io.IOException if the resource was not found.
   */
  public static InputStream getResourceAsStream( String absoluteName )
      throws IOException
  {
    String resourceName = absoluteName.startsWith( "/" ) ? absoluteName.substring( 1 ) : absoluteName;
    InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream( resourceName );

    if ( ins == null )
      throw new IOException( "Cannot find resource: " + resourceName );

    return new BufferedInputStream( ins );
  }


  /**
   * Returns the input stream for the resource with the specified relative name.
   * The name is treated as relative and the package name of the specified
   * class is prepended to that name to make the name absolute. The resource
   * is loaded through the current thread's context class loader.
   *
   * @param relativeName a resource name (relative, or absolute).
   * @param clazz        a class used to make the resource name absolute and load the resource.
   * @return the resource input stream.
   * @throws java.io.IOException if the resource was not found.
   */
  public static InputStream getResourceAsStream( String relativeName, Class<?> clazz )
      throws IOException
  {
    String newName = getAbsoluteResourceName( relativeName, clazz );
    return getResourceAsStream( newName );
  }


  /**
   * Returns the root cause of the specified exception.
   *
   * @param t an exception.
   * @return the root cause.
   */
  public static Throwable getRootCause( Throwable t )
  {
    Throwable cause = t;

    while ( getCause( cause ) != null )
      cause = getCause( cause );

    return cause;
  }


  /**
   * Returns the string representation of the stack trace of the
   * root cause of the specified exception.
   *
   * @param t an exception.
   * @return the stack trace as a string.
   */
  public static String getRootCauseStackTrace( Throwable t )
  {
    return getStackTrace( getRootCause( t ) );
  }


  /**
   * Returns the string representation of the stack trace of the specified exception including
   * all of its causes. This method supports dumping of SQLException chains which do not use the
   * standard chaining mechanism using the {@link Throwable#getCause()} method.
   *
   * @param t an exception.
   * @return the stack trace as a string.
   */
  public static String getStackTrace( Throwable t )
  {
    StringWriter sw = new StringWriter();
    PrintWriter w = new PrintWriter( sw );
    w.println( t );

    StackTraceElement[] traceElements = t.getStackTrace();
    for ( StackTraceElement traceElement : traceElements )
      w.println( "\tat " + traceElement );

    Throwable cause = t.getCause();

    // if the processed exception does not have a standard cause, try to extract
    // cause from "getNextException" in case the processed exception is an SQLException
    if ( cause == null && t instanceof SQLException )
      cause = ( (SQLException) t ).getNextException();

    if ( cause != null )
      printStackTraceAsCause( cause, w, traceElements );

    w.flush();
    w.close();

    return sw.toString();
  }


  /**
   * Returns the bigger of the two Integers. A null Integer
   * is treated as smaller then {@link Integer#MIN_VALUE}.
   *
   * @param i1 the first Integer.
   * @param i2 the second Integer.
   * @return the bigger of the two Integers.
   */
  public static Integer max( Integer i1, Integer i2 )
  {
    if ( i1 != null && i2 != null )
      return i1 > i2 ? i1 : i2;

    return i1 == null ? i2 : i1;
  }


  /**
   * Returns the bigger value of the two int arguments.
   *
   * @param i1 the first int.
   * @param i2 the second int.
   * @return the bigger value of the two int arguments.
   */
  public static int max( int i1, int i2 )
  {
    return i1 > i2 ? i1 : i2;
  }


  /**
   * Returns the smaller of the two Integers. A null Integer
   * is treated as smaller then {@link Integer#MIN_VALUE}.
   *
   * @param i1 the first Integer.
   * @param i2 the second Integer.
   * @return the smaller of the two Integers.
   */
  public static Integer min( Integer i1, Integer i2 )
  {
    if ( i1 != null && i2 != null )
      return min( i1.intValue(), i2.intValue() );

    return null;
  }


  /**
   * Returns the smaller value of the two int arguments.
   *
   * @param i1 the first int.
   * @param i2 the second int.
   * @return the smaller value of the two int arguments.
   */
  public static int min( int i1, int i2 )
  {
    return i1 < i2 ? i1 : i2;
  }


  /**
   * Returns the smaller of the two Doubles. A null Double
   * is treated as smaller then {@link Double#MIN_VALUE}.
   *
   * @param d1 the first Double.
   * @param d2 the second Double.
   * @return the smaller of the two Doubles.
   */
  public static Double min( Double d1, Double d2 )
  {
    if ( d1 != null && d2 != null )
      return min( d1.doubleValue(), d2.doubleValue() );

    return null;
  }

  // CSOFF: IllegalType


  /**
   * Returns the smaller value of the two double arguments.
   *
   * @param d1 the first double.
   * @param d2 the second double.
   * @return the smaller value of the two double arguments.
   */
  public static double min( double d1, double d2 )
  {
    return d1 < d2 ? d1 : d2;
  }
  // CSON: IllegalType

  // CSOFF: IllegalType


  /**
   * Returns true, if the specified argument is null, or it is an empty string,
   * false otherwise.
   *
   * @param value a string value.
   * @return true, if the specified argument is null, or it is an empty string,
   * false otherwise.
   */
  public static boolean nullOrEmpty( String value )
  {
    return value == null || CommonConst.EMPTY_STRING.equals( value );
  }
  // CSOFF: IllegalType


  /**
   * Recursively dumps the stack trace of the specified cause exception including
   * and all of its cause exceptions to the specified writer. This method handles
   * the SQLException chaining which does not use the standard Java cause chaining.
   *
   * @param t           a cause exception.
   * @param w           a print writer.
   * @param causedTrace the stack trace of the caused exception (i.e. of the
   *                    exception caused by t).
   */
  private static void printStackTraceAsCause( Throwable t, PrintWriter w,
      StackTraceElement[] causedTrace )
  {
    // Compute number of frames in common between this and caused
    StackTraceElement[] trace = t.getStackTrace();

    int m = trace.length - 1;
    int n = causedTrace.length - 1;

    while ( m >= 0 && n >= 0 && trace[m].equals( causedTrace[n] ) )
    {
      m--;
      n--;
    }
    int framesInCommon = trace.length - 1 - m;

    w.println( "Caused by: " + t );
    for ( int i = 0; i <= m; i++ )
      w.println( "\tat " + trace[i] );
    if ( framesInCommon != 0 )
      w.println( "\t... " + framesInCommon + " more" );

    // Recurse if we have a cause
    Throwable cause = t.getCause();

    // if the processed exception does not have a standard cause, try to extract
    // cause from "getNextException" in case the processed exception is an SQLException
    if ( cause == null && t instanceof SQLException )
      cause = ( (SQLException) t ).getNextException();

    if ( cause != null )
      printStackTraceAsCause( cause, w, trace );
  }


  /**
   * Removes elements at the specified indexes from the specified list
   * and returns the list of removed elements.
   *
   * @param list            the input list.
   * @param indicesToRemove the indexes representing elements to remove.
   * @param <T>             the list element type.
   * @return the list of removed elements.
   */
  public static <T> List<T> removeListElements( List<T> list, int[] indicesToRemove )
  {
    // 1. first, create a copy of the indices array
    int[] indices = new int[indicesToRemove.length];
    System.arraycopy( indicesToRemove, 0, indices, 0, indicesToRemove.length );

    // 2. sort the indices
    Arrays.sort( indices );

    List<T> removedElements = new ArrayList<T>();

    // 3. remove individual elements
    // sorted indexes of elements to remove are iterated in descending order -> avoids collisions
    for ( int i = indices.length - 1; i >= 0; i-- )
    {
      int index = indices[i];
      removedElements.add( list.get( index ) );
      list.remove( index );
    }

    return removedElements;
  }


  /**
   * Adds up all the values in the list and returns the result. Values
   * that are null are skipped. If all values are null, then the result is
   * null.
   *
   * @param values a list of values to add.
   * @return the result.
   */
  public static Integer safeAdd( Integer... values )
  {
    Integer result = null;

    for ( Integer value : values )
    {
      if ( value != null )
      {
        if ( result == null )
          result = 0;

        result += value;
      }
    }

    return result;
  }


  /**
   * Adds up all the values in the list and returns the result. Values
   * that are null are skipped. If all values are null, then the result is
   * null.
   *
   * @param values a list of values to add.
   * @return the result.
   */
  public static Long safeAdd( Long... values )
  {
    Long result = null;

    for ( Long value : values )
    {
      if ( value != null )
      {
        if ( result == null )
          result = 0L;

        result += value;
      }
    }

    return result;
  }


  /**
   * Returns 0 if both objects are null, -1 if c1 is null and c2 is not,
   * 1 if c1 is not null and c2 is null, otherwise the result of normal
   * comparison.
   * <p/>
   * <strong>
   * Please note that this method performs a lexicographic comparison
   * on string arguments using the standard {@link String#compareTo(String)}
   * method.
   * </strong>
   *
   * @param o1 the first object.
   * @param o2 the second object.
   * @return the comparison result.
   */
  @SuppressWarnings( "unchecked" )
  public static <T extends Comparable<T>> int safeCompare( T o1, T o2 )
  {
    if ( o1 == o2 )
      return 0;

    if ( o1 != null && o2 == null )
      return 1;

    if ( o1 == null )  // o2 != null
      return -1;

    /*
    Comparable c1 = (Comparable) o1;
    Comparable c2 = (Comparable) o2;
    */

    return o1.compareTo( o2 );
  }


  /**
   * Applies the {@link #safeCompare(Comparable, Comparable)} operation on the values in the
   * specified arrays and returns the result. This method is useful if we want to compare two
   * objects using multiple attributes.
   *
   * @param values1 the first value array.
   * @param values2 the second value array.
   * @return the comparison result.
   * @see #safeCompare(Comparable, Comparable) , Object)
   */
  public static <T extends Comparable<T>> int safeCompare( T[] values1, T[] values2 )
  {
    assert values1.length == values2.length : "Mismatched length of compared value arrays.";

    int result = 0;
    for ( int i = 0; i < values1.length && result == 0; i++ )
    {
      result = safeCompare( values1[i], values2[i] );
    }

    return result;
  }


  /**
   * Divides the specified two numbers and returns the result.
   * If either of the values is null, then the result is null. If the
   * first value is 0, the result is 0 regardless of the second value
   * (can be null).
   *
   * @param value1 the first value.
   * @param value2 the second value.
   * @return the result.
   */
  public static Double safeDiv( Number value1, Number value2 )
  {
    if ( value1 == null || value2 == null )
      return null;

    if ( value1.doubleValue() == 0L )
      return (double) 0;

    return value1.doubleValue() / value2.doubleValue();
  }


  /**
   * Compares the two Objects for equality. This method returns true if both Objects are null,
   * or they are equal according to the {@link Object#equals(Object)} method, false otherwise.
   * In case the specified objects are arrays, this method performs deep equality check of all
   * array elements.
   *
   * @param o1 the first object.
   * @param o2 the second object.
   * @return true if both Objects are equal, false otherwise.
   */
  public static boolean safeEquals( Object o1, Object o2 )
  {
    if ( o1 == o2 )
      return true;

    if ( o1 != null && o2 == null )
      return false;

    if ( o1 == null )  // o2 != null
      return false;

    if ( o1.getClass().isArray() && o2.getClass().isArray() )
    {
      int length1 = Array.getLength( o1 );
      int length2 = Array.getLength( o2 );

      // array lengths must be equal
      if ( length1 != length2 )
        return false;

      // array component types must be equal
      if ( !o1.getClass().getComponentType().equals( o2.getClass().getComponentType() ) )
        return false;

      // individual elements must be equal
      for ( int i = 0; i < length1; i++ )
      {
        if ( !safeEquals( Array.get( o1, i ), Array.get( o2, i ) ) )
          return false;
      }
      return true;
    }

    return o1.equals( o2 );
  }


  /**
   * Compares the two String for equality while ignoring the case. This method returns true if
   * both String are null,
   * or they are equal according to the {@link String#equalsIgnoreCase(String)} method, false otherwise.
   *
   * @param s1 the first object.
   * @param s2 the second object.
   * @return true if both Strings are equal while ignoring the case, false otherwise.
   */
  public static boolean safeEqualsIgnoreCase( String s1, String s2 )
  {
    if ( s1 == s2 )
      return true;

    if ( s1 != null && s2 == null )
      return false;

    if ( s1 == null )  // o2 != null
      return false;

    return s1.equalsIgnoreCase( s2 );
  }


  /**
   * Returns the hash-code of the specified object. If the specified object
   * is null, then this method returns 1, rather then throwing NullPointerException.
   *
   * @param o the object.
   * @return the object's hash-code, or 1 if the object is null.
   */
  public static int safeHashCode( Object o )
  {
    int hash = 1;

    if ( o != null )
    {
      if ( o.getClass().isArray() )
        return Arrays.deepHashCode( (Object[]) o );
      else
        hash = o.hashCode();
    }

    return hash;
  }


  /**
   * Returns the string representation of the specified object
   * as returned by the object's <code>toString()</code> method,
   * or null if the object is null.
   *
   * @param obj an object.
   * @return the string representation of the specified object.
   */
  public static String safeToString( Object obj )
  {
    return obj != null ? obj.toString() : null;
  }


  /**
   * Converts the specified list of Strings to the list of Integers.
   *
   * @param stringList a String list.
   * @return the Integer list.
   */
  public static List<Integer> toIntegerList( List<String> stringList )
  {
    List<Integer> integerList = new ArrayList<Integer>( stringList.size() );
    for ( String s : stringList )
    {
      integerList.add( new Integer( s ) );
    }
    return integerList;
  }


  /**
   * Converts the specified list of Strings to the list of Longs.
   *
   * @param stringList a String list.
   * @return the Long list.
   */
  public static List<Long> toLongList( List<String> stringList )
  {
    List<Long> longList = new ArrayList<Long>( stringList.size() );
    for ( String s : stringList )
    {
      longList.add( new Long( s ) );
    }
    return longList;
  }
}