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

import java.io.File;

import org.openbel.framework.common.model.DataFileIndex;
import org.openbel.framework.core.indexer.IndexingFailure;

/**
 * NamespaceIndexerService defines a service to index namespace values to
 * provide an efficient method of looking up namespace vales for BEL document
 * syntax verification.
 * 
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
public interface NamespaceIndexerService {

    /**
     * Indexes namespace values, encoded in {@code rawNamespaceFile}, for the
     * namespace defined in {@code namespace}. This will generate a
     * {@link DataFileIndex}, including namespace header and namespace index,
     * that can be used for efficient lookups of namespace values and semantic
     * function flags.
     * 
     * @param resourceLocation {@link String}, the namespace resource location,
     * which cannot be null
     * @param rawNamespaceFile {@link File}, the plain-text BEL namespace
     * document
     * @return {@link DataFileIndex}, the namespace index which consists of the
     * namespace header and index.
     * @throws IndexingFailure Thrown if there was an error parsing the
     * namespace header or building the namespace index
     */
    public DataFileIndex indexNamespace(final String resourceLocation,
            File rawNamespaceFile) throws IndexingFailure;
}
