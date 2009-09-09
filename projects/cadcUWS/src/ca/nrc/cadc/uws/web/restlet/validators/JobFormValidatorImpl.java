/******************************************************************************
 *
 *  Copyright (C) 2009                          Copyright (C) 2009
 *  National Research Council           Conseil national de recherches
 *  Ottawa, Canada, K1A 0R6                     Ottawa, Canada, K1A 0R6
 *  All rights reserved                         Tous droits reserves
 *
 *  NRC disclaims any warranties,       Le CNRC denie toute garantie
 *  expressed, implied, or statu-       enoncee, implicite ou legale,
 *  tory, of any kind with respect      de quelque nature que se soit,
 *  to the software, including          concernant le logiciel, y com-
 *  without limitation any war-         pris sans restriction toute
 *  ranty of merchantability or         garantie de valeur marchande
 *  fitness for a particular pur-       ou de pertinence pour un usage
 *  pose.  NRC shall not be liable      particulier.  Le CNRC ne
 *  in any event for any damages,       pourra en aucun cas etre tenu
 *  whether direct or indirect,         responsable de tout dommage,
 *  special or general, consequen-      direct ou indirect, particul-
 *  tial or incidental, arising         ier ou general, accessoire ou
 *  from the use of the software.       fortuit, resultant de l'utili-
 *                                                              sation du logiciel.
 *
 *
 *  This file is part of cadcUWS.
 *
 *  cadcUWS is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  cadcUWS is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with cadcUWS.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package ca.nrc.cadc.uws.web.restlet.validators;

import org.restlet.data.Form;

import java.util.Map;
import java.util.HashMap;
import java.text.ParseException;

import ca.nrc.cadc.uws.web.validators.FormValidator;
import ca.nrc.cadc.uws.JobAttribute;
import ca.nrc.cadc.uws.ExecutionPhase;
import ca.nrc.cadc.uws.util.StringUtil;
import ca.nrc.cadc.date.DateUtil;

import java.text.DateFormat;


/**
 * Form validator implementation specific to a Job Form.
 */
public class JobFormValidatorImpl implements FormValidator
{
    final Form form;


    /**
     * Default constructor.
     *
     * @param form      The form to operate on.
     */
    public JobFormValidatorImpl(final Form form)
    {
        this.form = form;
    }

    
    /**
     * Validate the given Form.
     *
     * @return Mapping of fields to error messages, if necessary, or
     *         empty Map otherwise, never NULL.
     */
    public Map<String, String> validate()
    {
        final Map<String, String> errors = new HashMap<String, String>();
        final Map<String, String> valuesMap = getForm().getValuesMap();

        // Check for a valid Exection Phase.
        if (valuesMap.containsKey(
                JobAttribute.EXECUTION_PHASE.getAttributeName().toUpperCase()))
        {
            try
            {
                ExecutionPhase.valueOf(
                        valuesMap.get(JobAttribute.EXECUTION_PHASE.
                                getAttributeName().toUpperCase()).
                                toUpperCase());
            }
            catch (final IllegalArgumentException e)
            {
                errors.put(JobAttribute.EXECUTION_PHASE.
                        getAttributeName().toUpperCase(),
                           "Execution Phase is mandatory.");
            }
        }
//
//        if (!valuesMap.containsKey(JobAttribute.EXECUTION_DURATION.
//                getAttributeName().toUpperCase()))
//        {
//            errors.put(JobAttribute.EXECUTION_DURATION.
//                    getAttributeName().toUpperCase(),
//                       "Execution Duration is mandatory.");
//        }

        final String destruction = valuesMap.get(
                JobAttribute.DESTRUCTION_TIME.getAttributeName().toUpperCase());

        if (StringUtil.hasText(destruction) && !isValidDate(destruction))
        {
            errors.put(JobAttribute.DESTRUCTION_TIME.
                    getAttributeName().toUpperCase(),
                       "Destruction Time is a mandatory date in the format "
                       + DateUtil.IVOA_DATE_FORMAT);
        }

//        final String quote = getForm().getFirstValue(
//                JobAttribute.QUOTE.getAttributeName().toUpperCase());
//
//        if (!StringUtil.hasText(quote) || !isValidDate(quote))
//        {
//            errors.put(JobAttribute.QUOTE.getAttributeName().toUpperCase(),
//                       "Quote Time is a mandatory date in the format "
//                       + DateUtil.ISO8601_DATE_FORMAT);
//        }

        return errors;
    }

    protected boolean isValidDate(final String dateString)
    {
        try
        {
            DateFormat df = DateUtil.getDateFormat(DateUtil.IVOA_DATE_FORMAT, DateUtil.UTC);
            df.parse(dateString);
        }
        catch (ParseException e)
        {
            return false;
        }
        return true;
    }


    public Form getForm()
    {
        return form;
    }
}
