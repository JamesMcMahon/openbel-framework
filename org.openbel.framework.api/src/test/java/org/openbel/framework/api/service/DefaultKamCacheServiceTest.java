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
package org.openbel.framework.api.service;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.openbel.framework.api.KamStore;
import org.openbel.framework.api.service.DefaultKamCacheService;
import org.openbel.framework.api.service.KamCacheService;
import org.openbel.framework.api.service.KamCacheServiceException;
import org.openbel.framework.common.enums.CitationType;
import org.openbel.framework.common.enums.FunctionEnum;
import org.openbel.framework.common.protonetwork.model.SkinnyUUID;
import org.openbel.framework.core.kamstore.KamDbObject;
import org.openbel.framework.core.kamstore.data.jdbc.KAMCatalogDao.AnnotationFilter;
import org.openbel.framework.core.kamstore.data.jdbc.KAMCatalogDao.KamFilter;
import org.openbel.framework.core.kamstore.data.jdbc.KAMCatalogDao.KamInfo;
import org.openbel.framework.core.kamstore.data.jdbc.KAMCatalogDao.NamespaceFilter;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.AnnotationType;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.BelDocumentInfo;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.BelStatement;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.BelTerm;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.Citation;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.Namespace;
import org.openbel.framework.core.kamstore.data.jdbc.KAMStoreDaoImpl.TermParameter;
import org.openbel.framework.core.kamstore.model.Kam;
import org.openbel.framework.core.kamstore.model.KamStoreException;
import org.openbel.framework.core.kamstore.model.Kam.KamEdge;
import org.openbel.framework.core.kamstore.model.Kam.KamNode;

/**
 * {@link DefaultKamCacheService} test cases.
 */
public class DefaultKamCacheServiceTest {

    /**
     * Test cache mechanisms.
     */
    @Test
    public void test() {
        KamCacheService kcSvc = new DefaultKamCacheService(new MockKAMStore1());
        KamDbObject kdbobj = new KamDbObject(1, null, null, null, null);
        KamInfo ki = new KamInfo(kdbobj);
        long t1 = currentTimeMillis();
        try {
            kcSvc.loadKam(ki, null);
        } catch (KamCacheServiceException e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
        long t2 = currentTimeMillis();

        // CACHE MISS ASSERTION
        String msg = "expected load of KAM to take more than 5 seconds";
        assertTrue(msg, (t2 - t1) >= (MockKAMStore1.KAM_LOAD_TIME * 1000));

        String handle = null;
        try {
            handle = kcSvc.loadKam(ki, null);
        } catch (KamCacheServiceException e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
        long t3 = currentTimeMillis();

        // CACHE HIT ASSERTION
        msg = "expected load of KAM to take less than 5 seconds";
        assertTrue(msg, (t3 - t2) < (MockKAMStore1.KAM_LOAD_TIME * 1000));

        kcSvc.releaseKam(handle);

        long t4 = currentTimeMillis();
        try {
            kcSvc.loadKam(ki, null);
        } catch (KamCacheServiceException e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
        long t5 = currentTimeMillis();

        // CACHE MISS ASSERTION
        msg = "expected second load of KAM to take more than 5 seconds";
        assertTrue(msg, (t5 - t4) >= (MockKAMStore1.KAM_LOAD_TIME * 1000));
    }

    static class MockKAMStore1 extends MockKAMStore {
        // Time to load "KAM" 5
        static final int KAM_LOAD_TIME = 5;

        @Override
        public Kam getKam(KamInfo info, KamFilter fltr)
                throws KamStoreException {
            try {
                sleep(KAM_LOAD_TIME * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Kam getKam(KamInfo info) throws KamStoreException {
            try {
                sleep(KAM_LOAD_TIME * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    abstract static class MockKAMStore implements KamStore {

        @Override
        public void close(Kam k) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void teardown() {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<KamInfo> readCatalog() {
            throw new UnsupportedOperationException();
        }

        @Override
        public KamInfo getKamInfo(String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Kam getKam(String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<AnnotationType> getAnnotationTypes(Kam k)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<AnnotationType> getAnnotationTypes(KamInfo info)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<String> getAnnotationTypeDomainValues(KamInfo info,
                AnnotationType type) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<BelDocumentInfo> getBelDocumentInfos(Kam k) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<BelDocumentInfo> getBelDocumentInfos(KamInfo info) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Namespace getNamespace(Kam k, String resourceLocation) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Namespace> getNamespaces(Kam k) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Namespace> getNamespaces(KamInfo info) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<BelStatement> getSupportingEvidence(KamEdge edge,
                AnnotationFilter fltr) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<BelStatement> getSupportingEvidence(KamEdge edge) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<BelTerm> getSupportingTerms(KamNode node) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<BelTerm> getSupportingTerms(
                KamNode node, boolean removeDups) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<BelTerm> getSupportingTerms(KamNode node,
                boolean removeDups, NamespaceFilter fltr) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<TermParameter> getTermParameters(
                KamInfo info, BelTerm term) {
            throw new UnsupportedOperationException();
        }

        @Override
        public KamNode getKamNode(Kam k, String termString) {
            throw new UnsupportedOperationException();
        }

        @Override
        public KamNode getKamNode(Kam k, BelTerm term) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<KamNode> getKamNodes(Kam k, FunctionEnum function,
                Namespace ns, String paramValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<KamNode> getKamNodes(
                Kam k, Namespace ns, String paramValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<KamNode> getKamNodes(Kam k, KamNode node) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<KamNode> getKamNodes(Kam k, SkinnyUUID uuid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<KamNode> getKamNodes(Kam k, FunctionEnum function,
                SkinnyUUID uuid) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Citation> getCitations(KamInfo kaminfo,
                BelDocumentInfo docinfo) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Citation> getCitations(KamInfo kaminfo,
                BelDocumentInfo docinfo, CitationType citation) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Citation> getCitations(KamInfo info, CitationType citation) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<Citation> getCitations(KamInfo info, CitationType citation,
                String... refIDs) {
            throw new UnsupportedOperationException();
        }
    }
}
