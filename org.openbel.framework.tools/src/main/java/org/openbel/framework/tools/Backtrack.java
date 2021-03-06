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
package org.openbel.framework.tools;

import static org.openbel.framework.common.BELUtilities.noItems;
import static org.openbel.framework.common.Strings.SYSTEM_CONFIG_PATH;
import static org.openbel.framework.common.enums.ExitCode.NO_XBEL_DOCUMENTS;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.Option;
import org.openbel.framework.common.enums.ExitCode;
import org.openbel.framework.common.model.Document;
import org.openbel.framework.core.CommandLineApplication;
import org.openbel.framework.core.ConversionError;
import org.openbel.framework.core.XBELConverterService;
import org.openbel.framework.core.XBELConverterServiceImpl;
import org.openbel.framework.core.XBELValidatorService;
import org.openbel.framework.core.XBELValidatorServiceImpl;
import org.openbel.framework.core.compiler.ValidationError;
import org.xml.sax.SAXException;

/**
 * The <tt>Backtrack</tt> BEL command-line application.
 */
public class Backtrack extends CommandLineApplication {
    private final XBELValidatorService validator;
    private final XBELConverterService converter;

    /**
     * System configuration command-line argument short option,
     * {@value #SHORT_OPT_SYSCFG}.
     */
    protected final static String SHORT_OPT_SYSCFG = "s";

    /**
     * System configuration command-line argument long option,
     * {@value #LONG_OPT_SYSCFG}.
     */
    protected final static String LONG_OPT_SYSCFG = "system-config";

    /**
     * Creates the application with the provided command-line arguments.
     *
     * @param args Command-line arguments
     * @throws JAXBException Thrown if an error was encountered during JAXB
     * processing
     * @throws SAXException Thrown if an error is found during processing the
     * specified schemas; the current API does not provide
     * {@link org.xml.sax.ErrorHandler SAX error handler} functionality
     * @throws MalformedURLException thrown if the schema file {@link URL}s
     * syntax is malformed
     * @throws URISyntaxException thrown if a {@link URL} in {@code schemas} has
     * invalid {@link URI} syntax
     * @throws IOException thrown if an i/o error occurs reading from the schema
     * url's {@link InputStream}
     */
    private Backtrack(String[] args) throws MalformedURLException,
            SAXException, IOException, URISyntaxException, JAXBException {
        super(args);
        setReportable(null);
        initializeSystemConfiguration();

        validator = new XBELValidatorServiceImpl();
        converter = new XBELConverterServiceImpl();

        List<String> extraargs = getExtraneousArguments();

        // Quit now if no XBEL document was provided, or too many args.
        if (noItems(extraargs) || extraargs.size() > 1) {
            printUsage();
            bail(NO_XBEL_DOCUMENTS);
        }

        // Quit now if file is not readable
        final File file = new File(extraargs.get(0));
        if (!file.canRead()) {
            reportable.error("bad input " + file);
            printUsage();
            bail(NO_XBEL_DOCUMENTS);
        }

        try {
            validate(file);
        } catch (ValidationError ve) {
            reportable.error(ve.getUserFacingMessage());
            bail(ExitCode.NO_VALID_DOCUMENTS);
        }

        Document document = null;
        try {
            document = convert(file);
        } catch (ConversionError ce) {
            reportable.error("Failed converting file to BEL model");
            reportable.error(ce.getUserFacingMessage());
            bail(ExitCode.NO_CONVERTED_DOCUMENTS);
        }

        String output = "";
        try {
            output = convert(document);
        } catch (ConversionError ce) {
            reportable.error("Failed converting BEL model to BEL document");
            reportable.error(ce.getUserFacingMessage());
            bail(ExitCode.NO_CONVERTED_DOCUMENTS);
        }

        reportable.output(output);
    }

    private void validate(final File f) throws ValidationError {
        validator.validate(f);
    }

    private String convert(final Document d) throws ConversionError {
        try {
            return converter.toXML(d);
        } catch (JAXBException e) {
            e.printStackTrace();
            final String name = d.getName();
            final String msg = e.getMessage();
            final Throwable cause = e;
            throw new ConversionError(name, msg, cause);
        } catch (IOException e) {
            final String name = d.getName();
            final String msg = e.getMessage();
            final Throwable cause = e;
            throw new ConversionError(name, msg, cause);
        }
    }

    private Document convert(final File f) throws ConversionError {
        try {
            Document document = converter.toCommon(f);
            return document;
        } catch (JAXBException e) {
            final String name = f.getAbsolutePath();
            final String msg = e.getMessage();
            final Throwable cause = e;
            throw new ConversionError(name, msg, cause);
        } catch (IOException e) {
            final String name = f.getAbsolutePath();
            final String msg = e.getMessage();
            final Throwable cause = e;
            throw new ConversionError(name, msg, cause);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Option> getCommandLineOptions() {
        final List<Option> ret = new LinkedList<Option>();

        ret.add(new Option(SHORT_OPT_SYSCFG, LONG_OPT_SYSCFG, true,
                SYSTEM_CONFIG_PATH));

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsage() {
        return "FILE";
    }

    /**
     * Returns {@code "BEL Backtrack"}.
     */
    @Override
    public String getApplicationName() {
        return "BEL Backtrack";
    }

    /**
     * Returns {@code "Backtrack"}.
     *
     * @return String
     */
    @Override
    public String getApplicationShortName() {
        return "Backtrack";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationDescription() {
        final StringBuilder bldr = new StringBuilder();
        bldr.append("Converts a BEL document to the BEL model, ");
        bldr.append("then reconstitutes the BEL document.");
        return bldr.toString();
    }

    /**
     * Backtrack {@code main()}.
     *
     * @param args Command-line arguments
     */
    public static void main(String... args) {
        try {
            Backtrack b = new Backtrack(args);
            b.end();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
