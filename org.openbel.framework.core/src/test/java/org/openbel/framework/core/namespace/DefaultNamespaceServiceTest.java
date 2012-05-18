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

import static org.junit.Assert.fail;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openbel.framework.common.SystemConfigurationFile;
import org.openbel.framework.common.cfg.SystemConfigurationBasedTest;
import org.openbel.framework.common.model.CommonModelFactory;
import org.openbel.framework.common.model.Namespace;
import org.openbel.framework.common.model.Parameter;
import org.openbel.framework.core.df.cache.CacheLookupService;
import org.openbel.framework.core.df.cache.CacheableResourceService;
import org.openbel.framework.core.df.cache.DefaultCacheLookupService;
import org.openbel.framework.core.df.cache.DefaultCacheableResourceService;
import org.openbel.framework.core.indexer.IndexingFailure;
import org.openbel.framework.core.namespace.DefaultNamespaceService;
import org.openbel.framework.core.namespace.NamespaceIndexerService;
import org.openbel.framework.core.namespace.NamespaceIndexerServiceImpl;
import org.openbel.framework.core.namespace.NamespaceService;
import org.openbel.framework.core.namespace.NamespaceSyntaxWarning;
import org.openbel.framework.core.protocol.ResourceDownloadError;

/**
 * NamespaceServiceTest provides a unit-test for the {@link NamespaceService}.
 * This goal of this unit-test is to validate the namespace operations with a
 * focus on concurrency.
 *
 * @author Anthony Bargnesi {@code <abargnesi@selventa.com>}
 */
@SystemConfigurationFile(path = "src/test/resources/org/openbel/framework/core/namespace/belframework.cfg")
public class DefaultNamespaceServiceTest extends SystemConfigurationBasedTest {
    private NamespaceService namespaceService;

    @Before
    @Override
    public void setup() {
        super.setup();

        final CacheLookupService cacheLookup = new DefaultCacheLookupService();
        final NamespaceIndexerService nsindexer =
                new NamespaceIndexerServiceImpl();
        final CacheableResourceService cacheService =
                new DefaultCacheableResourceService();
        namespaceService = new DefaultNamespaceService(
                cacheService, cacheLookup, nsindexer);
    }

    @After
    @Override
    public void teardown() {
        super.teardown();
    }

    @Test
    @Ignore
    public void testVerifyParameter() {
        URL nsresource = getClass().getResource(
                "/org/openbel/framework/core/namespace/test.belns");
        String nsFileUrl = nsresource.toString();

        if (nsFileUrl == null) {
            fail("Cannot read namespace location: " + nsFileUrl);
        }

        final Parameter p = CommonModelFactory.getInstance().createParameter(
                new Namespace("COMPLEX", nsFileUrl),
                "Propionyl Coa Carboxylase Complex");

        try {
            namespaceService.verify(p);
        } catch (NamespaceSyntaxWarning e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IndexingFailure e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (ResourceDownloadError e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
