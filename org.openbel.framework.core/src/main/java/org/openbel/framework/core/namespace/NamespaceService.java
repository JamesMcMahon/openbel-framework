/**
 * Copyright (C) 2012 Selventa, Inc.
 *
 * This file is part of the OpenBEL Framework.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The OpenBEL Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the OpenBEL Framework. If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional Terms under LGPL v3:
 *
 * This license does not authorize you and you are prohibited from using the
 * name, trademarks, service marks, logos or similar indicia of Selventa, Inc.,
 * or, in the discretion of other licensors or authors of the program, the
 * name, trademarks, service marks, logos or similar indicia of such authors or
 * licensors, in any marketing or advertising materials relating to your
 * distribution of the program or any covered product. This restriction does
 * not waive or limit your obligation to keep intact all copyright notices set
 * forth in the program as delivered to you.
 *
 * If you distribute the program in whole or in part, or any modified version
 * of the program, and you assume contractual liability to the recipient with
 * respect to the program or modified version, then you will indemnify the
 * authors and licensors of the program for any liabilities that these
 * contractual assumptions directly impose on those licensors and authors.
 */
package org.openbel.framework.core.namespace;

import java.util.Set;
import java.util.regex.Pattern;

import org.openbel.framework.common.InvalidArgument;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.common.model.Statement;
import org.openbel.framework.core.compiler.SymbolWarning;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * NamespaceService defines a service to handle all operations for working
 * with namespaces.  The responsibilities entail:<ul>
 * <li>Compiling namespace resource locations into operational form</li>
 * <li>Managing the lifecycle of namespace's jdbm btree index</li>
 * <li>Performing value lookups for a namespace resource location</li></ul>
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface NamespaceService {

    /**
     * Determines if a {@link Namespace} resource location is open and
     * available for lookups.
     * 
     * @param resourceLocation {@link String}, the namespace resource location
     * to check its open status, which cannot be null
     * @return <tt>boolean</tt>, the resource location's open status,
     * <tt>true</tt> if the resource location is open for lookups,
     * <tt>false</tt> if the resource location is not open
     */
    public boolean isOpen(String resourceLocation);

    /**
     * Compiles the namespace resource into usable form so lookups can be
     * possible.  This entails the following:<ol>
     * <li>Resolve the namespace resource location to the cache.</li>
     * <li>Build a jdbm btree index for the cached namespace resource.</li></ol>
     * 
     * @param resourceLocation {@link String}, the namespace resource location
     * to compile which cannot be null
     * @throws ResourceDownloadError Thrown if the namespace resource could
     * not be downloaded
     * @throws IndexingFailure Thrown if an error occurred while indexing the
     * namespace
     * @throws InvalidArgument Thrown if the <tt>resourceLocation</tt> argument
     * is null
     */
    public void compileNamespace(String resourceLocation)
            throws ResourceDownloadError, IndexingFailure;

    /**
     * Lookup and return the namespace value for the {@link Parameter}'s
     * associated {@link Namespace}.  The jdbm btree index uses the namespace
     * value as the entry key and the namespace value encoding as the entry
     * value.
     * 
     * @param p {@link Parameter}, the parameter to lookup the namespace
     * value for which cannot be null
     * @return {@link String} the namespace value encoding for the namespace
     * value of the parameter, or an empty string if the namespace value
     * encoding is blank, or null if:<ul>
     * <li>The {@link Parameter} has a <tt>null</tt> namespace.</li>
     * <li>The {@link Parameter}'s namespace value is not in the index.</li>
     * @throws NamespaceSyntaxWarning Thrown if the {@link Parameter#getValue()} is not
     * found in its declared namespace
     * @throws IndexingFailure if the namespace is not already open
     * @throws InvalidArgument Thrown if <tt>p</tt> argument is null, its
     * value is null, or if its namespace's resource location is null
     */
    public String lookup(Parameter p) throws NamespaceSyntaxWarning,
            IndexingFailure;

    /**
     * Perform a search on the values in namespace specified by the resource
     * location, namespace does not have to be open prior to calling this.
     * 
     * @param resourceLocation
     *            resource location, e.g.,
     *            "http://resource.belframework.org/belframework/1.0/ns/chebi-ids.belns"
     *            , can not be null
     * @param pattern
     *            {@link Pattern}, can not be null
     * @return {@link Set} of {@link String}s containing values that match
     * @throws ResourceDownloadError
     *             Thrown if the namespace resource cannot be downloaded when
     *             compilation is necessary
     * @throws IndexingFailure
     *             Thrown if an index could not be opened for a resource
     *             location
     */
    public Set<String> search(String resourceLocation, Pattern pattern)
            throws IndexingFailure, ResourceDownloadError;

    /**
     * Verify that the {@link Parameter}'s value exists in its defined
     * namespace index.
     * 
     * <p>
     * This call will automatically open the namespace index if needed.  If the
     * namespace index cannot be opened for lookups then it is assumed that it
     * does not exist.  In this case the namespace resource is downloaded,
     * cached, compiled into an index, and opened for use by this call.
     * </p>
     * 
     * @param p {@link Parameter}, the parameter to verify that its value is
     * defined in its namespace index
     * @throws NamespaceSyntaxWarning Thrown if the {@link Parameter#getValue()} is not
     * found in its declared namespace
     * @throws IndexingFailure Thrown if an index could not be opened for a
     * resource location
     * @throws ResourceDownloadError Thrown if the namespace resource cannot
     * be downloaded when compilation is necessary
     * @throws InvalidArgument Thrown if <tt>p</tt> argument is null, its
     * value is null, or if its namespace's resource location is null
     */
    public void verify(Parameter p) throws NamespaceSyntaxWarning,
            IndexingFailure,
            ResourceDownloadError;

    /**
     * Verify all parameters of {@link Statement} <tt>s</tt>.  If at least one
     * parameter could not be found in its namespace index then a
     * {@link SymbolWarning} is thrown to capture which parameters failed. 
     * 
     * <p>
     * This call will automatically open the namespace index if needed.  If the
     * namespace index cannot be opened for lookups then it is assumed that it
     * does not exist.  In this case the namespace resource is downloaded,
     * cached, compiled into an index, and opened for use by this call.
     * </p>
     * 
     * @param s {@link Statement}, the statement containing the parameters to
     * verify, which cannot be null
     * @throws SymbolWarning Thrown if at least one parameter could not be
     * found in its namespace index
     * @throws IndexingFailure Thrown if an index could not be opened for a
     * resource location
     * @throws ResourceDownloadError Thrown if the namespace resource cannot
     * be downloaded when compilation is necessary
     * @throws InvalidArgument Thrown if the {@link Statement} <tt>s</tt> is
     * null
     */
    public void verify(Statement s) throws SymbolWarning, IndexingFailure,
            ResourceDownloadError;

    /**
     * Verify all parameters of {@link Document} <tt>d</tt>.  If at least one
     * parameter could not be found in its namespace index then a
     * {@link SymbolWarning} is thrown to capture which parameters failed. 
     * 
     * <p>
     * This call will automatically open the namespace index if needed.  If the
     * namespace index cannot be opened for lookups then it is assumed that it
     * does not exist.  In this case the namespace resource is downloaded,
     * cached, compiled into an index, and opened for use by this call.
     * </p>
     * 
     * @param d {@link Document}, the document containing the parameters to
     * verify, which cannot be null
     * @throws SymbolWarning Thrown if at least one parameter could not be
     * found in its namespace index
     * @throws IndexingFailure Thrown if an index could not be opened for a
     * resource location
     * @throws ResourceDownloadError Thrown if the namespace resource cannot
     * be downloaded when compilation is necessary
     * @throws InvalidArgument Thrown if the {@link Document} <tt>d</tt> is
     * null
     */
    public void verify(Document d) throws SymbolWarning, IndexingFailure,
            ResourceDownloadError;
}
