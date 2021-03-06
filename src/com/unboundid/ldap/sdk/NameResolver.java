/*
 * Copyright 2019 Ping Identity Corporation
 * All Rights Reserved.
 */
/*
 * Copyright (C) 2019 Ping Identity Corporation
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPLv2 only)
 * or the terms of the GNU Lesser General Public License (LGPLv2.1 only)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 */
package com.unboundid.ldap.sdk;



import java.net.InetAddress;
import java.net.UnknownHostException;

import com.unboundid.util.Extensible;
import com.unboundid.util.ThreadSafety;
import com.unboundid.util.ThreadSafetyLevel;



/**
 * This class defines an API that the LDAP SDK can use to resolve host names to
 * IP addresses, and vice versa.  The default implementations of the name
 * resolution methods simply delegates to the corresponding methods provided in
 * the {@code InetAddress} class.  Subclasses may override these methods to
 * provide support for caching, improved instrumentation, or other
 * functionality.  Any such methods that are not overridden will get the
 * JVM-default behavior.
 */
@Extensible()
@ThreadSafety(level=ThreadSafetyLevel.INTERFACE_THREADSAFE)
public abstract class NameResolver
{
  /**
   * The name of the system property that the JVM uses to specify how long (in
   * seconds) to cache the results of successful name service lookups.
   */
  private static String JVM_PROPERTY_POSITIVE_ADDRESS_CACHE_TTL_SECONDS =
       "networkaddress.cache.ttl";



  /**
   * The name of the system property that the JVM uses to specify how long (in
   * seconds) to cache the results of unsuccessful name service lookups (that
   * is, lookups that return no mapping).
   */
  private static String JVM_PROPERTY_NEGATIVE_ADDRESS_CACHE_TTL_SECONDS =
       "networkaddress.cache.negative.ttl";



  /**
   * Creates a new instance of this default name resolver.
   */
  protected NameResolver()
  {
    // No implemenattion is required.
  }



  /**
   * Retrieves an {@code InetAddress} that encapsulates an IP address associated
   * with the provided host name.
   *
   * @param  host  The host name for which to retrieve a corresponding
   *               {@code InetAddress} object.  It can be a resolvable name or
   *               a textual representation of an IP address.  If the provided
   *               name is the textual representation of an IPv6 address, then
   *               it can use either the form described in RFC 2373 or RFC 2732,
   *               or it can be an IPv6 scoped address.  If it is {@code null},
   *               then the returned address should represent an address of the
   *               loopback interface.
   *
   * @return  An {@code InetAddress} that encapsulates an IP address associated
   *          with the provided host name.
   *
   * @throws  UnknownHostException  If the provided name cannot be resolved to
   *                                its corresponding IP addresses.
   *
   * @throws  SecurityException  If a security manager prevents the name
   *                             resolution attempt.
   */
  public InetAddress getByName(final String host)
         throws UnknownHostException, SecurityException
  {
    return InetAddress.getByName(host);
  }



  /**
   * Retrieves an array of {@code InetAddress} objects that encapsulate all
   * known IP addresses associated with the provided host name.
   *
   * @param  host  The host name for which to retrieve the corresponding
   *               {@code InetAddress} objects.  It can be a resolvable name or
   *               a textual representation of an IP address.  If the provided
   *               name is the textual representation of an IPv6 address, then
   *               it can use either the form described in RFC 2373 or RFC 2732,
   *               or it can be an IPv6 scoped address.  If it is {@code null},
   *               then the returned address should represent an address of the
   *               loopback interface.
   *
   * @return  An array of {@code InetAddress} objects that encapsulate all known
   *          IP addresses associated with the provided host name.
   *
   * @throws  UnknownHostException  If the provided name cannot be resolved to
   *                                its corresponding IP addresses.
   *
   * @throws  SecurityException  If a security manager prevents the name
   *                             resolution attempt.
   */
  public InetAddress[] getAllByName(final String host)
         throws UnknownHostException, SecurityException
  {
    return InetAddress.getAllByName(host);
  }



  /**
   * Retrieves the host name for the provided {@code InetAddress} object.
   *
   * @param  inetAddress  The address for which to retrieve the host name.  It
   *                      must not be {@code null}.
   *
   * @return  The host name for the provided {@code InetAddress} object, or a
   *          textual representation of the IP address if the name cannot be
   *          determined.
   */
  public String getHostName(final InetAddress inetAddress)
  {
    return inetAddress.getHostName();
  }



  /**
   * Retrieves the canonical host name for the provided {@code InetAddress}
   * object.
   *
   * @param  inetAddress  The address for which to retrieve the canonical host
   *                      name.  It must not be {@code null}.
   *
   * @return  The canonical host name for the provided {@code InetAddress}
   *          object, or a textual representation of the IP address if the name
   *          cannot be determined.
   */
  public String getCanonicalHostName(final InetAddress inetAddress)
  {
    return inetAddress.getCanonicalHostName();
  }



  /**
   * Retrieves the address of the local host.  This should be the name of the
   * host obtained from the system, converted to an {@code InetAddress}.
   *
   * @return  The address of the local host.
   *
   * @throws  UnknownHostException  If the local host name cannot be resolved.
   *
   * @throws  SecurityException  If a security manager prevents the name
   *                             resolution attempt.
   */
  public InetAddress getLocalHost()
         throws UnknownHostException, SecurityException
  {
    return InetAddress.getLocalHost();
  }



  /**
   * Retrieves the loopback address for the system.  This should be either the
   * IPv4 loopback address of 127.0.0.1, or the IPv6 loopback address of ::1.
   *
   * @return  The loopback address for the system.
   */
  public InetAddress getLoopbackAddress()
  {
    return InetAddress.getLoopbackAddress();
  }



  /**
   * Sets the length of time in seconds for which the JVM should cache the
   * results of successful name service lookups.
   * <BR><BR>
   * Note that this timeout only applies to lookups performed by the JVM itself
   * and may not apply to all name resolver implementations.  Some
   * implementations may provide their own caching or their own lookup
   * mechanisms that do not use this setting.
   *
   * @param  seconds  The length of time in seconds for which the JVM should
   *                  cache the results of successful name service lookups.  A
   *                  value that is less than zero indicates that values should
   *                  be cached forever.
   */
  public static void setJVMSuccessfulLookupCacheTTLSeconds(final int seconds)
  {
    if (seconds < 0)
    {
      System.setProperty(JVM_PROPERTY_POSITIVE_ADDRESS_CACHE_TTL_SECONDS, "-1");
    }
    else
    {
      System.setProperty(JVM_PROPERTY_POSITIVE_ADDRESS_CACHE_TTL_SECONDS,
           String.valueOf(seconds));
    }
  }



  /**
   * Sets the length of time in seconds for which the JVM should cache the
   * results of unsuccessful name service lookups (that is, lookups in which no
   * mapping is found).
   * <BR><BR>
   * Note that this timeout only applies to lookups performed by the JVM itself
   * and may not apply to all name resolver implementations.  Some
   * implementations may provide their own caching or their own lookup
   * mechanisms that do not use this setting.
   *
   * @param  seconds  The length of time in seconds for which the JVM should
   *                  cache the results of unsuccessful name service lookups.  A
   *                  value that is less than zero indicates that values should
   *                  be cached forever.
   */
  public static void setJVMUnsuccessfulLookupCacheTTLSeconds(final int seconds)
  {
    if (seconds < 0)
    {
      System.setProperty(JVM_PROPERTY_NEGATIVE_ADDRESS_CACHE_TTL_SECONDS, "-1");
    }
    else
    {
      System.setProperty(JVM_PROPERTY_NEGATIVE_ADDRESS_CACHE_TTL_SECONDS,
           String.valueOf(seconds));
    }
  }



  /**
   * Retrieves a string representation of this name resolver.
   *
   * @return  A string representation of this name resolver.
   */
  public final String toString()
  {
    final StringBuilder buffer = new StringBuilder();
    toString(buffer);
    return buffer.toString();
  }



  /**
   * Appends a string representation of this name resolver to the provided
   * buffer.
   *
   * @param  buffer  A buffer to which the string representation should be
   *                 appended.
   */
  public abstract void toString(final StringBuilder buffer);
}
