/**************************************************************
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 *************************************************************/


package com.sun.star.report.pentaho.layoutprocessor;

import com.sun.star.report.OfficeToken;
import com.sun.star.report.SDBCReportDataFactory;
import com.sun.star.report.pentaho.model.ObjectOleElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jfree.layouting.util.AttributeMap;
import org.jfree.report.DataFlags;
import org.jfree.report.DataRow;
import org.jfree.report.DataSourceException;
import org.jfree.report.JFreeReportInfo;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.flow.ReportTarget;
import org.jfree.report.flow.layoutprocessor.LayoutController;
import org.jfree.report.structure.Element;

/**
 *
 * @author Ocke Janssen
 */
public class ObjectOleLayoutController extends AbstractReportElementLayoutController
{

    public ObjectOleLayoutController()
    {
    }

    protected boolean isValueChanged()
    {
        final ObjectOleElement element = (ObjectOleElement) getNode();
        final List masterfields = element.getMasterfields();
        final DataRow view = getFlowController().getMasterRow().getGlobalView();
        for (final Iterator iter = masterfields.iterator(); iter.hasNext();)
        {
            final String master = (String) iter.next();
            try
            {
                final DataFlags flags = view.getFlags(master);
                if (flags != null && flags.isChanged())
                {
                    return true;
                }
            }
            catch (DataSourceException e)
            {
                // ignore .. assume that the reference has not changed.
            }
        }
        return false;
    }

    protected LayoutController delegateContentGeneration(final ReportTarget target) throws ReportProcessingException, ReportDataFactoryException, DataSourceException
    {
        final ObjectOleElement element = (ObjectOleElement) getNode();
        final String url = element.getUrl();
        if (url != null)
        {
            final AttributeMap ole = new AttributeMap();
            ole.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, Element.NAMESPACE_ATTRIBUTE, JFreeReportInfo.REPORT_NAMESPACE);
            ole.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, Element.TYPE_ATTRIBUTE, OfficeToken.OBJECT_OLE);
            ole.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, "href", url);
            ole.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, "class-id", element.getClassid());
            final List masterfields = element.getMasterfields();
            final List values = new ArrayList();
            final DataRow view = getFlowController().getMasterRow().getGlobalView();
            for (final Iterator iter = masterfields.iterator(); iter.hasNext();)
            {
                final String master = (String) iter.next();
                try
                {
                    final DataFlags flags = view.getFlags(master);
                    if (flags != null)
                    {
                        values.add(flags.getValue());
                    }
                }
                catch (DataSourceException e)
                {
                    // ignore .. assume that the reference has not changed.
                }
            }
            ole.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, SDBCReportDataFactory.MASTER_COLUMNS, masterfields);
            ole.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, SDBCReportDataFactory.MASTER_VALUES, values);
            ole.setAttribute(JFreeReportInfo.REPORT_NAMESPACE, SDBCReportDataFactory.DETAIL_COLUMNS, element.getDetailfields());

            target.startElement(ole);
            target.endElement(ole);
        }

        return join(getFlowController());
    }
}

